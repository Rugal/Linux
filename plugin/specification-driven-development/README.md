# Spec-Workflow Skill

Systematic spec-driven development workflow for feature implementation and bug fixing with validation at each stage.

## Overview

This skill provides a comprehensive workflow for systematic feature development, guiding you through Requirements → Design → Tasks → Implementation → Review → Test phases. Each phase includes validation to ensure quality before moving forward.

Additionally, it includes:
- A **streamlined bug fix workflow** for efficient bug resolution: Report → Analyze → Fix → Verify
- **Code review** as a post-implementation quality gate
- **Unit test generation** for completed features
- **Knowledge extraction (spec-reflect)** to persist project learnings from conversations
- **Context checkpoint/resume** for saving and restoring long conversation sessions

## Features

- **Phase-based workflow**: Structured progression from requirements to implementation
- **Built-in auto-validation**: Validator agents for requirements, design, and tasks — auto-fix until PASS
- **Template-driven**: Standardized templates for consistent documentation
- **Project initialization**: Quick setup with steering documents and conventions
- **Multi-module support**: Module-level initialization with `module-map.yaml` registry
- **Execution tracking**: Implementation log for progress monitoring
- **Codebase-aware**: Design validation ensures references to existing code are real
- **Code review**: Comprehensive post-implementation review with severity-based assessment
- **Test generation**: Automated unit test generation with framework detection
- **Knowledge extraction**: Auto-detect and persist project conventions from conversations
- **Bug fix workflow**: Streamlined 4-phase process for systematic bug resolution
- **Smart entry**: Bug workflow auto-detects state and triggers necessary phases
- **JIRA integration**: Auto-fetch bug details from JIRA tickets
- **Context management**: Save/restore conversation context across windows for long features

## Components

### Agents

#### spec-requirements-validator
Validates requirements documents for completeness, clarity, and quality.

**Features:**
- Template structure compliance checking
- Acceptance criteria validation
- Non-functional requirements verification
- Context alignment with project goals

#### spec-design-validator
Validates design documents for technical soundness and implementation readiness.

**Features:**
- Verifies references to existing code are real (no fabricated patterns)
- Checks architectural quality (performance, stability, security, compatibility)
- Ensures implementation-level detail with no ambiguity
- Validates module dependencies and placement

#### spec-task-validator
Validates task breakdowns for atomicity and agent-friendliness.

**Features:**
- One file edit per task validation (unlimited deletes allowed)
- Task independence checking
- Clear acceptance criteria verification
- Estimates task complexity

#### spec-task-executor
Executes individual implementation tasks with testing and documentation.

**Features:**
- Focused implementation of single tasks
- Automatic testing and validation
- Implementation log tracking
- Clean, tested code following project conventions

### Skills

#### spec-workflow (Feature Development)
Main workflow skill that orchestrates the entire spec-driven development process.

**Full Workflow:**
```
spec-init → requirements → design → tasks → spec-run → [spec-review / spec-test / spec-reflect]
```

Each phase is auto-validated before presenting to the user. The stage map (`workflow-stages.md`) is loaded before every phase as the ground truth for flow control.

**Phases:**
1. **spec-init**: Initialize project with steering documents (supports single-module and multi-module modes)
2. **spec-requirements**: Create requirements document via multi-round interview (4-8 rounds)
3. **spec-design**: Create technical design document with codebase research
4. **spec-tasks**: Break down design into atomic implementation tasks
5. **spec-run**: Execute tasks one at a time with validation
6. **all-tasks-complete**: Summary + A/B/C/D menu for next steps (test / review / both / end)

**Post-Implementation Options (offered via menu):**
- **A**: Generate unit tests → `spec-test`
- **B**: Run code review → `spec-review`
- **C**: Review first, then tests
- **D**: Skip both

#### spec-workflow (Bug Fix)
Streamlined workflow for systematic bug resolution.

**Phases:**
1. **bug-create**: Create bug report (supports JIRA integration)
2. **bug-analyze**: Investigate root cause with systematic debugging
3. **bug-fix**: Implement targeted fix based on analysis
4. **bug-verify**: Verify fix with real test execution and check for regressions

