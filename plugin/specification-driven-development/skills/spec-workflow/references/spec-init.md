Create or update steering documents that provide persistent project context. Supports both single-module (global) and multi-module modes.

## Instructions
You are helping set up steering documents that will guide all future spec development. These documents provide persistent context about the product vision, technology stack, and project structure.

## Process

### Step 1: Initialize Spec-Workflow Structure (MANDATORY)

**⚠️ CRITICAL: You MUST run the Python script below. DO NOT use mkdir or other manual methods.**

Before running the script, detect the initialization mode:
- If user explicitly said "initialize module {module-name}" → go to [Module Init](#module-init) section below
- Otherwise → continue with project initialization

Execute this command FIRST before any other action:
```bash
python ${skill_directory}/scripts/init_spec_workflow.py ${workspace_root_directory}
```
(Note: the `--multi-module` flag will be added later if user selects Mode B)

**After running the script, it will:**
- Create `.claude/spec-workflow/user-templates/` directory
- Create `.claude/spec-workflow/conventions/` directory
- Create `.claude/spec-workflow/steering/` directory
- **Copy all template files** (product, tech, structure, requirements, design, tasks, implementation-log)
- Report results to confirm successful initialization

**Wait for script output before proceeding to Step 2.**

**Purpose of created directories**:
- **user-templates/** - Customizable templates for your project
    - Contains: product, tech, structure, requirements, design, tasks, implementation-log templates
    - You can modify these templates to match your project needs
- **conventions/** - Store coding standards and project guidelines
    - Add coding standards and conventions (e.g., naming conventions, code style guides)
    - Document API design patterns and best practices
    - Keep project-specific guidelines and requirements
    - Reference documentation for team standards
    - **Suggested files to add** (optional):
        - `coding-standards.md` - Code style, naming conventions, formatting rules
        - `api-conventions.md` - REST API design patterns, endpoint naming
        - `git-workflow.md` - Branch naming, commit message format
        - `security-guidelines.md` - Security best practices for the project
        - Any other project-specific conventions
- **steering/** - Product, tech, and structure documentation
    - Will contain: product.md, tech.md, structure.md (generated in next steps)
    - These documents provide persistent context for all future spec work

### Step 2: Scan Workspace & Select Mode

1. **Scan workspace** for top-level directories that appear to be repositories (contain `.git/`, `pom.xml`, `package.json`, `go.mod`, `build.gradle`, `README.md`, or `src/` directories).

2. **Present findings and mode selection:**

```
Detected workspace contains the following repositories/projects:
- repo-a/
- repo-b/
- repo-c/
- ...

Please select initialization mode:

A. Global Mode - The entire workspace as a single unit, generating one set of steering documents.
   Suitable for: single-repo projects, or multi-repo but closely related business domains.

B. Multi-Module Mode - Organized by business modules, each module gets independent steering documents.
   Suitable for: multi-repo with different business domains, development organized by module.

⚠️ This choice cannot be changed later. To switch modes, you must manually adjust the file structure.

Enter your choice (A/B):
```

3. **Based on user's choice:**
   - **Mode A** → go to [Step 3A: Global Mode](#step-3a-global-mode-existing-flow)
   - **Mode B** → go to [Step 3B: Multi-Module Mode](#step-3b-multi-module-mode)

---

### Step 3A: Global Mode (Existing Flow)

This is the original initialization flow, unchanged.

#### 3A.1 Check for Existing Steering Documents
- Look for existing product.md, tech.md, structure.md files in `${workspace_root_directory}/.claude/spec-workflow/steering/`
- If they exist, load and display current content
- If not exist:
    - Load product template: `cat ${workspace_root_directory}/.claude/spec-workflow/user-templates/product-template.md`
    - Load tech template: `cat ${workspace_root_directory}/.claude/spec-workflow/user-templates/tech-template.md`
    - Load structure template: `cat ${workspace_root_directory}/.claude/spec-workflow/user-templates/structure-template.md`

#### 3A.2 Analyze the Project
- Review the codebase to understand:
    - Project type, purpose, features, target users
    - Technology stack in use: Frameworks, libraries, tools etc
    - Directory structure and patterns: File organization, naming conventions etc
    - Coding conventions
    - Existing features and functionality
- Look for:
    - package.json, requirements.txt, go.mod, application.yml, pom.xml etc.
    - README files
    - Configuration files
    - Source code structure

#### 3A.3 Conduct Steering Interview

**Purpose**: Gather information that cannot be inferred from code analysis alone.

Follow the structured interview process defined in [steering-interview.md](steering-interview.md):

##### Present Code Analysis Summary
Before starting the interview, present what was inferred from codebase:
```
Based on my analysis, here's what I've inferred:

**Product Details:**
- [Inferred detail 1]
- [Inferred detail 2]

**Technology Stack:**
- [Inferred tech 1]
- [Inferred tech 2]

**Project Structure:**
- [Inferred pattern 1]
- [Inferred pattern 2]
```

##### Conduct Domain-Specific Interview Rounds

**Interview Structure** (5-8 rounds total):

| Domain | Rounds | Focus |
|--------|--------|-------|
| **Product** | 2-3 rounds | Vision, users, business goals, constraints |
| **Tech** | 2-3 rounds | Architecture decisions, quality attributes, challenges |
| **Structure** | 1-2 rounds | Organization principles, conventions |

**Interview Guidelines**:
- Use **numbered options format** (A/B/C/D) for all questions
- Ask **2-4 questions per round** maximum
- **Validate code inferences** - confirm if assumptions are correct
- **Focus on gaps** - prioritize information not in code
- Allow user to skip questions (mark as TBD)

**Quick Mode Option**:
If user prefers faster setup, offer:
```
Would you like to:
A. Full interview - Comprehensive Q&A for high-quality documents (Recommended)
B. Quick mode - Answer only critical questions, infer the rest
C. Skip interview - Generate documents from code analysis only

Enter your choice (A/B/C):
```

##### Between-Round Analysis
Before each new round:
1. Summarize information collected so far
2. Check for contradictions with code analysis
3. Identify remaining critical gaps
4. Adjust next questions based on answers

##### Interview Completion
End interview when:
- All three domains have been covered
- No critical gaps remain (or marked as TBD)
- User indicates they have no more information to add

Present summary:
```
## Interview Summary

### Product Domain
- Vision: [summarized]
- Target Users: [summarized]
- Business Goals: [summarized]

### Tech Domain
- Architecture: [summarized]
- Quality Attributes: [summarized]
- Known Challenges: [summarized]

### Structure Domain
- Organization: [summarized]
- Conventions: [summarized]

Ready to generate steering documents?
```

#### 3A.4 Generate Steering Documents
- Generate three files in `${workspace_root_directory}/.claude/spec-workflow/steering/` based on templates and gathered information:

  **product.md**: Product vision, users, features, objectives
  **tech.md**: Technology stack, tools, constraints, decisions
  **structure.md**: File organization, naming conventions, patterns

- Save the documents to `${workspace_root_directory}/.claude/spec-workflow/steering/` directory

#### 3A.5 Notify User and Request Review
- Inform user that steering documents have been generated
- Summarize the document contents and list key highlights.
- Direct user to review the files in `${workspace_root_directory}/.claude/spec-workflow/steering/` directory
- Make any requested adjustments if user requests changes
- Provide clear path information for file access

---

### Step 3B: Multi-Module Mode

#### 3B.1 Run Init Script with Multi-Module Flag

Re-run the init script with the `--multi-module` flag to create additional directories:

```bash
python ${skill_directory}/scripts/init_spec_workflow.py ${workspace_root_directory} --multi-module
```

This additionally creates:
- `module-map.yaml` (template)

#### 3B.2 AI Analyzes Repos and Generates Module-Map

1. **Analyze each repository** detected in Step 2:
   - Read README.md, pom.xml, package.json, build.gradle, go.mod
   - Examine top-level source code structure
   - Identify business domain and purpose

2. **Auto-cluster repos into modules**:
   - Group repos that belong to the same business domain
   - Generate a `module-map.yaml` draft with module names, descriptions, and repo lists
   - **Module naming**: Choose concise, meaningful names that reflect the business domain or functional area (e.g., `margin-trading`, `gateway`, `user-auth`, `order-engine`). Do NOT use a fixed prefix pattern like `architecture-*` — the name should naturally describe what the module is about.

3. **For repos with insufficient information** (no README or uninformative content):
   - Ask the user individually: "I couldn't determine the domain for `{repo-name}`. Which module does it belong to? Or should I create a new module for it?"

4. **Present the complete module-map draft** for user confirmation:
   ```
   Based on code analysis, here's the proposed module grouping:

   margin-trading (margin, isolated-margin)
     → 保证金交易系统，包含全仓和逐仓杠杆交易

   api-gateway (main-gateway)
     → MGS 网关聚合层

   ...

   Is this grouping correct? Need any adjustments?
   ```

5. **Save the confirmed module-map.yaml** to `${workspace_root_directory}/.claude/spec-workflow/module-map.yaml`

#### 3B.3 Ask About Global Steering Documents

After module-map is confirmed, ask the user:

```
Do you want to create global-level steering documents (_global)?
Global steering provides shared cross-module context (product vision, security policies, shared tech stack).

A. Yes — Create _global steering (recommended for workspaces with shared infrastructure/policies)
B. No — Skip to module initialization directly

Enter your choice (A/B):
```

**If user chooses A** → proceed to 3B.4 (Global Steering)
**If user chooses B** → skip to 3B.6 (Notify and Guide Next Steps)

#### 3B.4 Create _global and Conduct Global Interview (Only if user chose A)

1. Create `${workspace_root_directory}/.claude/spec-workflow/steering/_global/` directory
2. Conduct a global-level steering interview focused on **cross-module shared context**.

Follow the interview process in [steering-interview.md](steering-interview.md), using the **Global Interview Mode** which focuses on:

**Product Domain (global):**
- Overall product vision and strategic positioning
- Unified security policies and compliance requirements (e.g., data encryption, audit logging, KYC/AML)
- Cross-module shared API specifications or protocol conventions

**Tech Domain (global):**
- Shared technology stack and rationale
- Common architecture principles (microservice communication, data consistency strategy, service discovery)
- Shared infrastructure (message queues, caching layer, monitoring/alerting, logging systems)

**Collaboration Domain (global, replaces Structure):**
- Team collaboration norms (release process, code review mechanisms, branch strategy)
- Cross-module dependency management approach

**Do NOT ask about project structure** in global mode — structure.md is module-level only.

#### 3B.5 Generate Global Steering Documents (Only if user chose A)

Generate two files in `${workspace_root_directory}/.claude/spec-workflow/steering/_global/`:

**product.md**: Overall product vision, security policies, compliance requirements, cross-module API conventions
**tech.md**: Shared technology stack, common architecture principles, shared infrastructure

Save the documents and present for user review.

#### 3B.6 Notify and Guide Next Steps

```
✅ Multi-module project initialization complete!

Created:
- module-map.yaml (module registry)
{if _global was created:}
- steering/_global/product.md (global product context)
- steering/_global/tech.md (global tech context)

Next: Initialize individual modules as needed by saying:
  "initialize module {module-name}"

For example:
  "initialize module margin-trading"

You only need to initialize modules you're actively working on.
```

---

### Module Init

This section handles `initialize module {module-name}`.

#### M.1 Check Prerequisites

1. **Check if `module-map.yaml` exists** at `${workspace_root_directory}/.claude/spec-workflow/module-map.yaml`
   - **If NOT exists**: Smart guidance — inform user and automatically trigger the project initialization flow:
     ```
     Module map not found. The project needs to be initialized first with mode selection.
     Let me guide you through project initialization...
     ```
     Then go to [Step 1](#step-1-initialize-spec-workflow-structure-mandatory) and run the full project init flow. After completion, continue with module init.
   - **If exists**: Continue

2. **Validate module exists** in module-map.yaml
   - Parse the YAML file and check if `{module-name}` is registered
   - **If NOT found**: Inform user of available modules and ask them to choose or update module-map.yaml

3. **Check if already initialized**
   - Check if `${workspace_root_directory}/.claude/spec-workflow/steering/{module-name}/` already exists
   - **If exists**: Inform user and ask if they want to re-initialize (overwrite) or keep existing

#### M.2 Analyze Module Repos

1. Load the module's repo list from module-map.yaml
2. **Deep analysis** of each repo in the module:
   - README.md, configuration files, source structure
   - Key packages, services, and domain models
   - Technology-specific patterns (framework usage, libraries)
   - Project structure and conventions
3. **Pre-fill steering content** based on analysis findings

#### M.3 Module-Level Steering Interview

Conduct a focused, shorter interview (2-3 rounds) for module-specific context.

Follow [steering-interview.md](steering-interview.md) with **Module Interview Mode**:

**Product Domain (module):**
- Module's role and positioning within the overall product
- Dependencies on and relationships with other modules (reference module-map for context, even if other modules aren't initialized)

**Tech Domain (module):**
- Module-specific technology decisions (beyond the shared stack)
- Module-specific architecture patterns or middleware

**Structure Domain (module):**
- Module's project directory structure and organization
- Module-specific naming conventions

Since AI already pre-analyzed the repos, the interview should focus on **what AI couldn't infer** — reducing from the standard 5-8 rounds to 2-3 rounds.

#### M.4 Generate Module Steering Documents

Create the module steering directory and files:

1. Create directory: `${workspace_root_directory}/.claude/spec-workflow/steering/{module-name}/`
2. Generate three files:
   - **product.md**: Module product positioning, relationships with other modules
   - **tech.md**: Module-specific technology decisions
   - **structure.md**: Module project structure and conventions

Save documents and present for user review.

#### M.5 Notify Completion

```
✅ Module '{module-name}' initialization complete!

Created:
- steering/{module-name}/product.md
- steering/{module-name}/tech.md
- steering/{module-name}/structure.md

You can now create specs and track bugs with this module's context:
  "create requirements for feature-name"
```

---

## Important Notes

- **Steering documents are persistent** - they will be referenced in all future spec commands
- **Keep documents focused** - each should cover its specific domain
- **Update regularly** - steering docs should evolve with the project
- **Never include sensitive data** - no passwords, API keys, or credentials
- **Mode is irreversible** - once Global or Multi-Module mode is chosen, it cannot be automatically switched
- **Module init is on-demand** - initialize modules only when you need to work on them

## Next Steps

### 👉 Now: Create Requirements
Project initialization is complete. The next step is to create a feature specification, starting with requirements:

```
"create requirements for {feature-name}"
```

Steering documents will be automatically loaded as context for all subsequent commands.

### 📋 Workflow Quick Reference

Here's everything you can do with the spec workflow:

**Feature Development Flow**: `requirements → design → tasks → execute → review / test`

| Command | What it does |
|---------|-------------|
| `create requirements for {feature}` | Define what to build — user stories, acceptance criteria |
| `create design for {feature}` | Translate requirements into technical architecture |
| `create tasks for {feature}` | Break design into atomic, executable tasks |
| `execute task {id} for {feature}` | Implement one task at a time with validation |
| `review code for {feature}` | Code review against design and conventions |
| `generate tests for {feature}` | Auto-generate unit tests for the feature |
| `validate requirements/design/tasks` | Validate spec documents for quality and completeness |
| `spec-reflect` or `remember: {rule}` | Extract and persist project knowledge to conventions |
| `save` / `checkpoint` | Save conversation context for later resumption |
| `resume {feature}` | Restore saved context in a new window |

**Bug Fix Flow**: `create → analyze → fix → verify`

| Command | What it does |
|---------|-------------|
| `bug-create {name}` or `bug-fix {name}` | Report a bug or jump straight to fixing (auto-triggers preceding steps) |
| `bug-verify {name}` | Verify the fix with real test execution |
