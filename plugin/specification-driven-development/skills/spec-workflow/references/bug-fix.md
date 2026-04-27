# Bug Fix Command

Implement the fix for the analyzed bug.

## Usage
Use natural language to fix a bug:
```
"Fix bug login-timeout"
"Implement fix for payment-failure"
"bug-fix JIRA-123"
"修复 bug login-timeout"
"修复这个问题：用户登录超时"
```

## Phase Overview
**Your Role**: Implement the solution based on the approved analysis

This is Phase 3 of the bug fix workflow. Your goal is to implement the fix while following project conventions.

## Smart Entry Point

**IMPORTANT**: This command can be an entry point to the bug workflow. Users often say "fix this bug" directly without going through create/analyze phases first.

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

### Step 0b: Determine Bug State

1. **Parse user input** to extract bug identifier:
   - JIRA key (e.g., `PROJ-123`) → use as bug-name
   - Bug name (e.g., `login-timeout`) → use directly
   - Description only (e.g., "fix the login timeout issue") → derive bug-name from description

2. **Check for existing bug directory**: `${workspace_root_directory}/.claude/spec-workflow/bugs/{bug-name}/`

3. **Determine current state and action**:

| State | Condition | Action |
|-------|-----------|--------|
| **New Bug** | Directory doesn't exist | Run full workflow: create → analyze → fix |
| **Report Only** | report.md exists, no analysis.md | Run: analyze → fix |
| **Analyzed** | analysis.md exists, not approved | Get approval → fix |
| **Ready to Fix** | analysis.md approved | Proceed with fix |

4. **If bug doesn't exist** (most common "direct fix" scenario):
   - Inform user: "I'll set up the bug workflow first."
   - **Auto-create bug report**: Follow [bug-create.md](bug-create.md)
   - **Auto-analyze**: Follow [bug-analyze.md](bug-analyze.md)
   - Get user approval on analysis
   - Then proceed to fix

## Instructions

You are working on the fix implementation phase of the bug fix workflow.

### Step 1: Load Context

**Load steering and conventions (OPTIONAL - does NOT block workflow):**
- **Steering documents**:
  - **Single-module mode**: Check `${workspace_root_directory}/.claude/spec-workflow/steering/` directory
    - If exists: Load for project patterns and tech context
    - If not exists: Skip, use codebase analysis for context
  - **Multi-module mode**:
    - Load `${workspace_root_directory}/.claude/spec-workflow/steering/_global/product.md` and `tech.md` (if exists)
    - Load `${workspace_root_directory}/.claude/spec-workflow/steering/{module-name}/product.md`, `tech.md`, `structure.md` (if initialized)
    - Merge order: global first, then module
- **Conventions**: Check `${workspace_root_directory}/.claude/spec-workflow/conventions/` directory (global, shared)
  - If exists: Load all `.md` files for coding standards
  - If not exists: Skip, follow patterns observed in existing code

**Bug documents to read (REQUIRED):**
- `${workspace_root_directory}/.claude/spec-workflow/bugs/{bug-name}/report.md`
- `${workspace_root_directory}/.claude/spec-workflow/bugs/{bug-name}/analysis.md`
- Understand the planned fix approach completely

2. **Implementation Process**
   1. **Follow the Implementation Plan**
      - Execute changes exactly as outlined in analysis.md
      - Make targeted, minimal changes
      - Follow existing code patterns and conventions

   2. **Code Changes**
      - Implement the fix following project standards
      - Add appropriate error handling
      - Include logging or debugging aids if needed
      - Update or add tests as specified

   3. **Quality Checks**
      - Verify fix addresses the root cause
      - Ensure no unintended side effects
      - Follow code style and conventions
      - Run tests and checks

3. **Implementation Guidelines**
   - **Follow steering documents**: Adhere to patterns in tech.md and conventions in structure.md
   - **Make minimal changes**: Fix only what's necessary
   - **Preserve existing behavior**: Don't break unrelated functionality
   - **Use existing patterns**: Leverage established code patterns and utilities
   - **Add appropriate tests**: Ensure the bug won't return

4. **Testing Requirements**
   - Test the specific bug scenario
   - Verify related functionality still works
   - Run existing test suite if available
   - Add regression tests for this bug

5. **Documentation Updates**
   - Update code comments if needed
   - Document any non-obvious changes
   - Update error messages if applicable

## Implementation Rules

### Code Quality
- Follow project coding standards
- Use existing utilities and patterns
- Add proper error handling
- Include meaningful comments for complex logic

### Testing Strategy
- Test the original bug reproduction steps
- Verify fix doesn't break related functionality
- Add tests to prevent regression
- Run full test suite if available

### Change Management
- Make atomic, focused changes
- Document the fix approach
- Preserve existing API contracts
- Consider backwards compatibility

## Completion Process

1. **Implement the Fix**
   - Make the necessary code changes
   - Follow the implementation plan from analysis.md
   - Ensure code follows project conventions

2. **Verify Implementation**
   - Test that the original bug is resolved
   - Verify no new issues introduced
   - Run relevant tests and checks

3. **Update Documentation**
   - Document the changes made
   - Update any relevant comments or docs

4. **Confirm Completion**
   - Present summary of changes made
   - Show test results confirming fix

5. **Final Confirmation**
   - Ask: "The fix has been implemented and reviewed. Should we proceed to verification?"
   - **CRITICAL**: Wait for user approval before proceeding

## Critical Rules
- **ONLY** implement the fix outlined in the approved analysis
- **ALWAYS** test the fix thoroughly
- **NEVER** make changes beyond the planned fix scope
- **MUST** wait for user approval before proceeding to verification

## Example Usage
```
"Fix bug login-timeout"
"Implement fix for payment-failure"
"修复 bug login-timeout"
```

## Next Steps
After fix implementation approval, proceed to verify the fix by saying:
- "Verify bug fix login-timeout"
- "验证 bug 修复 login-timeout"
