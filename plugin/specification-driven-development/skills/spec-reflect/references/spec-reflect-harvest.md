# Spec-Reflect: Harvest Phase

Extract knowledge from the current spec-workflow conversation context by scanning dialogue, spec documents, and newly created code, then comparing against existing steering and conventions documents to identify incremental learnings.

## Instructions

This phase runs in two modes depending on how spec-reflect was triggered.

> **Routing Note**: If the user input is a `remember:` / `记住:` direct convention statement, do NOT enter this file. Instead, go directly to [spec-reflect-propose.md](spec-reflect-propose.md) (Direct Input Mode). This file is only for Passive Mode and Active Mode scanning.

---

## Passive Mode (Phase Completion Trigger)

Triggered automatically when a spec-workflow phase completes (spec-run, spec-review, spec-test, bug-verify). The goal is a fast, lightweight scan that produces a brief summary without delaying the main workflow.

**Important**: Passive mode runs **within the current conversation** — it is NOT invoked as a separate skill. The current conversation history is the primary knowledge source.

### Step 1: Identify Trigger Context

Determine which phase just completed to set scan focus:

| Completed Phase | Scan Focus | Primary Signal Sources |
|-----------------|------------|----------------------|
| spec-run (All Tasks Complete) | Coding patterns, architecture decisions, new directories | Conversation: user tech decisions; Files: new packages, annotations, dependencies |
| spec-review (including Re-Review) | Universal coding standards from review | Review report: user-accepted suggestions |
| spec-test | Test framework conventions | Test generation: framework, mock strategy, naming patterns |
| bug-verify | Defensive programming, error handling | Bug analysis: root cause type; Fix: defensive patterns added |

**Re-Review Note**: When spec-review's Re-Review flow completes, passive scan is also triggered. The deduplication logic in Step 3 will automatically filter out any knowledge already written during the first review cycle, so no special handling is needed.

### Step 1.5: Resolve Module Context

Determine whether this is a single-module or multi-module workspace, and if multi-module, identify the target module(s) for correct write-back routing.

1. **Check mode**: Does `${workspace_root}/.claude/spec-workflow/module-map.yaml` exist?
   - If NOT → single-module mode. Set `resolved_modules = null`. Skip to Step 2.
   - If YES → multi-module mode. Continue below.

2. **Extract module from parent phase context**: The parent phase (spec-run, spec-review, spec-test, or bug-verify) already resolved the target module at its own Step 0. Extract this information from the current conversation context:
   - Look for the module name used in the parent phase's steering document loading (e.g., "Loading steering for module: margin-trading")
   - Or identify the module from the spec/bug directory context (e.g., tasks referencing `steering/margin-trading/tech.md`)
   - Set `primary_module` to the resolved module name

3. **Build cross-module task map** (spec-run trigger only): If the parent phase was spec-run (All Tasks Complete), scan tasks.md for `_Module:` annotations on completed tasks:
   ```
   For each completed task [X] in tasks.md:
     - If task has `_Module: {module-name}` annotation → record mapping: task-id → module-name
     - If task has no _Module: annotation → assign to primary_module
   ```
   - Set `task_module_map = { task-id: module-name, ... }`
   - Set `involved_modules = unique set of all module names from the map`

4. **Verify steering availability**: For each module in `involved_modules`:
   - Check if `steering/{module}/` directory exists
   - If not initialized → record as `uninitialized_modules` (steering writes will be skipped for these, conventions-only)

**Output of this step**: `primary_module`, `task_module_map` (if cross-module), `involved_modules`, `uninitialized_modules`

### Step 2: Gather Input Sources

**Already in LLM context (zero cost):**
- Full conversation history for this phase
- Spec documents loaded during phase execution (design.md, tasks.md, requirements.md)
- Review report (if spec-review phase)
- Bug report + analysis (if bug-verify phase)

**Incremental file reads (lightweight):**

1. **New/modified file signatures** — extract from tasks.md `[X]` completed tasks:
   - For each `File:` entry in completed tasks, read **only** the file signature:
     - `package` / `import` statements
     - Class/interface/struct declaration line
     - Public method/function signatures (name + parameters + return type)
     - Annotations/decorators on class and methods
   - **Do NOT read method bodies** — implementation details are not conventions
   - **Read strategy**: Read until end of all public method/function signatures. For Java/Kotlin (where imports are verbose), this may be ~40-60 lines. For Go/Python/Rust (where imports are compact), this may be ~20-30 lines. **Cap at 60 lines max per file** — if signatures extend beyond that, stop and use what was read.

