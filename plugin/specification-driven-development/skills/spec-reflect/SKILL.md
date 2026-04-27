---
name: spec-reflect
description: Extract and persist project knowledge from spec-workflow conversations. Scans user-AI dialogues for coding conventions, architecture decisions, and project patterns, then writes them back to steering documents or conventions directory. Use when (1) a spec-workflow phase completes and new learnings are detected, (2) the user wants to update project conventions, (3) the user says "remember" to persist a specific convention, or (4) the user wants to review and consolidate knowledge from recent features. Triggers include "spec-reflect", "learn conventions", "update conventions", "remember:", "学习规范", "更新规范", "总结回写", "记住这个约定".
---

# Spec-Reflect — Knowledge Extraction & Write-Back

Extract project knowledge from spec-workflow conversations and persist it to steering documents and conventions.

**Core idea**: Conversations are where knowledge is born. Documents are where knowledge lives. Spec-learn bridges the two.

## Workflow

```
Harvest → Distill → Propose → Commit
(scan)    (filter)  (review)  (write)
```

## When to Use

### Passive Trigger — Phase Completion (Primary Path)

Invoked automatically by `spec-workflow` at key phase completion points. Runs a silent scan, and **only prompts the user if new learnings are detected**. If nothing is found, the user sees nothing.

| Trigger Point | When | Scan Focus |
|---------------|------|------------|
| spec-run: All Tasks Complete | All implementation tasks finished | Coding patterns, API conventions, architecture decisions, new directory structures |
| spec-review complete | Code review report generated | User-accepted review suggestions with universal applicability |
| spec-test complete | Test generation finished | Test framework conventions, mock strategies (enhances test-conventions.md) |
| bug-verify complete | Bug fix verified | Defensive programming patterns, common pitfalls, error handling conventions |

**Action**: Follow [spec-reflect-harvest.md](references/spec-reflect-harvest.md)
- Integration: Appended naturally to the phase completion output, does not interrupt the main flow

### Active Trigger — User-Initiated

When the user explicitly wants to extract and write back knowledge.
**Action**: Follow [spec-reflect-harvest.md](references/spec-reflect-harvest.md) (Active Mode)
- Examples: "spec-reflect", "spec-reflect margin-liquidation", "spec-reflect from review", "learn conventions", "update conventions", "学习规范", "更新规范", "总结回写"

### Direct Input — Remember Mode

When the user directly provides a convention or rule to persist, skipping the scan phase.
**Action**: Follow [spec-reflect-propose.md](references/spec-reflect-propose.md) (Direct Input Mode)
- Examples: "remember: all REST APIs must return CommonResponse wrapper", "记住: BigDecimal 比较用 compareTo", "remember: BO objects must implement Serializable"

---

## Design Principles

| Principle | Description |
|-----------|-------------|
| **Don't interrupt** | Silent scan; only prompt when discoveries exist; zero UI when nothing found |
| **User decides** | All write-backs require user approval; support per-item accept/reject |
| **Incremental update** | Smart merge into existing document sections; never overwrite existing content |
| **Trace origins** | Every write-back annotated with source feature/bug and timestamp |
| **Distill, not dump** | Filter debug noise, temporary workarounds, and chat; only extract durable, reusable knowledge |
| **Dev-scoped** | Primary focus on current spec conversations; also captures strong conventions from general dev/devops/infra discussions in the same session; ignores non-development content entirely |

---

## Four-Phase Workflow

### Phase 1: Harvest (Knowledge Extraction)

Scan the current conversation context and newly created/modified files, compare against existing documentation to find incremental knowledge.

**Action**: Follow [spec-reflect-harvest.md](references/spec-reflect-harvest.md)

- Leverages what is already in the LLM context window (conversation history, loaded spec documents)
- Lightweight incremental file reads: only file signatures (imports + class declarations + public method signatures), not full implementations
- Reads existing steering + conventions as comparison baseline
- Per-phase scan focus: different phases emphasize different knowledge types

### Phase 2: Distill (Knowledge Filtering)

Built into the Harvest phase (not a separate reference file). Applies:

1. **Signal strength filtering** — only strong and medium signals pass through
2. **Deduplication** — skip knowledge already recorded in existing docs
3. **Classification** — categorize each finding by type and determine target file
4. **Conflict detection** — flag findings that contradict existing documentation
5. **Volume control** — if > 10 findings, rank by signal strength, show top items

