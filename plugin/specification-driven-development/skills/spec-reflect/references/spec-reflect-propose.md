# Spec-Reflect: Propose Phase

Present discovered knowledge items to the user for review and approval. Supports three entry modes: passive prompt, detailed review, and direct input.

## Instructions

---

## Entry Mode 1: Passive Prompt (From Phase Completion)

When spec-reflect is triggered passively after a phase completes, the Harvest phase produces a brief summary appended to the phase completion output.

### Integration with Phase Completion Output

The spec-reflect prompt is inserted **between** the phase completion summary and the existing next-step options. It uses a visual separator to distinguish itself without breaking the flow.

**Example — spec-run All Tasks Complete:**

```
✅ All 8 tasks for margin-liquidation have been completed!

[Summary of what was built — files created/modified, key components]

───────────────────────────────
📝 Detected 3 learnings from this implementation:

  • [convention] [Project-wide] BigDecimal comparison uses compareTo(), not equals()
  • [tech] [Project-wide] Redis Sorted Set chosen for margin level ranking
  • [structure] [Universal] New package: domain/event/ for domain events

L. Learn — review and write back to project docs
S. Skip — maybe later ("spec-reflect margin-liquidation")
───────────────────────────────

📝 Next recommended steps: Code Review & Unit Tests

A. Generate unit tests
B. Run code review
C. Both
D. Skip

Enter your choice:
```

**Example — spec-review complete:**

```
✅ Code review complete for margin-liquidation

[Review summary...]

───────────────────────────────
📝 Review surfaced 2 reusable conventions:

  • [convention] [Universal] All external API calls must have circuit breaker
  • [convention] [Project-wide] BO objects must implement Serializable

L. Learn — review and write back to project docs
S. Skip
───────────────────────────────
```

**Example — bug-verify complete:**

```
✅ Bug payment-timeout has been verified and resolved.

[Verification summary...]

───────────────────────────────
📝 Detected 1 learning from this bug fix:

  • [error-handling] [Feature-specific] External service calls must have timeout (3s) + retry (3 attempts)

L. Learn — review and write back to project docs
S. Skip
───────────────────────────────
```

### User Response Handling

| User Input | Action |
|------------|--------|
| `L` or `learn` | Enter Detailed Review (see below) |
| `S` or `skip` | Terminate spec-reflect; continue main flow |
| `A`, `B`, `C`, `D` (or other main flow option) | Terminate spec-reflect; proceed with selected main flow option |
| No explicit choice about L/S, directly answers main flow | Treat as Skip; proceed with main flow |

**Key rule**: spec-reflect never blocks the main workflow. If the user ignores the learn prompt and directly selects a main flow option, spec-reflect silently exits.

---

## Entry Mode 2: Detailed Review (From L selection or Active Trigger)

When the user selects `L` from passive prompt, or actively triggers `spec-reflect`, present the full discovery list for approval.

### Step 1: Present Grouped Discoveries

Group discoveries by target file. For each item, show:
- Category tag
- **Reusability tag** (Universal / Project-wide / Pattern-specific — Feature-specific items are already filtered out by Harvest)
- Content summary
- Code example (if applicable, using ✅/❌ format)
- Source annotation

**Note on Pattern-specific items**: These have already been abstracted during Harvest (business-specific details stripped). The `[Pattern]` tag signals to the user that the original finding was narrower but has been generalized.

**Format:**

```
📝 Proposed write-backs ({N} items):

─── {target_file_1} ───

 1. [{category}] [{reusability}] {title}
    {description}
    ```{language}
    // ✅ GOOD
    {good example}
    // ❌ BAD
    {bad example}
    ```
    > Source: {feature/bug}, {phase}, {date}

 2. [{category}] [{reusability}] {title}
    {description}
    > Source: {feature/bug}, {phase}, {date}

─── {target_file_2} (NEW FILE) ───

 3. [{category}] [{reusability}] {title}
    {description}
    > Source: {feature/bug}, {phase}, {date}

─────────────────────────────
A. Accept all
S. Skip all
Or pick items to accept (e.g., "1,3" or "1-3")

Enter your choice:
```

### Step 2: Handle Conflicts

If any discovery was marked as `conflict` during Harvest, present it separately with resolution options:

```
⚠️ Conflict detected for item {N}:

  Existing ({target_file}):
    "{existing content}"

  New finding:
    "{new content}"

  U. Update — replace existing with new (existing is outdated)
  S. Supplement — add alongside existing (both are valid, different contexts)
  K. Keep — keep existing, discard this finding (existing is correct, this is an exception)

  Enter your choice (U/S/K):
```

**Resolution actions:**
- **U (Update)**: Replace the existing content with the new finding in the target section
- **S (Supplement)**: Append the new finding as an additional point under the same section
- **K (Keep)**: Discard this finding, do not write anything

### Step 3: Handle User Selections

| User Input | Action |
|------------|--------|
| `A` / `accept all` | Accept all non-conflict items; process conflicts one by one |
| `S` / `skip all` | Terminate, write nothing |
| `1,3` or `1-3` | Accept only specified items; skip the rest |
| `1,3` + conflict resolution | Accept specified items + resolve conflicts |

### Step 4: Confirm and Proceed

After collecting user approvals:

```
Will write back {M} items:
  ✓ {target_file_1} — {N1} items
  ✓ {target_file_2} — {N2} items (new file)

Proceed? (Y/n):
```

