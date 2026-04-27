> **Current Stage**: spec-run | **Next**: all-tasks-complete (when all done) | **Done When**: validator each task and Task marked [X], STOP and wait for user

Execute tasks from the approved task list. Supports single task execution.

## Usage
Use natural language to execute tasks:
```
"Execute task 1 for user-authentication"
"Execute task 2.1 for payment-processing"
"Run task 3 for user-authentication"
"Implement task 1 for feature-name"
```

## Phase Overview
**Your Role**: Execute tasks systematically with validation

This is Phase 4 of the spec workflow. Your goal is to implement tasks from the task list one by one

## Instructions

**Step 0: Resolve Target Module** (MULTI-MODULE ONLY)
- Check if `${workspace_root_directory}/.claude/spec-workflow/module-map.yaml` exists
- If NOT exists → single-module mode, proceed with existing flow
- If exists → multi-module mode:
  - If the task has a `_Module:` annotation → use that module
  - Else check the most recently edited file path → match against module-map repos
  - Else use AI semantic inference
  - Load module-map.yaml for module metadata

**Execution Steps**:

**Step 1: Load Context and Update Beads**
- **Find task-level beads issue**: Search for corresponding beads issue using pattern "{feature-name} Task {N}:"
  ```bash
  bd list --status=open | grep "{feature-name} Task {task-id}:"
  ```
- **Mark task as in_progress**:
  ```bash
  bd update {task-beads-id} --status=in_progress
  ```
- **Load steering documents**:
  - **Single-module mode**: Load from `${workspace_root_directory}/.claude/spec-workflow/steering/` directory (product.md, tech.md, structure.md)
  - **Multi-module mode**:
    - Load `${workspace_root_directory}/.claude/spec-workflow/steering/_global/product.md` and `tech.md` (if exists — global background)
    - Load `${workspace_root_directory}/.claude/spec-workflow/steering/{module-name}/product.md`, `tech.md`, `structure.md` (if module initialized — focused context)
    - Merge order: global first (background), then module (focus)
- **Load conventions**: Load all `.md` files from `${workspace_root_directory}/.claude/spec-workflow/conventions/` directory if available (global, shared by all modules)
    - These include coding standards, API conventions, security guidelines, etc.
    - Use these conventions to ensure implementation follows project conventions
    - If conventions directory doesn't exist or is empty, proceed without conventions context
- Load specification context: load design.md, requirements.md and tasks.md from the feature directory
- Load specific task details: Extract the complete content of task id from tasks.md

**Step 2: Execute with Agent**
Use the `spec-task-executor` agent:
```
Use the spec-task-executor agent to implement task {task-id} for the {feature-name} specification.

## Steering Context
[PASTE THE COMPLETE OUTPUT FROM Steering documents in context HERE]

## Conventions
[PASTE THE COMPLETE OUTPUT FROM Conventions documents HERE - coding standards, API conventions, etc.]

## Specification Context
[PASTE THE REQUIREMENTS AND DESIGN SECTIONS FROM the context HERE]

## Task Details
[PASTE THE OUTPUT of specific task details FROM THE CONTEXT HERE]

## Instructions
- Implement ONLY the specified task: {task-id}
- Follow all project conventions and leverage existing code
   - Adhere to conventions and coding standards from conventions directory
- Mark the task as complete in tasks.md upon successful completion
- Provide a completion summary
```

**Cross-Module Task Handling** (Multi-module mode only):
- Check if the task has a `_Module:` annotation
- If the annotated module differs from the feature's primary module:
  - Load the annotated module's steering context instead (if initialized)
  - If the annotated module's steering is not initialized, use _global context with a warning
- Pass the correct module's steering context to the spec-task-executor agent

3. **Task Execution with Quality Assurance**
- Focus on ONE task at a time
- If task has sub-tasks, start with those
- Follow the implementation details from design.md
- Verify against requirements specified in the task
- **Perform comprehensive logic validation** on all generated code

4. **Implementation Guidelines with Logic Completeness**
   - Write clean, maintainable, and syntactically correct code
   - **Follow steering documents**: Adhere to patterns in tech.md and conventions in structure.md
   - **Follow conventions**: Apply coding standards, API conventions, and guidelines from conventions directory
   - Follow existing code patterns and conventions
   - Include appropriate error handling
   - **Do NOT generate unit tests** — tests are handled by the dedicated `spec-test` phase after all tasks are completed
   - Document complex logic
   - **IMPLEMENT COMPLETE BUSINESS LOGIC**: Every method must have full functional implementation
   - **NO PARTIAL CODE**: Avoid placeholder comments, TODOs, or incomplete logic blocks
   - **END-TO-END IMPLEMENTATION**: Ensure the entire functional flow is coded
   - **Proactively prevent common errors**:
       - Verify package/import consistency
       - Ensure all abstract methods are implemented
       - Check syntax before finalizing code
       - Validate against language-specific best practices

5. **Multi-Stage Validation**
   - Verify implementation meets acceptance criteria
   - Check for lint/type errors
   - Ensure integration with existing code
   - **Mark tasks complete** in tasks.md
   - **Syntax Validation**: Check for syntax errors, missing semicolons, brackets, etc.
   - **Import Validation**: Verify all imports are correct and necessary
   - **Implementation Validation**: Ensure interfaces/abstract classes are fully implemented
   - **Logic Completeness Validation**:
       - **CRITICAL - Verify each method has complete business logic implementation**
       - **Functional Flow Validation**: Ensure end-to-end functionality is properly coded
   - **Integration Validation**: Check compatibility with existing codebase
   - **Convention Validation**: Adhere to project coding standards

