Generate unit tests for a completed feature specification.

## Core Principles
- **Convention-First**: Detect and follow the project's existing test framework, directory structure, naming patterns, and mock strategies before writing any test
- **Post-Implementation**: Tests are generated after ALL tasks are completed, targeting the final stable codebase
- **Repo-Aware**: In multi-module/multi-repo projects, generate tests in the correct repo using that repo's test conventions
- **Requirements-Traceable**: Every test should trace back to specific requirements
- **Automatic Generation**: Once triggered, test plan creation and all test file generation run automatically without per-test confirmation
- **Verified Output**: Every generated test file must pass import/lint validation before being marked complete
- **Plan-to-Code Fidelity**: Generated test files must cover ALL test cases listed in the plan
- **Token-Efficient**: Sub-agents self-load context from files; main conversation only holds summaries

## Instructions

### Initial Setup

#### Step 0: Resolve Target Module (MULTI-MODULE ONLY)
Follow [module-resolver.md](module-resolver.md) to determine target module. Skip if `module-map.yaml` does not exist (single-module mode).

#### Step 1: Verify Prerequisites
- Ensure tasks.md exists at: `${workspace_root_directory}/.claude/spec-workflow/specs/{feature-name}/tasks.md`
- **Check task completion**: Scan tasks.md for uncompleted tasks (lines with `- [ ]`)
  - **If ALL tasks are completed** (`[X]`): Proceed normally
  - **If some tasks are NOT completed**: Warn user:
    ```
    ⚠️ The following tasks are not yet completed:
    - Task 3: [task description]
    - Task 7: [task description]
    
    A. Continue anyway — generate tests only for completed tasks
    B. Stop — complete remaining tasks first
    ```
  - If user chooses A → proceed, but only generate tests for files from completed tasks
  - If user chooses B → stop and inform user to run remaining tasks first
- Ensure requirements.md and design.md exist under same `specs/{feature-name}/` directory
- If any file doesn't exist, inform user: "Specification documents not found. Please ensure requirements, design, and tasks are created first."

#### Step 2: Load Context
Follow [context-loader.md](context-loader.md) to load steering documents and conventions.

Additionally load for this phase:
- Feature specs: `specs/{feature-name}/requirements.md`, `design.md`, `tasks.md`
- Test plan template — resolve in this order:
  1. Check `${workspace_root_directory}/.claude/spec-workflow/user-templates/test-plan-template.md`
  2. If not found → read from `${skill_directory}/assets/templates/test-plan-template.md`
  3. Auto-sync: copy the fallback file to `user-templates/test-plan-template.md` so the user can customize it later

---

## Test Generation Process

### Phase 1: Test Framework Detection (MANDATORY)

Before generating any test code, you MUST detect and document the project's existing test setup. **Do NOT assume any framework — always detect from the actual codebase.**

#### 1.1 Check Convention Cache

Before running full detection, check if a cached convention summary already exists:

1. Check if `${workspace_root_directory}/.claude/spec-workflow/conventions/test-conventions.md` exists
2. If exists and `Last Updated` timestamp is **less than 7 days old** → use cached version, skip to Phase 2
3. If stale or not exists → proceed with full detection below
4. **Force re-detect**: If user says "regenerate test conventions" or "re-detect test framework" → always run full detection regardless of cache

#### 1.2 Detect Build Tool and Test Dependencies

Scan each repo involved in the feature for build/dependency files (`pom.xml`, `build.gradle`, `package.json`, `go.mod`, `requirements.txt`, `Cargo.toml`, etc.) and identify test framework dependencies (e.g., junit, mockito, spock, jest, pytest, testify).

#### 1.3 Discover Existing Test Patterns

Search the codebase for existing test files and analyze their patterns:

