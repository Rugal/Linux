---
name: spec-workflow-code-review
description: |
  Perform comprehensive code review for features developed through the spec-workflow process. 
  Use when all implementation tasks are completed and the user wants to review code quality before 
  generating unit tests or merging. Triggers include "code review for {feature}", "review code", 
  "检查代码", "代码审查", "spec review", or when spec-run's All Tasks Complete Flow offers the 
  code review option. Also use when the user asks to fix review issues, re-review after fixes, 
  or wants to understand the quality of their spec-workflow implementation. This skill integrates 
  with spec-workflow as a post-implementation quality gate — think of it as having a senior 
  engineer review your feature branch before it goes anywhere.
---

# Spec Workflow Code Review

Comprehensive code review for features built through the spec-driven development workflow.

## Overview

This skill reviews all code created/modified during a spec-workflow feature implementation. It checks code quality, architecture alignment with design.md, requirements traceability, convention compliance, security, performance, and error handling — then produces a structured review report with actionable findings.

**Where it fits in the workflow:**
```
spec-init → requirements → design → tasks → spec-run → [spec-review] → spec-test
```

The review happens after all implementation tasks are done. It's a quality gate that catches issues before test generation or merge, saving the cost of writing tests for code that needs fundamental changes.

## When to Use

### Review Feature Code
When all tasks are complete and the user wants a code review.
**Action**: Follow [spec-review.md](references/spec-review.md)
- Examples: "review code for user-authentication", "code review for margin-liquidation", "检查代码", "代码审查"

### Fix Review Issues
When the user wants to address issues found during review.
**Action**: Follow the Fix Mode section in [spec-review.md](references/spec-review.md)
- Examples: "fix review issues for user-authentication", "fix critical issues", "fix issue C-1"

### Re-Review After Fixes
When issues have been addressed and the user wants to verify.
**Action**: Follow the Re-Review Flow in [spec-review.md](references/spec-review.md)
- Examples: "re-review user-authentication", "check if review issues are fixed"

## Quick Reference

### Review Output
The review produces `review-report.md` saved to `specs/{feature-name}/review-report.md` containing:
- Executive summary with overall assessment (APPROVED / APPROVED_WITH_COMMENTS / NEEDS_REVISION)
- Issues categorized by severity (Critical / Important / Minor)
- Requirements traceability matrix
- Convention compliance summary
- Security and performance review

### Assessment Levels

| Level | Meaning | Action |
|-------|---------|--------|
| APPROVED | No Critical or Important issues | Proceed to tests or merge |
| APPROVED_WITH_COMMENTS | No Critical, few Important | Address Important issues, then proceed |
| NEEDS_REVISION | Critical issues found | Fix Critical issues, re-review |

### Integration with spec-workflow

This skill is referenced from `spec-run.md`'s "All Tasks Complete Flow". When all tasks finish, the user is offered:
- **A**: Generate unit tests (→ spec-test)
- **B**: Run code review (→ this skill)
- **C**: Both — review first, then tests
- **D**: Skip both

After review completes, a passive `spec-reflect` scan is performed within the current conversation to detect reusable coding conventions surfaced during the review. If the `spec-reflect` skill is not installed, this step is silently skipped.

The detailed review process and report generation are defined in [spec-review.md](references/spec-review.md).

## Key Principles

Borrowed from the best of code review practices:

1. **Technical rigor, not performance** — every finding is verified against the actual code with file:line references
2. **Verify before suggesting** — don't flag issues based on assumptions; read the code and confirm
3. **Convention-aware** — judge by THIS project's patterns, not generic best practices
4. **Severity-honest** — a naming issue is Minor, a null pointer in production flow is Critical
5. **YAGNI-conscious** — flag code that goes beyond requirements scope
6. **Pushback-ready** — acknowledge reasonable design choices even when they differ from textbook patterns