2. **Existing steering and conventions documents** — follow the loading strategy from [context-loader.md](../../spec-workflow/references/context-loader.md) to determine which files to read:
   - **Single-module**: `${workspace_root}/.claude/spec-workflow/steering/product.md`, `tech.md`, `structure.md`
   - **Multi-module**: `steering/_global/product.md`, `_global/tech.md` (if exist) + `steering/{module}/product.md`, `tech.md`, `structure.md` (if exist)
   - **Conventions**: All `.md` files from `${workspace_root}/.claude/spec-workflow/conventions/`
   - **Degraded mode**: If steering files are missing, skip them and proceed with whatever is available. If conventions directory doesn't exist, treat all findings as "new" (no deduplication baseline).

### Step 3: Structured Knowledge Extraction

Apply the following extraction prompt internally (this is a reflection instruction for the LLM, not shown to user):

---

**Extraction Instructions:**

**STEP A — Conversation Scope Filter (apply BEFORE extraction):**

A session may contain conversations that are unrelated to the current spec feature — the user might ask ad-hoc questions, discuss other projects, or perform unrelated tasks between spec steps. Before extracting knowledge, classify each conversation segment into one of three relevance tiers:

| Tier | Description | Examples | Action |
|------|-------------|----------|--------|
| **T1 — Current Spec** | Directly related to the feature/bug being worked on in this spec phase | Task implementation discussions, design decisions for this feature, review feedback on this feature's code | **Primary scan target** — apply full extraction with all signal strengths |
| **T2 — General Dev** | Not about the current spec, but related to programming, development practices, architecture, devops, infrastructure, tooling, or engineering processes | "How should we handle DB migrations?", "What's our CI pipeline convention?", general coding style discussions, infra/deployment patterns, git workflow questions, IDE/tooling preferences | **Secondary scan target** — extract only **strong signals** (user explicitly stated a convention or rule). Medium/inferred signals from T2 content are too risky (lack spec context to validate). |
| **T3 — Non-Dev** | Unrelated to software development entirely | Personal chat, scheduling, HR topics, business strategy without technical implications, general knowledge questions | **Filter out entirely** — skip these segments |

**How to identify conversation segments:**

- A "segment" is a continuous block of user-assistant turns about the same topic
- Topic shifts are typically signaled by: the user asking an unrelated question, changing subject mid-conversation, or returning from a tangent ("OK, back to the task")
- When in doubt about whether a segment is T1 or T2, treat it as T1 (safer — the downstream deduplication and user approval will catch false positives)
- When in doubt about whether a segment is T2 or T3, treat it as T2 (prefer inclusion — development-adjacent topics may contain valuable conventions)

**T2 classification guidelines — what counts as "General Dev":**

Include as T2 (scan for strong signals):
- Coding conventions discussed outside the current feature context ("we should always use structured logging")
- Architecture patterns for other services or general design principles
- DevOps/CI/CD/deployment discussions (pipeline configs, container conventions, monitoring setup)
- Infrastructure decisions (cloud services, networking patterns, scaling strategies)
- Git/SCM workflow conventions (branching strategy, commit message format, PR rules)
- Dependency management discussions (version pinning, library selection criteria)
- Performance/observability patterns (tracing, metrics naming, alert thresholds)
- Security practices discussed in general (not feature-specific)
- Cross-team or cross-project standards mentioned in passing

Exclude from T2 (these are T3):
- Questions about non-technical business processes
- Personal productivity tools or preferences unrelated to code
- General knowledge questions ("what is the capital of France")
- Conversations about meetings, schedules, organizational matters

**Apply the filter, then proceed to STEP B with only T1 and T2 content.**

---

**STEP B — Knowledge Extraction (on filtered content):**

Based on the filtered conversation context (T1 + T2) and loaded documents, extract knowledge items that are worth persisting to project documentation.

**SCAN FOR (include):**

- User's explicit technical decisions or preferences stated in conversation
  - Signal words: "let's use", "we should always", "from now on", "以后都", "统一用"
- AI proposals that user accepted (affirmed or did not object to)
  - Signal words: "yes", "good", "agreed", "同意", "好的", "可以"