1. **Test directory structure**: Note the exact path pattern (e.g., `src/test/java/` mirrors `src/main/java/`)
2. **Test file naming convention**: Record the actual pattern used (e.g., `*Test.java`, `*Test.groovy`, `*.test.ts`, `*_test.go`)
3. **Test class/function patterns** — Read 2-3 existing test files to identify:
   - Base test class (e.g., `extends Specification`, `@SpringBootTest`)
   - Setup/teardown patterns
   - Assertion style (e.g., Spock `then:` blocks, JUnit `assertEquals`, Jest `expect().toBe()`)
   - Mock/stub strategy (e.g., Spock `Mock()`, Mockito `@Mock`, `jest.mock()`)
   - Test method naming convention
   - Test data creation patterns (builders, factories, `where:` data tables, fixtures)
4. **Shared test infrastructure**: Search for shared base test classes, test utilities, test data factories (e.g., `*TestHelper.java`, `*TestFixture.groovy`)
5. In multi-module mode, **detect each repo independently** — different repos may use different frameworks

#### 1.4 Generate and Persist Convention Summary

Produce a structured summary for each repo:

```
### Repo: {repo-name}
- **Framework**: {e.g., Spock 2.2 + Groovy 4.0}
- **Test Directory**: {e.g., src/test/java/ (mirrors main package structure)}
- **File Naming**: {e.g., {ClassName}Test.groovy}
- **Method Naming**: {e.g., def "method_scenario"()}
- **Base Class**: {e.g., extends Specification}
- **Mock Strategy**: {e.g., Spock Mock() + field injection}
- **Assertion Style**: {e.g., Spock then: blocks with ==, thrown()}
- **Test Data Pattern**: {e.g., @Unroll + where: data tables, Builder with Map overrides}
- **Shared Infrastructure**: {any shared test utilities found}
- **Reference Tests**: {2-3 representative existing test file paths}
```

This summary will be passed as context to every test file generation agent.

Save to: `${workspace_root_directory}/.claude/spec-workflow/conventions/test-conventions.md` with `Last Updated: YYYY-MM-DD` at top.

---

### Phase 2: Generate Test Plan

Based on the framework detection results and the completed tasks, generate a structured test plan.

#### 2.1 Analyze Changed Files

1. Extract all files created or modified from tasks.md (look at the `File:` entries in each task)
2. **Filter out non-testable files**: Skip configuration files, templates, static resources, migration scripts, DDL scripts, enums (unless they contain logic), POJOs without logic
3. Group remaining files by repo (in multi-module mode)

#### 2.2 Public Method Coverage Audit (MANDATORY)

For each testable source file identified in 2.1:

1. **Read the source file** and extract ALL public methods/functions
2. Cross-reference with requirements.md and design.md to understand each method's intent
3. For each public method, determine if it needs test cases:
   - **YES**: Methods with business logic, conditional branches, calculations, state changes, external interactions
   - **NO**: Simple getters/setters, trivial delegation without logic, `toString()`/`hashCode()`/`equals()` (unless custom)
4. **Flag uncovered methods**: If any testable public method is not included in the planned test cases, mark it as `⚠️ Uncovered` in the test plan with reason
5. Log uncovered methods in the test plan so they are visible in the completion summary

#### 2.3 Detect Existing Tests for Target Files

For each testable source file:

1. Determine the expected test file path based on detected convention (e.g., `src/main/.../Foo.java` → `src/test/.../FooTest.java`)
2. Check if a test file already exists at that path
3. If a test file **already exists**:
   - Read the existing test file and extract already-tested method names
   - Determine which methods are **new** (added by this feature) and need new test cases
   - **Mark the test entry** with `Mode: APPEND` — list only the NEW test cases to add
   - When generating, use `edit_existing_file` to append new test methods rather than overwriting
4. If no test file exists → mark `Mode: CREATE`, generate a complete new test file

#### 2.4 Detect Shared Test Fixture Opportunities

After analyzing all test entries:

1. Identify domain/BO/DTO classes that appear as mock targets or builder subjects in **3 or more** test entries
2. Check if the project already has shared test fixtures or data factories for those classes
3. **Auto-decide** based on overlap:
   - If a class appears in **5 or more** test entries AND no existing shared fixture exists → automatically add a "Test 0" fixture entry to the test plan
   - If a class appears in **3-4** test entries → generate self-contained tests with per-file builders (some duplication is acceptable)
   - If a class appears in **fewer than 3** → no action needed

