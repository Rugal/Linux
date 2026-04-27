# Weight Interpretation Reference

## Overview

This document explains how to interpret context weight values calculated by the checkpoint skill, and how they influence resume loading priority.

---

## Weight Formula Recap

From checkpoint skill:

```
FinalWeight = BaseWeight × DecayFactor × RelevanceBoost × StatusModifier

Where:
- BaseWeight: Type-based (recent-turns=1.0, chunk=0.7, log=0.8)
- DecayFactor: Time-based 0.5^(days_old/3)
- RelevanceBoost: Topic-related (1.0-1.2)
- StatusModifier: active=1.0, revised=0.9, deprecated=0.05
```

---

## Weight Ranges & Meaning

| Weight Range | Priority | Typical Contents | Action |
|--------------|----------|------------------|--------|
| **0.8 - 1.0** | **Highest** | Recent session, active chunk | Always load (all modes) |
| **0.5 - 0.8** | **High** | Related chunks (1-3 days old) | Load in Standard/Full |
| **0.3 - 0.5** | **Medium** | Historical chunks (4-7 days) | Load in Full |
| **0.1 - 0.3** | **Low** | Old chunks (> 1 week) | Load in Full only |
| **0.0 - 0.1** | **Very Low** | Deprecated contexts | Skip (unless Full + explicit) |

---

## Example Weight Calculations

### Scenario 1: Recent Active Work

```
Context: session-20260129-1430 (today, 2 hours ago)
BaseWeight: 1.0 (recent-turns type)
DecayFactor: 0.5^(0.08/3) = 0.98 (0.08 days = 2 hours)
RelevanceBoost: 1.0 (current session, always relevant)
StatusModifier: 1.0 (active)

FinalWeight = 1.0 × 0.98 × 1.0 × 1.0 = 0.98

Interpretation: Near-perfect relevance, load first
```

### Scenario 2: Recent Related Chunk

```
Context: chunk-003 (caching, today, 3 hours ago)
BaseWeight: 0.7 (semantic-chunk type)
DecayFactor: 0.5^(0.125/3) = 0.97 (0.125 days = 3 hours)
RelevanceBoost: 1.2 (linked to current active topic)
StatusModifier: 1.0 (active)

FinalWeight = 0.7 × 0.97 × 1.2 × 1.0 = 0.81

Interpretation: Very high relevance, load early
```

### Scenario 3: Related But Older Chunk

```
Context: chunk-002 (api-design, 2 days ago)
BaseWeight: 0.7 (semantic-chunk type)
DecayFactor: 0.5^(2/3) = 0.63 (one time-decay half-life)
RelevanceBoost: 1.1 (somewhat related to current work)
StatusModifier: 1.0 (active)

FinalWeight = 0.7 × 0.63 × 1.1 × 1.0 = 0.49

Interpretation: Medium relevance, load in Standard/Full modes
```

### Scenario 4: Old Historical Chunk

```
Context: chunk-001 (requirements, 1 week ago)
BaseWeight: 0.7 (semantic-chunk type)
DecayFactor: 0.5^(7/3) = 0.19 (2.3 half-lives)
RelevanceBoost: 1.0 (not specifically related)
StatusModifier: 1.0 (active)

FinalWeight = 0.7 × 0.19 × 1.0 × 1.0 = 0.13

Interpretation: Low relevance, load in Full mode only
```

### Scenario 5: Deprecated Context

```
Context: chunk-001 (old-1D-approach, deprecated due to v1→v2)
BaseWeight: 0.7 (semantic-chunk type)
DecayFactor: 0.5^(2/3) = 0.63 (2 days old)
RelevanceBoost: 1.0 (not related)
StatusModifier: 0.05 (deprecated - major penalty)

FinalWeight = 0.7 × 0.63 × 1.0 × 0.05 = 0.022

Interpretation: Almost ignore, skip unless Full mode explicitly requested
```

### Scenario 6: Implementation Log

```
Context: impl-log-task-1.2 (completed yesterday)
BaseWeight: 0.8 (impl-log type)
DecayFactor: 0.5^(1/3) = 0.79 (1/3 of half-life)
RelevanceBoost: 1.0 (standard relevance)
StatusModifier: 1.0 (active)

FinalWeight = 0.8 × 0.79 × 1.0 × 1.0 = 0.63

Interpretation: High relevance (recent work), load in Standard/Full
```

---

## Time Decay Visualization