- User-accepted review suggestions that have universal applicability (not feature-specific)
- New directory/package structures created during implementation
- New dependencies, frameworks, or tools introduced
- Architecture patterns adopted in design discussions
- Defensive programming patterns added during bug fixes
- Error handling conventions established
- Naming conventions observed in newly created classes/methods
- User corrections of AI behavior or assumptions — these represent AI knowledge blind spots and are often the highest-value learnings
  - Signal words: "no", "wrong", "not right", "actually", "should be", "不对", "错了", "应该是", "不是这样"
  - Extract the CORRECT knowledge the user provided, not the incorrect assumption that was corrected

**FILTER OUT (exclude):**

- Pure debugging dialogue ("try this", "that didn't work", "let me check")
- Temporary workarounds (user said "for now", "临时方案", "先这样")
- Chat/greetings/confirmations with no technical content
- Feature-specific business logic (not reusable across features — see REUSABILITY FILTER below for detailed classification)
- One-time configuration values (port numbers, env var values)
- Knowledge already explicitly recorded in existing steering or conventions documents
- AI suggestions that user rejected or did not respond to
- **T3 content** (non-dev conversations identified in STEP A)
- **T2 medium/weak signals** — only strong signals from general dev conversations survive; medium signals ("AI proposed, user didn't object") from T2 segments are unreliable because the user may not have been paying attention to a tangential topic

**SIGNAL STRENGTH classification:**

| Strength | Criteria | Action |
|----------|----------|--------|
| **Strong** | User explicitly said "always do this" / "this is our standard" / "remember this" | Always extract |
| **Strong** | User accepted a review suggestion ("agreed", "will follow this") | Always extract |
| **Strong** | New directory/package used by multiple tasks (structural convention) | Always extract |
| **Medium** | AI proposed a pattern, user didn't object (silent acceptance) | Extract, mark as "inferred" |
| **Medium** | New dependency/framework introduced | Extract |
| **Weak** | Pattern used only once in one file | Do NOT extract |
| **Weak** | User said "for now" / "temporary" / "just this once" | Do NOT extract |
| **Noise** | Debugging, trial-and-error, chat | Filter out |

**REUSABILITY FILTER (apply AFTER signal strength, BEFORE deduplication):**

Every candidate that passed signal strength filtering must pass a reusability test before proceeding. The core question: **"Would this help someone working on a DIFFERENT feature in this project?"**

| Reusability Level | Definition | Example | Action |
|-------------------|-----------|---------|--------|
| **Universal** | Applies to any Java/Spring project | BigDecimal 比较用 compareTo | ✅ Write to conventions as-is |
| **Project-wide** | Applies across different features in this project | Repository save() 后回写自增ID、Facade 多入口统一 rate limit | ✅ Write to conventions as-is |
| **Pattern-specific** | Only relevant when using a specific technical pattern | "RPC失败后转中间态由Job重试" — 只有涉及"状态变更+外部调用"时才相关 | ⚠️ **Abstract first**: strip business-specific details (enum names, table names, specific fields), keep only the general principle + generic code example. Then write. |
| **Feature-specific** | Tied to one feature's business logic | "SCAN_STATUSES 要加 RETRYING"、"Mapper XML 要去掉 active_flag=0" | ❌ **Do NOT write** — this lives in `review-report.md` as feature-specific knowledge. It will be loaded when someone revisits this feature, but should not pollute global conventions. |

**How to apply the test:**

For each candidate item, ask these questions in order:

1. **Remove the feature name from the statement.** Does it still make sense?
   - "TargetML 的 SCAN_STATUSES 要加 RETRYING" → remove "TargetML" → "SCAN_STATUSES 要加 RETRYING" → meaningless without context → **Feature-specific** ❌
   - "Repository save() 后要回写自增ID" → already generic → **Project-wide** ✅

2. **Imagine a developer working on a completely different feature reads this.** Would they benefit?
   - "状态变更后外部调用失败要保留中间态" → yes, any feature with state + RPC needs this → **Pattern-specific** ⚠️ (abstract it)
   - "Mapper XML WHERE 要和 SCAN_STATUSES 对齐" → no, too tied to enum-scan pattern of this feature → **Feature-specific** ❌

3. **For Pattern-specific items: can you write a generic version?**
   - If yes → abstract and keep
   - If the abstraction is so vague it becomes useless ("be careful with SQL conditions") → drop it

