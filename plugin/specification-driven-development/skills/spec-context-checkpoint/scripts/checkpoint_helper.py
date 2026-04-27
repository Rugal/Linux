#!/usr/bin/env python3
"""
Checkpoint Helper Script

Provides utility functions for spec-context-checkpoint skill.
All functions output JSON for easy parsing by AI.

Commands:
  filter <session_file>     - Filter skill meta-operations from turns
  weights <manifest_path>   - Calculate context weights
  session-id                - Generate session ID with current timestamp
  timestamp                 - Get current timestamp in ISO format

Usage:
  python3 checkpoint_helper.py filter session_turns.json
  python3 checkpoint_helper.py weights manifest.json
  python3 checkpoint_helper.py session-id
  python3 checkpoint_helper.py timestamp
"""

import json
import sys
import re
from datetime import datetime, timezone
from typing import Dict, Any, List

# ============================================================================
# Turn Filtering
# ============================================================================

CHECKPOINT_USER_KEYWORDS = [
    r'保存.*上下文',
    r'保存.*spec',
    r'checkpoint',
    r'/ckpt',
    r'save.*context',
    r'帮我保存',
    r'保存一下',
    r'保存下',
]

CHECKPOINT_AI_KEYWORDS = [
    r'执行.*checkpoint',
    r'spec-context-checkpoint',
    r'正在保存',
    r'开始checkpoint',
    r'Creating manifest',
    r'Saving context',
    r'已.*manifest',
    r'保存完成',
    r'checkpoint.*完成',
]

def is_checkpoint_turn(turn: Dict[str, Any]) -> bool:
    """
    Check if a turn is checkpoint-related meta-operation.
    
    Args:
        turn: Turn object with user and ai fields
        
    Returns:
        True if turn should be skipped (is checkpoint-related)
    """
    user_msg = turn.get('user', {}).get('message', '').lower()
    ai_msg = turn.get('ai', {}).get('message', '').lower()
    
    # Check user message
    for pattern in CHECKPOINT_USER_KEYWORDS:
        if re.search(pattern, user_msg, re.IGNORECASE):
            # Make sure checkpoint is primary intent
            # If message is long (>100 chars) and checkpoint is just mentioned, keep it
            if len(user_msg) > 100 and '保存' in user_msg and user_msg.index('保存') > 50:
                continue  # Checkpoint mentioned in passing
            return True
    
    # Check AI message
    for pattern in CHECKPOINT_AI_KEYWORDS:
        if re.search(pattern, ai_msg, re.IGNORECASE):
            return True
    
    return False

def filter_turns(turns: List[Dict[str, Any]]) -> Dict[str, Any]:
    """
    Filter out checkpoint-related turns from session.
    
    Args:
        turns: List of turn objects
        
    Returns:
        Dict with filtered turns and metadata
    """
    filtered_turns = []
    skipped_indices = []
    
    for i, turn in enumerate(turns):
        if is_checkpoint_turn(turn):
            skipped_indices.append(turn.get('turn', i + 1))
        else:
            filtered_turns.append(turn)
    
    # Renumber turns sequentially
    for i, turn in enumerate(filtered_turns):
        turn['turn'] = i + 1
    
    return {
        'filtered_turns': filtered_turns,
        'original_count': len(turns),
        'filtered_count': len(filtered_turns),
        'skipped_count': len(skipped_indices),
        'skipped_turns': skipped_indices
    }

# ============================================================================
# Weight Calculation
# ============================================================================

def calculate_time_decay(days_old: float, half_life_days: float = 3.0) -> float:
    """
    Calculate time decay factor using exponential decay.
    
    Formula: decay = 0.5 ^ (days_old / half_life)
    
    Args:
        days_old: Number of days since creation
        half_life_days: Half-life in days (default: 3 days)
        
    Returns:
        Decay factor between 0 and 1
    """
    return 0.5 ** (days_old / half_life_days)