#### 2.5 Generate and Save test-plan.md

Use the test plan template to create a structured document:

- **Follow the template structure precisely**
- **One entry per test file** to be generated
- Each entry specifies:
  - Target source file path and test file path
  - **Mode**: `CREATE` (new file) or `APPEND` (add to existing test file)
  - **Priority**: `HIGH` (core business logic), `MEDIUM` (standard CRUD/delegation), `LOW` (simple mapping)
  - **Complexity**: `HIGH` (4+ mocks, conditional flows), `MEDIUM` (2-3 mocks), `LOW` (0-1 mocks, pure logic)
  - Test cases with descriptive names
  - Requirements traceability (`_Requirements: X.Y_`)
  - Key dependencies to mock
  - `⚠️ Uncovered` section listing any public methods deliberately excluded, with reason
- **Order tests** by dependency: shared fixtures first → domain/model → service → facade/controller

Save the plan to: `specs/{feature-name}/test-plan.md` and **proceed immediately to Phase 3** — no user approval gate.

> **Rationale**: The test plan is an internal working document for agents. Users review the final generated test files, not the plan itself.

---

### Phase 3: Generate All Tests

After test-plan.md is saved, generate ALL test files for the feature automatically.

#### 3.1 Determine Execution Order

1. Sort entries: shared fixtures first → then by priority (HIGH→LOW), within same priority by complexity (LOW→HIGH)
2. Group into batches:
   - **Batch 0** (sequential): Shared test fixtures (if any) — must complete first since other tests may reference them
   - **Batch 1+** (parallel): All remaining test entries — generate in parallel via sub-agents

> **Note**: Previous designs batched by complexity (LOW → MEDIUM → HIGH) sequentially. This is unnecessary when there are no shared fixtures — test files are independent and can all be generated in parallel. Only Batch 0 (shared fixtures) requires sequential execution before others.

#### 3.2 Per-File Generation (Sub-Agent Process)

Each test file is generated by a sub-agent. The agent receives:

**Context provided in agent prompt:**
- Convention summary from cache file (~1K tokens)
- The specific test-plan entry for this test (~0.5K tokens — NOT the full plan)

**Files for agent to self-load** (agent reads these itself — do NOT paste into prompt):
- The target source file
- 1-2 existing test files from the same repo as reference
- Relevant requirements and design sections
- If a shared test fixture was generated (Test 0), reference its path

**Agent execution steps:**

**Step 1 — Generate test code** following detected conventions exactly:
- For `Mode: APPEND`: use `edit_existing_file` to add new test methods, preserving existing tests
- For `Mode: CREATE`: write a complete new test file
- Same assertion style, mock strategy, naming pattern as existing tests
- All test cases listed in the test plan entry
- Proper setup/teardown, meaningful test names, mock setup for all external dependencies
- Both happy path and edge case coverage
- Do NOT introduce frameworks or base classes not already in the project

**Step 2 — Import Path Validation (MANDATORY)**:
After writing the test file, validate EVERY import statement:
- For each `import` line, use Glob to verify the target class/file exists at the expected path
- Common error patterns to catch:
  - Wrong sub-package (e.g., `domain` vs `bo`, `repo` vs `repository`)
  - Wrong module (e.g., class in `core` but import points to `engine`)
  - Typos in class names
- If an import is incorrect → search for correct location via `Glob(**/{ClassName}.java)`, fix, re-validate

**Step 3 — Lint Validation (MANDATORY)**:
After import validation, run IDE linting on the generated test file:
- Use ReadLints to check for compilation errors, syntax issues, type mismatches
- If errors found → fix each error, re-lint until zero errors
- **Do NOT mark the test as completed until lint passes with zero errors**