**Why this matters — document growth projection:**

Without reusability filter: ~3-5 items per feature → 50 features → 150-250 items → **unmaintainable**
With reusability filter: ~1-2 items per feature (diminishing as project-wide patterns stabilize) → 50 features → **40-60 unique items** after dedup → sustainable

**DEDUPLICATION (semantic, cross-file):**

For each extracted item, compare against **all loaded** steering and conventions content — not just the file matching the item's category, but all `.md` files in both `steering/` and `conventions/`. This ensures that even if a convention file was renamed or knowledge was placed in a different file than expected, duplicates are still caught.

Apply these checks in order for each candidate:

**Check 1 — Exact or near-duplicate:**
The new item says essentially the same thing as an existing entry, possibly with different wording.
- "save方法要把自增主键写回BO" vs existing "Repository save() 后必须回写自增ID到Bo" → **same principle, different wording → skip**
- Test: Could you merge the two entries without adding any new information? If yes → skip.
- Action: **skip** (do not write)

**Check 2 — Subsumption (new is a specific instance of existing general principle):**
The existing convention already covers the new finding as a special case.
- Existing: "状态变更后外部调用失败必须保留中间态" → New: "Kafka 发送失败后要保留中间态" → Kafka 是外部调用的一种 → **subsumed → skip**
- Existing: "Facade层所有写入入口必须统一应用rate limit" → New: "Controller 多入口统一 rate limit" → Controller 是 Facade 的类似层 → **subsumed → skip**
- Test: Does the existing entry, if followed correctly, automatically prevent the problem described by the new item? If yes → skip.
- Action: **skip** (do not write)

**Check 3 — Generalization (new is broader than existing):**
The new finding is a more general principle that subsumes an existing entry.
- Existing: "Facade层写入入口统一rate limit" → New: "所有对外暴露的写入接口（Facade/Controller/API）必须统一rate limit" → broader scope
- Test: Does the new item cover strictly more cases than the existing one?
- Action: mark as **"update"** — the new item should replace or broaden the existing entry, not be added alongside it.

**Check 4 — Complementary (different aspect of same topic):**
The new item adds genuinely new information about a topic that has existing entries.
- Existing: "Repository save()回写ID" → New: "Repository update() 必须用乐观锁（version字段）" → same layer, different concern
- Test: Are these independent rules that a developer needs to know separately?
- Action: mark as **"new"** — write as a separate entry.

**Check 5 — Contradiction:**
The new item conflicts with an existing entry.
- Existing: "用 Kafka 做异步通信" → New: "用 RocketMQ 做异步通信"
- Action: mark as **"conflict"** — will be presented to user with U/S/K options in Propose phase.

**Summary decision table:**

| Relationship | Action | Result |
|-------------|--------|--------|
| Same thing, different words | skip | No write |
| New is specific instance of existing | skip (subsumed) | No write |
| New is broader than existing | update | Replace/broaden existing |
| New is independent, same topic | new | Add as new entry |
| New contradicts existing | conflict | User decides (U/S/K) |

**Why subsumption matters for convergence:**

As conventions accumulate, they naturally become more general (via abstraction in REUSABILITY FILTER and via generalization updates). More general principles subsume more specific future findings. This creates a natural convergence curve:
- Early features: most findings are "new" → conventions grow
- Mid-project: ~50% of findings are subsumed by existing principles → growth slows
- Mature project: ~80%+ subsumed → conventions stabilize, only genuinely novel patterns get added

Without explicit subsumption checking, the LLM tends to treat "different wording = different thing", causing linear growth instead of convergence.

**CLASSIFY each discovery:**

Assign each item:
- `category`: product | tech | structure | coding-standard | api-convention | error-handling | test-convention | security
- `target_file`: the specific file path to write to — **determined by the Module Routing Rules below**
- `target_section`: the markdown heading section to insert under (e.g., `## 缓存` or `## 数值处理`), or `NEW_SECTION` if no existing section matches
- `content`: the knowledge statement (concise, actionable)
- `code_example`: optional ✅/❌ code example if applicable
- `source`: feature/bug name + phase name
- `signal`: strong | medium
- `type`: new | update | conflict

**MODULE ROUTING RULES for `target_file`:**

The `target_file` path depends on the knowledge category AND the workspace mode (single-module vs multi-module). Use the `resolved_modules` output from Step 1.5 (Passive Mode) or Step 0 (Active Mode).

