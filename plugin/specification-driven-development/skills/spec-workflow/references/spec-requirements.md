> **Current Stage**: requirements | **Next**: design | **Done When**: Validation PASS + user approves

Create requirements document for a feature specification.

## Workflow Philosophy

You are an AI assistant that specializes in requirements analysis. Your role is to create comprehensive, well-structured requirements documents that clearly define what needs to be built.

### Core Principles
- **Structured Requirements**: Follow template structure exactly
- **User-Centric**: Focus on user stories and acceptance criteria
- **User Approval Required**: Requirements must be explicitly approved
- **Alignment with Vision**: Ensure requirements support product goals

## Instructions

You are helping create a requirements document for a feature. Follow these steps:

### Initial Setup

0. **Resolve Target Module** (MULTI-MODULE ONLY)
   - Check if `${workspace_root_directory}/.claude/spec-workflow/module-map.yaml` exists
   - If NOT exists → single-module mode, skip to step 1
   - If exists → multi-module mode:
     - If user input contains `{module}/{feature}` format → use specified module
     - Else check the most recently edited file path → match file's parent repo directory against repos in module-map.yaml
     - Else use AI semantic inference: compare user's input against each module's name, description, and repo names in module-map.yaml
       - High confidence (input contains module/repo name, or strongly matches one module) → use directly
       - Medium confidence (matches one module more than others) → use it but inform user
       - Low confidence (ambiguous across modules) → present all modules as options (A/B/C/D) and ask user to choose
     - If only one module registered → use it directly
     - If still unresolved → present module options for user to select
   - After module is resolved, **always confirm with user**:
     ```
     Detected target module: {module-name} ({module-description})
     This will be used to load module-specific steering context.
     
     Is this correct? (Y/N):
     ```
     - If user says N → present all modules for selection
   - After resolving module, check if `${workspace_root_directory}/.claude/spec-workflow/steering/{module-name}/` exists
     - If NOT exists → warn user:
       ```
       ⚠️ Module {module-name}'s steering documents are not initialized.
          Missing module-level product, tech, and structure context may affect requirements quality.
          
       A. Pause — initialize module steering first ("initialize module {module-name}")
       B. Continue — use only global steering + conventions (degraded mode)
       
       Enter your choice (A/B):
       ```
     - If user chooses A → stop and guide to module init
     - If user chooses B → continue with _global context only

1. **Create Directory Structure**
    - Create `${workspace_root_directory}/.claude/spec-workflow/specs/{feature-name}/` directory
    - Initialize empty requirements.md file
    - Create `implementation-logs/` subdirectory inside the feature directory

2. **Load Context**
   Load complete context at the beginning:
    - Load requirements template: `cat ${workspace_root_directory}/.claude/spec-workflow/user-templates/requirements-template.md`
    - **Load steering documents**:
      - **Single-module mode**: Load from `${workspace_root_directory}/.claude/spec-workflow/steering/` directly (product.md, tech.md, structure.md)
      - **Multi-module mode**:
        - Load `${workspace_root_directory}/.claude/spec-workflow/steering/_global/product.md` and `tech.md` (if exists — global background)
        - Load `${workspace_root_directory}/.claude/spec-workflow/steering/{module-name}/product.md`, `tech.md`, `structure.md` (if module initialized — focused context)
        - Merge order: global first (background), then module (focus)
    - **Load conventions**: Load all `.md` files from `${workspace_root_directory}/.claude/spec-workflow/conventions/` directory if available (global, shared by all modules)
        - These conventions include coding standards, API conventions, security guidelines, etc.
        - Use these conventions to ensure requirements align with project conventions
        - If conventions directory doesn't exist or is empty, proceed without conventions context

3. **Analyze Existing Codebase** (BEFORE starting)
    - **Search for similar features**: Look for existing patterns relevant to the new feature
    - **Identify reusable components**: Find utilities, services, hooks, or modules that can be leveraged
    - **Review architecture patterns**: Understand current project structure, naming conventions, and design patterns
    - **Cross-reference with steering documents**: Ensure findings align with documented standards
    - **Find integration points**: Locate where new feature will connect with existing systems
    - **Document findings**: Note what can be reused vs. what needs to be built from scratch

## Requirements Creation Process

### 1. JIRA Integration Check
- **Check for JIRA Reference**: Analyze user input for JIRA issue keys (format: PROJECT-123)
- **If JIRA key found**:
    - Call `mcp-jira_get_issue` tool with the JIRA key
    - **If token error**: Stop workflow and inform user: "JIRA token needs to be configured. Please set up your JIRA token by clicking the settings button in the top-right corner → Manage Tokens to add JIRA token."
    - **If successful**: Use JIRA issue title and description as the primary task description
- **If no JIRA reference**: Use original user input as initial requirements draft

### 2. Requirements Interview (Mandatory)

**CRITICAL**: Before generating the requirements document, conduct an interactive interview to ensure completeness.

Follow the interview process defined in [interview.md](interview.md):

#### 2.1 Gather Initial Input
- Use user's input (or JIRA issue content) as the initial requirements draft
- Combine with codebase analysis findings from Initial Setup

