# Bug Create Command

Initialize a new bug fix workflow for tracking and resolving bugs.

## Usage
Use natural language to create a bug report:
```
"Create bug report for login-timeout"
"Report bug payment-failure: Credit card transactions failing"
"bug-create JIRA-123"
"创建 bug login-timeout"
```

## Workflow Overview

This is the **streamlined bug fix workflow** - a lighter alternative to the full spec workflow for addressing bugs and issues.

### Bug Fix Phases
1. **Report Phase** (This command) - Document the bug
2. **Analysis Phase** (`bug-analyze`) - Investigate root cause
3. **Fix Phase** (`bug-fix`) - Implement solution
4. **Verification Phase** (`bug-verify`) - Confirm resolution

## Instructions

You are helping create a new bug fix workflow. This is designed for smaller fixes that don't need the full spec workflow overhead.

### Step 0: Resolve Target Module (MULTI-MODULE ONLY)
- Check if `${workspace_root_directory}/.claude/spec-workflow/module-map.yaml` exists
- If NOT exists → single-module mode, skip to Step 1
- If exists → multi-module mode:
  - If user input contains `{module}/{bug-name}` format → use specified module
  - Else check the most recently edited file path → match file's parent repo directory against repos in module-map.yaml
  - Else use AI semantic inference: compare user's input against each module's name, description, and repo names
    - High confidence → use directly; Medium → use but inform user; Low → ask user to choose
  - If only one module → use it directly; If unresolved → present options
- After resolving module, check if steering is initialized → warn if not (same A/B choice as spec-requirements)

### Step 1: Input Source Detection

Detect the bug information source from user input:

| Input Pattern | Source Type | Action |
|---------------|-------------|--------|
| `JIRA-123` or `PROJECT-123` | JIRA Issue | Fetch from JIRA API |
| `bug-name: description` | User Description | Use provided info |
| `bug-name` only | Minimal Input | Ask for details |

**JIRA Integration**:
- If JIRA key detected (format: `PROJECT-123`):
  - Call `mcp-jira_get_issue` tool with the JIRA key
  - **If token error**: Stop and inform user: "JIRA token needs to be configured. Please set up your JIRA token in settings."
  - **If successful**: Use JIRA issue title, description, and metadata as bug report content
  - Use JIRA key as bug-name (e.g., `PROJ-123`)

### Step 2: Check Existing Bug

Check if bug already exists:
- Look for `${workspace_root_directory}/.claude/spec-workflow/bugs/{bug-name}/` directory
- **If exists**: Inform user and ask if they want to view status or continue existing workflow
- **If not exists**: Proceed to create new bug workflow

### Step 3: Create Directory Structure
- Create `${workspace_root_directory}/.claude/spec-workflow/bugs/{bug-name}/` directory
- Initialize empty report.md file

### Step 4: Load Context
Load complete context at the beginning for the bug creation process:
- Load bug report template: `cat ${workspace_root_directory}/.claude/spec-workflow/user-templates/bug-report-template.md`
- **Load steering documents (OPTIONAL)**:
  - **Single-module mode**: Check `${workspace_root_directory}/.claude/spec-workflow/steering/` directory
    - If exists: Load product.md, tech.md, structure.md for project context
    - If not exists: Skip, proceed without steering context (does NOT block workflow)
  - **Multi-module mode**:
    - Load `${workspace_root_directory}/.claude/spec-workflow/steering/_global/product.md` and `tech.md` (if exists)
    - Load `${workspace_root_directory}/.claude/spec-workflow/steering/{module-name}/product.md`, `tech.md`, `structure.md` (if initialized)
    - Merge order: global first, then module
- **Load conventions (OPTIONAL)**: Check `${workspace_root_directory}/.claude/spec-workflow/conventions/` directory (global, shared)
  - If exists: Load all `.md` files for coding standards context
  - If not exists: Skip, infer conventions from codebase during analysis

### Step 5: Gather Bug Information

**If from JIRA**:
- Extract: title, description, priority, assignee, labels, components
- Map to bug report template sections
- Ask user to confirm or supplement missing details

**If from user description**:
- Parse the provided description
- Ask clarifying questions for missing sections

**If minimal input**:
- Guide user through each section of the bug report
- Use structured format for consistency

### Step 6: Generate Bug Report
- **Template to Follow**: Use the bug report template from the pre-loaded context
- Create detailed bug description following the bug report template structure
- **Follow exact structure**: Use loaded bug report template precisely
- **Include all sections**: Don't omit any required template sections

### Step 7: Save and Proceed
- Save the completed bug report to: `${workspace_root_directory}/.claude/spec-workflow/bugs/{bug-name}/report.md`
- Ask: "Is this bug report accurate? If so, we can move on to the analysis."
- Wait for explicit approval before proceeding

## Key Differences from Spec Workflow

- **Faster**: No requirements/design phases
- **Targeted**: Focus on fixing existing functionality
- **Streamlined**: 4 phases instead of detailed workflow
- **Practical**: Direct from problem to solution

## Rules

- Only create ONE bug fix at a time
- Always use kebab-case for bug names
- Must analyze existing codebase during investigation
- Follow existing project patterns and conventions
- Do not proceed without user approval between phases

## Error Handling

If issues arise during the workflow:
- **Bug unclear**: Ask targeted questions to clarify
- **Too complex**: Suggest breaking into smaller bugs or using spec workflow
- **Reproduction blocked**: Document blockers and suggest alternatives

## Example Usage
```
"Create bug report for login-timeout: Users getting logged out too quickly"
"Report a bug called payment-failure"
"创建 bug login-timeout"
```

## Next Steps
After bug report approval, proceed to analyze the bug by saying:
- "Analyze bug login-timeout"
- "分析 bug login-timeout"