| Category | Single-Module | Multi-Module |
|----------|--------------|--------------|
| `product` | `steering/product.md` | `steering/{module}/product.md` |
| `tech` | `steering/tech.md` | `steering/{module}/tech.md` |
| `structure` | `steering/structure.md` | `steering/{module}/structure.md` |
| `coding-standard` | `conventions/coding-standards.md` | `conventions/coding-standards.md` (global) |
| `api-convention` | `conventions/api-conventions.md` | `conventions/api-conventions.md` (global) |
| `error-handling` | `conventions/error-handling.md` | `conventions/error-handling.md` (global) |
| `test-convention` | `conventions/test-conventions.md` | `conventions/test-conventions.md` (global) |
| `security` | `conventions/security-guidelines.md` | `conventions/security-guidelines.md` (global) |

**Key rule**: Convention categories (`coding-standard`, `api-convention`, `error-handling`, `test-convention`, `security`) **always write to global `conventions/`** regardless of module mode. Only steering categories (`product`, `tech`, `structure`) are module-scoped.

**Determining `{module}` for steering categories in multi-module mode:**

1. **If the knowledge originated from a specific task** (traceable via conversation context) AND `task_module_map` exists (from Step 1.5):
   - Look up the task's `_Module:` annotation in `task_module_map`
   - Use the annotated module as `{module}`

2. **If the knowledge is a general decision** (not tied to a specific task, e.g., a user preference stated during discussion):
   - Use `primary_module` as `{module}`

3. **If the knowledge is explicitly cross-module** (applies to all modules equally, e.g., "all modules must use the same Redis cluster"):
   - If `steering/_global/` exists → use `steering/_global/{category}.md`
   - If `steering/_global/` does NOT exist → write to `conventions/` as a fallback (re-classify as the closest convention category), and append a note: "(originally a global tech decision, written as convention because _global steering not initialized)"

