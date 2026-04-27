# Loading Strategies Reference

## Overview

This document provides detailed guidance on selecting the appropriate loading mode for resuming spec feature context.

---

## Loading Modes Comparison

| Aspect | Quick | Standard | Full |
|--------|-------|----------|------|
| **Target Size** | ~5KB | ~12KB | ~25KB |
| **Load Time** | 2-3 seconds | 5-6 seconds | 10-12 seconds |
| **Contexts Loaded** | Minimal | Balanced | Complete |
| **Best For** | Very recent work | After hours/days | Long absence |
| **Token Cost** | Low | Medium | High |

---

## Mode Details

### 🚀 Quick Mode

**Target**: ~5KB (~5000 tokens)

**Contents**:
1. **Recent session** (last 5-10 turns, full detail)
   - Complete conversation turns with user/AI messages
   - File changes, code generated, decisions made
2. **Active chunk** (most recent semantic chunk)
   - Current topic being discussed
   - Latest decisions and open questions
3. **Latest decisions summary**
   - Top 3-5 most recent decisions
   - Quick reference for continuity

**When to use**:
- Last checkpoint < 4 hours ago
- User just taking short break
- Simple continuation: "Keep working on X"
- Context is fresh in user's mind
- Small feature (< 3 chunks total)

**Example scenario**:
```
User: "今天上午我们讨论了caching，现在继续"
[Last checkpoint: 2 hours ago]
→ Quick mode: Load last session (caching discussion) + active chunk
→ Time: 2 seconds
→ User continues immediately
```

**Pros**:
- ✅ Very fast (2-3s)
- ✅ Minimal token cost
- ✅ Sufficient for short breaks

**Cons**:
- ❌ Limited history (may miss context from earlier sessions)
- ❌ No implementation log details
- ❌ May need to load more if user asks about earlier decisions

---

### ⭐ Standard Mode (RECOMMENDED)

**Target**: ~12KB (~12000 tokens)

**Contents**:
1. **Quick mode content** (recent session + active chunk)
2. **Session summary**
   - Key decisions across all chunks
   - Evolution of thinking (how we got here)
   - Open questions carried forward
3. **Recent implementation logs** (last 2, full detail)
   - What was actually implemented
   - Code patterns and decisions
   - Issues encountered
4. **Related chunks** (top 2-3 by weight)
   - Chunks linked to current topic
   - Context dependencies
   - Historical decisions that inform current work

**When to use** (DEFAULT):
- Last checkpoint 4 hours to 2 days ago
- User returning after break (overnight, weekend)
- Medium-sized context (3-6 chunks, 20-60 turns)
- User needs broader context: "What did we decide about X?"
- Moderate complexity feature

**Example scenario**:
```
User: "恢复payment feature的工作"
[Last checkpoint: 1 day ago]
→ Standard mode: Recent session + session summary + related chunks + logs
→ Time: 5 seconds
→ User has full picture of decisions and progress
```

**Pros**:
- ✅ Balanced: Fast but comprehensive
- ✅ Covers most use cases
- ✅ Includes implementation context
- ✅ Sufficient for most resumes

**Cons**:
- ❌ May miss some historical details
- ❌ Doesn't load deprecated contexts (usually good, but not always)

---

### 📚 Full Mode

**Target**: ~25KB (~25000 tokens)

**Contents**:
1. **Standard mode content** (all of above)
2. **All conversation chunks** (including deprecated)
   - Complete discussion history
   - Evolution of requirements/design
   - Abandoned approaches (with reasons)
3. **Complete implementation history**
   - All implementation logs (or full summary)
   - Test coverage, metrics
   - Historical issues and resolutions
4. **Version history**
   - All snapshots (if requirements v1→v2→v3)
   - Changelogs explaining why changes happened
   - Deprecated context with reasons

**When to use**:
- Last checkpoint > 2 days ago (long absence)
- User explicitly wants: "Give me the full picture"
- Large context (> 6 chunks, > 60 turns)
- User needs to understand full evolution
- Major decisions need review
- Debugging why we made certain choices