#### spec-workflow-code-review
Comprehensive code review for features built through the spec-driven development workflow.

**Where it fits:**
```
spec-init → requirements → design → tasks → spec-run → [spec-review] → spec-test
```

**Features:**
- Reviews all code created/modified during implementation
- Checks code quality, architecture alignment, requirements traceability, convention compliance
- Security, performance, and error handling review
- Produces structured review report with severity-based findings (Critical / Important / Minor)
- Assessment levels: APPROVED, APPROVED_WITH_COMMENTS, NEEDS_REVISION
- Supports fix mode and re-review after fixes
- Auto-triggers spec-reflect passive scan after review

**Trigger Phrases:**
- "review code for {feature}", "code review for {feature}", "fix review issues", "re-review {feature}"

#### spec-reflect
Extract and persist project knowledge from spec-workflow conversations back to steering documents and conventions.

**Core idea**: Conversations are where knowledge is born. Documents are where knowledge lives. Spec-reflect bridges the two.

**Workflow:**
```
Harvest → Distill → Propose → Commit
(scan)    (filter)  (review)  (write)
```

**Features:**
- **Passive trigger**: Auto-scans at phase completion (spec-run done, spec-review, spec-test, bug-verify); silent if nothing found
- **Active trigger**: User-initiated knowledge extraction ("spec-reflect", "learn conventions")
- **Remember mode**: Direct input ("remember: all REST APIs must return CommonResponse wrapper")
- **Smart merge**: Section-level merge into existing documents, never overwrite
- **Multi-module aware**: Steering goes to module dir, conventions go to global dir

**Knowledge Categories & Write Targets:**

| Knowledge Type | Write Target |
|----------------|--------------|
| Product insight | `steering/{module}/product.md` |
| Technical decision | `steering/{module}/tech.md` |
| Directory convention | `steering/{module}/structure.md` |
| Coding standard | `conventions/coding-standards.md` |
| API convention | `conventions/api-conventions.md` |
| Error handling pattern | `conventions/error-handling.md` |
| Test convention | `conventions/test-conventions.md` |
| Security guideline | `conventions/security-guidelines.md` |

**Trigger Phrases:**
- "spec-reflect", "learn conventions", "update conventions", "remember: {convention}"

#### spec-context-checkpoint
Save all conversation context from the current window for a spec feature so it can be resumed later in a new window.

**Features:**
- Saves all dialogue turns (full content, not summaries)
- Filters out skill trigger messages (save/resume commands)
- Assigns global turn numbers for tracking
- Auto-generates summaries for old sessions when total turns >= 20
- Updates manifest with session info

**When to use:**
- Long conversations approaching context window limits
- Reaching important milestones
- Before switching to a different feature

**Trigger Phrases:**
- "save", "checkpoint", "save context for {feature}", "保存"

#### spec-context-resume
Restore previously saved conversation context for a feature in a new window.

**Features:**
- Loads recent session conversations (up to 12 turns, full content)
- Loads historical summaries (compressed semantic chunks)
- Loads document previews (requirements, design, tasks)
- Initial load < 15KB, more content on demand

**When to use:**
- Starting a new window to continue work on a feature
- After a checkpoint was saved

**Trigger Phrases:**
- "resume {feature}", "restore {feature}", "恢复 {feature}", "继续 {feature}"

## Installation

Install as a local plugin:

```bash
claude plugin install ~/.claude/plugins/local/specification-driven-development
```

Or add the marketplace:

```bash

```

## Quick Start

### 1. Initialize Project

First, set up the project structure with steering documents:

```
"initialize project"
```

This creates:
- `.claude/spec-workflow/steering/` - product.md, tech.md, structure.md
- `.claude/spec-workflow/conventions/` - coding standards, API conventions
- `.claude/spec-workflow/user-templates/` - document templates

For multi-module projects:
```
"initialize module margin-trading"
```
This creates module-specific steering docs and registers the module in `module-map.yaml`.

### 2. Create Feature Specification

Follow the workflow for your feature:

