# 💾 Spec Context Checkpoint Skill

> Save your conversation context for a spec feature to resume later in a new window.

---

## 🎯 What is this?

This skill saves all conversations from your current window for a specific spec feature. It's useful for:

- Long development sessions that span multiple days
- Preserving important decisions and discussions
- Enabling context handoff between sessions

---

## 📦 Installation

### Step 1: Symlink the skill to Claude Code

```bash
# Create skills directory if it doesn't exist
mkdir -p ~/.claude/skills

# Symlink this skill
ln -s /path/to/specification-driven-development/skills/spec-context-checkpoint ~/.claude/skills/spec-context-checkpoint
```

### Step 2: Verify installation

The skill should now appear in your AI assistant's available skills list.

---

## 🚀 Usage

When you want to save your conversation:

```
"Save context for block-user-personal-chat"
"Checkpoint the current session"
"保存当前对话"
```

After saving, **open a new window** and use `spec-context-resume` to continue.

---

## 📁 What Gets Saved

| Content | Location |
|---------|----------|
| Full conversations | `sessions/{session-id}/turn-*.md` |
| Session metadata | `sessions/{session-id}/metadata.json` |
| Historical summaries | `semantic-chunks/chunk-*.md` |
| Manifest | `manifest.json` |

### Directory Structure

```
.claude/spec-workflow/specs/{feature}/.context/
├── manifest.json
└── conversations/
    ├── semantic-chunks/
    │   ├── chunk-001.md    # Summary of turns 1-15
    │   └── chunk-002.md    # Summary of turns 16-30
    └── sessions/
        └── session-003/    # Current session (turns 31-40)
            ├── metadata.json
            ├── turn-001.md
            └── turn-010.md
```

---

## 📋 How It Works

1. **Collects all conversations** from current window
2. **Filters out** skill trigger messages (save/resume commands)
3. **Assigns global turn numbers** for tracking
4. **Generates summaries** when total turns ≥ 20
5. **Updates manifest** with session info

---

## ⚠️ Important Notes

- **Full content is preserved** - No summarization of recent conversations
- **Summaries only for old sessions** - When total turns exceed 20
- **New window required** - After saving, open new window to resume

---

## 🔄 Workflow

```
1. Work on feature in current window
2. "Save context for {feature}"  ← Checkpoint
3. Open new window
4. "Resume {feature}"  ← Continue working
```

---

## 📄 License