**Example scenario**:
```
User: "我一周没看payment feature了，详细恢复一下"
[Last checkpoint: 1 week ago]
→ Full mode: Everything including deprecated chunks
→ Time: 10 seconds
→ User sees complete history: requirements v1→v2, all decisions, full evolution
```

**Pros**:
- ✅ Complete picture
- ✅ Includes deprecated contexts (understand why abandoned)
- ✅ Full implementation history
- ✅ Best for long absence

**Cons**:
- ❌ Slower (10-12s)
- ❌ Higher token cost
- ❌ May include irrelevant deprecated contexts
- ❌ Information overload if not needed

---

## Selection Decision Tree

```
                    Start
                      |
          Last checkpoint < 4 hours?
                    /   \
                 Yes     No
                  |       |
             QUICK MODE   |
                          |
              Last checkpoint < 2 days?
                        /   \
                     Yes     No
                      |       |
               STANDARD MODE  |
                              |
                   User wants "full picture"?
                            /   \
                         Yes     No
                          |       |
                     FULL MODE  STANDARD MODE
                                   (default)
```

---

## Context Loading Priority (by Weight)

### Weight Ranges

| Weight Range | Meaning | Typical Contents |
|--------------|---------|------------------|
| **0.8 - 1.0** | Highest priority | Recent session, active chunk |
| **0.5 - 0.8** | High priority | Related chunks (1-3 days old) |
| **0.3 - 0.5** | Medium priority | Historical chunks (4-7 days old) |
| **0.1 - 0.3** | Low priority | Old chunks (> 1 week old) |
| **0.0 - 0.1** | Very low priority | Deprecated contexts |

### Loading Order

All modes follow this priority:
1. Recent session (weight ~1.0)
2. Active chunk (weight ~0.7-0.9)
3. Related chunks by weight (descending)
4. Session summaries (weight ~0.6-0.8)
5. Implementation logs (weight ~0.5-0.8)
6. Historical chunks (weight ~0.2-0.5)
7. Deprecated chunks (weight ~0.02-0.05) - Full mode only

**Example weight distribution**:
```
session-20260129-1430:  1.00  ← Always loaded first
chunk-003 (caching):    0.84  ← Active topic
chunk-002 (api-design): 0.61  ← Related, 2 days old
impl-log-2:             0.58  ← Recent implementation
chunk-001 (deprecated): 0.02  ← Only in Full mode
```

---

## Token Budget Management

### Estimation

**Typical context sizes**:
- Semantic chunk summary: ~1KB (1000 tokens)
- Recent session (7 turns): ~2KB (2000 tokens)
- Implementation log: ~1.5KB (1500 tokens)
- Session summary: ~1KB (1000 tokens)
- Document (requirements/design/tasks): ~3-5KB each

**Mode budgets**:
- **Quick**: 5KB → ~2-3 contexts
- **Standard**: 12KB → ~6-8 contexts
- **Full**: 25KB → ~15-20 contexts

### Dynamic Adjustment

If contexts exceed budget:
1. **Quick mode**: Keep only recent session + active chunk (drop decisions summary if needed)
2. **Standard mode**: Keep Quick content + session summary, reduce related chunks (2→1)
3. **Full mode**: Load all, but summarize older chunks on-the-fly

If contexts under budget:
1. **Quick mode**: Add session summary or one more chunk
2. **Standard mode**: Add one more related chunk or implementation log
3. **Full mode**: Load everything (no limit)

---

## Special Cases

### Case 1: Version Change Detected (v1→v2)

**Behavior**:
- **Quick/Standard**: Skip deprecated chunks (weight ~0.02)
- **Full**: Load deprecated chunks, flag as "[DEPRECATED]" in summary

**Rationale**:
- Deprecated contexts rarely needed for continuation
- Full mode includes them for understanding evolution

