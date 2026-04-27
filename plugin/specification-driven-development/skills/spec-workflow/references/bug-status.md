# Bug Status Command

Show current status of all bug fixes or a specific bug fix.

## Usage
Use natural language to check bug status:
```
"Show bug status"
"Check status of bug login-timeout"
"查看 bug 状态"
```

## Instructions
Display the current status of bug fix workflows.

1. **If no bug-name provided:**
   - List all bugs in `${workspace_root_directory}/.claude/spec-workflow/bugs/` directory
   - Show current phase for each bug
   - Display completion status

2. **If bug-name provided:**
   - Show detailed status for that bug at `${workspace_root_directory}/.claude/spec-workflow/bugs/{bug-name}/`
   - Display current workflow phase
   - Show completed vs pending phases
   - List next recommended actions

3. **Status Information:**
   - Report: [Complete/In Progress/Pending]
   - Analysis: [Complete/In Progress/Pending]
   - Fix: [Complete/In Progress/Pending]
   - Verification: [Complete/In Progress/Pending]

4. **Output Format:**
   ```
   Bug: login-timeout
   Phase: Fix Implementation
   Progress: Report ✅ | Analysis ✅ | Fix 🔄 | Verification ⏳
   Status: Implementing fix for session timeout issue
   Next: Complete implementation and verify fix works
   ```

## Bug Fix Phases
- **Report**: Bug description and impact assessment
- **Analysis**: Root cause investigation and solution planning
- **Fix**: Implementation of the planned solution
- **Verification**: Testing and confirmation of resolution
- **Complete**: Bug fully resolved and verified
