# Test Plan: {feature-name}

## Test Convention Summary

### Repo: {repo-name}
- **Framework**: {e.g., JUnit 5 + Mockito 4.x / Spock 2.2 + Groovy 4.0}
- **Test Directory**: {e.g., src/test/java/ (mirrors main package structure)}
- **File Naming**: {e.g., {ClassName}Test.java / {ClassName}Test.groovy}
- **Method Naming**: {e.g., test{Method}_{scenario} / def "Method_scenario"()}
- **Base Class**: {e.g., None / extends Specification / extends BaseTest}
- **Mock Strategy**: {e.g., @ExtendWith(MockitoExtension.class) + @Mock / Spock Mock() + field injection}
- **Assertion Style**: {e.g., org.junit.jupiter.api.Assertions / Spock then: blocks with ==, thrown()}
- **Test Data Pattern**: {e.g., Builder pattern / @Unroll + where: data tables / static factory}
- **Shared Infrastructure**: {e.g., TestDataFactory.java / None}
- **Reference Tests**:
  - `{path/to/ExampleTest1.java}` (describe what this test demonstrates)
  - `{path/to/ExampleTest2.java}` (describe what this test demonstrates)

<!-- Repeat for each repo involved in the feature -->

---

## Shared Test Fixtures

<!-- Optional section — only include if shared fixture was agreed upon in Phase 2.4 -->

### Test 0: {ClassName}TestFixture
- **Test File**: `{repo}/{path/to/ClassNameTestFixture.groovy}`
- **Purpose**: Shared builder/factory methods for {ClassName}, used by Test {X}, {Y}, {Z}
- **Methods to provide**:
  - `build{ClassName}(Map overrides)` — creates a default instance with optional overrides
  - `buildDefault{ClassName}()` — creates a minimal valid instance

---

## Test Entries

### Test 1: {TestClassName}
- **Target Source**: `{repo}/{path/to/SourceFile.java}`
- **Test File**: `{repo}/{path/to/SourceFileTest.java}`
- **Mode**: CREATE | APPEND <!-- CREATE = new file, APPEND = add to existing test file -->
- **Priority**: HIGH | MEDIUM | LOW <!-- HIGH = core business logic, MEDIUM = standard CRUD, LOW = simple mapping -->
- **Complexity**: HIGH | MEDIUM | LOW <!-- HIGH = 4+ mocks, conditional flows; MEDIUM = 2-3 mocks; LOW = 0-1 mocks -->
- **_Module: {module-name}_** <!-- only in multi-module mode -->
- **_Requirements: {X.Y, X.Z}_**
- **Dependencies to Mock**: {ServiceA, RepositoryB}
- **Test Cases**:
  - [ ] `test{Method}_success` — {brief description of happy path}
  - [ ] `test{Method}_{edgeCase}` — {brief description of edge case}
  - [ ] `test{Method}_{errorCondition}` — {brief description of error scenario}
- **⚠️ Uncovered Methods** (intentionally excluded): <!-- optional section -->
  - `{methodName}()` — Reason: {e.g., trivial getter, no business logic}

### Test 2: {TestClassName}
- **Target Source**: `{repo}/{path/to/AnotherFile.java}`
- **Test File**: `{repo}/{path/to/AnotherFileTest.java}`
- **Mode**: CREATE
- **Priority**: MEDIUM
- **Complexity**: LOW
- **_Module: {module-name}_**
- **_Requirements: {X.Y}_**
- **Dependencies to Mock**: {ServiceC}
- **Test Cases**:
  - [ ] `test{Method}_success` — {description}
  - [ ] `test{Method}_{scenario}` — {description}

<!-- Add more test entries as needed -->

---

## Coverage Summary

| Requirement | Covered By Tests |
|-------------|------------------|
| {1.1} | Test 1, Test 3 |
| {1.2} | Test 1 |
| {2.1} | Test 2 |

---

## Execution Commands

### Run New Tests Only
```bash
# {repo-1}
{command to run only new test classes}

# {repo-2}
{command to run only new test classes}
```

### Run Full Regression
```bash
# {repo-1}
{command to run full test suite}

# {repo-2}
{command to run full test suite}
```
