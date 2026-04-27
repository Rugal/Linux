#!/usr/bin/env python3
"""
Turn Calculator Script

Calculates turn numbers, watermarks, and chunking decisions.
Called by AI during checkpoint Step 4.

Usage:
    python3 turn_calculator.py <manifest_path> <session_turn_count>

Output (JSON):
    {
        "success": true,
        "sessionGlobalRange": [51, 66],
        "createChunk": true,
        "chunkRange": [36, 50],
        "newWatermark": {"lastChunkedTurn": 50, "lastSessionEndTurn": 66},
        "newTotalTurns": 66
    }

Fallback: If script fails, AI executes equivalent logic from SKILL.md Step 4
"""

import json
import sys
from typing import Dict, Any, Optional, Tuple, List


def calculate_turn_numbers(
    total_turns_so_far: int,
    last_chunked_turn: int,
    last_session_end: int,
    session_turn_count: int,
    topic_shift_detected: bool = False
) -> Dict[str, Any]:
    """
    Calculate turn numbers and chunking decision.
    
    This implements the exact logic from SKILL.md Step 4.
    
    Args:
        total_turns_so_far: Current totalConversationTurns from manifest
        last_chunked_turn: turnWatermark.lastChunkedTurn from manifest
        last_session_end: turnWatermark.lastSessionEndTurn from manifest
        session_turn_count: Number of filtered turns in current session
        topic_shift_detected: Whether a topic shift was detected
        
    Returns:
        Dict with calculation results
    """
    # Step 1: Calculate current session's global turn range
    session_start_turn = total_turns_so_far + 1
    session_end_turn = total_turns_so_far + session_turn_count
    
    # Step 2: Determine what needs chunking
    unchunked_start = last_chunked_turn + 1
    unchunked_end = session_end_turn
    unchunked_count = unchunked_end - unchunked_start + 1
    
    # Step 3: Decide if chunking needed
    create_chunk = False
    chunk_range: Optional[List[int]] = None
    
    # Special case: First checkpoint (no history to chunk)
    if last_session_end == 0:
        create_chunk = False
        chunk_range = None
    elif unchunked_count >= 20:
        # Too many unchunked turns, create chunk
        chunk_start = unchunked_start
        chunk_end = session_start_turn - 1  # Right before current session
        
        if chunk_end >= chunk_start and (chunk_end - chunk_start + 1) >= 10:
            create_chunk = True
            chunk_range = [chunk_start, chunk_end]
        else:
            create_chunk = False
    elif unchunked_count >= 10 and topic_shift_detected:
        # Moderate unchunked but topic shifted
        chunk_start = unchunked_start
        chunk_end = session_start_turn - 1
        if chunk_end >= chunk_start:
            create_chunk = True
            chunk_range = [chunk_start, chunk_end]
        else:
            create_chunk = False
    else:
        # Too few unchunked, keep as sessions
        create_chunk = False
    
    # Step 4: Calculate new watermark
    if create_chunk and chunk_range:
        new_last_chunked_turn = chunk_range[1]
    else:
        new_last_chunked_turn = last_chunked_turn
    
    new_last_session_end_turn = session_end_turn
    
    return {
        "success": True,
        "sessionGlobalRange": [session_start_turn, session_end_turn],
        "createChunk": create_chunk,
        "chunkRange": chunk_range,
        "newWatermark": {
            "lastChunkedTurn": new_last_chunked_turn,
            "lastSessionEndTurn": new_last_session_end_turn
        },
        "newTotalTurns": session_end_turn,
        "calculation": {
            "unchunkedStart": unchunked_start,
            "unchunkedEnd": unchunked_end,
            "unchunkedCount": unchunked_count,
            "isFirstCheckpoint": last_session_end == 0
        }
    }


def main():
    """CLI entry point"""
    if len(sys.argv) < 3:
        print(json.dumps({
            "success": False,
            "error": "Usage: turn_calculator.py <manifest_path> <session_turn_count> [topic_shift:true/false]",
            "fallback": "AI should execute Step 4 logic manually"
        }))
        sys.exit(1)
    
    manifest_path = sys.argv[1]
    session_turn_count = int(sys.argv[2])
    topic_shift = sys.argv[3].lower() == 'true' if len(sys.argv) > 3 else False
    
    try:
        # Read manifest
        with open(manifest_path, 'r', encoding='utf-8') as f:
            manifest = json.load(f)
        
        # Extract values
        lifecycle = manifest.get('lifecycle', {})
        watermark = manifest.get('turnWatermark', {})
        
        total_turns = lifecycle.get('totalConversationTurns', 0)
        last_chunked = watermark.get('lastChunkedTurn', 0)
        last_session_end = watermark.get('lastSessionEndTurn', 0)
        
        # Calculate
        result = calculate_turn_numbers(
            total_turns_so_far=total_turns,
            last_chunked_turn=last_chunked,
            last_session_end=last_session_end,
            session_turn_count=session_turn_count,
            topic_shift_detected=topic_shift
        )
        
        print(json.dumps(result, indent=2))
        
    except Exception as e:
        print(json.dumps({
            "success": False,
            "error": str(e),
            "fallback": "AI should execute Step 4 logic manually using values from manifest"
        }))
        sys.exit(1)


if __name__ == '__main__':
    main()