```
"create requirements for user authentication"
→ Creates .claude/spec-workflow/specs/user-auth/requirements.md
→ Auto-validates via spec-requirements-validator, fixes until PASS

"create design for user authentication"
→ Creates .claude/spec-workflow/specs/user-auth/design.md
→ Auto-validates via spec-design-validator, fixes until PASS

"create tasks for user authentication"
→ Creates .claude/spec-workflow/specs/user-auth/tasks.md
→ Auto-validates via spec-task-validator, fixes until PASS
```

### 3. Execute Implementation

Once tasks are validated:

```
"execute task task-1"
→ Runs spec-task-executor agent for specific task
→ Updates implementation-log.md
→ Waits for user before proceeding to next task
```

### 4. Post-Implementation (A/B/C/D Menu)

When all tasks are complete, the system presents a menu:

```
A: Generate unit tests
B: Run code review
C: Review first, then tests
D: Skip both
```

### 5. Save and Resume Context

For long conversations:
```
"save context for user-auth"     → Checkpoint
[open new window]
"resume user-auth"               → Restore and continue
```

## Workflow Diagrams

### Feature Development Workflow

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│  spec-init  │ ──▶ │ requirements│ ──▶ │   design    │ ──▶ │    tasks    │
│  (Phase 0)  │     │  (Phase 1)  │     │  (Phase 2)  │     │  (Phase 3)  │
└─────────────┘     └──────┬──────┘     └──────┬──────┘     └──────┬──────┘
                          │                   │                   │
                          ▼                   ▼                   ▼
                   [auto-validate]     [auto-validate]     [auto-validate]
                   [fix until PASS]    [fix until PASS]    [fix until PASS]
                          │                   │                   │
                          ▼                   ▼                   ▼
                    [user approve]      [user approve]      [user approve]
                                                                  │
                                                                  ▼
                                                           ┌─────────────┐
                                                           │  spec-run   │
                                                           │  (Phase 4)  │
                                                           └──────┬──────┘
                                                                  │
                                                                  ▼
                                                        ┌─────────────────┐
                                                        │ all-tasks-done  │
                                                        │ + spec-reflect  │
                                                        │  passive scan   │
                                                        └──────┬──────────┘
                                                               │
                                              ┌────────────────┼────────────────┐
                                              ▼                ▼                ▼
                                       ┌────────────┐  ┌────────────┐  ┌────────────┐
                                       │ spec-review│  │ spec-test  │  │   (end)    │
                                       │ + reflect  │  │ + reflect  │  │            │
                                       └────────────┘  └────────────┘  └────────────┘
```

### Bug Fix Workflow

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│ bug-create  │ ──▶ │ bug-analyze │ ──▶ │  bug-fix    │ ──▶ │ bug-verify  │
│  (Report)   │     │ (Root Cause)│     │(Implement)  │     │  (Verify)   │
└─────────────┘     └─────────────┘     └─────────────┘     └──────┬──────┘
      │                   │                   │                    │
      ▼                   ▼                   ▼                    ▼
 report.md           analysis.md        [code changes]     verification.md
                                                             + spec-reflect
                                                              passive scan
```

**Smart Entry Feature**: Any phase can be an entry point. The system auto-detects bug state and triggers necessary preceding phases.

| User Command | Bug State | System Action |
|--------------|-----------|---------------|
| `bug-fix X` | Not exists | Auto: create → analyze → fix |
| `bug-fix X` | No analysis | Auto: analyze → fix |
| `bug-fix X` | Analyzed | Proceed to fix |
| `bug-analyze X` | Not exists | Auto: create → analyze |
| `bug-fix JIRA-123` | Not exists | Fetch JIRA → create → analyze → fix |

### Context Management Workflow

```
┌────────────────────┐     ┌───────────────┐     ┌────────────────────┐
│  Long conversation │ ──▶ │  checkpoint   │ ──▶ │  New window        │
│  (any phase)       │     │  (save all    │     │  "resume {feature}"|
│                    │     │   turns)      │     │  (load context)    │
└────────────────────┘     └───────────────┘     └────────────────────┘
```

## Usage Examples

### Example 1: Complete Feature Development

