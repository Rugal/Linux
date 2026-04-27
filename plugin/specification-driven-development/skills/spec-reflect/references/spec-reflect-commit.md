# Spec-Reflect: Commit Phase

Write approved knowledge items to steering documents and/or conventions files using section-level smart merge. Track changes with source annotations.

## Instructions

This phase receives a list of approved items from the Propose phase. Each item contains:
- `category`, `target_file`, `target_section`, `content`, `code_example`, `source`, `type` (new/update/conflict-resolved)

---

## Step 1: Resolve Target Module (MULTI-MODULE ONLY)

If not already resolved in earlier phases:
- Check if `${workspace_root}/.claude/spec-workflow/module-map.yaml` exists
- If exists → ensure module is resolved (should already be from Harvest phase)
- Steering targets: `steering/{module}/product.md`, `tech.md`, `structure.md`
- Convention targets: `conventions/` (global, always)

### Cross-Module Write Routing

When the approved items target **multiple modules** (from a cross-module feature), each item's `target_file` already contains the correct module path (set by Harvest's Module Routing Rules). Commit phase does not re-resolve — it trusts the `target_file` from the approved item list.

**Write target summary by path prefix:**

| `target_file` prefix | Write destination | Scope |
|-----------------------|-------------------|-------|
| `steering/{module}/` | Module-specific steering doc | Only for that module |
| `steering/_global/` | Global steering doc (shared across all modules) | Cross-module decisions |
| `conventions/` | Global conventions | All modules |

### `_global` Steering Write Rules

Items targeting `steering/_global/` are cross-module technical decisions that apply to the entire project. Handle with care:

1. **Verify `_global` directory exists**: Check `${workspace_root}/.claude/spec-workflow/steering/_global/`
   - If exists → proceed with write
   - If NOT exists → this item was already re-classified to `conventions/` by Harvest phase (fallback rule). Write to conventions as-is.

2. **File creation in `_global`**: Only `product.md` and `tech.md` exist in `_global` (no `structure.md` — structure is always module-specific). If `target_file` is `steering/_global/structure.md`, redirect to the `primary_module`'s `steering/{module}/structure.md`.

### Steering Not Initialized Safeguard

Before writing any item with `target_file` starting with `steering/{module}/`:
1. Verify that `${workspace_root}/.claude/spec-workflow/steering/{module}/` directory exists
2. If NOT exists → **do not write this item**. Add to the confirmation output:
   ```
   ⚠️ Skipped: {item summary} → steering/{module}/ not initialized
      Tip: Run "initialize module {module}" to enable steering write-back
   ```
3. Continue processing remaining items — one module's missing steering should not block others.

---

## Step 2: Group Items by Target File

Organize approved items by their `target_file` path:

```
steering/margin-trading/tech.md → [item1, item4]
conventions/coding-standards.md → [item2, item3, item5]
conventions/error-handling.md   → [item6]  (new file)
```

Process each target file in sequence.

---

## Step 3: Write to Each Target File

For each target file, apply the appropriate write strategy:

### Strategy A: Update Existing File (Section-Level Merge)

Used when the target file already exists.

**Process:**

1. **Read the full target file** content
2. **Parse markdown heading structure** — identify all `##` and `###` level sections
3. **For each approved item targeting this file:**

   a. **Match target section**:
      - If `target_section` is a specific heading (e.g., `## 缓存`) → find that section
      - If `target_section` is `NEW_SECTION` → will create a new section (see step d)

   b. **Determine insertion point within the section**:
      - Find the end of the target section (before the next same-level or higher-level heading)
      - Insert new content at the end of that section's content, before any blank lines leading to the next heading

   c. **Apply write based on item type:**

      **type = "new"**: Append content at end of section
      ```
      ## 缓存                                    ← existing heading
      - Redis 6.x 用于 session 和热数据缓存       ← existing content
      - Redis Sorted Set 用于保证金水平排行 <!-- Source: feature/margin-liquidation, 2026-03-03 -->  ← NEW
      ```

      **type = "update"** (conflict resolved as Update): Replace the specific existing line/block
      ```
      ## 消息队列
      - RocketMQ 用于保证金事件处理（需要事务消息特性） <!-- Source: feature/margin-liquidation, 2026-03-03 -->  ← REPLACED
      ```

      **type = "supplement"** (conflict resolved as Supplement): Add alongside existing
      ```
      ## 消息队列
      - Kafka：主要异步通信中间件                   ← existing (kept)
      - RocketMQ：保证金事件处理（事务消息场景） <!-- Source: feature/margin-liquidation, 2026-03-03 -->  ← ADDED
      ```

   d. **If target_section = NEW_SECTION**:
      - Generate an appropriate section heading based on the item's category and content
      - Append the new section at the end of the file (before any trailing blank lines)
      ```
      ## 事件处理                                 ← NEW SECTION
      - 领域事件统一放在 domain/event/ 包下 <!-- Source: feature/margin-liquidation, 2026-03-03 -->
      ```