**Output**: 0–N discoveries. If 0 → flow terminates silently, user sees nothing.

### Phase 3: Propose (Write-Back Proposal & Approval)

Present discoveries to the user for review and approval.

**Action**: Follow [spec-reflect-propose.md](references/spec-reflect-propose.md)

- Passive mode: brief summary appended to phase completion output
- Detailed review: grouped by target file, per-item accept/reject
- Conflict resolution: Update / Supplement / Keep options
- Batch operations: Accept all / Skip all / Cherry-pick

### Phase 4: Commit (Write to Documents)

Write approved knowledge to steering documents and/or conventions directory.

**Action**: Follow [spec-reflect-commit.md](references/spec-reflect-commit.md)

- Section-level smart merge (not append, not rewrite)
- Convention entry standard format with ✅/❌ code examples
- Source annotation on every write-back
- Post-write confirmation summary

---

## Knowledge Categories & Write Targets

| Knowledge Type | Typical Source | Write Target |
|----------------|---------------|--------------|
| Product insight | Requirements discussion | `steering/{module}/product.md` |
| Technical decision | Design / implementation | `steering/{module}/tech.md` |
| Directory convention | Implementation file organization | `steering/{module}/structure.md` |
| Coding standard | Code review / implementation | `conventions/coding-standards.md` |
| API convention | Interface design | `conventions/api-conventions.md` |
| Error handling pattern | Bug fix | `conventions/error-handling.md` |
| Test convention | Test generation | `conventions/test-conventions.md` |
| Security guideline | Review / implementation | `conventions/security-guidelines.md` |

---

## Multi-Module Support

Reuses spec-workflow's module-resolver mechanism:

- Check if `module-map.yaml` exists → if yes, multi-module mode
- Resolve target module using the 5-level priority chain (explicit → recent file → AI inference → single module → interactive selection)
- **Steering write-back**: goes to `steering/{module}/` for the resolved module
- **Convention write-back**: goes to global `conventions/` directory (conventions are shared across all modules)
- **Cross-module features**: follow task `_Module:` annotations for tech/structure knowledge; conventions always go to global
- **Global steering (`_global`) write-back**: knowledge that is explicitly cross-module (applies to all modules equally) goes to `steering/_global/tech.md` or `steering/_global/product.md` if `_global/` was initialized; falls back to `conventions/` if not initialized
- **Steering not initialized**: skip steering write-back for that module, only write to conventions; suggest user run `initialize module`
- **Passive mode module resolution**: in passive mode (phase completion trigger), the module context is inherited from the parent phase's already-resolved module — no separate module-resolver invocation needed
- **Remember mode module resolution**: `remember:` direct input triggers module resolution only when the knowledge is a steering category (product/tech/structure); convention categories skip module resolution entirely

---

## Shared Context

All operations access these spec-workflow directories:

| Directory | Role in spec-reflect |
|-----------|--------------------|
| `.claude/spec-workflow/steering/` | **Read** as comparison baseline; **Write** updated knowledge |
| `.claude/spec-workflow/steering/_global/` | **Read** global context (multi-module mode) |
| `.claude/spec-workflow/steering/{module}/` | **Read + Write** module-specific knowledge |
| `.claude/spec-workflow/conventions/` | **Read** as comparison baseline; **Write** new/updated conventions |
| `.claude/spec-workflow/specs/{feature}/` | **Read** feature specs (requirements, design, tasks, review reports) |
| `.claude/spec-workflow/bugs/{bug-name}/` | **Read** bug reports, analysis, fix details |
| `.claude/spec-workflow/module-map.yaml` | **Read** for module resolution (multi-module mode) |

---

## Entry Paths

This skill can be reached via two equivalent paths:
1. **Direct trigger**: User says "spec-reflect", "learn conventions", "remember:", etc. → LLM loads this skill directly
2. **Indirect trigger via spec-workflow**: User says "spec-reflect" and LLM routes through spec-workflow's "Learn / Update Project Docs" → which invokes this skill

Both paths lead to the same workflow. In passive mode (phase completion), the scan runs **inline within the current conversation** following harvest.md instructions — it is never invoked as a separate Skill tool call, because the current conversation context is the primary knowledge source.