**Step 4 — Test Quality Self-Review**:
Before reporting completion, internally verify:
- **Completeness**: All test cases from the plan entry are implemented (count methods vs planned cases)
- **Edge cases**: Boundary values tested (nulls, zeros, empty collections, MAX values)
- **Assertion depth**: Assertions check specific values, not just "not null" or "no exception"
- **Mock verification**: Important interactions verified with `N * mock.method()` or `verify(mock, times(N))`
- If any dimension is weak → enhance before reporting

**Step 5 — Report back to main conversation**:
- Return a brief summary (NOT the full generated code): file path, N/N planned cases implemented, import/lint status
- Mark this test entry as `[X]` in test-plan.md

#### 3.3 Execute Generation

1. If Batch 0 exists (shared fixtures), generate it first (single agent), wait for completion
2. Launch all remaining test entries as **parallel sub-agents** — each agent follows the per-file process in 3.2
3. Each agent receives: convention summary, its specific test plan entry, file paths to self-load
4. **Main conversation receives only brief completion summaries** — NOT the full generated code (token efficiency)

#### 3.4 Post-Generation Completeness Verification (MANDATORY)

After ALL test files are generated:
1. **Re-read each generated test file** and extract all test method names
2. **Cross-reference against test-plan.md**: every planned `[ ]` case must have a corresponding method
3. **Report gaps**:
   ```
   ✅ Plan-to-Code Verification:
   
   | Test | Planned | Generated | Status |
   |------|---------|-----------|--------|
   | Test 1: TargetMarginLevelBoTest | 6 | 6 | ✅ Complete |
   | Test 5: ReducePositionServiceImplTest | 8 | 5 | ⚠️ 3 MISSING |
   ```
4. If gaps found → automatically regenerate ONLY the incomplete test files, re-verify
5. Only proceed to Phase 4 after 100% plan-to-code fidelity confirmed

#### 3.5 Cross-Module Test Generation (MULTI-MODULE ONLY)
- Check `_Module:` annotation on each test plan entry
- Load the correct repo's test convention summary
- Place test files in the correct repo's test directory using that repo's patterns

---

### Phase 4: Completion and Manual Verification Guidance

After all tests generated AND completeness verification passes.

#### 4.1 Completion Summary

```
## Test Generation Complete for {feature-name}

| # | Test File | Repo | Mode | Test Cases | Lint | Requirements |
|---|-----------|------|------|------------|------|--------------|
| 1 | .../BoTest.groovy | isolated-margin | CREATE | 6/6 ✅ | ✅ | R1, R2 |
| 7 | .../FacadeTest.groovy | isolated-margin | APPEND | 7/7 ✅ | ✅ | R7 |

Total: {N} test files ({A} created, {B} appended), {M} test cases
Plan-to-Code Fidelity: ✅ 100%
```

#### 4.2 Execution Commands

Generate run commands based on the detected build tool. Provide **two levels**:

1. **Run only new tests** (quick verification):
   ```
   cd {repo} && {build-tool test command for new test classes only}
   ```
2. **Run full regression** (recommended before merge):
   ```
   cd {repo} && {build-tool full test command}
   ```

**Do NOT run tests automatically** — provide commands for user to run manually.

#### 4.2.1 Spec-Reflect: Silent Scan for New Learnings

After presenting the test completion summary and execution commands, follow the spec-reflect passive mode instructions from the `spec-reflect` skill's [spec-reflect-harvest.md](../../../spec-reflect/references/spec-reflect-harvest.md) **within the current conversation context**. Do NOT invoke spec-reflect as a separate skill via the Skill tool — the passive scan requires access to the current conversation history as its primary knowledge source. Focus on test-related knowledge worth persisting (e.g., test framework conventions, mock strategies, naming patterns).

The passive scan will:
- Silently scan the conversation context and test generation findings
- Compare against existing conventions (especially `conventions/test-conventions.md`)
- **If new learnings detected**: Append a learn prompt:
  ```
  ───────────────────────────────
  📝 Detected {N} learnings from test generation:

    • [{category}] {one-line summary}

  L. Learn — review and write back to project docs
  S. Skip — maybe later ("spec-reflect {feature-name}")
  ───────────────────────────────
  ```
- **If no learnings detected**: Show nothing

