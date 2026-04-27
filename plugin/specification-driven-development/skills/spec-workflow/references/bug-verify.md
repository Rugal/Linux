# Bug Verify Command

Verify that the bug fix works correctly and doesn't introduce regressions.

## Usage
Use natural language to verify a bug fix:
```
"Verify bug fix login-timeout"
"Verify fix for payment-failure"
"bug-verify JIRA-123"
"验证 bug 修复 login-timeout"
```

## Phase Overview
**Your Role**: Thoroughly verify the fix works correctly and document the results **with actual test execution**

This is Phase 4 (final) of the bug fix workflow. Your goal is to confirm the bug is resolved and the fix is safe.

**CRITICAL**: This phase requires **actual test execution**, not just code analysis. All test results must reflect real execution status.

## Smart Entry Point

**IMPORTANT**: This command can be an entry point. Before proceeding:

### Step 0a: Resolve Target Module (MULTI-MODULE ONLY)
- Check if `${workspace_root_directory}/.claude/spec-workflow/module-map.yaml` exists
- If NOT exists → single-module mode, skip to Step 0b
- If exists → multi-module mode:
  - If user input contains `{module}/{bug-name}` format → use specified module
  - Else check the most recently edited file path → match file's parent repo directory against repos in module-map.yaml
  - Else use AI semantic inference: compare user's input against each module's name, description, and repo names
    - High confidence → use directly; Medium → use but inform user; Low → ask user to choose
  - If only one module → use it directly; If unresolved → present options
- After resolving module, check if steering is initialized → warn if not (same A/B choice as spec-requirements)

### Step 0b: Check Bug State

1. **Check for existing bug directory**: `${workspace_root_directory}/.claude/spec-workflow/bugs/{bug-name}/`

2. **Determine current state and action**:

| State | Condition | Action |
|-------|-----------|--------|
| **No Bug** | Directory doesn't exist | Run full workflow: create → analyze → fix → verify |
| **Not Fixed** | No code changes made | Run: (create if needed →) analyze → fix → verify |
| **Fixed** | Fix implemented | Proceed with verification |

3. **If bug doesn't exist or is incomplete**:
   - Inform user of current state
   - Trigger necessary preceding phases first
   - Then proceed to verification

## Instructions

You are working on the verification phase of the bug fix workflow.

### Step 1: Load Context

**Load templates and steering:**
- Load verification template: `cat ${workspace_root_directory}/.claude/spec-workflow/user-templates/bug-verification-template.md`
- **Load steering documents**:
  - **Single-module mode**: Load from `${workspace_root_directory}/.claude/spec-workflow/steering/` directory if needed
  - **Multi-module mode**:
    - Load `${workspace_root_directory}/.claude/spec-workflow/steering/_global/product.md` and `tech.md` (if exists)
    - Load `${workspace_root_directory}/.claude/spec-workflow/steering/{module-name}/product.md`, `tech.md`, `structure.md` (if initialized)
    - Merge order: global first, then module
- **Load conventions**: Load from `${workspace_root_directory}/.claude/spec-workflow/conventions/` directory (global, shared)

**Bug documents to read:**
- `${workspace_root_directory}/.claude/spec-workflow/bugs/{bug-name}/report.md`
- `${workspace_root_directory}/.claude/spec-workflow/bugs/{bug-name}/analysis.md`
- Understand what was changed and why
- Have the verification plan from analysis.md

### Step 2: Detect Project Test Framework

Before running tests, detect the project's test framework:

```bash
# Check for common test frameworks
ls package.json 2>/dev/null && echo "Node.js project"
ls pom.xml 2>/dev/null && echo "Maven project"
ls build.gradle 2>/dev/null && echo "Gradle project"
ls Cargo.toml 2>/dev/null && echo "Rust project"
ls go.mod 2>/dev/null && echo "Go project"
```

| Project Type | Test Command |
|--------------|--------------|
| Maven (Java) | `mvn test -Dtest=TestClassName` |
| Gradle | `./gradlew test --tests TestClassName` |
| Node.js (Jest) | `npm test -- --testPathPattern=TestName` |
| Node.js (Mocha) | `npm test -- --grep "TestName"` |
| Python (pytest) | `pytest test_file.py -v` |
| Go | `go test -v -run TestName` |
| Rust | `cargo test test_name` |

### Step 3: Execute Tests (MANDATORY)

**CRITICAL**: You MUST actually run the tests, not just analyze the code.

#### 3.1 Run New Tests Created During Fix

```bash
# Example for Maven project
mvn test -Dtest=NewTestClassName -pl module-name

# Capture and record the ACTUAL output
```

#### 3.2 Record Test Execution Status

Use these **exact status indicators** based on actual execution:

| Status | Symbol | Meaning |
|--------|--------|---------|
| **PASSED** | ✅ | Test executed and all assertions passed |
| **FAILED** | ❌ | Test executed but assertions failed |
| **ERROR** | 💥 | Test could not execute (compilation error, etc.) |
| **SKIPPED** | ⏭️ | Test was intentionally skipped |
| **NOT RUN** | ⚪ | Test was not executed (environment issues, etc.) |

**NEVER** mark a test as ✅ PASSED if it was not actually executed!

#### 3.3 Handle Test Execution Failures

If tests cannot be executed:

1. **Document the exact error**:
   ```
   Test Execution Status: ⚪ NOT RUN
   Reason: [Exact error message]
   ```

2. **Provide manual verification command**:
   ```bash
   # User can run this command manually to verify:
   [exact command to run tests]
   ```

3. **Offer alternative verification**:
   - Code review verification
   - Logic analysis with specific examples
   - Request user to run tests manually

