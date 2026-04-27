---
name: spec-context-resume
description: |
  恢复功能的开发上下文。
  加载最近会话完整内容 + 历史摘要。
---

# 上下文恢复

## 核心规则

### 加载什么？
1. **最近会话**：完整的对话文件（最多 12 轮）
2. **历史摘要**：语义压缩块
3. **文档索引**：requirements.md, design.md, tasks.md 的概要

### 加载多少？
- 初始加载 < 15KB
- 更多内容按需加载

---

## 执行流程

### 第一步：识别功能

从用户请求识别功能名称：
- "恢复 xxx"
- "resume xxx"
- "继续 xxx"

确认清单文件存在（**使用工作空间相对路径**）：
```
<工作空间>/.claude/spec-workflow/specs/{功能名}/.context/manifest.json
```

> ⚠️ **路径规则**
> - 必须使用**相对于工作空间的路径**，不要使用绝对路径
> - 每个用户的工作空间目录可能不同，但相对路径结构一致
> - 示例：如果功能名是 `block-user-personal-chat`，则路径为：
>   `.claude/spec-workflow/specs/block-user-personal-chat/.context/manifest.json`

---

### 第二步：读取清单文件

```python
manifest = read_json(f".claude/spec-workflow/specs/{功能名}/.context/manifest.json")

info = {
    "feature": manifest["specId"],
    "total_turns": manifest["turnWatermark"]["lastSessionEndTurn"],
    "last_chunked": manifest["turnWatermark"]["lastChunkedTurn"],
    "chunks": manifest.get("semanticChunks", []),
    "sessions": manifest.get("recentSessions", [])
}
```

---

### 第三步：加载最近会话（完整）

```python
# 找最近的会话
recent = sorted(manifest["recentSessions"], 
                key=lambda s: s["globalRange"][1], 
                reverse=True)[0]

session_dir = f".claude/spec-workflow/specs/{功能名}/.context/conversations/sessions/{recent['sessionId']}"
metadata = read_json(f"{session_dir}/metadata.json")

# 加载对话文件（最多 12 轮）
turn_files = sorted(glob(f"{session_dir}/turn-*.md"))[-12:]
for f in turn_files:
    content = read_file(f)
    # 加入上下文
```

---

### 第四步：加载历史摘要

```python
for chunk in manifest.get("semanticChunks", []):
    chunk_path = f".claude/spec-workflow/specs/{功能名}/.context/conversations/{chunk['file']}"
    chunk_content = read_file(chunk_path)
    # 加入上下文
```

---

### 第五步：加载文档索引

```python
docs = ["requirements.md", "design.md", "tasks.md"]
for doc in docs:
    path = f".claude/spec-workflow/specs/{功能名}/{doc}"
    if exists(path):
        # 读取前 30 行作为预览
        preview = read_lines(path, 1, 30)
```

---

### 第六步：输出上下文

```markdown
# 恢复: {功能名}

## 📊 状态概览

| 项目 | 值 |
|------|-----|
| 功能 | block-user-personal-chat |
| 总对话轮数 | 30 |
| 当前阶段 | 设计 |
| 最近会话 | session-003 (第23-30轮) |

---

## 📝 最近对话 (会话-003)

### 第25轮
**用户**: API 的错误码怎么设计？

**助手**: 建议使用以下错误码结构：
```json
{
  "code": "BLOCK_001",
  "message": "用户已被屏蔽"
}
```
...

### 第26轮
**用户**: 加个批量接口

**助手**: 好的，添加批量屏蔽接口：
```http
POST /api/v1/block/batch
```
...

---

## 📚 历史摘要

### 摘要-001 (第1-10轮)
需求分析阶段，确定了用户屏蔽功能的基本需求。
- 决定使用 scenario 字段区分场景
- 确定复用现有数据库表

### 摘要-002 (第11-22轮)
设计阶段，完成了 API 和数据库设计。
- 采用 RESTful 风格
- 设计了缓存策略

---

## 📄 文档

| 文档 | 状态 |
|------|------|
| requirements.md | ✅ 3 个用户故事 |
| design.md | ✅ API + 数据库设计 |
| tasks.md | ✅ 12 个任务，完成 8 个 |

---

## 🚀 继续工作

上下文已加载，请继续之前的工作。

如需查看完整文档，请说：
- "查看 design.md"
- "查看 requirements.md"
```

---

## 按需加载

### 查看文档

**用户**："查看 design.md"

**AI**：读取并展示完整文档内容

### 查看特定摘要

**用户**："查看摘要-001详情"

**AI**：读取并展示摘要完整内容

---

## 目录结构

```
<工作空间>/
└── .claude/
    └── spec-workflow/
        └── specs/
            └── {功能名}/
                ├── requirements.md      ← 文档索引
                ├── design.md
                ├── tasks.md
                └── .context/
                    ├── manifest.json    ← 首先读取
                    └── conversations/
                        ├── semantic-chunks/
                        │   ├── chunk-001.md    ← 加载摘要
                        │   └── chunk-002.md
                        └── sessions/
                            └── session-003/    ← 完整加载
                                ├── metadata.json
                                └── turn-*.md
```

> 📌 **注意**：所有路径都是相对于工作空间根目录，确保跨环境一致性。

---

## 错误处理

### 清单文件不存在

```markdown
❌ **无法恢复**

功能 `{名称}` 没有保存的上下文。

请检查：
1. 功能名称是否正确
2. 是否执行过保存
```

### 会话文件损坏

```markdown
⚠️ **部分加载**

部分文件无法读取，已加载可用内容。

建议重新执行保存。
```
