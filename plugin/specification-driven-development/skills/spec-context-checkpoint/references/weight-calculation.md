# Context Weight Calculation Algorithm

## Overview

Calculate relevance weights for each context piece to enable intelligent loading prioritization.

**Goal**: Load most relevant contexts first within token budget.

## Formula

```
FinalWeight = BaseWeight × DecayFactor × RelevanceBoost × StatusModifier × RecentEditBoost
```

Bounded: `FinalWeight ∈ [0, 1.0]`

## Components

### 1. Base Weight (by context type)

```python
BASE_WEIGHTS = {
    "recent-turns": 1.0,      # Highest - most recent complete conversations
    "session-summary": 0.8,   # High - summarized session context
    "impl-log": 0.8,          # High - recent implementation details
    "semantic-chunk": 0.7,    # Medium-high - topic-specific summaries
    "impl-summary": 0.6,      # Medium - historical implementation overview
    "design-section": 0.7,    # Medium-high - specific design sections
    "requirements-section": 0.5  # Medium - requirements context
}
```

### 2. Decay Factor (time-based exponential decay)

```python
def calculate_decay(timestamp, current_time, half_life_days=3.0):
    """
    Exponential decay with configurable half-life.
    Default: 3 days (contexts become 50% less relevant every 3 days)
    """
    days_old = (current_time - timestamp).days
    decay_factor = 0.5 ** (days_old / half_life_days)
    return decay_factor

# Examples:
# 0 days old  → decay = 1.0    (100%)
# 1 day old   → decay = 0.79   (79%)
# 3 days old  → decay = 0.5    (50%, one half-life)
# 6 days old  → decay = 0.25   (25%, two half-lives)
# 14 days old → decay = 0.034  (3.4%, very old)
```

**Why exponential decay?**
- Recent contexts are significantly more relevant
- Smooth degradation (no cliff at specific day)
- Matches human memory decay patterns

**Half-life tuning**:
- Short projects (<1 month): 2-day half-life
- Medium projects (1-3 months): 3-day half-life (default)
- Long projects (>3 months): 5-day half-life

### 3. Relevance Boost (topic linkage)

```python
def calculate_relevance_boost(context, current_focus):
    """
    Boost weight if context is related to current work.
    """
    boost = 1.0  # Default (neutral)
    
    # Linked to current topic
    if context.is_linked_to(current_focus):
        boost *= 1.2  # +20%
    
    # Frequently referenced
    if context.reference_count >= 3:
        boost *= 1.1  # +10%
    
    # Same phase
    if context.phase == current_focus.phase:
        boost *= 1.05  # +5%
    
    return boost

# Max boost: 1.2 × 1.1 × 1.05 = 1.386 (~38% increase)
```

**Link detection**:
- Explicit: `linkedChunks` in manifest
- Implicit: Shared files, shared keywords, same phase

### 4. Status Modifier (content lifecycle)

```python
STATUS_MODIFIERS = {
    "active": 1.0,       # Current, relevant content
    "revised": 0.9,      # Superseded by new version, but still useful
    "deprecated": 0.05,  # Obsolete due to requirement/design change
    "superseded": 0.02   # Completely replaced, load only if explicitly asked
}
```

**Deprecation triggers**:
- Requirements v1→v2: Old requirement discussions marked deprecated
- Design revision: Old approach discussions marked deprecated
- Task reorganization: Old task breakdown marked superseded

### 5. Recent Edit Boost (activity signal)

```python
def recent_edit_boost(context, current_time):
    """
    Boost recently modified/created contexts.
    """
    hours_since_edit = (current_time - context.last_modified).hours
    
    if hours_since_edit < 24:
        return 1.15  # +15% if edited in last 24 hours
    else:
        return 1.0   # No boost
```

## Complete Algorithm

```python
def calculate_context_weight(context, current_time, current_focus, parameters):
    """
    Calculate final weight for a context piece.
    
    Args:
        context: Context object with {type, timestamp, status, ...}
        current_time: Current timestamp
        current_focus: Current work focus {phase, topics, files}
        parameters: Weight calculation parameters
    
    Returns:
        float: Final weight in [0, 1.0]
    """
    # 1. Base weight
    base_weight = parameters["baseWeights"].get(context.type, 0.5)
    
    # 2. Time decay
    decay_factor = calculate_decay(
        context.timestamp, 
        current_time, 
        half_life_days=parameters["decayHalfLife"]
    )
    
    # 3. Relevance boost
    relevance_boost = calculate_relevance_boost(context, current_focus)
    
    # 4. Status modifier
    status_modifier = parameters["statusModifiers"].get(context.status, 1.0)
    
    # 5. Recent edit boost
    recent_boost = recent_edit_boost(context, current_time)
    
    # 6. Combine
    final_weight = (
        base_weight * 
        decay_factor * 
        relevance_boost * 
        status_modifier * 
        recent_boost
    )
    
    # 7. Bound to [0, 1.0]
    return min(final_weight, 1.0)


def calculate_all_weights(manifest, current_time, current_focus):
    """
    Calculate weights for all contexts in manifest.
    
    Returns:
        List[dict]: Sorted by weight descending
    """
    weights = []
    
    # Recent turns (always weight 1.0)
    weights.append({
        "id": "recent-turns",
        "type": "recent-turns",
        "weight": 1.0,
        "tokens": 2000
    })
    
    # Semantic chunks
    for chunk in manifest["conversationTimeline"]["semanticChunks"]:
        w = calculate_context_weight(chunk, current_time, current_focus, PARAMETERS)
        weights.append({
            "id": chunk["chunkId"],
            "type": "semantic-chunk",
            "weight": w,
            "tokens": estimate_tokens(chunk["summaryPath"])
        })
    
    # Recent sessions
    for session in manifest["conversationTimeline"]["recentSessions"]:
        w = calculate_context_weight(session, current_time, current_focus, PARAMETERS)
        weights.append({
            "id": session["sessionId"],
            "type": "session",
            "weight": w,
            "tokens": estimate_tokens(session)
        })
    
    # Implementation logs
    for log in manifest["implLogs"]["recent"]:
        w = calculate_context_weight(log, current_time, current_focus, PARAMETERS)
        weights.append({
            "id": log["file"],
            "type": "impl-log",
            "weight": w,
            "tokens": estimate_tokens(log["file"])
        })
    
    # Sort by weight descending
    weights.sort(key=lambda x: x["weight"], reverse=True)
    
    return weights
```