- **Y**: Proceed to [spec-reflect-commit.md](spec-reflect-commit.md) with approved items
- **N**: Return to item list for re-selection

---

## Entry Mode 3: Direct Input (Remember Mode)

When user provides a convention directly via "remember:" or "记住:" prefix.

### Step 1: Parse User Input

Extract the convention statement from user input:
- `"remember: all REST APIs must return CommonResponse wrapper"` → convention about API response format
- `"记住: BigDecimal 比较用 compareTo"` → convention about numeric comparison
- `"remember: BO objects go in domain/bo/ package"` → structure convention

### Step 1.5: Resolve Module Context (MULTI-MODULE ONLY)

Before classifying, determine if the workspace is multi-module and resolve the target module for steering-category knowledge.

1. **Check mode**: Does `${workspace_root}/.claude/spec-workflow/module-map.yaml` exist?
   - If NOT → single-module mode. Skip to Step 2.
   - If YES → multi-module mode. Continue below.

2. **Classify first (lightweight)**: Quickly determine if the input is likely a convention (global) or a steering item (module-scoped):
   - **Convention indicators**: coding patterns, API format rules, error handling, test practices, security → will write to `conventions/` regardless of module. **Skip module resolution** — not needed.
   - **Steering indicators**: product behavior, technology choice, directory/package structure → will write to `steering/{module}/`. **Module resolution required** — continue below.

3. **Resolve module** using the same priority chain as spec-workflow's module-resolver:
   - **Priority 1**: User explicitly specified module in input (e.g., `"remember for margin-trading: use Redis Sorted Set"`)
   - **Priority 2**: Most recently edited file in IDE → match against module-map repos
   - **Priority 3**: AI semantic inference — match the knowledge content against module descriptions
   - **Priority 4**: Single module in module-map → use directly
   - **Priority 5**: Present modules for user to choose:
     ```
     This knowledge targets a steering document. Which module?
     
     A. margin-trading — Margin trading core
     B. isolated-margin — Isolated margin engine
     
     Enter your choice:
     ```

4. **Verify steering exists**: Check if `steering/{resolved_module}/` is initialized
   - If NOT → inform user:
     ```
     ⚠️ Steering for module "{module}" is not initialized.
     
     A. Write as a convention instead (global)
     B. Initialize module steering first ("initialize module {module}")
     ```

### Step 2: Classify and Determine Target

Apply classification logic:

1. **Determine category**: coding-standard, api-convention, error-handling, structure, tech, etc.
2. **Determine target file**: Apply the **Module Routing Rules** from [spec-reflect-harvest.md](spec-reflect-harvest.md) (CLASSIFY section):
   - Convention categories (`coding-standard`, `api-convention`, `error-handling`, `test-convention`, `security`) → always `conventions/{topic}.md` (global)
   - Steering categories (`product`, `tech`, `structure`):
     - Single-module → `steering/{category}.md`
     - Multi-module → `steering/{resolved_module}/{category}.md` (using module from Step 1.5)
     - If knowledge is explicitly cross-module → `steering/_global/{category}.md` if `_global/` exists, else fallback to `conventions/`
3. **Check for duplicates**: read target file and check if semantically equivalent content exists
4. **Check for conflicts**: check if it contradicts existing content

### Step 3: Present Write-Back Proposal

```
📝 Got it. Proposed write-back:

─── {target_file} ───

  [{category}] {title}
  {formatted content with code example if applicable}
  > Source: user-specified, {date}

{if duplicate detected}
  ⚠️ Similar content already exists in {target_file}:
    "{existing content}"

  U. Update existing entry with new version
  S. Add alongside existing (both are valid)
  K. Cancel — existing is sufficient

  Enter your choice (U/S/K):
{else}
  Write to {target_file}? (Y/n):
{end if}
```

### Step 4: Proceed

- **Y / U / S**: Proceed to [spec-reflect-commit.md](spec-reflect-commit.md) with the item
- **N / K**: Cancel, write nothing

---

## Convention Entry Generation Guidelines

When generating convention entries for user review, follow these formatting rules:

### With Code Example (Preferred for Coding Standards)

```markdown
### {Convention Title}
> Source: {feature/bug-name}, {date}

{One-sentence description of what the convention is and why it matters.}

```{language}
// ✅ GOOD
{correct code example}

// ❌ BAD
{incorrect code example with comment explaining why it's wrong}
```
```

### Without Code Example (For Architecture/Process Conventions)

```markdown
### {Convention Title}
> Source: {feature/bug-name}, {date}

{Description of the convention. Can be multiple sentences if needed.}

- {Specific rule or guideline 1}
- {Specific rule or guideline 2}
```

### Guidelines for Good Convention Entries

- **Actionable**: State what TO DO, not just what to avoid
- **Specific**: "Use `compareTo()` for BigDecimal comparison" not "Handle BigDecimal carefully"
- **Contextual**: Include WHY when the reason isn't obvious
- **Concise**: One convention per entry, not a paragraph of multiple rules
- **Exemplified**: Include code examples when the convention involves code patterns

---

## Important Rules

- **Never block the main workflow**: In passive mode, if user ignores the learn prompt, silently exit
- **Respect user choices**: If user says Skip, stop immediately — do not persuade or re-ask
- **Quality presentation**: Group by target file, show code examples, make it easy to scan
- **Conflict transparency**: Always surface conflicts explicitly, never silently overwrite
- **Batch-friendly**: Always offer "Accept all" for efficient approval when many items are detected
