---
name: spec-workflow
description: This skill provides a spec-driven development workflow for systematic feature implementation with validation at each stage. Use when the user wants to (1) initialize a project with steering documents, (2) create feature specifications (requirements, design, tasks), (3) validate specification documents (requirements, design, tasks), (4) execute implementation tasks, (5) generate unit tests for a completed feature, (6) review code for a completed feature, (7) create/analyze/fix/verify bugs, (8) asks about spec-driven development workflow, (9) extract and persist project knowledge from conversations, (10) save/checkpoint conversation context for a feature, or (11) resume/restore conversation context for a feature. Triggers include "create spec", "create requirements/design/tasks", "initialize project", "initialize module", "validate requirements/design/tasks", "execute task", "generate tests", "create unit tests", "code review", "review code", "检查代码", "代码审查", "spec-reflect", "learn conventions", "update conventions", "remember:", "学习规范", "更新规范", "总结回写", "记住这个约定", "bug-create", "bug-analyze", "bug-fix", "bug-verify", "bug-status", "创建规范", "初始化项目", "初始化模块", "执行任务", "生成测试", "生成单元测试", "创建bug", "修复bug", "save", "checkpoint", "保存", "resume", "restore", "恢复", "继续".
---

# Spec-Driven Development Workflow

Systematic workflow for feature development: Requirements → Design → Tasks → Implementation.

Also includes a streamlined bug fix workflow: Report → Analyze → Fix → Verify.

## Workflow
`spec-init → requirements → design → tasks → spec-run → spec-review / spec-test → spec-reflect` (each phase validated before proceeding; review, test, and learn can run in any order; learn is auto-prompted at phase completion when new learnings are detected)

### ⚠️ Stage Map (MANDATORY — load before every phase)
Before executing any phase, you **MUST** read [workflow-stages.md](references/workflow-stages.md) to confirm:
1. What the current stage needs to do
2. What the completion condition is
3. What the correct next step is

**Load this file at the START of every phase execution. This is non-negotiable.**
Long conversations cause earlier instructions to scroll out of the active context window — the stage map is your ground truth for flow control.

## When to Use

### Initialize Project
When the user wants to initialize a project, setup steering documents, or create project documentation.
**Action**: Follow [spec-init.md](references/spec-init.md)
- Examples: "initialize project", "setup project", "create steering docs", "初始化项目", "创建项目文档"

### Initialize Module
When the user wants to initialize a specific module's steering documents in a multi-module workspace.
**Action**: Follow [spec-init.md](references/spec-init.md) (Module Init section)
- Examples: "initialize module margin-trading", "初始化模块 margin-trading"

### Create Requirements
When the user wants to create or draft requirements for a feature.
**Action**: Follow [spec-requirements.md](references/spec-requirements.md)
- Examples: "create requirements for {feature}", "创建需求", "创建 {feature} 的需求"

### Create Design
When the user wants to create or draft a design document for a feature.
**Action**: Follow [spec-design.md](references/spec-design.md)
- Examples: "create design for {feature}", "创建设计", "创建 {feature} 的设计"

### Create Tasks
When the user wants to create or break down tasks for a feature.
**Action**: Follow [spec-tasks.md](references/spec-tasks.md)
- Examples: "create tasks for {feature}", "创建任务", "创建 {feature} 的任务"

### Execute Task
When the user wants to execute, implement, or run a specific task.
**Action**: Follow [spec-run.md](references/spec-run.md)
- Examples: "execute task", "implement task", "run task {id}", "执行任务", "实现任务"

### Code Review
When the user wants to review the code for a completed feature implementation.
**Action**: Invoke the `spec-workflow-code-review` skill
- Examples: "review code for {feature}", "code review for {feature}", "检查代码", "代码审查", "fix review issues for {feature}", "re-review {feature}"

### Generate Tests
When the user wants to generate unit tests for a completed feature.
**Action**: Follow [spec-test.md](references/spec-test.md)
- Examples: "generate tests for {feature}", "create unit tests for {feature}", "generate test 1 for {feature}", "generate all tests for {feature}", "生成测试", "生成单元测试", "生成 {feature} 的测试"

### Learn / Update Project Docs
When the user wants to extract and persist project knowledge from spec-workflow conversations, or when a phase completes and new learnings are detected.
**Action**: Invoke the `spec-reflect` skill
- **Auto-prompted**: After spec-run (all tasks complete), spec-review, spec-test, and bug-verify — only when new learnings are detected
- Examples: "spec-reflect", "spec-reflect {feature}", "learn conventions", "update conventions", "remember: {convention}", "学习规范", "更新规范", "总结回写", "记住这个约定"