6. **Task Completion Protocol**
   When completing any task:
    1. **Mark task complete**: Update the [ ] to [X] in tasks.md file for that task
    2. **Close task-level beads issue**:
       ```bash
       bd close {task-beads-id}
       ```
    3. **Confirm to user**: State clearly "Task X has been marked as complete"
    4. **Check all-tasks-complete**: After marking, scan tasks.md for any remaining uncompleted tasks (`- [ ]`)
       - **If uncompleted tasks remain**: Stop execution, wait for user to request next task
       - **If ALL tasks are now completed** (`[X]`): Trigger the **All Tasks Complete Flow** below
    5. **Stop execution**: Do not proceed to next task automatically
    6. **Wait for instruction**: Let user decide next steps

### All Tasks Complete Flow

When the final task is marked complete and NO uncompleted tasks remain in tasks.md, you MUST:

1. **Close feature-level beads issue**:
   ```bash
   bd close {feature-beads-id}
   ```
   All task-level issues should already be closed at this point.

2. **Present completion summary**:
   ```
   ✅ All {N} tasks for {feature-name} have been completed!

   [Summary of what was built — files created/modified, key components, architecture highlights]

   Beads tracking:
   - Feature issue {feature-beads-id}: Closed
   - All {N} task issues: Closed
   ```

2. **Spec-Reflect: Silent scan for new learnings**:
   Before presenting next-step options, follow the spec-reflect passive mode instructions from the `spec-reflect` skill's [spec-reflect-harvest.md](../../../spec-reflect/references/spec-reflect-harvest.md) **within the current conversation context**. Do NOT invoke spec-reflect as a separate skill via the Skill tool — the passive scan requires access to the current conversation history as its primary knowledge source.

   The passive scan will:
   - Silently scan the conversation context and newly created files
   - Compare findings against existing steering and conventions documents
   - **If new learnings are detected**: Append a learn prompt between the completion summary and the next-step options:
     ```
     ───────────────────────────────
     📝 Detected {N} learnings from this implementation:
     
       • [{category}] {one-line summary}
       • [{category}] {one-line summary}
     
     L. Learn — review and write back to project docs
     S. Skip — maybe later ("spec-reflect {feature-name}")
     ───────────────────────────────
     ```
   - **If no learnings detected**: Show nothing, proceed directly to next-step options

3. **Proactively ask about next steps**:
   ```
   📝 Next recommended steps: Code Review & Unit Tests
   
   All implementation tasks are done. What would you like to do next?
   
   A. Generate unit tests — detect framework, create test plan, generate all test files
   B. Run code review — comprehensive review of all changes (quality, architecture, security, conventions)
   C. Both — run code review first, then generate unit tests
   D. Skip — finish without tests or review
   
   Enter your choice (A/B/C/D){if learn prompt was shown: " or L/S"}:
   ```

4. **Based on user's response**:
   - **If L** (Learn): Follow the `spec-reflect` skill's [spec-reflect-propose.md](../../../spec-reflect/references/spec-reflect-propose.md) to enter detailed review and write-back flow **within the current conversation**. After spec-reflect completes, re-present the A/B/C/D options.
   - **If S** (Skip learn) or user ignores L/S and directly selects A/B/C/D: Proceed with the selected option. Spec-learn silently exits.
   - **If A**: Transition to the `spec-test` phase — follow [spec-test.md](spec-test.md). The entire test generation (plan + all test files) will run automatically without further confirmation.
   - **If B**: Invoke the `spec-workflow-code-review` skill. Runs comprehensive code review and generates a review report.
   - **If C**: First invoke the `spec-workflow-code-review` skill. After review completes, ask the user whether to proceed to unit test generation:
     ```
     Code review is complete. Would you like to generate unit tests now?
     
     A. Yes — generate unit tests
     B. No — I'll do it later
     ```
   - **If D**: Acknowledge and end:
     ```
     Understood. You can run these later anytime by saying:
       "Generate tests for {feature-name}"
       "Review code for {feature-name}"
       "spec-reflect {feature-name}"
     ```

**CRITICAL**: This prompt is MANDATORY when all tasks are complete. Do NOT skip it or only mention it passively in "Next Steps".

## Critical Workflow Rules

### Task Execution
- **ONLY** execute one task at a time during implementation
- **ALWAYS** stop after completing a task
- **NEVER** automatically proceed to the next task
- **MUST** wait for user to request next task execution
- **CONFIRM** task completion status to user

### Requirement References
- **ALL** tasks must reference specific requirements using _Requirements: X.Y_ format
- **ENSURE** traceability from requirements through design to implementation
- **VALIDATE** implementations against referenced requirements
- **VERIFY** each requirement is fully translated to complete code implementation

## Task Selection
If no task-id specified:
- Look at tasks.md for the spec
- Recommend the next pending task
- Ask user to confirm before proceeding

If no feature-name specified:
- Check `${workspace_root_directory}/.claude/spec-workflow/specs/` directory for available specs
- If only one spec exists, use it
- If multiple specs exist, ask user which one to use
- Display error if no specs are found

## Examples
```
"Execute task 1 for user-authentication"
"Execute task 2.1 for user-authentication"
"Run task 3 for payment-processing"
"Implement task 1 for feature-name"
```

## Important Rules
  - Only execute ONE task at a time
  - **ALWAYS** mark completed tasks in tasks.md
  - Always stop after completing a task 
  - Wait for user approval before continuing
  - Never skip tasks or jump ahead
  - Confirm task completion status to user

## Next Steps
After task completion, you can:
- Address any issues identified in the review
- Execute the next task (e.g., "Execute task 2 for {feature-name}")
- When all tasks are complete, you will be prompted to generate unit tests, code review, or learn conventions
