# Manifest.json Schema Documentation

Complete schema for `.context/manifest.json` - the central index for all context.

## Top-Level Structure

```json
{
  "feature": "string",           // Feature name (e.g., "payment-sensitive-word-detection")
  "manifestVersion": "3.0",       // Manifest schema version
  "lastUpdated": "ISO8601",       // Last update timestamp
  
  "lifecycle": {...},             // Feature lifecycle metadata
  "documentVersions": {...},      // requirements/design/tasks version tracking
  "conversationTimeline": {...},  // Conversation chunks and sessions
  "implLogs": {...},              // Implementation log metadata
  "contextWeighting": {...},      // Weight calculation results
  "loadRecommendations": {...},   // Smart loading strategies
  "deprecated": {...}             // Deprecated contexts
}
```

## lifecycle

```json
"lifecycle": {
  "status": "in-progress",        // "in-progress" | "completed" | "on-hold"
  "currentPhase": "implementation", // "requirements" | "design" | "tasks" | "implementation"
  "progress": 0.40,               // 0.0-1.0
  "startDate": "2026-01-15",
  "estimatedCompletion": "2026-02-05",
  "totalConversationTurns": 156,  // Accumulated across all sessions
  "totalSessions": 8
}
```

## documentVersions

Track version history for requirements/design/tasks documents.

```json
"documentVersions": {
  "requirements": {
    "currentVersion": "v2",
    "versions": [
      {
        "version": "v1",
        "createdAt": "2026-01-15T10:00:00Z",
        "deprecatedAt": "2026-01-28T14:00:00Z",
        "reason": "Changed from 1D to 3D config",
        "snapshotPath": "versions/v1-requirements/snapshot.json",
        "conversationsAffected": ["session-20260115-1000", ...],
        "status": "deprecated"
      },
      {
        "version": "v2",
        "createdAt": "2026-01-28T14:00:00Z",
        "currentFile": "requirements.md",
        "changelog": "versions/v2-requirements/changelog.md",
        "status": "active"
      }
    ]
  },
  "design": {...},  // Same structure
  "tasks": {...}    // Same structure
}
```

## documentReferences (NEW - Dynamic Loading)

**Purpose**: Store document paths and metadata for Resume skill to dynamically load steering documents.

**Structure**:
```json
"documentReferences": {
  "requirements": {
    "path": "specs/block-user-personal-chat/requirements.md",
    "currentVersion": "v1",        // Synced with documentVersions
    "lastModified": "2026-01-25T00:00:00Z",
    "checksum": "sha256:abc123...",  // Optional, for change detection
    "loadStrategy": "on-demand-full",
    "estimatedTokens": 2500
  },
  "design": {
    "path": "specs/block-user-personal-chat/design.md",
    "currentVersion": "v1",
    "lastModified": "2026-01-28T15:00:00Z",
    "checksum": "sha256:def456...",
    "loadStrategy": "on-demand-standard",
    "estimatedTokens": 3500
  },
  "tasks": {
    "path": "specs/block-user-personal-chat/tasks.md",
    "currentVersion": "v1",
    "lastModified": "2026-01-28T15:00:00Z",
    "checksum": "sha256:ghi789...",
    "loadStrategy": "on-demand-standard",
    "estimatedTokens": 2000
  },
  "steering": {
    "product": {
      "pathEn": ".claude/spec-workflow/steering/product.md",
      "pathZh": ".claude/spec-workflow/steering/product-zh.md",
      "loadStrategy": "on-demand-standard",
      "estimatedTokens": 1500,
      "preferredLanguage": "auto"
    },
    "structure": {
      "pathEn": ".claude/spec-workflow/steering/structure.md",
      "pathZh": ".claude/spec-workflow/steering/structure-zh.md",
      "loadStrategy": "on-demand-standard",
      "estimatedTokens": 2000,
      "preferredLanguage": "auto"
    },
    "tech": {
      "pathEn": ".claude/spec-workflow/steering/tech.md",
      "pathZh": ".claude/spec-workflow/steering/tech-zh.md",
      "loadStrategy": "on-demand-standard",
      "estimatedTokens": 1800,
      "preferredLanguage": "auto"
    }
  }
}
```