4. **Write the modified content** back to the file using edit_existing_file tool

   **Fallback**: If `edit_existing_file` fails (e.g., anchor context cannot be uniquely matched), fall back to: (1) Read the full file content, (2) perform the merge in memory, (3) write back the complete file using the Write tool. This ensures writes succeed even when the file structure makes precise anchoring difficult.

### Strategy B: Create New File

Used when the target file does not exist (typically for new convention topics).

**Process:**

1. **Determine file name**: Use the `target_file` path from the item classification
   - Convention files: `conventions/{topic}.md` in kebab-case (e.g., `coding-standards.md`, `error-handling.md`)
   - File name should reflect the topic, not the feature

2. **Generate file content** with standard structure:

```markdown
# {Topic Title}

Project conventions for {topic description}.

{For each approved item targeting this file:}

### {Convention Title}
> Source: {source}, {date}

{Content description}

{If code_example exists:}
```{language}
// ✅ GOOD
{good example}

// ❌ BAD
{bad example}
```
{end if}
```

3. **Write the new file** using the Write tool

---

## Step 4: Source Annotations

Every write-back must include a source annotation so future readers know where the convention came from and when it was added.

### For Steering Documents (product.md, tech.md, structure.md)

Use **HTML comment** annotations (invisible in rendered markdown, keeps documents clean):

```markdown
- Redis Sorted Set 用于保证金水平排行 <!-- Source: feature/margin-liquidation, 2026-03-03 -->
```

### For Convention Files (conventions/*.md)

Use **blockquote** annotations (visible, since conventions benefit from explicit provenance):

```markdown
### BigDecimal 比较规范
> Source: feature/margin-liquidation, 2026-03-03

BigDecimal 比较必须使用 `compareTo()`，禁止使用 `equals()`。
```

### Annotation Format

```
Source: {type}/{name}, {YYYY-MM-DD}
```

Where:
- `type` = `feature` | `bug` | `review` | `user-specified`
- `name` = feature-name or bug-name (kebab-case)
- `date` = ISO date when the knowledge was written back

Examples:
- `Source: feature/margin-liquidation, 2026-03-03`
- `Source: bug/payment-timeout, 2026-03-01`
- `Source: user-specified, 2026-03-04`

---

## Step 5: Post-Write Confirmation

After all files are written, present a confirmation summary to the user:

```
✅ Knowledge written back:

  ✓ steering/margin-trading/tech.md — 2 items added
      → "## 缓存" section: Redis Sorted Set for margin ranking
      → "## 事件处理" section (new): domain event package convention

  ✓ conventions/coding-standards.md (new file) — 2 items
      → BigDecimal comparison convention
      → Retry annotation convention

  ✓ conventions/error-handling.md — 1 item added
      → "## 超时与重试" section: external call timeout convention

These will be loaded as context in future spec-workflow operations
(requirements, design, tasks, implementation, review, testing).
```

---

## Write Safety Rules

### Never Overwrite
- Section-level merge only — never replace the entire file
- When updating within a section, only modify the specific line/block identified during conflict resolution
- Preserve all existing content that is not part of an "Update" conflict resolution

### Preserve Document Structure
- Maintain existing heading hierarchy
- Do not re-order existing sections
- New sections are always appended at the end
- Keep consistent markdown formatting (bullet style, heading level, spacing)