#### 2.2 Conduct Structured Q&A Rounds
- **Round limit**: 4-8 rounds (or fewer if requirements are already clear)
- **Questions per round**: 3-5 focused questions max
- **Use AskUserQuestion tool**: ALWAYS use this tool to ask questions
- **Cycle through categories**:
  - A: User & Context
  - B: Functional Requirements
  - C: Edge Cases & Error Handling
  - D: Data & State
  - E: UI/UX Details
  - F: Integration & Dependencies

#### 2.3 Between-Round Analysis
Before each new round:
1. Summarize information collected so far
2. Cross-reference answers for contradictions
3. Identify implicit requirements
4. Explore codebase for related implementations
5. Consider technical feasibility

#### 2.4 Interview Completion Criteria
End the interview when:
- All major categories have been covered
- No significant gaps or ambiguities remain
- User indicates they have no more information to add
- Requirements are clear (typically 4-8 rounds)

### 3. Synthesize & Generate requirements.md Document

**Synthesize all collected knowledge**:
- Information from interview rounds
- Codebase analysis findings
- JIRA issue content (if applicable)
- Steering documents context
- Conventions and standards

**Generate the document**:
- Use the requirements template structure precisely
- **Align with product.md**: Ensure requirements support the product vision and goals
- **Follow conventions**: Reference loaded conventions documents (coding standards, API conventions, etc.) to ensure requirements align with project conventions
- Create user stories in "As a [role], I want [feature], so that [benefit]" format
- Write acceptance criteria in EARS format (WHEN/IF/THEN statements)
- Include edge cases discovered during interview
- **Reference steering documents**: Note how requirements align with product vision
- Don't generate Non-Functional Requirements like Usability, Maintainability, Accessibility and test (testing or manual testing) parts

### 4. Requirements Template Usage
- **Read and follow**: Use the requirements template from the pre-loaded context
- **Use exact structure**: Follow all sections and formatting from the template
- **Include all sections**: Don't omit any required template sections

### 5. Requirements Validation and Approval

**Required: Automatic Validation**: Use the `spec-requirements-validator` agent to validate the requirements:

```
Use the spec-requirements-validator agent to validate the requirements document for the {feature-name} specification.

The agent should:
1. Read the requirements document from: `${workspace_root_directory}/.claude/spec-workflow/specs/{feature-name}/requirements.md`
2. Validate against all quality criteria (structure, user stories, acceptance criteria, etc.)
3. Check alignment with steering documents (product.md, tech.md, structure.md)
4. Provide specific feedback and improvement suggestions
5. Rate the overall quality as PASS, NEEDS_IMPROVEMENT, or MAJOR_ISSUES

If validation fails, use the feedback to improve the requirements before presenting to the user.
```

- **Requirements Validation NEEDS_IMPROVEMENT / MAJOR_ISSUES**:
  If validation rating is NEEDS_IMPROVEMENT or MAJOR_ISSUES, use the feedback to make revisions of the requirements.md until the issue is resolved.

- **Requirements Validation PASS**:
  If validation rating is PASS:
  - **Present the validated requirements document** with codebase analysis summary
  - Ask: "Do the requirements look good? If so, you can proceed to create the design document."
  - **CRITICAL**: Wait for explicit approval before completing
  - Accept only clear affirmative responses: "yes", "approved", "looks good", etc.
  - If user provides feedback, make revisions and ask for approval again

### 6. Save Requirements Document
- Save the approved requirements to: `${workspace_root_directory}/.claude/spec-workflow/specs/{feature-name}/requirements.md`
- Confirm save location to user

## Next Steps

After requirements are approved and saved:
- Inform user: "Requirements document has been created and saved. You can now proceed to create the design document by saying 'create design for {feature-name}'"

## Critical Rules

### Universal Rules
- **Only create ONE requirements document at a time**
- **Always use kebab-case for feature names**
- **MANDATORY**: Always analyze existing codebase before starting
- **Follow exact template structure** from the requirements template
- **Do not proceed without explicit user approval**

### Approval Requirements
- Ask user for approval: "Do the requirements look good?"
- Accept only clear affirmative responses: "yes", "approved", "looks good", etc.
- **NEVER** complete without explicit user approval
- If user provides feedback, make revisions and ask for approval again
- Continue revision cycle until explicit approval is received

### Template Usage
- **Requirements**: Must follow requirements template structure exactly
- **Include all template sections** - do not omit any required sections
- **Reference the loaded template** - template was loaded at the beginning

## Error Handling

If issues arise during the workflow:
- **Requirements unclear**: Ask targeted questions to clarify
- **Template not found**: Inform user that templates should be generated during setup
- **Validation fails repeatedly**: Suggest simplifying requirements or breaking into smaller features

## Success Criteria

A successful requirements document includes:
- [x] Complete requirements with user stories and acceptance criteria (using requirements template)
- [x] Alignment with product.md vision and goals
- [x] Compliance with conventions from conventions directory
- [x] Pass validation from spec-requirements-validator agent
- [x] Explicit user approval
- [x] Saved to correct location

## Example Usage
```
"Create requirements for user-authentication"
"Generate requirements document for payment-processing"
"创建用户登录的需求文档"
```