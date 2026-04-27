# Stage Map

Locate your current stage → follow its flow → transition to correct next stage.

## Feature Flow

```
spec-init → requirements → design → tasks → spec-run → [all done] → review / test / learn
```

| Stage | Do | Done When | Next |
|-------|----|-----------|------|
| **spec-init** | Run init script → steering interview → generate product/tech/structure.md | Steering docs saved + user reviewed | → requirements |
| **requirements** | Create feature dir → JIRA check → requirements interview (4-8 rounds) → generate requirements.md → **auto-validate** → fix until PASS → **user approval** | Validation PASS + user approves | → design |
| **design** | Verify requirements.md exists → codebase research → generate design.md → **auto-validate** → fix until PASS → **user approval** | Validation PASS + user approves | → tasks |
| **tasks** | Verify requirements+design exist → generate atomic task list → **auto-validate** → fix until PASS → **user approval** → show execution examples | Validation PASS + user approves | → spec-run |
| **spec-run** | Execute ONE task via agent → validate → mark `[X]` → **STOP, wait for user** | Task marked `[X]` | If tasks remain → wait for user. If ALL done → all-tasks-complete |
| **all-tasks-complete** | Show summary → **spec-reflect passive scan (inline)** → if findings show L/S → show A/B/C/D menu | User selects option | L→learn (then re-show menu) · A→test · B→review · C→review then test · D→end |
| **spec-review** | Build change inventory → **user confirms scope** → review code inline → generate report → present assessment → **spec-reflect passive scan** | Report saved | APPROVED→test/merge · NEEDS_REVISION→fix→re-review (re-review also triggers learn scan) |
| **spec-test** | Detect framework → generate test-plan → **generate ALL tests (parallel agents)** → verify 100% plan coverage → show manual run commands → **spec-reflect passive scan** | All tests generated + lint pass | User runs tests manually |
| **spec-reflect** | Harvest→Distill→Propose (user approve per item)→Commit to docs | Items written | Back to parent flow |

**Auto-validate** = launch validator agent, fix until PASS, only then present to user.
**Spec-learn passive scan** = inline harvest, silent if no findings, show L/S only if findings exist.

## Bug Flow

```
bug-create → bug-analyze → bug-fix → bug-verify → [learn]
```

**Smart entry**: any stage auto-triggers missing preceding phases.

| Stage | Do | Done When | Next |
|-------|----|-----------|------|
| **bug-create** | Detect source (JIRA/description) → generate report.md → **user approval** | Report approved | → bug-analyze |
| **bug-analyze** | Investigate root cause → solution plan → generate analysis.md → **user approval** | Analysis approved | → bug-fix |
| **bug-fix** | Implement fix per approved analysis → quality checks → **user approval** | Fix approved | → bug-verify |
| **bug-verify** | **Execute tests (real, not code analysis)** → honest status → generate verification.md → **user approval** → **spec-reflect passive scan** | User confirms resolved | Done. Optionally → spec-reflect |

## Transition Rules

| Rule | Detail |
|------|--------|
| **One task at a time** | spec-run: NEVER auto-proceed to next task |
| **Validate before present** | requirements/design/tasks: auto-validate first, fix until PASS |
| **Learn scan at phase end** | all-tasks-complete, spec-review (including re-review), spec-test, bug-verify |
| **Learn runs inline** | NEVER invoke spec-reflect as separate Skill in passive mode — needs conversation context |
| **Review runs inline** | NEVER use sub-agents for code review — needs cross-file context |
| **Tests auto-generate** | Generate ALL tests after plan — no per-test user confirmation |
| **A/B/C/D menu mandatory** | all-tasks-complete: NEVER skip the next-step menu |
| **Tests must actually run** | bug-verify: NEVER mark PASSED without real execution |
| **Prerequisites required** | design needs requirements.md; tasks needs both; check before starting |
