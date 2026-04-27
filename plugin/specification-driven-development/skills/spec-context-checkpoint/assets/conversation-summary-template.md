# Conversation Summary - {CHUNK_TOPIC}

**Chunk ID**: {CHUNK_ID}  
**Turns**: {START_TURN}-{END_TURN} ({TOTAL_TURNS} conversations)  
**Duration**: {DURATION_HOURS} hours  
**Date**: {DATE_RANGE}  
**Sessions**: {SESSION_IDS}

---

## 🎯 Topic Overview

{1-2_SENTENCES_DESCRIBING_MAIN_TOPIC}

This chunk records {KEY_ASPECTS}, including:
1. {ASPECT_1}
2. {ASPECT_2}
3. {ASPECT_3}

---

## 💬 Key Discussions (Semantic Grouping)

### Discussion 1: {SUBTOPIC_1} (Turns {TURN_RANGE})

**Context**: {WHAT_WAS_BEING_DISCUSSED}

**Problem Statement**:
> {USER_QUESTION_OR_PROBLEM}

**Options Explored**:

| Option | Pros | Cons | Decision |
|--------|------|------|----------|
| {OPTION_A} | {PROS} | {CONS} | {✅/❌} |
| {OPTION_B} | {PROS} | {CONS} | {✅/❌} |

**Key Reasoning** (Turn {TURN_NUMBER}):
> {QUOTE_EXPLAINING_WHY_DECISION_WAS_MADE}

**Final Decision** (Turn {TURN_NUMBER}):
- **Chosen**: {SELECTED_OPTION}
- **Rationale**: {1-2_SENTENCES}
- **Implementation Notes**: {ANY_CAVEATS_OR_DETAILS}

**Code Outcome** (if applicable):
```{LANGUAGE}
// Only include核心片段, not full implementation
{KEY_CODE_SNIPPET}
```

---

### Discussion 2: {SUBTOPIC_2} (Turns {TURN_RANGE})

{SAME_STRUCTURE_AS_DISCUSSION_1}

---

### Discussion 3: {SUBTOPIC_3} (Turns {TURN_RANGE})

{SAME_STRUCTURE_AS_DISCUSSION_1}

---

## 📊 Outcome Summary

### Decisions Made
1. ✅ {DECISION_1}
   - **Reason**: {WHY}
   - **Impact**: {WHAT_CHANGES}

2. ✅ {DECISION_2}
   - **Reason**: {WHY}
   - **Impact**: {WHAT_CHANGES}

### Documents Updated
| File | Sections Updated | Turns |
|------|------------------|-------|
| {FILE_1} | {SECTIONS} | {TURN_RANGE} |
| {FILE_2} | {SECTIONS} | {TURN_RANGE} |

### Code Implemented (if applicable)
- {FILE_1}: {LINES_OF_CODE} lines ({SUMMARY})
- {FILE_2}: {LINES_OF_CODE} lines ({SUMMARY})

### Metrics (if applicable)
- **Performance**: {BEFORE} → {AFTER} ({IMPROVEMENT})
- **Test Coverage**: {PERCENTAGE}%
- **Estimated Impact**: {HIGH/MEDIUM/LOW}

---

## 🚧 Open Questions (Still Discussing)

1. **{QUESTION_1}**
   - **Options**: {OPTION_A} vs {OPTION_B}
   - **Current Lean**: {TENTATIVE_DIRECTION}
   - **Pending**: {WHAT_NEEDS_TO_BE_RESOLVED}

2. **{QUESTION_2}**
   - {SAME_STRUCTURE}

---

## 🎓 User's Preferences Observed (AI Internal Notes)

**Technical Level**: {SENIOR/INTERMEDIATE/JUNIOR} (observation)  
**Communication Style**: {OBSERVATIONS}  
**Decision Pattern**: {HOW_USER_MAKES_DECISIONS}

**Examples from this session**:
- Prefers {PREFERENCE_1} (Turn {N}: {EVIDENCE})
- Likes to see {PREFERENCE_2} (Turn {N}: {EVIDENCE})
- Always asks about {CONCERN} (Turns {N, M}: {EVIDENCE})

**For Next Session**:  
{HINTS_FOR_FUTURE_AI_ASSISTANT}

---

## 🔗 Related Chunks

- **{CHUNK_ID_1}** ({TOPIC}): {HOW_RELATED}
- **{CHUNK_ID_2}** ({TOPIC}): {HOW_RELATED}

---

## 🏷️ Tags

`#{TAG_1}` `#{TAG_2}` `#{TAG_3}` `#{TAG_4}`

---

**Summary Generation**: Compressed {TOTAL_TURNS} turns → {SUMMARY_WORD_COUNT} words  
**Compression Ratio**: {PERCENTAGE}%

---

## Template Usage Notes

When generating summary, remember:

1. **Emphasize WHY over WHAT**
   - Bad: "We decided to use Redis"
   - Good: "We chose Redis over Polling because real-time cache invalidation is critical for compliance (<100ms vs 5s delay)"

2. **Semantic grouping, not turn-by-turn**
   - Group related turns into logical discussions
   - Each discussion = Problem → Exploration → Decision

3. **Code snippets: Only核心**
   - Don't include full implementations
   - Show the key pattern or decision point

4. **User preferences are gold**
   - These help future AI sessions communicate effectively
   - Be specific with evidence (turn numbers, quotes)

5. **Link to related chunks**
   - Help AI understand context dependencies
   - Example: Caching chunk links to API design chunk

6. **Keep it concise but informative**
   - Target: 1000-1500 words for 20-turn chunk
   - Longer chunks (30-40 turns) → 1500-2000 words
   - Compression ratio: 80-85% (原始 → 压缩)