**Fields** (spec documents):
- `path`: Relative path from workspace root to the document
- `currentVersion`: Current version (must match documentVersions.{doc}.currentVersion)
- `lastModified`: File modification timestamp (ISO8601)
- `checksum`: Optional SHA256 hash for detecting file changes
- `loadStrategy`: When Resume should load this document
  - `"on-demand-quick"`: Never load (deprecated/historical)
  - `"on-demand-standard"`: Load in Standard and Full modes (active docs)
  - `"on-demand-full"`: Load only in Full mode (reference docs)
- `estimatedTokens`: Approximate token count for context budget planning

**Fields** (steering documents):
- `pathEn`: Path to English version (e.g., product.md)
- `pathZh`: Path to Chinese version (e.g., product-zh.md)
- `preferredLanguage`: Language preference
  - `"auto"`: Detect from conversation (default)
  - `"en"`: Always use English version
  - `"zh"`: Always use Chinese version
- `loadStrategy`: Same as spec documents
- `estimatedTokens`: Estimated size (for ONE language version)

**Language Detection** (Resume):
- Count Chinese characters vs English words in recent conversation
- If Chinese > English * 2: Load `-zh.md` files
- Else: Load `.md` files
- Only load ONE language version per document (saves ~1500 tokens each)

**Load Strategy Guidelines**:
- **on-demand-full**: Use for requirements.md (reference), historical docs
- **on-demand-standard**: Use for design.md, tasks.md (actively worked on)
- **on-demand-quick**: Use for old snapshots that shouldn't be loaded

**Heuristic for auto-selection**:
```
If document modified in last 3 days:
  → "on-demand-standard" (actively worked on)
Else if document is requirements.md:
  → "on-demand-full" (reference only)
Else:
  → "on-demand-standard" (default)
```

**Resume Loading Behavior**:
- **Quick mode**: No documents loaded (only chunks + sessions)
- **Standard mode**: Load documents with `loadStrategy = "on-demand-standard"`
- **Full mode**: Load documents with `loadStrategy = "on-demand-standard" OR "on-demand-full"`

**Example Resume Flow**:
```
User: 恢复上下文 (Standard mode)

Resume reads manifest.json:
  documentReferences.design.path = "specs/payment/design.md"
  documentReferences.design.loadStrategy = "on-demand-standard"
  
Resume loads:
  ✅ design.md (loadStrategy = standard)
  ✅ tasks.md (loadStrategy = standard)
  ❌ requirements.md (loadStrategy = full, but mode is standard)

Result: User gets design + tasks context automatically
```

**Checksum Usage** (Optional):
```
# Calculate checksum
checksum = sha256(file_content)

# On Resume, detect changes
if current_checksum != manifest_checksum:
  warn("design.md has changed since last checkpoint!")
  offer("Load latest version? (Y/n)")
```

## conversationTimeline

### semanticChunks

```json
"conversationTimeline": {
  "semanticChunks": [
    {
      "chunkId": "chunk-001",
      "topic": "requirements-discussion",      // Semantic topic label
      "sessionIds": ["session-20260115-1000"], // Which sessions included
      "turnRange": [1, 45],                    // Turn numbers
      "createdAt": "2026-01-16T18:00:00Z",
      "summaryPath": "conversations/semantic-chunks/chunk-001.md",
      "status": "active" | "deprecated",
      "deprecatedReason": "Requirements changed to v2",
      "relevanceDecay": 1.0,                   // 1.0 (active) or 0.05 (deprecated)
      "linkedChunks": ["chunk-002"]            // Related chunks
    }
  ],
  
  "recentSessions": [
    {
      "sessionId": "session-20260129-0900",
      "startTime": "2026-01-29T09:00:00Z",
      "endTime": "2026-01-29T11:00:00Z",
      "turns": 25,
      "phase": "implementation",
      "focusAreas": ["caching", "redis-pubsub"],
      "documentsModified": ["design.md"],
      "semanticChunks": ["chunk-004"],         // Chunks created in this session
      "weight": 1.0                            // Current weight
    }
  ]
}
```

## implLogs

