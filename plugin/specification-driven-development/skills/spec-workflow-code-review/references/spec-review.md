Perform a comprehensive code review of all files created/modified during a feature implementation within the spec-workflow.

## Core Principles
- **Technical Rigor over Politeness**: Identify real issues with evidence, not performative praise. If code is good, say what specifically is good. If code has problems, explain exactly why with file:line references.
- **Verify Before Suggesting**: Every issue raised must be verified against the actual codebase. Don't suggest changes based on assumptions — read the code, trace the logic, confirm the problem exists.
- **Requirements-Driven**: Every review finding should trace back to requirements, design, or established conventions. Opinions without grounding in project context are noise.
- **Convention-Aware**: Detect and enforce the project's existing conventions rather than imposing external standards. The codebase's existing patterns are the authority.
- **Severity-Honest**: Categorize issues by actual impact, not inflated urgency. A naming inconsistency is Minor, not Critical. A null pointer in production flow is Critical, not Minor.
- **YAGNI-Conscious**: Flag over-implementation. Code that goes beyond requirements scope is a maintenance burden, not a feature.
- **Pushback-Ready**: If the implementation made a reasonable choice that differs from a textbook pattern, acknowledge the reasoning. Not every deviation is a defect.

## Usage
Use natural language to trigger code review:
```
"Review code for user-authentication"
"Code review for payment-processing"
"检查代码"
"代码审查"
```

## Phase Overview
This phase runs after ALL tasks are completed (Phase 4 of spec-workflow), alongside or instead of unit test generation.

Code review is fundamentally about understanding the full picture — how files interact, how data flows across layers, whether the architecture holds together. That requires the holistic context of a single conversation, not fragmented sub-agent views. So the review runs inline in the main conversation.

## Instructions

### Step 0: Resolve Target Module (MULTI-MODULE ONLY)
- Check if `${workspace_root_directory}/.claude/spec-workflow/module-map.yaml` exists
- If NOT exists → single-module mode, proceed with existing flow
- If exists → multi-module mode:
  - Load module-map.yaml for module metadata
  - Determine which modules are involved in this feature from tasks.md `_Module:` annotations
  - Load each relevant module's steering context

### Step 1: Verify Prerequisites
- Ensure tasks.md exists at: `${workspace_root_directory}/.claude/spec-workflow/specs/{feature-name}/tasks.md`
- **Check task completion**: Scan tasks.md for uncompleted tasks (lines with `- [ ]`)
  - **If ALL tasks are completed** (`[X]`): Proceed normally
  - **If some tasks are NOT completed**: Warn user:
    ```
    ⚠️ The following tasks are not yet completed:
    - Task 3: [task description]
    - Task 7: [task description]
    
    A. Continue anyway — review only completed tasks' code
    B. Stop — complete remaining tasks first
    ```
  - If user chooses A → proceed, but only review files from completed tasks
  - If user chooses B → stop and inform user to run remaining tasks first
- Ensure requirements.md and design.md exist under same `specs/{feature-name}/` directory
- If any file doesn't exist, inform user: "Specification documents not found. Please ensure requirements, design, and tasks are created first."

### Step 2: Load Context
- **Load steering documents**:
  - **Single-module mode**: Load from `${workspace_root_directory}/.claude/spec-workflow/steering/` directory (product.md, tech.md, structure.md)
  - **Multi-module mode**:
    - Load `${workspace_root_directory}/.claude/spec-workflow/steering/_global/product.md` and `tech.md` (if exists — global background)
    - Load `${workspace_root_directory}/.claude/spec-workflow/steering/{module-name}/product.md`, `tech.md`, `structure.md` (if module initialized — focused context)
    - Merge order: global first (background), then module (focus)
- **Load conventions**: Load all `.md` files from `${workspace_root_directory}/.claude/spec-workflow/conventions/` directory if available (global, shared by all modules)
- **Load specification context**: Load requirements.md, design.md, and tasks.md from the feature directory
- **Load review report template** — resolve in this order:
  1. Check `${workspace_root_directory}/.claude/spec-workflow/user-templates/review-report-template.md`
  2. If not found → read from `${skill_directory}/assets/templates/review-report-template.md`
  3. Auto-sync: copy the fallback file to `user-templates/review-report-template.md` so the user can customize it later

