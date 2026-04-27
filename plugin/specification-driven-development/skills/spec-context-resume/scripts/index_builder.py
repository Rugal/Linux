#!/usr/bin/env python3
"""
Index Builder Script

Builds reverse index from manifest.richIndexing for Resume dynamic queries.
Called by AI during resume Step 2.

Usage:
    python3 index_builder.py <manifest_path>

Output (JSON):
    {
        "success": true,
        "queryIndex": {
            "keywords": {"redis": ["chunk-003"], ...},
            "files": {"design.md": ["chunk-002", "chunk-003"], ...},
            "decisionTags": {"architecture": ["chunk-001"], ...},
            "phases": {"design": ["chunk-002", "chunk-003"], ...}
        },
        "stats": {"keywords": 45, "files": 8, "tags": 6, "phases": 3}
    }

Fallback: If script fails, AI builds index manually using algorithm from SKILL.md Step 2
"""

import json
import sys
from typing import Dict, Any, List
from collections import defaultdict


def build_reverse_index(rich_indexing: Dict[str, Any]) -> Dict[str, Any]:
    """
    Build reverse index from forward index (chunk→keywords to keyword→chunks).
    
    This implements the exact algorithm from SKILL.md Resume Step 2.
    
    Args:
        rich_indexing: manifest.richIndexing object containing chunks and sessions
        
    Returns:
        Dict with query_index and statistics
    """
    query_index = {
        "keywords": defaultdict(list),
        "files": defaultdict(list),
        "decisionTags": defaultdict(list),
        "phases": defaultdict(list)
    }
    
    # Process chunks
    chunks = rich_indexing.get('chunks', {})
    for chunk_id, chunk_data in chunks.items():
        # Keywords
        for keyword in chunk_data.get('keywords', []):
            query_index["keywords"][keyword].append(chunk_id)
        
        # Files
        for file in chunk_data.get('filesReferenced', []):
            query_index["files"][file].append(chunk_id)
        
        # Decision tags
        for tag in chunk_data.get('decisionTags', []):
            query_index["decisionTags"][tag].append(chunk_id)
        
        # Phase
        phase = chunk_data.get('phase')
        if phase:
            query_index["phases"][phase].append(chunk_id)
    
    # Process sessions (only if not chunked)
    sessions = rich_indexing.get('sessions', {})
    for session_id, session_data in sessions.items():
        # Skip if already chunked
        if session_data.get('isChunked', False):
            continue
        
        # Keywords
        for keyword in session_data.get('keywords', []):
            query_index["keywords"][keyword].append(session_id)
        
        # Files
        for file in session_data.get('filesReferenced', []):
            query_index["files"][file].append(session_id)
        
        # Decision tags
        for tag in session_data.get('decisionTags', []):
            query_index["decisionTags"][tag].append(session_id)
        
        # Phase
        phase = session_data.get('phase')
        if phase:
            query_index["phases"][phase].append(session_id)
    
    # Convert defaultdict to regular dict for JSON serialization
    result_index = {
        "keywords": dict(query_index["keywords"]),
        "files": dict(query_index["files"]),
        "decisionTags": dict(query_index["decisionTags"]),
        "phases": dict(query_index["phases"])
    }
    
    stats = {
        "keywords": len(result_index["keywords"]),
        "files": len(result_index["files"]),
        "decisionTags": len(result_index["decisionTags"]),
        "phases": len(result_index["phases"]),
        "totalChunks": len(chunks),
        "totalSessions": len(sessions),
        "activeSessionsIndexed": sum(1 for s in sessions.values() if not s.get('isChunked', False))
    }
    
    return {
        "success": True,
        "queryIndex": result_index,
        "stats": stats
    }


def query_index(
    query_index: Dict[str, Dict[str, List[str]]],
    keywords: List[str] = None,
    file: str = None,
    phase: str = None,
    decision_tag: str = None,
    max_results: int = 2
) -> Dict[str, Any]:
    """
    Query the reverse index to find relevant contexts.
    
    Args:
        query_index: The reverse index built by build_reverse_index
        keywords: List of keywords to match
        file: File name to match
        phase: Phase to match
        decision_tag: Decision tag to match
        max_results: Maximum number of results to return (default 2)
        
    Returns:
        Dict with matched context IDs and scores
    """
    matches = {}  # context_id -> match_count
    
    # Match keywords
    if keywords:
        for kw in keywords:
            kw_lower = kw.lower()
            for index_kw, contexts in query_index.get("keywords", {}).items():
                if kw_lower in index_kw.lower() or index_kw.lower() in kw_lower:
                    for ctx in contexts:
                        matches[ctx] = matches.get(ctx, 0) + 1
    
    # Match file
    if file:
        for ctx in query_index.get("files", {}).get(file, []):
            matches[ctx] = matches.get(ctx, 0) + 2  # File match weights more
    
    # Match phase
    if phase:
        for ctx in query_index.get("phases", {}).get(phase, []):
            matches[ctx] = matches.get(ctx, 0) + 1
    
    # Match decision tag
    if decision_tag:
        for ctx in query_index.get("decisionTags", {}).get(decision_tag, []):
            matches[ctx] = matches.get(ctx, 0) + 1
    
    # Sort by match count and return top N
    sorted_matches = sorted(matches.items(), key=lambda x: x[1], reverse=True)
    top_matches = sorted_matches[:max_results]
    
    return {
        "success": True,
        "matches": [{"contextId": ctx, "score": score} for ctx, score in top_matches],
        "totalMatches": len(matches)
    }


def main():
    """CLI entry point"""
    if len(sys.argv) < 2:
        print(json.dumps({
            "success": False,
            "error": "Usage: index_builder.py <manifest_path> [query:keywords,file,phase,tag]",
            "fallback": "AI should build index manually using algorithm from SKILL.md"
        }))
        sys.exit(1)
    
    manifest_path = sys.argv[1]
    
    try:
        # Read manifest
        with open(manifest_path, 'r', encoding='utf-8') as f:
            manifest = json.load(f)
        
        rich_indexing = manifest.get('richIndexing', {'chunks': {}, 'sessions': {}})
        
        # Build index
        result = build_reverse_index(rich_indexing)
        
        # If query parameters provided, also run query
        if len(sys.argv) > 2:
            query_params = sys.argv[2]
            # Parse query:keyword1,keyword2|file:design.md|phase:design|tag:architecture
            keywords = []
            file = None
            phase = None
            tag = None
            
            for param in query_params.split('|'):
                if param.startswith('keywords:'):
                    keywords = param[9:].split(',')
                elif param.startswith('file:'):
                    file = param[5:]
                elif param.startswith('phase:'):
                    phase = param[6:]
                elif param.startswith('tag:'):
                    tag = param[4:]
            
            query_result = query_index(
                result["queryIndex"],
                keywords=keywords,
                file=file,
                phase=phase,
                decision_tag=tag
            )
            result["queryResult"] = query_result
        
        print(json.dumps(result, indent=2))
        
    except Exception as e:
        print(json.dumps({
            "success": False,
            "error": str(e),
            "fallback": "AI should build reverse index manually: for each chunk/session, add its keywords/files/tags to query_index[dimension][value].append(context_id)"
        }))
        sys.exit(1)


if __name__ == '__main__':
    main()