## Loading Budget Allocation

```python
def allocate_loading_budget(weights, max_tokens):
    """
    Select contexts to load within token budget.
    Greedy algorithm: load highest weight contexts first.
    """
    selected = []
    remaining_tokens = max_tokens
    
    for item in weights:
        if item["tokens"] <= remaining_tokens:
            selected.append(item)
            remaining_tokens -= item["tokens"]
        
        if remaining_tokens < 500:  # Stop if <500 tokens left
            break
    
    return {
        "selected": selected,
        "totalTokens": max_tokens - remaining_tokens,
        "remainingTokens": remaining_tokens,
        "coverageRatio": len(selected) / len(weights)
    }
```

## Example Calculation

```python
# Context: semantic chunk about caching strategy
context = {
    "type": "semantic-chunk",
    "timestamp": datetime(2026, 1, 26, 10, 0),  # 3 days ago
    "status": "active",
    "linkedChunks": ["chunk-002"],  # Links to API design chunk
    "phase": "design"
}

current_time = datetime(2026, 1, 29, 10, 0)
current_focus = {
    "phase": "design",
    "topics": ["caching", "redis"],
    "files": ["design.md"]
}

# Calculate
base_weight = 0.7  # semantic-chunk
decay_factor = 0.5 ** (3 / 3.0) = 0.5  # 3 days old, one half-life
relevance_boost = 1.2 * 1.05 = 1.26  # Linked + same phase
status_modifier = 1.0  # Active
recent_boost = 1.0  # No recent edit

final_weight = 0.7 * 0.5 * 1.26 * 1.0 * 1.0 = 0.441

# Result: Weight 0.441 (medium-high relevance)
```

## Parameters Configuration

```python
DEFAULT_PARAMETERS = {
    "baseWeights": BASE_WEIGHTS,
    "decayHalfLife": 3.0,  # Days
    "statusModifiers": STATUS_MODIFIERS,
    "maxRelevanceBoost": 1.4,
    "recentEditThresholdHours": 24,
    "recentEditBoost": 1.15
}

# Can be customized per feature in manifest:
manifest["contextWeighting"]["parameters"] = {
    "decayHalfLife": 5.0,  # Longer project, slower decay
    ...
}
```

## Validation

Test weight calculation with known scenarios:

```python
def test_weights():
    # Test 1: Recent active content should have highest weight
    recent_active = calculate_weight(
        {"type": "recent-turns", "timestamp": now(), "status": "active"}
    )
    assert recent_active >= 0.95
    
    # Test 2: Deprecated content should have very low weight
    deprecated = calculate_weight(
        {"type": "semantic-chunk", "timestamp": now(), "status": "deprecated"}
    )
    assert deprecated <= 0.1
    
    # Test 3: Old content should decay
    old_active = calculate_weight(
        {"type": "semantic-chunk", "timestamp": now() - days(6), "status": "active"}
    )
    assert old_active < 0.3  # Two half-lives = 25% base weight
    
    # Test 4: Linked content should get boost
    linked = calculate_weight(
        {"type": "semantic-chunk", "timestamp": now() - days(1), "status": "active", 
         "linkedTo": current_focus}
    )
    unlinked = calculate_weight(
        {"type": "semantic-chunk", "timestamp": now() - days(1), "status": "active"}
    )
    assert linked > unlinked
```

## Performance

- **Time complexity**: O(n) where n = number of contexts
- **Space complexity**: O(n) for weight results
- **Typical runtime**: <100ms for 50 contexts

## Future Enhancements

1. **Machine Learning**: Learn from user's loading patterns
2. **Collaborative Filtering**: Weight based on what similar users load
3. **Content Similarity**: Use embeddings to calculate semantic similarity
4. **Adaptive Parameters**: Auto-tune half-life based on project duration