### Step 3: Build Change Inventory

Use a priority cascade to determine the most accurate source of change information. The goal is to know: which files changed, what exactly changed in each file, and which areas deserve extra scrutiny.

#### 3.1 Determine Review Mode

```
Check: Is workspace a git repo with commit history?
  → Run: git rev-parse --is-inside-work-tree

If YES → GIT_DIFF mode
If NO  → FILELIST mode
```

#### 3.2a GIT_DIFF Mode (preferred)

When git is available, use it as the source of truth for what actually changed.

**Identify base commit:**
1. Check if user specified a base (e.g., "review code for X against main")
2. If not specified, try to infer:
   - `git merge-base HEAD main` or `git merge-base HEAD master` — find where the feature branched off
   - If that fails (no main/master or linear history), use the commit before the first task execution. Scan implementation-logs timestamps to estimate when implementation started, then find the nearest commit.
   - If all else fails, ask the user: "I can't determine the base commit automatically. Please specify: 'review code for {feature} against {commit/branch}'"

**Extract change inventory:**
```bash
# File list with stats
git diff --stat {base}..HEAD -- {paths-for-relevant-repos}

# Per-file diff for review
git diff {base}..HEAD -- {file-path}
```

**Review approach in GIT_DIFF mode:**
- For each changed file, read the `git diff` output instead of the full source file
- Focus review on the `+` (added) lines — these are the new code to review
- Read surrounding context from the diff's `@@` hunk headers to understand placement
- Only read the full source file when cross-file context is needed (e.g., understanding a class's full interface, or checking how a modified method is called elsewhere)

This dramatically reduces token usage: reviewing 30 changed lines in a 500-line file is much more efficient than reviewing all 500 lines.

#### 3.2b FILELIST Mode (fallback)

When git is not available, build the file list from spec-workflow artifacts.

**Priority cascade for file discovery:**

1. **Implementation logs** (most accurate):
   - Check if `${workspace_root_directory}/.claude/spec-workflow/specs/{feature-name}/implementation-logs/` exists
   - If exists, scan log file names to:
     - Build the file change list from each log's `## Changes` table (read only the first 8 lines of each log)
     - Identify **high-risk tasks**: tasks with multiple log files (e.g., `task-27` appearing 5 times means 5 execution attempts — likely a tricky area that deserves deeper review)
   - For high-risk tasks, read the full implementation log of the latest attempt to understand what was difficult

2. **tasks.md File: entries** (fallback):
   - If no implementation-logs directory, parse tasks.md `File:` entries from completed tasks
   - This is planning-level info and may not reflect the actual implementation

**Review approach in FILELIST mode:**
- Read entire source files since we don't have diff information
- Pay extra attention to files from high-risk tasks

#### 3.3 Filter to Reviewable Files

Regardless of mode, filter out files that don't need code review:
- Pure DDL migration scripts (CREATE TABLE / ALTER TABLE only)
- Auto-generated code (e.g., MyBatis XML from generators, protobuf output)
- Configuration boilerplate (application.yml changes that only add feature flags)
- Test files (tests are covered by spec-test, not code review)

#### 3.4 Present Review Map and Confirm Scope

Present a structured inventory to the user and **wait for confirmation** before starting the review:

```
📋 Review Map for {feature-name}

Review Mode: GIT_DIFF (base: abc1234)
Files to review: {N} files, {+X/-Y} lines changed

  1. path/to/ServiceImpl.java        [CREATED, +120 lines]  — Task 8
  2. path/to/Controller.java          [MODIFIED, +30/-5]     — Task 12
  3. path/to/Repository.java          [CREATED, +93 lines]   — Task 7
  ...

⚠️ High-risk areas (multiple execution attempts):
  - Task 27: IsolatedMarginPairAdminFacadeImpl.java (5 attempts)

How would you like to proceed?
A. Review all files
B. Review only specific files or layers (specify which)
C. Review only high-risk areas first
```

**Wait for user response** before proceeding to Phase 1. If the user narrows the scope, only review the specified files and note the scope limitation in the review report.

---

## Review Process

### Phase 1: Convention Detection

Before reviewing any code, understand the project's standards. The reviewer must judge code against _this project's_ conventions, not generic rules.

#### 1.1 Check Convention Cache
1. Check if `${workspace_root_directory}/.claude/spec-workflow/conventions/` contains relevant convention files
2. If conventions exist → use them as the baseline for compliance checking
3. If no conventions → detect patterns from existing codebase

#### 1.2 Detect Existing Patterns
If no explicit conventions are documented, scan the codebase for:
- Naming patterns (class, method, variable, package)
- Error handling patterns (custom exceptions, error codes, response wrappers)
- Logging patterns (framework, levels, structured vs unstructured)
- API patterns (REST conventions, request/response DTOs, validation)
- Dependency injection patterns
- Database access patterns (ORM, raw queries, repository pattern)

Read 2-3 existing files from the same packages as the changed files to establish what "normal" looks like in this project.

---

### Phase 2: Review Changed Code

Review the code using the approach determined in Step 3:
- **GIT_DIFF mode**: For each file, run `git diff {base}..HEAD -- {file}` and review the diff output. Focus on added/modified lines. Only read the full source file when you need broader context (class structure, caller analysis, etc.).
- **FILELIST mode**: Read each source file in full. If implementation-logs flagged high-risk tasks, prioritize reviewing those files first and with extra depth.

The review runs in the main conversation so the reviewer maintains full cross-file context — understanding how services call each other, how data flows through layers, and whether the overall architecture is coherent.

#### Design Alignment Check

Before diving into the code, extract key design decisions from design.md as a review checklist:
- **API design decisions**: endpoint paths, HTTP methods, request/response shapes → verify against actual controller/DTO code
- **Data model decisions**: table structure, field types, constraints → verify against entity/BO/PO definitions
- **Flow/process decisions**: call chains, validation order, error handling strategy → verify against service layer code
- **Technology decisions**: specific frameworks, patterns, third-party libs chosen → verify actual usage

Track each design decision as checked/unchecked during review. Any unchecked decision at the end means a potential misalignment.

#### Review Checklist

**Code Quality:**
- Clean separation of concerns — each class/method has a single responsibility
- Proper error handling — no swallowed exceptions, meaningful error messages, consistent error patterns
- Null safety — null checks where needed, Optional usage where appropriate
- Resource management — streams/connections properly closed, try-with-resources used
- No dead code, no commented-out code blocks, no TODO placeholders left from implementation
- Method length reasonable (no 200-line methods with nested logic)

**Architecture:**
- Implementation aligns with design.md decisions (use the Design Alignment Check above)
- Proper layering — no shortcuts (e.g., controller directly calling repository)
- Interface/abstraction usage matches project patterns
- Dependencies flow in the right direction
- No circular dependencies introduced

**Requirements Traceability:**
- Cross-reference each requirement in requirements.md against the implementation
- Flag requirements that appear under-implemented or not implemented
- Flag code that implements things not in requirements (scope creep / YAGNI)

**Convention Compliance:**
- Naming follows project patterns
- Package/directory structure follows structure.md
- API design follows project conventions
- Logging follows project patterns
- Error response format matches existing APIs

**Security:**
- Input validation present on external-facing endpoints
- No hardcoded credentials or secrets
- SQL/query parameters properly parameterized
- Authentication/authorization checks in place where needed
- Sensitive data not logged or exposed in error messages

**Performance:**
- No N+1 query patterns
- Appropriate use of caching where applicable
- No unbounded collections or missing pagination
- Proper index usage for new database queries
- Resource-intensive operations not on hot paths without reason

**Error Handling:**
- All external calls (API, DB, cache) have proper error handling
- Meaningful error messages that help debugging
- Appropriate retry/fallback strategies where needed
- Transaction boundaries correctly set

#### Cross-File Analysis

Because the review runs in the main conversation with full context, also check:
- **Data flow consistency**: DTOs/models passed between layers have consistent field names and types
- **Contract alignment** (multi-module): API contracts between services match (request/response DTOs align)
- **Error propagation**: Errors raised in lower layers are properly caught and transformed in upper layers
- **Transactional correctness**: Operations that should be atomic are wrapped in transactions

#### For Each Issue Found

Record:
- **File:line reference** — vague issues are useless
- **Severity**: Critical / Important / Minor
- **Category**: BUG, SECURITY, ARCHITECTURE, ERROR_HANDLING, EDGE_CASE, PERFORMANCE, LOGIC, CONVENTION, STYLE, NAMING, DOCUMENTATION, OPTIMIZATION, REFACTORING
- **Description**: what's wrong
- **Impact**: why it matters
- **Suggested fix**: how to address it
- **Requirement reference**: which requirement is affected (if applicable)

Also note specific **strengths** — things done well with file references. A good review acknowledges quality, not just problems.

---

### Phase 3: Generate Report

#### 3.1 Requirements Traceability Matrix
Cross-reference all requirements from requirements.md:
- For each requirement, check which files implement it
- Flag any requirement that is:
  - NOT_COVERED: No implementation found
  - PARTIALLY_COVERED: Implementation incomplete
  - OVER_IMPLEMENTED: Code goes beyond requirement scope

#### 3.2 Determine Assessment

| Assessment | Criteria |
|------------|----------|
| APPROVED | Zero Critical issues AND zero Important issues |
| APPROVED_WITH_COMMENTS | Zero Critical issues AND 1-3 Important issues that are non-blocking |
| NEEDS_REVISION | Any Critical issue OR 4+ Important issues |

#### 3.3 Generate Review Report

Use the review-report-template.md to create a structured report:
- Fill in executive summary with overall assessment
- **Review Mode** used (GIT_DIFF / FILELIST) and base commit if applicable
- **Review Scope** if user narrowed the scope in Step 3.4
- List all issues organized by severity (Critical → Important → Minor)
- Include requirements traceability matrix
- Include design alignment check results
- Include convention compliance summary
- Include security review summary
- Include performance considerations
- List all files reviewed with issue counts
- Note any high-risk areas and their findings

Save the report to: `${workspace_root_directory}/.claude/spec-workflow/specs/{feature-name}/review-report.md`

---

### Phase 4: Present Results

#### 4.1 Completion Summary

```
## Code Review Complete for {feature-name}

**Assessment**: {APPROVED | APPROVED_WITH_COMMENTS | NEEDS_REVISION}
**Review Mode**: {GIT_DIFF (base: abc1234) | FILELIST (from implementation-logs)}
**Scope**: {All files | User-specified subset}

| Severity | Count |
|----------|-------|
| Critical | {N} |
| Important | {N} |
| Minor | {N} |

Requirements Coverage: {N}/{M} fully covered
Design Alignment: {N}/{M} decisions verified

Full report saved to: specs/{feature-name}/review-report.md
```

#### 4.2 Issue Highlights

Present the important findings directly in the conversation (don't make the user go read the file for the key stuff):
- All Critical issues with file references and suggested fixes
- Top 3 Important issues
- For Minor issues, just reference the report

#### 4.3 Next Steps

Based on the assessment:

**If APPROVED:**
```
✅ Code review passed! No blocking issues found.

Would you like to:
A. Generate unit tests for this feature
B. Proceed to merge
```

**If APPROVED_WITH_COMMENTS:**
```
✅ Code review passed with comments. {N} important issues to address.

Would you like to:
A. Address the important issues now (I can help fix them)
B. Generate unit tests first, fix issues later
C. Proceed as-is
```

**If NEEDS_REVISION:**
```
⚠️ Code review found issues that should be addressed.

{List of Critical issues with brief description}

Would you like me to help fix the critical issues? After fixes, we can re-run the review.
```

#### 4.4 Spec-Reflect: Silent Scan for New Learnings

After presenting the review results and next-step options, follow the spec-reflect passive mode instructions from the `spec-reflect` skill's [spec-reflect-harvest.md](../../../spec-reflect/references/spec-reflect-harvest.md) **within the current conversation context**. Do NOT invoke spec-reflect as a separate skill via the Skill tool — the passive scan requires access to the current conversation history as its primary knowledge source. Focus on coding conventions and standards surfaced during the review.

The passive scan will:
- Silently scan the review report and conversation for user-accepted review suggestions with universal applicability
- Compare against existing conventions documents
- **If new learnings detected**: Append a learn prompt after the next-step options:
  ```
  ───────────────────────────────
  📝 Review surfaced {N} reusable conventions:

    • [{category}] {one-line summary}

  L. Learn — review and write back to project docs
  S. Skip — maybe later ("spec-reflect {feature-name}")
  ───────────────────────────────
  ```
- **If no learnings detected**: Show nothing

If user selects L → follow the `spec-reflect` skill's [spec-reflect-propose.md](../../../spec-reflect/references/spec-reflect-propose.md) to enter detailed review flow **within the current conversation**. If user selects S or ignores → continue with selected next-step option.

**Note**: In the review context, only extract conventions that the user explicitly accepted or agreed with during the review discussion. Review suggestions that the user rejected or deferred should NOT be extracted.

---

## Fix Mode

When the user wants to address review issues:

### Auto-Fix Flow
- User says: "Fix review issues for {feature-name}" or "Fix critical issues"
- Read review-report.md
- For each issue (Critical first, then Important):
  1. Read the source file
  2. Verify the issue still exists (code may have changed)
  3. Apply the fix
  4. **Post-fix validation**:
     - Run `ReadLints` on the modified file — ensure zero compilation/syntax errors
     - If lint errors found → fix them immediately before moving to the next issue
     - If the fix breaks imports (new dependency, removed class) → resolve before proceeding
  5. Move to next issue
- After all fixes applied:
  - Run `ReadLints` on ALL fixed files as a final batch check
  - Present fix summary with lint status per file
  - Offer to re-run the review

### Selective Fix
- User says: "Fix issue C-1 for {feature-name}"
- Read the specific issue from review-report.md
- Read the source file, verify the issue, apply the fix
- **Post-fix validation**: Run `ReadLints` on the modified file, fix any lint errors introduced
- Update the issue status in review-report.md

---

## Re-Review Flow

When triggered after fixes:
1. Read existing review-report.md to understand previous findings
2. Re-read the changed files (use git diff if in GIT_DIFF mode to see only the fix changes)
3. Check if previous Critical/Important issues are resolved
4. Look for new issues introduced by the fixes
5. Generate an updated review-report.md
6. Present delta: "Previously: N Critical, M Important → Now: X Critical, Y Important"

---

## Critical Rules

### Review Quality
- Every issue must include a file:line reference — vague issues are useless
- Verify against the actual code before flagging — false positives destroy trust
- Categorize by actual severity — not everything is Critical
- Acknowledge strengths — the review should identify what's done well, not just problems
- Judge by THIS project's conventions, not generic best practices
- If the code makes a reasonable design choice that differs from textbook, acknowledge the reasoning before suggesting alternatives
- In GIT_DIFF mode, only flag issues in the changed code — don't review pre-existing code that wasn't touched by the feature

### YAGNI Enforcement
- Flag code that implements functionality beyond what requirements specify
- "Professional" additions (metrics, monitoring, complex caching) that aren't in requirements should be questioned
- If you can't find a requirement for a piece of code, it might be scope creep

### Technical Rigor (borrowed from receiving-code-review principles)
- Don't suggest changes you can't verify would improve the code
- If a suggestion might break existing functionality, say so
- Consider backward compatibility
- Consider the full context — code that looks odd in isolation may make sense in the broader system

## Error Handling
- No tasks.md found → inform user to create tasks first
- No completed tasks → inform user to execute tasks first
- Source file not found → skip that file, note in report
- Convention detection fails → review against general best practices with disclaimer
- Git base commit not determinable → ask user or fall back to FILELIST mode
- Implementation-logs directory not found → fall back to tasks.md File: entries

## Example Usage
```
"Review code for user-authentication"
"Review code for margin-liquidation against main"
"Code review for target-margin-level-set against abc1234"
"检查代码"
"为 target-margin-level-set 做代码审查"
"Fix review issues for user-authentication"
"Fix critical issues for payment-processing"
"Re-review user-authentication"
```