```
User: "I need to add user authentication feature"

Step 1: Initialize (if not done)
> "initialize project"

Step 2: Create requirements
> "create requirements for user authentication"
[System creates requirements.md via multi-round interview]
[Auto-validates, fixes until PASS]

Step 3: Create design
> "create design for user authentication"
[System creates design.md with codebase research]
[Auto-validates, fixes until PASS]

Step 4: Break into tasks
> "create tasks for user authentication"
[System creates tasks.md with atomic implementation tasks]
[Auto-validates, fixes until PASS]

Step 5: Execute tasks (one at a time)
> "execute task task-1"
> "execute task task-2"
[Each task waits for user before proceeding]

Step 6: All tasks done → A/B/C/D menu
> Select "C" (review first, then tests)

Step 7: Code review
[Generates review-report.md]
[Auto-triggers spec-reflect scan]

Step 8: Generate tests
[Generates unit tests for all task files]
[Auto-triggers spec-reflect scan]
```

### Example 2: Quick Start for Small Feature

```
User: "Add password reset functionality"

> "initialize project"  # If first time
> "create requirements for password reset"
[Auto-validated, user approves]
> "create design for password reset"
[All code references verified as real]
> "create tasks for password reset"
[One-file-per-task rule verified]
> "execute task task-1"
[Implement and track progress]
```

### Example 3: Bug Fix Workflow (Full Process)

```
User: "There's a login timeout bug"

Step 1: Create bug report
> "bug-create login-timeout"
[System creates .claude/spec-workflow/bugs/login-timeout/report.md]

Step 2: Analyze root cause
> "bug-analyze login-timeout"
[System investigates and creates analysis.md with root cause]

Step 3: Implement fix
> "bug-fix login-timeout"
[System implements fix based on analysis]

Step 4: Verify fix
> "bug-verify login-timeout"
[System verifies fix with real test execution and creates verification.md]
[Auto-triggers spec-reflect passive scan]
```

### Example 4: Bug Fix with Smart Entry (Quick Fix)

```
User: "Fix JIRA-1234"

> "bug-fix JIRA-1234"
[System auto-detects bug doesn't exist]
[Auto: fetch JIRA details → create report → analyze → fix]

# Or describe the issue directly
> "fix bug: users can't login after password change"
[System auto: create → analyze → fix]
```

### Example 5: Check Bug Status

```
> "bug-status"
[Shows all bugs with their current phase]

Bug Status Overview:
┌─────────────────┬──────────┬────────┬─────┬────────┐
│ Bug Name        │ Report   │Analysis│ Fix │ Verify │
├─────────────────┼──────────┼────────┼─────┼────────┤
│ login-timeout   │ ✓        │ ✓      │ ⟳   │ ○      │
│ memory-leak     │ ✓        │ ○      │ ○   │ ○      │
│ JIRA-1234       │ ✓        │ ✓      │ ✓   │ ✓      │
└─────────────────┴──────────┴────────┴─────┴────────┘

> "bug-status login-timeout"
[Shows detailed status for specific bug]
```

### Example 6: Code Review

```
User: "review code for user-authentication"

[Builds change inventory from tasks.md]
[User confirms review scope]
[Reviews code inline: quality, architecture, conventions, security, performance]
[Generates review-report.md with assessment]
[Auto-triggers spec-reflect scan]

Assessment: APPROVED_WITH_COMMENTS
- 0 Critical, 2 Important, 5 Minor issues found
```

### Example 7: Knowledge Extraction

```
# Active mode
> "spec-reflect user-authentication"
[Scans conversations for coding conventions, architecture decisions]
[Proposes write-backs to steering docs and conventions]
[User approves per item]
[Writes to documents]

# Remember mode
> "remember: all REST APIs must return CommonResponse wrapper"
[Directly writes to conventions/api-conventions.md]

# Passive mode (automatic)
[After spec-run completes all tasks]
[Silent scan — only prompts if new learnings detected]
```

### Example 8: Save and Resume Context

```
# Save (in current window)
> "save context for user-authentication"
✅ Saved! session-002, 10 turns, global #16-25

# Resume (in new window)
> "resume user-authentication"
[Loads recent 12 turns + historical summaries + document previews]
[Ready to continue working]
```

## Directory Structure

After initialization, your project will have:

```
.claude/spec-workflow/
├── steering/
│   ├── product.md          # Product vision and goals (single-module mode)
│   ├── tech.md             # Technical standards
│   ├── structure.md        # Project structure
│   ├── _global/            # Multi-module: shared product.md, tech.md
│   └── {module}/           # Multi-module: module-specific steering docs
├── conventions/
│   ├── coding-standards.md # Code style guidelines
│   ├── api-conventions.md  # API design patterns
│   ├── error-handling.md   # Error handling conventions
│   ├── test-conventions.md # Test framework conventions
│   └── security-guidelines.md
├── module-map.yaml         # Multi-module: module registry (module-to-repo mapping)
├── user-templates/
│   ├── requirements-template.md
│   ├── design-template.md
│   ├── tasks-template.md
│   ├── implementation-log.md
│   ├── module-map-template.yaml
│   ├── test-plan-template.md
│   ├── bug-report-template.md
│   ├── bug-analysis-template.md
│   └── bug-verification-template.md
├── specs/
│   └── {feature-name}/
│       ├── requirements.md
│       ├── design.md
│       ├── tasks.md
│       ├── implementation-log.md
│       ├── review-report.md        # Generated by spec-review
│       └── .context/               # Generated by checkpoint/resume
│           ├── manifest.json
│           └── conversations/
│               ├── semantic-chunks/
│               │   └── chunk-001.md
│               └── sessions/
│                   └── session-001/
│                       ├── metadata.json
│                       └── turn-*.md
└── bugs/
    └── {bug-name}/
        ├── report.md
        ├── analysis.md
        └── verification.md
```

## Trigger Phrases

### Initialize Project
- "initialize project", "setup project", "create steering docs"
- "initialize module {name}" (multi-module)
- "初始化项目", "设置项目结构", "初始化模块"

### Create Requirements
- "create requirements for {feature}"
- "创建需求", "创建 {feature} 的需求"

### Create Design
- "create design for {feature}"
- "创建设计", "创建 {feature} 的设计"

### Create Tasks
- "create tasks for {feature}"
- "创建任务", "创建 {feature} 的任务"

### Execute Task
- "execute task {id}", "implement task {id}"
- "执行任务", "执行 task-1"

### Code Review
- "review code for {feature}", "code review for {feature}"
- "fix review issues for {feature}", "re-review {feature}"
- "检查代码", "代码审查"

### Generate Tests
- "generate tests for {feature}", "create unit tests for {feature}"
- "generate test 1 for {feature}", "generate all tests for {feature}"
- "生成测试", "生成单元测试"

### Knowledge Extraction (spec-reflect)
- "spec-reflect", "spec-reflect {feature}"
- "learn conventions", "update conventions"
- "remember: {convention}"
- "学习规范", "更新规范", "总结回写", "记住这个约定"

### Validators
- "validate requirements" → spec-requirements-validator
- "validate design" → spec-design-validator
- "validate tasks" → spec-task-validator
- "验证/校验/审核需求/设计/任务"

### Bug Workflow
- **bug-create**: "bug-create {name}", "bug-create JIRA-123", "create bug report", "report bug"
- **bug-analyze**: "bug-analyze {name}", "analyze bug", "investigate bug"
- **bug-fix**: "bug-fix {name}", "fix bug", "fix this issue: {description}"
- **bug-verify**: "bug-verify {name}", "verify fix", "verify bug fix"
- **bug-status**: "bug-status", "bug-status {name}", "show bugs"
- "创建bug", "分析bug", "修复bug", "验证修复", "查看bug状态"

### Context Management
- **Checkpoint**: "save", "checkpoint", "save context for {feature}", "保存"
- **Resume**: "resume {feature}", "restore {feature}", "恢复 {feature}", "继续 {feature}"

## Key Validation Features

### Requirements Validation
- Template compliance
- Clear and testable acceptance criteria
- Proper scope and dependencies
- Non-functional requirements coverage

### Design Validation
- **Codebase reality check**: Ensures all referenced code exists (no fabrication)
- Architectural quality factors (performance, stability, security)
- Implementation-ready detail (no ambiguity)
- Module dependencies and placement correctness
- Integration with existing systems