```
Weight decay over time (BaseWeight = 0.7, no relevance boost):

Day 0 (now):     0.70 ████████████████ (100%)
Day 1:           0.61 █████████████    (87%)
Day 2:           0.53 ███████████      (76%)
Day 3:           0.47 ██████████       (67% - one half-life)
Day 4:           0.41 █████████        (59%)
Day 5:           0.36 ████████         (51%)
Day 6:           0.31 ███████          (45%)
Day 7 (1 week):  0.27 ██████           (39%)
Day 14:          0.11 ███              (16%)
Day 21:          0.04 █                (6%)

Deprecated:      0.03 █                (4% - status modifier applied)
```

**Key observation**: After ~10 days, even active contexts drop below 0.2 (low priority).

---

## Weight-Based Loading Decisions

### Quick Mode (~5KB target)

**Loading criteria**: Weight >= 0.8

**Typical contexts loaded**:
- Recent session (weight ~0.98)
- Active chunk (weight ~0.8-0.9)
- Latest decisions summary (if weight ~0.8+)

**Example**:
```
Weights:
- session-today: 0.98 ✅ Load
- chunk-003: 0.81 ✅ Load
- chunk-002: 0.49 ❌ Skip (< 0.8)
- chunk-001: 0.13 ❌ Skip

Loaded: 2 contexts (~4KB)
```

### Standard Mode (~12KB target)

**Loading criteria**: Weight >= 0.3

**Typical contexts loaded**:
- Quick mode contexts (weight >= 0.8)
- High priority chunks (weight 0.5-0.8)
- Recent implementation logs (weight ~0.6-0.8)
- Session summaries (weight ~0.6-0.7)

**Example**:
```
Weights:
- session-today: 0.98 ✅ Load
- chunk-003: 0.81 ✅ Load
- impl-log-2: 0.63 ✅ Load
- chunk-002: 0.49 ✅ Load
- session-summary: 0.68 ✅ Load
- chunk-001: 0.13 ❌ Skip (< 0.3)
- deprecated-chunk: 0.02 ❌ Skip

Loaded: 5 contexts (~11KB)
```

### Full Mode (~25KB target)

**Loading criteria**: Weight >= 0.0 (load everything)

**Typical contexts loaded**:
- Standard mode contexts
- Low priority chunks (weight 0.1-0.3)
- Deprecated contexts (weight ~0.02-0.05)
- Complete history

**Example**:
```
Weights:
- session-today: 0.98 ✅ Load
- chunk-003: 0.81 ✅ Load
- impl-log-2: 0.63 ✅ Load
- session-summary: 0.68 ✅ Load
- chunk-002: 0.49 ✅ Load
- impl-log-1: 0.41 ✅ Load
- chunk-001: 0.13 ✅ Load
- deprecated-chunk: 0.02 ✅ Load (flagged as deprecated)

Loaded: 8 contexts (~23KB)
```

---

## Relevance Boost Impact

### No Boost (1.0)

Standard contexts with no special relationship to current work.

```
chunk-002 (api-design, not related to current caching discussion)
Weight = 0.7 × 0.63 × 1.0 × 1.0 = 0.44
```

### Small Boost (1.1)

Contexts somewhat related to current work.

```
chunk-002 (api-design, caching discussion references API endpoints)
Weight = 0.7 × 0.63 × 1.1 × 1.0 = 0.48
```

### Medium Boost (1.15)

Contexts directly related to current work.

```
chunk-002 (api-design, caching depends on API design decisions)
Weight = 0.7 × 0.63 × 1.15 × 1.0 = 0.51
```

### High Boost (1.2)

Contexts tightly coupled to current work.

```
chunk-002 (api-design, caching implements API-specified behavior)
Weight = 0.7 × 0.63 × 1.2 × 1.0 = 0.53
```

**Impact**: 20% boost can move context from "skip" to "load" in Standard mode (threshold ~0.5).

---

## Status Modifier Impact

### Active (1.0)

Current, valid contexts.

```
chunk-002 (api-design, still relevant)
Weight = 0.7 × 0.63 × 1.0 × 1.0 = 0.44
```

### Revised (0.9)

Contexts that were updated/superseded but still somewhat relevant.

```
chunk-002 (api-design, minor revisions made)
Weight = 0.7 × 0.63 × 1.0 × 0.9 = 0.40
```

### Deprecated (0.05)

Contexts made obsolete by requirement/design changes.

```
chunk-001 (1D-approach, replaced by 3D config)
Weight = 0.7 × 0.63 × 1.0 × 0.05 = 0.022
```

