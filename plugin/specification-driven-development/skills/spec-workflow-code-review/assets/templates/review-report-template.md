# Code Review Report: {feature-name}

**Generated**: {date}
**Reviewer**: AI Code Review Agent
**Feature Spec**: `specs/{feature-name}/`
**Review Mode**: {GIT_DIFF (base: {commit-sha}) | FILELIST (from implementation-logs | from tasks.md)}
**Review Scope**: {All files | Subset — {user-specified scope description, e.g. "service layer only", "high-risk areas only"}}

---

## Executive Summary

**Overall Assessment**: {APPROVED | APPROVED_WITH_COMMENTS | NEEDS_REVISION}

{1-3 sentence summary of overall code quality and readiness}

### Key Metrics

| Metric | Value |
|--------|-------|
| Files Reviewed | {N} |
| Total Issues Found | {N} |
| Critical Issues | {N} |
| Important Issues | {N} |
| Minor Issues | {N} |
| Requirements Coverage | {N}/{M} ({%}) |
| Design Alignment | {N}/{M} decisions verified |
| Convention Compliance | {HIGH / MEDIUM / LOW} |

---

## High-Risk Areas

<!-- Tasks with multiple execution attempts — these areas deserve extra scrutiny. Remove section if none found. -->

| Task | File | Attempts | Findings |
|------|------|----------|----------|
| Task {N} | `{file-path}` | {N} attempts | {brief summary of issues found, or "No issues"} |

---

## Strengths

{List specific things done well — not generic praise. Reference file:line where appropriate.}

1. {Strength 1 with file reference}
2. {Strength 2 with file reference}
3. {Strength 3 with file reference}

---

## Issues

### Critical (Must Fix Before Merge)

Issues that will cause bugs, security vulnerabilities, data loss, or broken functionality in production.

<!-- Use this format for each issue. Remove this section if no critical issues found. -->

#### C-{N}: {Issue title}
- **File**: `{file-path}:{line-range}`
- **Category**: {BUG | SECURITY | DATA_LOSS | BROKEN_FUNCTIONALITY}
- **Description**: {What's wrong}
- **Impact**: {Why it matters — what breaks, who's affected}
- **Suggested Fix**: {How to fix, or direction to investigate}
- **_Requirements: {X.Y}_** <!-- which requirement is affected -->

---

### Important (Should Fix)

Issues with architecture, missing edge cases, poor error handling, or significant code quality problems.

#### I-{N}: {Issue title}
- **File**: `{file-path}:{line-range}`
- **Category**: {ARCHITECTURE | ERROR_HANDLING | EDGE_CASE | PERFORMANCE | LOGIC | CONVENTION}
- **Description**: {What's wrong}
- **Impact**: {Why it matters}
- **Suggested Fix**: {How to fix}
- **_Requirements: {X.Y}_**

---

### Minor (Nice to Have)

Code style improvements, minor optimizations, documentation gaps, or non-critical refactoring opportunities.

#### M-{N}: {Issue title}
- **File**: `{file-path}:{line-range}`
- **Category**: {STYLE | NAMING | DOCUMENTATION | OPTIMIZATION | REFACTORING}
- **Description**: {What could be improved}
- **Suggested Fix**: {How to improve}

---

## Design Alignment

Verification of implementation against key decisions in design.md.

| # | Design Decision | Verified In | Status | Notes |
|---|----------------|-------------|--------|-------|
| 1 | {API: POST /api/v1/xxx with fields a,b,c} | `{Controller.java}` | {ALIGNED | DEVIATED | NOT_FOUND} | {brief note} |
| 2 | {Data Model: table xxx with columns a,b,c} | `{Entity.java}` | {status} | {note} |
| 3 | {Flow: Service calls A → B → C in sequence} | `{ServiceImpl.java}` | {status} | {note} |
| 4 | {Tech: Uses Redis cache for xxx} | `{CacheService.java}` | {status} | {note} |

### Deviations
<!-- List any design decisions where implementation deviated -->

- **Decision {N}**: {What design.md specified vs what was implemented, and whether the deviation is justified}

---

## Requirements Traceability

| Requirement | Implemented In | Status | Notes |
|-------------|---------------|--------|-------|
| {1.1} | `{file-path}` | {FULLY_COVERED | PARTIALLY_COVERED | NOT_COVERED | OVER_IMPLEMENTED} | {brief note} |
| {1.2} | `{file-path}` | {status} | {note} |

### Coverage Gaps
<!-- List any requirements not fully covered by implementation -->

- **{Requirement X.Y}**: {What's missing and why it matters}

### Over-Implementation
<!-- List any code that goes beyond requirements scope -->

- **{file-path}**: {What was added beyond scope — potential YAGNI}

---

## Convention Compliance

### Compliant
- {Convention 1}: {evidence of compliance}
- {Convention 2}: {evidence of compliance}

### Non-Compliant
- {Convention N}: {what deviates and where}
  - File: `{file-path}:{line}`
  - Expected: {what the convention requires}
  - Actual: {what the code does}

---

## Security Review

| Check | Status | Notes |
|-------|--------|-------|
| Input Validation | {PASS / FAIL / N/A} | {details} |
| Authentication/Authorization | {PASS / FAIL / N/A} | {details} |
| Sensitive Data Handling | {PASS / FAIL / N/A} | {details} |
| SQL/NoSQL Injection | {PASS / FAIL / N/A} | {details} |
| XSS Prevention | {PASS / FAIL / N/A} | {details} |
| Dependency Safety | {PASS / FAIL / N/A} | {details} |

---

## Performance Considerations

{List any performance concerns found during review, or state "No significant performance concerns identified."}

- **{concern}**: `{file-path}:{line}` — {description and recommendation}

---

## Files Reviewed

| # | File | Action | Lines Changed | Issues | Source |
|---|------|--------|---------------|--------|--------|
| 1 | `{file-path}` | {CREATED / MODIFIED} | {+N/-M or +N (new)} | {N critical, N important, N minor} | Task {N} |
| 2 | `{file-path}` | {action} | {lines} | {issues summary} | Task {N} |

---

## Verdict

**Assessment**: {APPROVED | APPROVED_WITH_COMMENTS | NEEDS_REVISION}

**Reasoning**: {Technical assessment in 2-3 sentences explaining the verdict}

### Next Steps
<!-- Based on verdict -->

**If APPROVED**:
- Ready to proceed to unit test generation or merge

**If APPROVED_WITH_COMMENTS**:
- Address Important issues before merge
- Minor issues can be addressed in follow-up

**If NEEDS_REVISION**:
- Fix all Critical issues
- Address Important issues
- Re-run code review after fixes: `"review code for {feature-name}"`