### Task Validation
- One file edit per task (unlimited deletes OK)
- Task independence verification
- Clear acceptance criteria for each task
- Complexity estimation

### Code Review Assessment

| Level | Meaning | Action |
|-------|---------|--------|
| APPROVED | No Critical or Important issues | Proceed to tests or merge |
| APPROVED_WITH_COMMENTS | No Critical, few Important | Address Important issues, then proceed |
| NEEDS_REVISION | Critical issues found | Fix Critical issues, re-review |

## Spec-Reflect: Passive Scan Trigger Points

| Trigger Point | When | Scan Focus |
|---------------|------|------------|
| spec-run: All Tasks Complete | All implementation tasks finished | Coding patterns, API conventions, architecture decisions |
| spec-review complete | Code review report generated | User-accepted review suggestions |
| spec-test complete | Test generation finished | Test framework conventions, mock strategies |
| bug-verify complete | Bug fix verified | Defensive programming patterns, error handling |

## Best Practices

### Feature Development
1. **Always initialize first**: Run "initialize project" before creating specs
2. **Trust auto-validation**: Each phase auto-validates and fixes before presenting to user
3. **Fix issues before proceeding**: Address validator feedback before moving to next phase
4. **Keep steering docs updated**: Use spec-reflect to auto-capture conventions
5. **One feature at a time**: Focus on completing one feature's workflow before starting another
6. **Trust the validators**: Design validator checks if code references are real
7. **Use context save/resume**: For long features, checkpoint regularly to avoid losing context
8. **Don't skip the A/B/C/D menu**: After all tasks complete, choose review/test/both for quality

### Bug Fixing
1. **Don't skip analysis**: Always analyze root cause before fixing
2. **Follow systematic debugging**: Use root cause tracing, not quick patches
3. **Verify with real tests**: bug-verify must execute actual tests, not just code analysis
4. **Document findings**: Keep analysis.md updated with investigation results
5. **Use JIRA integration**: Provide JIRA ticket ID for automatic context fetching
6. **Smart entry is your friend**: Just say "fix bug X" and let the system handle phases

### Knowledge Management
1. **Let passive scans work**: Don't dismiss spec-reflect prompts — they capture valuable conventions
2. **Use "remember:" for quick rules**: Directly persist conventions without scanning
3. **Review before committing**: Always approve/reject spec-reflect proposals individually
4. **Keep conventions global**: Coding conventions apply across all modules

## Troubleshooting

### Feature Workflow Issues

**Templates not found**: Run "initialize project" to create template files

**Validator fails**: Check that steering documents exist in `.claude/spec-workflow/steering/`

**Design validator flags fabricated code**: Ensure all referenced files, classes, methods actually exist in your codebase

**Task execution fails**: Verify tasks.md is validated and approved first

**Context window running out**: Use "save" to checkpoint, then "resume {feature}" in a new window

### Bug Workflow Issues

**Bug already exists**: Use "bug-status {name}" to check current state, then continue from appropriate phase

**Analysis incomplete**: Ensure root cause is identified before proceeding to fix

**JIRA fetch fails**: Check JIRA credentials and ticket ID format (PROJECT-123)

**Verification fails**: Review analysis.md to ensure fix addresses the actual root cause

### Code Review Issues

**Review scope too large**: Confirm change inventory with user before starting review

**NEEDS_REVISION assessment**: Fix critical issues first, then "re-review {feature}"

### Spec-Reflect Issues

**No findings detected**: Normal — passive scan is silent when nothing new is found

**Wrong target file**: Review the proposed write-back target before approving

**Conflict with existing docs**: Choose Update / Supplement / Keep when conflicts are detected

## Requirements

- Claude Code >= 0.1.0
- Python 3.7+ (for initialization script)

## Version

1.3.0 - Added spec-context-checkpoint, spec-context-resume for conversation context management
1.2.0 - Added spec-reflect for knowledge extraction & write-back, spec-workflow-code-review for post-implementation review, spec-test for unit test generation, multi-module support
1.1.0 - Added bug fix workflow (bug-create, bug-analyze, bug-fix, bug-verify, bug-status)
1.0.0 - Initial release

## Author

Rugal Bernstein

## License

MIT