### Step 4: Verification Process

1. **Original Bug Testing**
   - Reproduce the original steps from report.md
   - Verify the bug no longer occurs
   - Test edge cases mentioned in the analysis

2. **Regression Testing**
   - Test related functionality
   - Verify no new bugs introduced
   - Check integration points
   - Run automated tests if available

3. **Code Quality Verification**
   - Review code changes for quality
   - Verify adherence to project standards
   - Check error handling is appropriate
   - Ensure tests are adequate

### Step 5: Create Verification Document

**Template to Follow**: Use the bug verification template precisely

**IMPORTANT**: The verification document must honestly reflect:
- Which tests were actually executed
- Which tests passed/failed/errored
- Which verifications are based on code analysis (not execution)

Save verification to: `${workspace_root_directory}/.claude/spec-workflow/bugs/{bug-name}/verification.md`

### Step 6: Final Approval

1. **Present verification summary**:
   ```
   ## Verification Summary
   
   | Verification Type | Status | Method |
   |-------------------|--------|--------|
   | Unit Tests | ✅/❌/⚪ | Executed / Not Run |
   | Integration Tests | ✅/❌/⚪ | Executed / Not Run |
   | Code Analysis | ✅ | Manual Review |
   | Lint Check | ✅/❌ | Executed |
   ```

2. **If tests were NOT executed**, clearly state:
   ```
   ⚠️ WARNING: Automated tests were not executed due to [reason].
   Manual verification command: [command]
   Please run tests manually before deploying to production.
   ```

3. **Ask for confirmation**:
   - "The bug fix has been verified. [Summary of what was verified and how]. Is this bug resolved?"

4. **Spec-Reflect: Silent scan for new learnings (after user confirms bug is resolved)**:
   After the user confirms the bug is resolved, follow the spec-reflect passive mode instructions from the `spec-reflect` skill's [spec-reflect-harvest.md](../../../spec-reflect/references/spec-reflect-harvest.md) **within the current conversation context**. Do NOT invoke spec-reflect as a separate skill via the Skill tool — the passive scan requires access to the current conversation history as its primary knowledge source. Focus on defensive programming patterns, error handling conventions, and lessons learned from this bug fix.

   The passive scan will:
   - Silently scan the conversation context, bug analysis, and fix details
   - Compare against existing steering and conventions documents
   - **If new learnings detected**: Present a learn prompt:
     ```
     ───────────────────────────────
     📝 Detected {N} learnings from this bug fix:

       • [{category}] {one-line summary}

     L. Learn — review and write back to project docs
     S. Skip — maybe later ("spec-reflect from bug {bug-name}")
     ───────────────────────────────
     ```
   - **If no learnings detected**: Show nothing

   If user selects L → follow the `spec-reflect` skill's [spec-reflect-propose.md](../../../spec-reflect/references/spec-reflect-propose.md) to enter detailed review flow **within the current conversation**. If user selects S or ignores → continue normally.

## Test Execution Requirements

### Minimum Verification Requirements

| Requirement | Must Execute? | Fallback if Cannot Execute |
|-------------|---------------|---------------------------|
| New unit tests | ✅ YES | Provide command + mark as NOT RUN |
| Existing related tests | ✅ YES | Provide command + mark as NOT RUN |
| Lint/Type check | ✅ YES | Use IDE linting tools |
| Code review | N/A | Always do manual review |

### Verification Completeness Levels

| Level | Description | When to Use |
|-------|-------------|-------------|
| **Full** ✅ | All tests executed and passed | Ideal - ready for production |
| **Partial** ⚠️ | Some tests executed, some not run | Acceptable - note limitations |
| **Analysis Only** 📋 | No tests executed, code review only | Requires manual testing before deploy |

## Verification Checklist

Before completing verification, ensure:

- [ ] **Test Execution Attempted**: Actually tried to run tests (not just analyzed code)
- [ ] **Honest Status Reporting**: All test statuses reflect actual execution results
- [ ] **Failure Documentation**: Any test failures or errors are documented with reasons
- [ ] **Manual Commands Provided**: If tests couldn't run, provide commands for user
- [ ] **Verification Level Stated**: Clearly indicate Full/Partial/Analysis Only

## Critical Rules

- **NEVER** mark tests as "passed" without actually running them
- **ALWAYS** document the exact test execution status
- **ALWAYS** provide manual test commands if automated execution fails
- **ALWAYS** clearly distinguish between "code analysis" and "actual test execution"
- **GET** final user approval before considering bug resolved

## Example Verification Summary

### Good Example ✅
```
## Test Execution Results

| Test | Status | Execution Method |
|------|--------|------------------|
| CalcTotalLiabilityTest | ✅ PASSED | `mvn test` executed successfully |
| MarginLevelServiceTest | ⚪ NOT RUN | Compilation error in unrelated file |
| Code Lint | ✅ PASSED | IDE lint tool |

⚠️ Verification Level: PARTIAL
- 1 of 2 test files executed successfully
- Manual command for remaining tests: `mvn test -Dtest=MarginLevelServiceTest`
```

### Bad Example ❌
```
## Test Execution Results

| Test | Status |
|------|--------|
| CalcTotalLiabilityTest | ✅ PASSED |  ← WRONG: Test was not actually run!
| MarginLevelServiceTest | ✅ PASSED |  ← WRONG: Marked passed without execution
```

## Next Steps
After verification approval, the bug fix workflow is complete. You can:
- Check bug status: "Show bug status"
- Start a new bug: "Create bug report for {bug-name}"
- Write back learnings from this bug: "spec-reflect from bug {bug-name}"
