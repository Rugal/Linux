# Bug Analyze Command

Investigate and analyze the root cause of a reported bug.

## Usage
Use natural language to analyze a bug:
```
"Analyze bug login-timeout"
"Investigate bug payment-failure"
"bug-analyze JIRA-123"
"分析 bug login-timeout"
```

## Phase Overview
**Your Role**: Investigate the bug and identify the root cause

This is Phase 2 of the bug fix workflow. Your goal is to understand why the bug is happening and plan the fix approach.

## Smart Entry Point

**IMPORTANT**: This command can be an entry point to the bug workflow. Before proceeding:

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

### Step 0b: Check Bug Exists

1. **Check for existing bug directory**: `${workspace_root_directory}/.claude/spec-workflow/bugs/{bug-name}/`
2. **If directory does NOT exist**:
   - This is a new bug - **trigger full workflow from bug-create first**
   - Inform user: "Bug '{bug-name}' doesn't exist yet. Let me create it first."
   - Follow [bug-create.md](bug-create.md) to create the bug report
   - Then continue with analysis
3. **If directory exists but report.md is missing or incomplete**:
   - Complete the bug report first following [bug-create.md](bug-create.md)
4. **If directory exists and report.md is complete**:
   - Proceed with analysis below

## Investigation Methodology

For systematic root cause investigation, follow the **systematic-debugging** methodology:

**Core Principle**: NO FIXES WITHOUT ROOT CAUSE INVESTIGATION FIRST

**Four Phases**:
1. **Root Cause Investigation** - Read errors carefully, reproduce consistently, check recent changes, gather evidence
2. **Pattern Analysis** - Find working examples, compare against references, identify differences
3. **Hypothesis and Testing** - Form single hypothesis, test minimally, verify before continuing
4. **Implementation** - Create failing test, implement single fix, verify fix

**Key Techniques** (see `systematic-debugging` skill for details):
- **Root Cause Tracing**: Trace backward through call chain to find original trigger, fix at source not symptom
- **Defense in Depth**: Add validation at every layer (entry point, business logic, environment guards, debug logging)
- **Condition-Based Waiting**: For timing-related bugs, wait for actual conditions not arbitrary delays

**Red Flags - Stop and Re-investigate**:
- "Quick fix for now, investigate later"
- "Just try changing X and see if it works"
- "I don't fully understand but this might work"
- Proposing solutions before tracing data flow
- 3+ fix attempts failed → question the architecture

## Instructions

**Analysis Process**:

1. **Load Context**
   - Load the bug report from `${workspace_root_directory}/.claude/spec-workflow/bugs/{bug-name}/report.md`
   - **Load steering documents (OPTIONAL)**:
     - **Single-module mode**: Check `${workspace_root_directory}/.claude/spec-workflow/steering/` directory
       - If exists: Load tech.md, structure.md, product.md for project context
       - If not exists: Skip, analyze codebase directly to understand project patterns
     - **Multi-module mode**:
       - Load `${workspace_root_directory}/.claude/spec-workflow/steering/_global/product.md` and `tech.md` (if exists)
       - Load `${workspace_root_directory}/.claude/spec-workflow/steering/{module-name}/product.md`, `tech.md`, `structure.md` (if initialized)
       - Merge order: global first, then module
   - **Load conventions (OPTIONAL)**: Check `${workspace_root_directory}/.claude/spec-workflow/conventions/` directory (global, shared)
     - If exists: Load all `.md` files for coding standards
     - If not exists: Skip, infer conventions from existing code
   - Understand the reported issue completely

2. **Investigation Process**
   1. **Code Investigation**
      - Search codebase for relevant functionality
      - Identify files, functions, and components involved
      - Map data flow and identify potential failure points
      - Look for similar issues or patterns

   2. **Root Cause Analysis**
      - Determine the underlying cause of the bug
      - Identify contributing factors
      - Understand why existing tests didn't catch this
      - Assess impact and risks

   3. **Solution Planning**
      - Design fix strategy
      - Consider alternative approaches
      - Plan testing approach
      - Identify potential risks

3. **Create Analysis Document**
   - **Load template**: `cat ${workspace_root_directory}/.claude/spec-workflow/user-templates/bug-analysis-template.md`
   - **Read and follow**: Use the bug analysis template and follow all sections precisely
   - Document investigation findings following the template structure
   - Save analysis to: `${workspace_root_directory}/.claude/spec-workflow/bugs/{bug-name}/analysis.md`

## Template Usage
- **Follow exact structure**: Use `${workspace_root_directory}/.claude/spec-workflow/user-templates/bug-analysis-template.md` precisely
- **Include all sections**: Don't omit any required template sections
- **Detailed analysis**: Follow the template's format for comprehensive investigation

4. **Investigation Guidelines**
   - **Follow tech.md standards**: Understand existing patterns before proposing changes
   - **Respect structure.md**: Know where fixes should be placed
   - **Search thoroughly**: Look for existing utilities, similar bugs, related code
   - **Think systematically**: Consider data flow, error handling, edge cases
   - **Plan for testing**: How will you verify the fix works

5. **Approval Process**
   - Present the complete analysis document
   - **Show code reuse opportunities**: Note existing utilities that can help
   - **Highlight integration points**: Show how fix fits with existing architecture
   - Ask: "Does this analysis look correct? If so, we can proceed to implement the fix."
   - Incorporate feedback and revisions
   - Continue until explicit approval
   - **CRITICAL**: Do not proceed without explicit approval

## Analysis Guidelines

### Code Investigation
- Use search tools to find relevant code
- Understand existing error handling patterns
- Look for similar functionality that works correctly
- Check for recent changes that might have caused the issue

### Root Cause Identification
- Don't just fix symptoms - find the real cause
- Consider edge cases and error conditions
- Look for design issues vs implementation bugs
- Understand the intended behavior vs actual behavior
- **Trace backward**: Where does bad value originate? What called this with bad value?
- **Multi-component systems**: Add diagnostic logging at each component boundary to find where it breaks

### Solution Design
- Prefer minimal, targeted fixes
- Reuse existing patterns and utilities
- Consider backwards compatibility
- Plan for future prevention of similar bugs
- **Add defense in depth**: Validate at entry point, business logic, environment guards, and debug logging
- **If 3+ fixes failed**: Stop and question the architecture, don't attempt fix #4 without discussion

## Critical Rules
- **NEVER** proceed to the next phase without explicit user approval
- Accept only clear affirmative responses: "yes", "approved", "looks good", etc.
- If user provides feedback, make revisions and ask for approval again
- Continue revision cycle until explicit approval is received

## Example Usage
```
"Analyze bug login-timeout"
"Investigate bug payment-failure"
"分析 bug login-timeout"
```

## Next Steps
After analysis approval, proceed to fix the bug by saying:
- "Fix bug login-timeout"
- "修复 bug login-timeout"