If user selects L → follow the `spec-reflect` skill's [spec-reflect-propose.md](../../../spec-reflect/references/spec-reflect-propose.md) to enter detailed review flow **within the current conversation**. If user selects S or ignores → continue normally.

#### 4.3 Troubleshooting
- **Compilation errors**: Check imports and source code is up to date
- **Mock issues**: Verify mocked classes/interfaces match actual implementation
- **Missing dependencies**: Ensure test dependencies declared in build file

Fix specific tests: `"Fix test N for {feature-name}: [error description]"`

---

### Phase 5: Error Recovery (Fix Mode)

Activates when user says:
- `"Fix test N for {feature-name}: [error description]"` — fix a specific test
- `"Fix tests for {feature-name}"` — fix all failing tests (user provides error output)
- `"Regenerate test N for {feature-name}"` — completely regenerate a test file

#### 5.1 Diagnosis Process

Read the error message and classify the root cause:

| Category | Symptoms | Fix Strategy |
|----------|----------|--------------|
| `IMPORT_ERROR` | `cannot find symbol`, `package does not exist` | Search for correct class location using Glob, fix import paths |
| `MOCK_MISMATCH` | wrong method signatures, `MissingMethodException` | Re-read source interface/class, update mock setup |
| `MISSING_METHOD` | `method does not exist`, `no such property` | Re-read source file, check if name or signature changed |
| `LOGIC_ERROR` | assertion fails, wrong expected values | Re-trace source logic with test inputs, fix test expectations |
| `MISSING_CASES` | planned test cases absent | Diff test-plan.md vs generated code, add missing methods |

Then read the affected test file and corresponding source file, identify specific lines needing fixes.

#### 5.2 Fix Process

- Use `edit_existing_file` to fix in-place for all categories
- For `IMPORT_ERROR`: search with `Glob(**/{ClassName}.java)`, update import path
- For `MOCK_MISMATCH`/`MISSING_METHOD`：re-read source, update mock setup/method calls/assertions
- For `LOGIC_ERROR`: re-trace source logic with test inputs, fix expected values
- For `MISSING_CASES`: read test-plan entry, append missing test methods

#### 5.3 Post-Fix Validation

After applying fixes:
1. Re-validate imports using Glob
2. Re-lint using ReadLints
3. Verify fix against the original error
4. Present the fixed file to user with change summary

---

## Critical Rules

### Quality
- Follow existing conventions exactly — do not introduce new patterns
- Unit tests only — no integration/e2e tests
- One test file per source file
- Complete meaningful assertions — not just "no exception thrown"
- Mock all external dependencies; deterministic tests (no random/time-dependent)
- Every import MUST be verified against actual file paths
- Every planned test case MUST appear in generated code

### Validation (MANDATORY — never skip)
- **Import Validation**: every import verified via Glob before marking complete
- **Lint Validation**: zero lint errors required before marking complete
- **Completeness Verification**: all planned cases must exist in generated code after generate-all

### Execution
- NEVER run tests automatically — provide commands for user to run manually
- After test plan is generated, **automatically generate ALL test files** for the feature — no per-test user confirmation needed
- User confirmation is only required at **feature level** (whether to generate tests for this feature), not at individual test level

### Token Efficiency
- Sub-agents **self-load** files from given paths — do NOT read large spec documents in main conversation during Phase 3
- Sub-agents return **brief summaries** — do NOT return full generated code to main conversation
- Main conversation stays lightweight: dispatches agents, shows summaries, runs validation

## Error Handling
- No existing tests found → use framework from build dependencies, inform user
- Framework not detectable → ask user to specify
- Source file not found → skip that test entry, inform user
- Unfixable import → comment out with `// TODO: verify import path`, warn user
- Unfixable lint error → present error to user for guidance

## Example Usage
```
"Generate tests for user-authentication"
"Generate tests for margin-liquidation"
"生成单测"
"为 target-margin-level-set 生成测试"
"Fix test 3 for user-authentication: import error on line 5"
"Regenerate test 5 for margin-liquidation"
"修复 test 3"
```