**Example**:
```
requirements changed v1→v2 (1D→3D config)
chunk-001 discusses 1D approach → deprecated

Quick/Standard: Skip chunk-001 (not relevant)
Full: Load chunk-001 with note:
  "⚠️ This discussion is DEPRECATED (requirements changed to 3D config)"
```

### Case 2: Very Old Checkpoint (> 1 month)

**Recommendation**: Always suggest Full mode

**Rationale**:
- Significant context loss possible
- User likely forgot many details
- Documents may have changed externally

**Alert**:
```
⚠️ Last checkpoint is 2 months old. Recommend Full mode for complete refresh.
Many things may have changed since then.
```

### Case 3: No Conversation History

**Scenario**: Manifest exists but no chunks/sessions saved

**Behavior**: Load only documents

**Alert**:
```
ℹ️ Checkpoint exists but no conversation history saved.
Loading document state only:
- requirements.md (v1)
- design.md (v1)
- tasks.md (12 tasks)
```

### Case 4: Implementation Phase

**Adjustment**: Prioritize implementation logs over conversation chunks

**Rationale**:
- Actual code more relevant than design discussions
- Implementation logs show current patterns
- Recent code > old design decisions

**Weight boost**:
```
impl-log-recent: 0.8 → 0.9 (boost)
impl-summary: 0.6 → 0.7 (boost)
old-design-chunk: 0.5 → 0.4 (reduce)
```

---

## User Preferences

### Implicit Mode Selection

**User signals for Quick mode**:
- "继续" (continue)
- "接着做" (keep going)
- "刚才我们说到..." (we were just discussing...)
- Last activity < 4 hours ago

**User signals for Standard mode**:
- "恢复" (resume)
- "开始" (start)
- "继续做X" (continue with X)
- No specific request (default)

**User signals for Full mode**:
- "详细恢复" (detailed resume)
- "完整历史" (full history)
- "我忘了我们讨论了什么" (I forgot what we discussed)
- "好久没看了" (haven't looked in a while)
- Last activity > 2 days ago

### Explicit Mode Selection

User can always override:
```
/skill spec-context-resume payment --mode quick
/skill spec-context-resume payment --mode standard
/skill spec-context-resume payment --mode full
```

---

## Performance Considerations

### Load Time Breakdown

**Quick mode (~2-3s)**:
- Read manifest.json: 0.5s
- Read recent session JSON: 0.5s
- Read active chunk markdown: 0.5s
- Generate summary: 0.5-1s
- **Total**: 2-2.5s

**Standard mode (~5-6s)**:
- Quick mode: 2.5s
- Read 2-3 related chunks: 1-1.5s
- Read 2 impl logs: 1s
- Generate comprehensive summary: 1-1.5s
- **Total**: 5.5-6.5s

**Full mode (~10-12s)**:
- Standard mode: 6s
- Read all remaining chunks: 2-3s
- Read full impl history: 1-2s
- Generate complete summary: 2-3s
- **Total**: 11-14s

### Optimization Tips

1. **Parallel reads**: Read multiple files concurrently if tools support
2. **Lazy loading**: Load documents (requirements/design/tasks) only when user asks
3. **Incremental expansion**: Start Quick, expand to Standard/Full if user needs more
4. **Cache manifest**: If resuming same feature multiple times in session

---

## Summary

| Mode | Use When | Time | Best For |
|------|----------|------|----------|
| **Quick** | < 4 hours ago | 2-3s | Short break, fresh context |
| **Standard** | 4h-2d ago | 5-6s | After break, need refresh ⭐ |
| **Full** | > 2 days ago | 10-12s | Long absence, full picture |

**Default recommendation**: **Standard mode** - balances speed and completeness for most scenarios.

**Progressive disclosure**: Start Quick, expand if needed:
```
User: "Resume payment"
→ Quick mode (2s)

User: "What did we decide about API design earlier?"
→ Expand: Load chunk-002 (api-design)

User: "Show me full history"
→ Expand: Load all remaining chunks (Full mode)
```

This approach minimizes latency while ensuring users can access full context when needed.