**Impact**: Deprecated modifier drops weight to ~5% of original, effectively removing from Quick/Standard loading.

---

## Practical Weight Interpretation

### Weight 0.9-1.0: "Must Load"

**What it means**: Most recent, highly relevant content

**Contents**:
- Today's session
- Just-created chunks
- Current active work

**Action**: Load in all modes (Quick/Standard/Full)

---

### Weight 0.7-0.9: "Very Important"

**What it means**: Recent and related content

**Contents**:
- Yesterday's chunks
- Related topics
- Recent implementation

**Action**: Load in Standard/Full modes

---

### Weight 0.5-0.7: "Important"

**What it means**: Recent or somewhat related content

**Contents**:
- 2-3 day old chunks
- Tangentially related topics
- Recent logs

**Action**: Load in Standard/Full modes, prioritize in Standard

---

### Weight 0.3-0.5: "Useful Context"

**What it means**: Historical but potentially relevant

**Contents**:
- 4-7 day old chunks
- Background information
- Older implementation

**Action**: Load in Full mode only

---

### Weight 0.1-0.3: "Background Only"

**What it means**: Old, low relevance

**Contents**:
- 1-2 week old chunks
- Unrelated topics
- Historical context

**Action**: Load in Full mode only, lower priority

---

### Weight 0.0-0.1: "Usually Skip"

**What it means**: Deprecated or very old

**Contents**:
- Deprecated chunks
- Month+ old content
- Obsolete discussions

**Action**: Skip unless Full mode explicitly requested

---

## Dynamic Weight Adjustment

### Situation 1: Implementation Phase

**Adjustment**: Boost implementation logs, reduce design chunks

```
Before:
- impl-log-2: 0.63
- chunk-002 (design): 0.49

After (implementation phase detected):
- impl-log-2: 0.69 (+0.06 boost)
- chunk-002 (design): 0.44 (-0.05 reduce)
```

**Rationale**: Code matters more than design discussions during implementation.

### Situation 2: Design Review

**Adjustment**: Boost design chunks, reduce implementation logs

```
Before:
- chunk-002 (design): 0.49
- impl-log-2: 0.63

After (design review detected):
- chunk-002 (design): 0.54 (+0.05 boost)
- impl-log-2: 0.60 (-0.03 reduce)
```

**Rationale**: Design decisions matter more than implementation details during review.

### Situation 3: Requirements Change

**Adjustment**: Dramatically reduce old requirement chunks

```
Before (requirements v1):
- chunk-001 (req-v1): 0.44 (active)

After (requirements v2 detected):
- chunk-001 (req-v1): 0.02 (deprecated, status modifier 0.05 applied)
```

**Rationale**: Old requirements discussions no longer relevant.

---

## Edge Cases

### Case 1: All Weights Low (< 0.3)

**Scenario**: Feature very old (> 2 weeks since checkpoint)

**Action**: 
- Warn user: "Last checkpoint very old, all contexts have low weights"
- Recommend Full mode
- Consider re-checkpoint after resume

### Case 2: Single High-Weight Context

**Scenario**: Only 1 context with weight > 0.5, rest < 0.3

**Action**:
- Quick mode: Load that one context
- Standard mode: Load it + force-load next 2-3 contexts regardless of weight
- Rationale: Need some context, even if old

### Case 3: No Active Contexts

**Scenario**: All contexts deprecated or very old

**Action**:
- Warn user: "All conversation contexts are deprecated/outdated"
- Load documents only (requirements/design/tasks)
- Suggest: "Consider starting fresh or reviewing documents"

---

## Summary

**Key weight thresholds**:
- **>= 0.8**: Must load (Quick mode)
- **>= 0.5**: Load in Standard mode
- **>= 0.3**: Load in Full mode
- **< 0.1**: Usually skip (deprecated/obsolete)

**Weight components**:
1. **Base**: Type-dependent (0.6-1.0)
2. **Decay**: Time-dependent (exponential, half-life 3 days)
3. **Relevance**: Topic-dependent (1.0-1.2)
4. **Status**: State-dependent (0.05-1.0)

**Practical interpretation**:
- High weight (>0.7) = Recent + relevant → Load first
- Medium weight (0.3-0.7) = Useful context → Load if budget allows
- Low weight (<0.3) = Background only → Full mode or skip
- Very low (<0.1) = Deprecated → Skip unless explicit Full mode

Resume skill uses these weights to intelligently prioritize context loading, ensuring most relevant information loads first within token budget constraints.