### Validate Documents
When the user wants to validate specification documents.
**Action**: Use the corresponding agent:

| Trigger | Agent |
|---------|-------|
| validate requirements | `spec-requirements-validator` |
| validate design | `spec-design-validator` |
| validate tasks | `spec-task-validator` |

- Examples: "validate requirements", "validate design", "validate tasks", "验证需求", "校验设计", "审核任务"

### Save Context / Checkpoint
When the user wants to save the current conversation context for a feature, especially during long conversations or at important milestones. Saves all dialogue turns to disk so work can be resumed in a new window.
**Action**: Invoke the `spec-context-checkpoint` skill
- **When to use**: Long conversations approaching context window limits, reaching important milestones, before switching to a different feature, or when the user explicitly requests saving
- Examples: "save", "checkpoint", "save context for {feature}", "保存", "保存上下文", "保存 {feature} 的对话"

### Resume Context
When the user wants to restore previously saved conversation context for a feature in a new window. Loads recent session dialogue and historical summaries to rebuild working context.
**Action**: Invoke the `spec-context-resume` skill
- **When to use**: Starting a new window to continue work on a feature, after a checkpoint was saved, or when the user wants to pick up where they left off
- Examples: "resume {feature}", "restore {feature}", "恢复 {feature}", "继续 {feature}"

---

## Bug Fix Workflow

Streamlined workflow for bug fixes: `bug-create → bug-analyze → bug-fix → bug-verify`

**Smart Entry**: Any command can be an entry point. The system auto-detects bug state and triggers necessary preceding phases.

| User Says | Bug Exists? | System Action |
|-----------|-------------|---------------|
| "fix bug X" | No | Auto: create → analyze → fix |
| "fix bug X" | Yes (no analysis) | Auto: analyze → fix |
| "fix bug X" | Yes (analyzed) | Proceed to fix |
| "analyze bug X" | No | Auto: create → analyze |
| "fix JIRA-123" | No | Fetch JIRA → create → analyze → fix |

### Create Bug Report
When the user wants to create a new bug report.
**Action**: Follow [bug-create.md](references/bug-create.md)
- Examples: "bug-create {bug-name}", "bug-create JIRA-123", "create bug report", "report bug", "创建bug"

### Analyze Bug
When the user wants to investigate and analyze the root cause of a bug.
**Action**: Follow [bug-analyze.md](references/bug-analyze.md)
- **Smart Entry**: If bug doesn't exist, auto-triggers create first
- Examples: "bug-analyze {bug-name}", "analyze bug", "investigate bug", "分析bug"

### Fix Bug
When the user wants to implement the fix for a bug.
**Action**: Follow [bug-fix.md](references/bug-fix.md)
- **Smart Entry**: If bug doesn't exist or isn't analyzed, auto-triggers preceding phases
- Examples: "bug-fix {bug-name}", "fix bug", "fix this issue: {description}", "修复bug", "修复这个问题"

### Verify Bug Fix
When the user wants to verify that a bug fix works correctly.
**Action**: Follow [bug-verify.md](references/bug-verify.md)
- **Smart Entry**: If bug isn't fixed yet, auto-triggers preceding phases
- Examples: "bug-verify {bug-name}", "verify fix", "验证修复"

### Bug Status
When the user wants to check the status of bugs.
**Action**: Follow [bug-status.md](references/bug-status.md)
- Examples: "bug-status", "bug-status {bug-name}", "show bugs", "show bug status for margin-trading", "查看bug状态"

---

## Shared Context

All actions access these directories:

| Directory | Contents |
|-----------|----------|
| `.claude/spec-workflow/steering/` | **Single-module mode**: product.md, tech.md, structure.md directly |
| `.claude/spec-workflow/steering/_global/` | **Multi-module mode** (optional): Global product.md, tech.md (shared across modules) |
| `.claude/spec-workflow/steering/{module}/` | **Multi-module mode**: Module-specific product.md, tech.md, structure.md |
| `.claude/spec-workflow/conventions/` | Coding standards, API conventions, any other convention docs (global, all modules) |
| `.claude/spec-workflow/user-templates/` | Document templates (including bug templates) |
| `.claude/spec-workflow/module-map.yaml` | **Multi-module mode only**: Module registry (module-to-repo mapping) |
| `.claude/spec-workflow/specs/{feature}/` | Feature specifications (flat structure, same in both modes) |
| `.claude/spec-workflow/bugs/{bug-name}/` | Bug reports, analysis, and verification (flat structure, same in both modes) |
