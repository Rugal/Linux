# Resume Summary Template

This template guides the AI in generating user-facing resume summaries.

---

## Template Structure

```markdown
# Resume: {Feature Name}

## 📊 Current State
- **Phase**: {requirements/design/tasks/implementation} ({X%} complete)
- **Last worked**: {2 hours ago / 1 day ago / 1 week ago}
- **Total history**: {X turns across Y sessions}
- **Checkpoint**: {timestamp} ({how long ago})

## 🎯 What We Decided

### {Topic/Category 1}
**{Decision Title}** ({when: today/yesterday/3 days ago})
- **Reason**: {1-2 sentences explaining why}
- **Impact**: {what this affects}
- **Related**: {link to chunk/session if relevant}

### {Topic/Category 2}
**{Decision Title}** ({when})
- **Reason**: {why}
- **Impact**: {what}

[Continue for top 3-5 decisions]

## 💬 Recent Conversation Context

{1-2 paragraphs summarizing last session or recent chunks}

**Last discussed**:
- **{Topic 1}**: {2-3 sentence summary}
- **{Topic 2}**: {2-3 sentence summary}
- **{Topic 3}**: {2-3 sentence summary}

**Open questions** (still unresolved):
- {Question 1}?
- {Question 2}?

## 📝 Current Documents Status

- **requirements.md**: {Complete/In-progress/Not started} (v{version})
  {If modified: "⚠️ Modified since checkpoint: {what changed}"}
- **design.md**: {Complete/In-progress/Not started} (v{version})
  {If modified: "⚠️ Modified since checkpoint: {what changed}"}
- **tasks.md**: {X tasks total: Y done, Z in-progress, W todo}
  {If modified: "⚠️ Modified since checkpoint: {what changed}"}

## 🔨 Implementation Progress

{If implementation phase}
**Completed tasks**:
- ✅ {task-1.1}: {brief description}
- ✅ {task-1.2}: {brief description}

**In progress**:
- 🔄 {task-2.1}: {brief description} ({X% done})

**Next up**:
- ⏳ {task-2.2}: {brief description}
- ⏳ {task-2.3}: {brief description}

{If not in implementation phase}
Not started yet - currently in {phase} phase.

## ⚠️ Important Notes

{Include if applicable}
- 🏷️ Requirements underwent major revision (v{old}→v{new})
  - {Brief explanation of what changed}
  - Early discussions about {old-approach} are deprecated
- ⚡ Document changes detected since checkpoint:
  - {file}: {MAJOR/MINOR changes description}
- ⏰ Long absence detected (last checkpoint {X} ago)
  - Many context details may need refresh
- 📦 Version snapshot available: `.context/versions/v{version}-{doc-type}/`

## 📚 Context Loaded ({mode} mode)

{Quick mode}
- ✅ Recent session ({X turns, {Y minutes ago})
- ✅ Active chunk ({topic})
- ✅ Latest decisions summary

{Standard mode}
- ✅ Quick mode content
- ✅ Session summary (key decisions across all chunks)
- ✅ Recent implementation logs ({count})
- ✅ Related chunks ({list topics})

{Full mode}
- ✅ Standard mode content
- ✅ All conversation chunks ({count total})
- ✅ Complete implementation history
- ✅ Deprecated contexts (flagged)

## ➡️ Next Steps

{Suggest 2-4 concrete actions based on current state}

1. **{Action 1}**: {description}
   - {Why this makes sense}
   - {What it involves}

2. **{Action 2}**: {description}
   - {Why this makes sense}

3. **{Action 3}**: {description}

{If appropriate}
4. **Or explore freely**: Ask me anything about the decisions, review specific sections, or change direction

---

✅ Context fully loaded! Ready to continue working on {feature}.

{Conversational closing matching user's style}
```

---

## Filling Instructions

### Current State
- **Phase**: Detect from manifest `lifecycle.currentPhase` or infer from documents
- **Progress**: From manifest `lifecycle.progress` or calculate from tasks.md
- **Last worked**: Calculate time difference from manifest `lifecycle.lastUpdated`
- **Total history**: From manifest `lifecycle.totalConversationTurns` and `totalSessions`

