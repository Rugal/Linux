# 🔄 Spec Context Resume Skill

> Resume development context for a spec feature from a previously saved checkpoint.

---

## 🎯 What is this?

This skill loads saved conversation context for a spec feature, allowing you to continue where you left off. It loads:

- Recent conversations (full content)
- Historical summaries (compressed)
- Document previews (requirements, design, tasks)

---

## 📦 Installation

### Step 1: Symlink the skill to Claude Code

```bash
# Create skills directory if it doesn't exist
mkdir -p ~/.claude/skills

# Symlink this skill
ln -s /path/to/specification-driven-development/skills/spec-context-resume ~/.claude/skills/spec-context-resume
```

### Step 2: Verify installation

The skill should now appear in your AI assistant's available skills list.

---

## 🚀 Usage

In a **new window**, ask to resume your feature:

```
"Resume block-user-personal-chat"
"Continue working on payment-detection"
"恢复 xxx 功能的上下文"
```

---

## 📋 What Gets Loaded

| Content | Amount | Source |
|---------|--------|--------|
| Recent conversations | Last 12 turns (full) | `sessions/{latest}/turn-*.md` |
| Historical summaries | All chunks | `semantic-chunks/chunk-*.md` |
| Document previews | First 30 lines each | `requirements.md`, `design.md`, `tasks.md` |

### Initial Load Size

- Target: < 15KB initial load
- More content loaded on demand

---

## 📊 Resume Output

```markdown
# Resume: {feature-name}

## 📊 Status Overview
| Item | Value |
|------|-------|
| Feature | block-user-personal-chat |
| Total turns | 30 |
| Current phase | Design |
| Recent session | session-003 (turns 23-30) |

## 📝 Recent Conversations (Session-003)

### Turn 25
**User**: How should we design the error codes?
**Assistant**: I suggest using this structure...

### Turn 26
**User**: Add a batch endpoint
**Assistant**: Here's the batch API design...

## 📚 Historical Summaries

### Summary-001 (Turns 1-10)
Requirements analysis phase...

### Summary-002 (Turns 11-22)
Design phase completed...

## 📄 Documents
| Document | Status |
|----------|--------|
| requirements.md | ✅ 3 user stories |
| design.md | ✅ API + DB design |
| tasks.md | ✅ 12 tasks, 8 completed |

## 🚀 Continue Working
Context loaded. Ready to continue.
```

---

## 🔄 Workflow

```
1. Open new window
2. "Resume {feature}"  ← Load context
3. Continue working
4. "Save context"  ← Checkpoint again
```

---

## ⚠️ Error Handling

| Error | Solution |
|-------|----------|
| "No saved context" | Run checkpoint first |
| "Manifest not found" | Check feature name spelling |
| "Partial load" | Some files corrupted, re-save |

---

## 📄 License