4. **If the module's steering is not initialized** (`{module}` is in `uninitialized_modules`):
   - Skip steering write for this item
   - If the knowledge can also serve as a convention (e.g., a tech pattern that's reusable) → re-classify to convention category and write to `conventions/`
   - Otherwise → note in output: "Skipped: steering/{module}/ not initialized. Run `initialize module {module}` to enable steering write-back."

**VOLUME CONTROL:**

- If discoveries > 10: rank by signal strength (strong first), then by category importance (convention > tech > structure > product). Keep top 10, note remaining count.
- If discoveries = 0: output empty list — the flow will terminate silently.

---

### Step 4: Format Output

If discoveries ≥ 1, format as a brief summary to be appended to the phase completion output.

**Output format (appended to phase completion message):**

```
───────────────────────────────
📝 Detected {N} learnings from this {phase}:

  • [{category}] {one-line summary}
  • [{category}] {one-line summary}
  • [{category}] {one-line summary}
  {if more: "  ... and {M} more"}

L. Learn — review and write back to project docs
S. Skip — maybe later ("spec-reflect {feature-name}")
```

**If discoveries = 0:** Output nothing. Do not show any learn-related UI. The phase completion message appears exactly as it would without spec-reflect.

### Step 5: Handle User Response

- **User selects L (Learn)**: Proceed to [spec-reflect-propose.md](spec-reflect-propose.md) with the full discovery list
- **User selects S (Skip) or ignores and selects other options (A/B/C/D)**: Terminate spec-reflect flow. The main phase flow continues normally.
- **User can also defer**: "spec-reflect {feature-name}" later in a new conversation

---

## Active Mode (User-Initiated Trigger)

Triggered when user explicitly says "spec-reflect", "learn conventions", "update conventions", etc.

### Step 0: Resolve Target Module (MULTI-MODULE ONLY)

- Check if `${workspace_root}/.claude/spec-workflow/module-map.yaml` exists
- If NOT exists → single-module mode, skip to Step 1
- If exists → multi-module mode:
  - If user input contains `{module}/{feature}` format → use specified module
  - Else check the most recently edited file path → match against module-map repos
  - Else use AI semantic inference: compare user's input against each module's name, description, and repo names in module-map.yaml
    - High confidence (input contains module/repo name, or strongly matches one module) → use directly
    - Medium confidence (matches one module more than others) → use it but inform user
    - Low confidence (ambiguous across modules) → present all modules as options (A/B/C/D) and ask user to choose
  - If only one module → use directly; if unresolved → present options
- After resolving, confirm with user: "Detected target module: {module}. Reflect results will write back to this module's steering. Correct? (Y/N)"
  - If N → present all modules for selection
- After confirming, check if `steering/{module}/` exists → warn if not initialized

### Step 1: Determine Scope

Parse user input to identify what to scan:

| User Input | Scope |
|------------|-------|
| `"spec-reflect"` (no feature name) | Find the most recently completed feature in `specs/` directory (look for tasks.md with all `[X]`) |
| `"spec-reflect {feature-name}"` | Scan the specified feature |
| `"spec-reflect from review"` | Scan only the most recent review report |
| `"spec-reflect from bug {bug-name}"` | Scan the specified bug workflow |

If no feature can be determined:
- List available features in `specs/` directory
- Ask user to choose

### Step 2: Load Context

Since this is a new conversation (no phase context in window), load explicitly:

1. **Spec documents**: Read `specs/{feature}/requirements.md`, `design.md`, `tasks.md`
2. **Implementation logs** (if exist in `specs/{feature}/implementation-logs/`):
   - Read only the `## Completion Summary` or `## Changes` section of each log file
   - If no such section exists, read only the **last 20 lines** of each log
   - **Do NOT read full implementation logs** — they can be very large across many tasks. The goal is to extract key decisions and outcomes, not implementation details.
   - For high-risk tasks (multiple log files for same task ID, indicating retry attempts), read the latest log's summary more carefully.
3. **Review report**: Read if exists at `specs/{feature}/review-report.md`
4. **Bug documents**: Read `bugs/{bug-name}/report.md`, `analysis.md`, `verification.md` if bug scope
5. **New file signatures**: Extract `File:` entries from tasks.md, read signatures (same rules as Passive Mode Step 2, item 1)
6. **Existing steering + conventions**: Follow the loading strategy from [context-loader.md](../../spec-workflow/references/context-loader.md) — same rules as Passive Mode Step 2, items 2-3

### Step 3: Extract Knowledge

Apply the same Structured Knowledge Extraction instructions from Passive Mode Step 3.

### Step 4: Present Results

If discoveries ≥ 1:
- Directly proceed to [spec-reflect-propose.md](spec-reflect-propose.md) with the full discovery list (no need for the brief L/S prompt since user already explicitly requested learning)

If discoveries = 0:
```
No new learnings detected for {feature-name}.
Current steering and conventions documents are up to date.
```

---

## Error Handling

| Scenario | Handling |
|----------|----------|
| **Steering files not found** (not initialized) | Skip steering comparison baseline. All findings will lack dedup against steering, which may increase false positives — but do not block the scan. Note in output if any item targets a missing steering file. |
| **Conventions directory not found** | Treat all convention-type findings as "new" (no dedup baseline). Proceed normally — spec-reflect-commit will create convention files as needed. |
| **File signature read fails** (file deleted or moved) | Skip that file silently. Log which files were skipped. Continue scanning remaining files. |
| **No tasks.md found** (active mode) | Inform user: "No tasks.md found for {feature-name}. Cannot determine which files to scan." Suggest user specify a feature name or check the specs directory. |
| **Implementation log read fails** (active mode) | Skip implementation logs. Fall back to tasks.md `File:` entries for file list. Conversation context (if available) remains the primary source. |
| **Module resolution fails** (multi-module) | Ask user to specify module. Do not guess — wrong module leads to wrong steering comparison baseline. |
| **Scan produces unexpected error** (passive mode) | **Silently skip the entire spec-reflect prompt.** Do not show any error to the user. The main phase flow continues as if spec-reflect was never triggered. Passive mode must never block or degrade the main workflow. |

---

## Important Notes

- **Scan must be fast**: The entire Harvest phase should add minimal latency. Keep file reads to signatures only (capped at 60 lines per file).
- **Conversation is the primary source**: The LLM context window already contains the richest knowledge source. File reads are supplementary.
- **Respect existing docs**: Always compare against existing content. Never extract what's already documented.
- **Quality over quantity**: Better to surface 3 high-value conventions than 15 marginal ones.
- **Passive mode is invisible on failure**: If anything goes wrong during passive scan, fail silently. The user should never see a spec-reflect error interrupting their main workflow.