### What We Decided
- **Source**: Scan loaded chunks for decisions
- **Priority**: Show most recent and most impactful decisions
- **Count**: Top 3-5 decisions (don't overwhelm)
- **Format**: Decision → Reason → Impact (always answer "why")
- **Grouping**: Group by topic if many decisions (e.g., "API Design", "Caching")

### Recent Conversation Context
- **Source**: Recent session JSON + active chunk
- **Length**: 1-2 paragraphs, 100-200 words
- **Focus**: What was being discussed, not just what was decided
- **Open questions**: List unresolved issues carried forward

### Documents Status
- **Source**: Manifest `documentVersions` + file read (if change detection enabled)
- **Status**: Complete (all sections done), In-progress (some sections), Not started
- **Versions**: Show current version (v1, v2, etc.)
- **Changes**: Alert if modified since checkpoint

### Implementation Progress
- **Source**: tasks.md + implementation logs
- **Show**: Only if in implementation phase
- **Format**: ✅ Done, 🔄 In-progress, ⏳ Next
- **Count**: Show 2-3 completed, 1-2 in-progress, 2-3 next

### Important Notes
- **Show only if applicable**
- **Version changes**: Alert to requirement/design v1→v2
- **Document changes**: Warn about modifications
- **Long absence**: Flag if > 1 week
- **Deprecated contexts**: Note what's no longer relevant

### Context Loaded
- **Show mode**: Quick/Standard/Full
- **List contents**: What was actually loaded
- **Purpose**: Help user understand scope of resume

### Next Steps
- **Source**: Infer from current phase + progress + open questions
- **Count**: 2-4 suggestions
- **Format**: Action → Why → What
- **Balance**: Mix of "continue current work" + "alternatives"

---

## Tone & Style

### Be Conversational
- ❌ "Resuming context for feature payment-sensitive-word-detection"
- ✅ "Welcome back to payment sensitive word detection!"

### Show Continuity
- ❌ "Requirements document exists"
- ✅ "Last time we finalized requirements (v2 - changed to 3D config)"

### Be Actionable
- ❌ "Many tasks remain"
- ✅ "Ready to finish design documentation? We have 2 sections left."

### Reference Specifics
- ❌ "We made some decisions"
- ✅ "We decided on Redis pubsub for real-time cache invalidation"

### Acknowledge Time
- ❌ "Last checkpoint: 2026-01-29T14:30:00Z"
- ✅ "Last worked on this 2 hours ago"

---

## Examples by Mode

### Quick Mode Example

```markdown
# Resume: Payment Sensitive Word Detection

## 📊 Current State
- **Phase**: Design (45% complete)
- **Last worked**: 2 hours ago
- **Total history**: 27 turns across 3 sessions

## 🎯 What We Decided

**Use Redis pubsub for cache invalidation** (2 hours ago)
- **Reason**: Real-time requirement (<100ms) rules out polling
- **Impact**: Need Redis instance, add pubsub listener to services

## 💬 Recent Conversation Context

Last session we discussed **caching strategy**. Explored Redis pubsub vs polling approaches, ultimately deciding on pubsub for the <100ms real-time guarantee. Outlined cache key structure: `sw:v1:{locale}:{sensitivity}:{hash}`.

**Last discussed**:
- **Caching approach**: Redis pubsub chosen for real-time invalidation
- **Cache key design**: Three-part key structure with version prefix

## ➡️ Next Steps

1. **Continue design**: Finish remaining sections (error handling, monitoring)
2. **Review caching design**: Want to walk through the caching approach again?

---

✅ Quick context loaded! Ready to continue where we left off.

We just finished the caching section. Ready to move on to error handling?
```

### Standard Mode Example

```markdown
# Resume: Payment Sensitive Word Detection

## 📊 Current State
- **Phase**: Design (45% complete)
- **Last worked**: 1 day ago
- **Total history**: 27 turns across 3 sessions
- **Checkpoint**: 2026-01-29 14:30 (yesterday)

## 🎯 What We Decided

### API & Architecture
**REST API with async processing** (2 days ago)
- **Reason**: Sensitive word detection can take >500ms for long text
- **Impact**: POST returns job ID, GET retrieves results

### Caching & Performance
**Redis pubsub for cache invalidation** (yesterday)
- **Reason**: Real-time requirement (<100ms) rules out polling
- **Impact**: Need Redis instance, add pubsub listener to all services

### Configuration
**Three-dimensional configuration: sensitivity + locale + context** (3 days ago)
- **Reason**: Requirements evolved to handle regional + context-specific differences
- **Impact**: Deprecated earlier 1D discussion, more complex config

## 💬 Recent Conversation Context

**Session 3** (yesterday): Finalized caching strategy. Explored Redis pubsub vs polling, decided on pubsub for real-time guarantee. Designed cache key structure and invalidation flow.

**Session 2** (2 days ago): API design. Chose REST over GraphQL for simplicity. Decided on async processing for long-running detections.

**Session 1** (3 days ago): Requirements evolved from 1D to 3D configuration to handle regional differences.

**Last discussed**:
- **Caching**: Redis pubsub, cache key structure
- **API**: Async job pattern (POST → job ID, GET → results)
- **Config**: 3D model (sensitivity × locale × context)

**Open questions**:
- Error handling strategy for cache failures?
- Monitoring metrics and alerts?

## 📝 Current Documents Status
- **requirements.md**: Complete (v2 - changed to 3D config)
- **design.md**: In-progress (v1 - section 5.2 caching done)
- **tasks.md**: 12 tasks total: 0 done, 0 in-progress, 12 todo

## 🔨 Implementation Progress
Not started yet - still in design phase.

## 📚 Context Loaded (Standard mode)
- ✅ Recent session (7 turns, yesterday)
- ✅ Session summary (key decisions across 3 chunks)
- ✅ Related chunks (requirements, api-design, caching)
- ✅ Documents (requirements, design, tasks)

## ➡️ Next Steps

1. **Finish design**: Complete remaining sections (error handling, monitoring, deployment)
2. **Review & approve**: Walk through full design for approval
3. **Break down tasks**: Create detailed task breakdown once design approved
4. **Or**: Ask me anything about past decisions

---

✅ Context fully loaded! Ready to continue working on payment sensitive word detection.

Welcome back! Yesterday we wrapped up the caching design. Ready to tackle error handling next?
```

### Full Mode Example

(Similar to Standard but with additional sections showing full history, deprecated contexts, complete timeline)

---

## Customization Tips

### Match User's Language
- If user uses Chinese: Use more Chinese in summary
- If user uses English: Use English
- Mix naturally as appropriate

### Adjust Detail Level
- Quick mode: Minimal detail, just essentials
- Standard mode: Balanced detail
- Full mode: Rich detail, full context

### Highlight Changes
- If version change: Make it prominent
- If long absence: Emphasize refresh
- If document changes: Alert clearly

### Be Helpful
- Don't just list facts
- Offer actions
- Show understanding of context
- Invite questions

---

This template ensures consistent, high-quality resume summaries that help users quickly understand current state and continue work seamlessly.