### Handle Edge Cases

| Scenario | Handling |
|----------|----------|
| Target file is empty (exists but no content) | Write content with standard structure (title + sections) |
| Target section has no content (heading only) | Insert as first content under the heading |
| Multiple items target the same section | Insert all items in the order they were approved |
| File write fails (permission, etc.) | Report error for that specific file, continue with remaining files |
| Steering directory not initialized | Skip steering writes for that module, only write conventions; inform user |
| Items target multiple modules (cross-module feature) | Process each item by its own `target_file` path; do NOT merge items from different modules into one file |
| Item targets `steering/_global/` but `_global/` not created | Item should already be re-classified to `conventions/` by Harvest; write to conventions |
| Item targets `steering/_global/structure.md` | Redirect to `steering/{primary_module}/structure.md` — `_global` does not have structure.md |

### Validate After Write

After writing each file:
1. **Read back** the written file to verify content integrity
2. **Check** that the file is valid markdown (no broken formatting, no orphaned code fences)
3. **Verify** source annotations are present on new content
4. If validation fails:
   - **Do NOT rollback** — partial writes within a section may be correct, and rollback could lose valid changes
   - Mark the file as `⚠️ NEEDS_REVIEW` in the confirmation summary (Step 5)
   - Include the specific issue description (e.g., "broken markdown fence at line 42")
   - Suggest a manual fix action (e.g., "check the code block closure in the BigDecimal section")
   - Continue processing remaining files — one file's issue should not block others

---

## Convention File Organization Guide

When creating new convention files, follow this naming convention:

| Topic | File Name | Typical Content |
|-------|-----------|-----------------|
| General coding rules | `coding-standards.md` | Naming, formatting, data types, null handling |
| API design patterns | `api-conventions.md` | REST conventions, response format, versioning |
| Error handling | `error-handling.md` | Exception patterns, retry logic, timeout policies |
| Security practices | `security-guidelines.md` | Input validation, auth patterns, data protection |
| Database usage | `database-conventions.md` | Query patterns, indexing rules, migration conventions |
| Test practices | `test-conventions.md` | Framework, mock strategy, naming (already exists from spec-test) |
| Logging standards | `logging-conventions.md` | Log levels, format, structured logging patterns |
| Git workflow | `git-workflow.md` | Branch naming, commit format, PR conventions |

**File creation rules:**
- Use kebab-case for file names
- One topic per file — do not create catch-all files
- If a topic doesn't fit existing files, create a new one
- Check existing files first before creating — avoid splitting a topic across multiple files

---

## Complete Flow Example

**Input** (from Propose phase): 3 approved items

```yaml
items:
  - category: tech
    target_file: steering/margin-trading/tech.md
    target_section: "## 缓存"
    content: "Redis Sorted Set 用于保证金水平排行"
    type: new
    source: feature/margin-liquidation

  - category: coding-standard
    target_file: conventions/coding-standards.md
    target_section: NEW_SECTION
    content: "BigDecimal 比较必须使用 compareTo()"
    code_example: {good: "marginLevel.compareTo(threshold) < 0", bad: "marginLevel.equals(threshold)"}
    type: new
    source: feature/margin-liquidation

  - category: error-handling
    target_file: conventions/error-handling.md
    target_section: NEW_SECTION
    content: "外部服务调用必须配置超时(3s)和重试(3次)"
    type: new
    source: bug/payment-timeout
```

**Process:**

1. Group: tech.md(1 item), coding-standards.md(1 item, new file), error-handling.md(1 item, new file)

2. Write tech.md: read existing → find "## 缓存" → append item with HTML comment annotation

3. Create coding-standards.md: generate new file with title + BigDecimal convention entry with ✅/❌ examples + blockquote source

4. Create error-handling.md: generate new file with title + timeout/retry convention entry + blockquote source

5. Read back all 3 files to verify

6. Show confirmation:
```
✅ Knowledge written back:

  ✓ steering/margin-trading/tech.md — 1 item added to "## 缓存"
  ✓ conventions/coding-standards.md — 1 item (new file created)
  ✓ conventions/error-handling.md — 1 item (new file created)

These will be loaded as context in future spec-workflow operations.
```