def calculate_context_weight(
    context_type: str,
    created_at: str,
    current_time: str,
    status: str = 'active',
    relevance_boost: float = 1.0
) -> Dict[str, Any]:
    """
    Calculate weight for a context item.
    
    Weight = BaseWeight × DecayFactor × RelevanceBoost × StatusModifier
    
    Args:
        context_type: Type of context (semantic-chunk, recent-session, impl-log)
        created_at: ISO8601 timestamp of creation
        current_time: Current ISO8601 timestamp
        status: Context status (active, deprecated)
        relevance_boost: Relevance boost multiplier (default 1.0)
        
    Returns:
        Dict with weight and calculation details
    """
    # Base weights by type
    BASE_WEIGHTS = {
        'semantic-chunk': 0.7,
        'recent-session': 1.0,
        'impl-log': 0.8,
        'document': 0.9
    }
    
    # Status modifiers
    STATUS_MODIFIERS = {
        'active': 1.0,
        'deprecated': 0.05,
        'archived': 0.01
    }
    
    base_weight = BASE_WEIGHTS.get(context_type, 0.5)
    status_modifier = STATUS_MODIFIERS.get(status, 1.0)
    
    # Calculate days old
    created = datetime.fromisoformat(created_at.replace('Z', '+00:00'))
    current = datetime.fromisoformat(current_time.replace('Z', '+00:00'))
    days_old = (current - created).total_seconds() / 86400
    
    # Calculate decay
    decay_factor = calculate_time_decay(days_old)
    
    # Final weight
    weight = base_weight * decay_factor * relevance_boost * status_modifier
    
    return {
        'weight': round(weight, 3),
        'calculation': {
            'base_weight': base_weight,
            'decay_factor': round(decay_factor, 3),
            'days_old': round(days_old, 2),
            'relevance_boost': relevance_boost,
            'status_modifier': status_modifier
        },
        'formula': f'{base_weight} × {round(decay_factor, 3)} × {relevance_boost} × {status_modifier} = {round(weight, 3)}'
    }

def calculate_all_weights(manifest: Dict[str, Any]) -> Dict[str, Any]:
    """
    Calculate weights for all contexts in manifest.
    
    Args:
        manifest: Manifest.json content
        
    Returns:
        Dict with all weights and metadata
    """
    current_time = datetime.now(timezone.utc).isoformat()
    weights = {}
    
    # Semantic chunks
    for chunk in manifest.get('conversationTimeline', {}).get('semanticChunks', []):
        chunk_id = chunk['chunkId']
        result = calculate_context_weight(
            'semantic-chunk',
            chunk['createdAt'],
            current_time,
            chunk.get('status', 'active'),
            chunk.get('relevanceDecay', 1.0)
        )
        weights[chunk_id] = result['weight']
    
    # Recent sessions
    for session in manifest.get('conversationTimeline', {}).get('recentSessions', []):
        session_id = session['sessionId']
        result = calculate_context_weight(
            'recent-session',
            session['startedAt'],
            current_time
        )
        weights[session_id] = result['weight']
    
    # Implementation logs
    for log in manifest.get('implLogs', {}).get('recent', []):
        log_id = f"impl-log-{log['taskId']}"
        result = calculate_context_weight(
            'impl-log',
            log['createdAt'],
            current_time,
            log.get('completionStatus', 'active')
        )
        weights[log_id] = result['weight']
    
    return {
        'weights': weights,
        'lastCalculated': current_time,
        'totalContexts': len(weights)
    }

# ============================================================================
# CLI Interface
# ============================================================================

def filter_turns_cli():
    """CLI for turn filtering"""
    if len(sys.argv) < 2:
        print("Usage: checkpoint_helper.py filter <turns_json_file>", file=sys.stderr)
        sys.exit(1)
    
    turns_file = sys.argv[2]
    
    try:
        with open(turns_file, 'r', encoding='utf-8') as f:
            data = json.load(f)
        
        turns = data.get('recentTurns', data.get('turns', []))
        result = filter_turns(turns)
        
        # Update data
        data['recentTurns'] = result['filtered_turns']
        data['turnsTotal'] = result['filtered_count']
        data['turnsSaved'] = result['filtered_count']
        data['turnsFiltered'] = result['skipped_count']
        data['filterNote'] = f"Skipped {result['skipped_count']} checkpoint turns: {result['skipped_turns']}"
        
        # Add filtering metadata
        if 'metadata' not in data:
            data['metadata'] = {}
        data['metadata']['filtering'] = {
            'originalTurnCount': result['original_count'],
            'checkpointTurnsSkipped': result['skipped_count'],
            'skippedTurns': result['skipped_turns'],
            'reason': 'Checkpoint meta-operations'
        }
        
        # Write back
        with open(turns_file, 'w', encoding='utf-8') as f:
            json.dump(data, f, indent=2, ensure_ascii=False)
        
        print(json.dumps({
            'success': True,
            'original_count': result['original_count'],
            'filtered_count': result['filtered_count'],
            'skipped_count': result['skipped_count']
        }))
        
    except Exception as e:
        print(json.dumps({
            'success': False,
            'error': str(e),
            'fallback': 'AI should handle filtering manually'
        }), file=sys.stderr)
        sys.exit(1)