```json
"implLogs": {
  "total": 12,
  "summaryPath": "impl-logs/summary-latest.md",
  "summaryLastUpdated": "2026-01-28T10:20:00Z",
  "summaryCovered": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],  // Task IDs covered in summary
  "recent": [
    {
      "file": "task-11_20260128_1015.md",
      "task": "3.1",
      "timestamp": "2026-01-28T10:15:00Z",
      "summary": "Implemented SensitiveWordMatcher",
      "linesOfCode": 156,
      "issues": []
    },
    {
      "file": "task-12_20260129_0930.md",
      "task": "3.2",
      "timestamp": "2026-01-29T09:30:00Z",
      "summary": "Added batch matching with CompletableFuture",
      "linesOfCode": 89,
      "issues": []
    }
  ]
}
```

## contextWeighting

```json
"contextWeighting": {
  "strategy": "time-decay-with-relevance",
  "parameters": {
    "recentSessionsWeight": 1.0,
    "decayHalfLife": 3,                 // Days
    "deprecatedContentWeight": 0.05,
    "activeRevisionsWeight": 0.9
  },
  "currentWeights": {
    "recent-turns": 1.0,
    "session-20260129": 1.0,
    "chunk-004": 0.85,
    "chunk-003": 0.54,
    "chunk-001": 0.009,                 // Deprecated, very low
    "impl-task-11": 0.79
  }
}
```

## loadRecommendations

```json
"loadRecommendations": {
  "quick": {
    "items": [
      {"type": "recent-turns", "sessionId": "...", "weight": 1.0, "tokens": 2000},
      {"type": "semantic-chunk", "chunkId": "chunk-004", "weight": 1.0, "tokens": 1500}
    ],
    "totalTokens": 4700,
    "loadTime": "2s"
  },
  "standard": {
    "items": [
      {"type": "recent-turns", "weight": 1.0, "tokens": 2000},
      {"type": "session-summary", "weight": 0.85, "tokens": 1800},
      {"type": "semantic-chunk", "weight": 1.0, "tokens": 1500},
      {"type": "impl-recent-logs", "count": 2, "tokens": 2500}
    ],
    "totalTokens": 12000,
    "loadTime": "4s"
  },
  "full": {
    "note": "Include deprecated contexts for historical reference",
    "totalTokens": 25000,
    "loadTime": "8s"
  }
}
```

## deprecated

```json
"deprecated": {
  "contexts": [
    {
      "id": "chunk-001",
      "type": "semantic-chunk",
      "deprecatedAt": "2026-01-28T14:00:00Z",
      "reason": "Requirements v1→v2, single-dimension discussions no longer relevant",
      "impactedSessions": ["session-20260115-1000", "session-20260116-1400"],
      "archivePath": "deprecated/chunk-001-archived.md",
      "loadOnlyIf": "user explicitly asks for historical context"
    }
  ]
}
```

## Usage in Resume Skill

The resume skill reads manifest.json to decide what to load:

```python
manifest = load_json(f"specs/{feature}/.context/manifest.json")

# Get loading recommendation
if mode == "quick":
    items = manifest["loadRecommendations"]["quick"]["items"]
elif mode == "standard":
    items = manifest["loadRecommendations"]["standard"]["items"]
else:
    items = manifest["loadRecommendations"]["full"]["items"]

# Load each item
for item in items:
    if item["type"] == "recent-turns":
        load_recent_turns(item["sessionId"])
    elif item["type"] == "semantic-chunk":
        load_chunk(item["chunkId"])
    elif item["type"] == "impl-recent-logs":
        load_recent_impl_logs(item["count"])
```

## Validation Rules

When creating/updating manifest:

1. **Required fields**: feature, manifestVersion, lifecycle, documentVersions
2. **Version consistency**: currentVersion must exist in versions array
3. **Timestamp format**: ISO 8601 (e.g., "2026-01-29T10:30:00Z")
4. **Weight bounds**: All weights in [0.0, 1.0]
5. **Status values**: Only "active", "deprecated", "superseded"
6. **Phase values**: Only "requirements", "design", "tasks", "implementation"

## Migration Guide

### v2.0 → v3.0

Added fields:
- `conversationTimeline.semanticChunks[].linkedChunks`
- `contextWeighting.parameters`
- `loadRecommendations.quick/standard/full` structure

Removed fields:
- `conversationTimeline.allChunks` (merged into semanticChunks)