def calculate_weights_cli():
    """CLI for weight calculation"""
    if len(sys.argv) < 3:
        print(json.dumps({
            'success': False,
            'error': 'Missing manifest file path',
            'usage': 'checkpoint_helper.py weights <manifest_json_file>',
            'fallback': 'AI should calculate weights manually using formula: Weight = BaseWeight × DecayFactor × StatusModifier'
        }))
        sys.exit(1)
    
    manifest_file = sys.argv[2]
    
    try:
        with open(manifest_file, 'r', encoding='utf-8') as f:
            manifest = json.load(f)
        
        result = calculate_all_weights(manifest)
        
        # Update manifest
        manifest['contextWeighting']['currentWeights'] = result['weights']
        manifest['contextWeighting']['lastCalculated'] = result['lastCalculated']
        
        # Write back
        with open(manifest_file, 'w', encoding='utf-8') as f:
            json.dump(manifest, f, indent=2, ensure_ascii=False)
        
        print(json.dumps({
            'success': True,
            'total_contexts': result['totalContexts'],
            'weights': result['weights']
        }))

    except Exception as e:
        print(json.dumps({
            'success': False,
            'error': str(e),
            'fallback': 'AI should calculate weights manually'
        }))
        sys.exit(1)

def generate_session_id(manifest_path=None):
    """Generate session ID with auto-increment number

    Format: session-NNN (3-digit zero-padded)
    Example: session-001, session-002, session-003

    Args:
        manifest_path: Optional path to manifest.json to read existing sessions

    Returns JSON:
    {
        "success": true,
        "sessionId": "session-004",
        "sessionNumber": 4
    }
    """
    next_num = 1

    if manifest_path:
        try:
            with open(manifest_path, 'r', encoding='utf-8') as f:
                manifest = json.load(f)

            recent_sessions = manifest.get('recentSessions', [])
            if recent_sessions:
                # Extract numbers from session IDs like "session-003"
                nums = []
                for s in recent_sessions:
                    sid = s.get('sessionId', '')
                    if sid.startswith('session-'):
                        try:
                            num = int(sid.split('-')[1])
                            nums.append(num)
                        except (ValueError, IndexError):
                            pass
                if nums:
                    next_num = max(nums) + 1
        except Exception:
            pass  # If manifest doesn't exist or can't be read, start from 1

    session_id = f"session-{next_num:03d}"

    return {
        'success': True,
        'sessionId': session_id,
        'sessionNumber': next_num,
        'timestamp': datetime.now(timezone.utc).strftime('%Y-%m-%dT%H:%M:%SZ')
    }


def get_timestamp():
    """Get current timestamp in ISO format

    Returns JSON:
    {
        "success": true,
        "timestamp": "2026-01-29T14:30:52Z",
        "unix": 1769778652
    }
    """
    now = datetime.now(timezone.utc)
    return {
        'success': True,
        'timestamp': now.strftime('%Y-%m-%dT%H:%M:%SZ'),
        'unix': int(now.timestamp())
    }


if __name__ == '__main__':
    if len(sys.argv) < 2:
        print(json.dumps({
            'success': False,
            'error': 'No command specified',
            'usage': 'checkpoint_helper.py <command> [args]',
            'commands': {
                'filter <turns_json>': 'Filter checkpoint turns',
                'weights <manifest>': 'Calculate context weights',
                'session-id [manifest]': 'Generate auto-increment session ID',
                'timestamp': 'Get current timestamp'
            },
            'fallback': 'AI should execute the required logic manually'
        }))
        sys.exit(1)
    
    command = sys.argv[1]
    
    if command == 'filter':
        filter_turns_cli()
    elif command == 'weights':
        calculate_weights_cli()
    elif command == 'session-id':
        # Optional: pass manifest path to read existing sessions
        manifest_path = sys.argv[2] if len(sys.argv) > 2 else None
        print(json.dumps(generate_session_id(manifest_path)))
    elif command == 'timestamp':
        print(json.dumps(get_timestamp()))
    else:
        print(json.dumps({
            'success': False,
            'error': f'Unknown command: {command}',
            'fallback': 'AI should execute the required logic manually'
        }))
        sys.exit(1)
