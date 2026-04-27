---
name: spec-context-checkpoint
description: |
  保存当前窗口的所有对话记录。保存后建议新开窗口 Resume。
  触发时机：用户请求保存、重要节点、长对话。
---

# 上下文保存

## 核心规则

### 1. 保存什么？
**当前窗口的所有对话, 不要遗漏任何对话，一定要完整保留当前窗口能获取到的所有对话，而不是只有最近两三轮对话**

### 2. 什么时候压缩？
**总轮数 >= 20 时**，对旧会话生成摘要

### 3. 保存后怎么办？
**新开窗口，执行恢复**

---

## 执行流程

### 第一步：识别功能

从用户请求识别功能名称，确认目录存在（**使用工作空间相对路径**）：
```
<工作空间>/.claude/spec-workflow/specs/{功能名}/.context/
```

> ⚠️ **路径规则**
> - 必须使用**相对于工作空间的路径**，不要使用绝对路径
> - 每个用户的工作空间目录可能不同，但相对路径结构一致
> - 示例：如果功能名是 `block-user-personal-chat`，则路径为：
>   `.claude/spec-workflow/specs/block-user-personal-chat/.context/`

---

### 第二步：读取清单文件

```python
manifest_path = f".claude/spec-workflow/specs/{功能名}/.context/manifest.json"
if exists(manifest_path):
    manifest = read_json(manifest_path)
    last_global = manifest["turnWatermark"]["lastSessionEndTurn"]
    last_chunked = manifest["turnWatermark"]["lastChunkedTurn"]
else:
    manifest = None
    last_global = 0
    last_chunked = 0
```

---

### 第三步：统计当前窗口对话

**遍历当前窗口所有用户/助手对话对**

```
过滤规则（跳过以下轮次）：
- 纯 skill 触发："保存"、"checkpoint"、"resume"、"恢复"
- skill 执行回复：包含 "保存完成"、"恢复:" 等

保留规则（保留以下轮次）：
- 包含实际内容讨论
- 包含代码、设计、需求等
```

**示例**：
```
第1轮: 用户: "帮我设计API" → 助手: "好的..." ✅ 保留
第2轮: 用户: "加个字段" → 助手: "修改如下..." ✅ 保留
第3轮: 用户: "保存" → 助手: "执行保存..." ❌ 过滤
```

**输出**：
```python
filtered_turns = [...]  # 过滤后的对话列表
turn_count = len(filtered_turns)  # 本次保存的轮数
```

---

### 第四步：计算全局编号

```python
global_start = last_global + 1
global_end = last_global + turn_count
```

**示例**：
```
manifest.lastSessionEndTurn = 15
本次保存 10 轮
→ 全局编号: 16-25
```

---

### 第五步：检查是否触发摘要

```python
new_total = global_end  # 保存后的总轮数
unchunked = new_total - last_chunked

if unchunked >= 20 and last_global > 0:
    # 触发摘要：压缩旧的会话
    need_chunk = True
    chunk_range = (last_chunked + 1, last_global)  # 不含当前会话
else:
    need_chunk = False
```

**示例**：
```
保存后总轮数 = 25
已压缩到 = 0
未压缩 = 25 >= 20 → 触发！
压缩范围 = 第1-15轮（旧会话）
```

---

### 第六步：生成摘要（如果触发）

**读取旧会话的对话文件，生成摘要**

```markdown
# 摘要 001 (第1-15轮)

## 概述
讨论了用户屏蔽功能的需求和初步设计。

## 关键决策
1. 使用 scenario 字段区分屏蔽类型
2. 复用 c2c-core 的数据库表

## 重要内容
- 第3轮: 确定了 API 风格为 RESTful
- 第8轮: 设计了数据库表结构
- 第12轮: 讨论了缓存策略

## 涉及文件
- requirements.md
- design.md
```

**保存路径**：
```
.context/conversations/semantic-chunks/chunk-001.md
```

---

### 第七步：保存会话

**7.1 确定会话 ID**

```python
if manifest and manifest.get("recentSessions"):
    nums = [int(s["sessionId"].split("-")[1]) for s in manifest["recentSessions"]]
    next_num = max(nums) + 1
else:
    next_num = 1
session_id = f"session-{next_num:03d}"
```

**7.2 创建目录**

```
.context/conversations/sessions/{session_id}/
├── metadata.json
├── turn-001.md
├── turn-002.md
...
└── turn-{NNN}.md
```

**7.3 保存对话文件**

每轮对话一个文件，**保存原始完整内容**：

> ⚠️ **严格要求**
> 
> 1. **必须保存用户输入的完整原文** - 不能省略、不能摘要、不能改写
> 2. **必须保存AI回复的完整原文** - 包括所有代码、所有解释、所有细节
> 3. **不能依赖任何外部信息** - turn 文件必须独立完整
> 4. **不能引用其他文件** - 不能说"见 design.md"代替实际内容
> 5. **代码必须完整** - 不能用 "..." 或 "省略" 代替实际代码


**错误示例**（❌ 禁止）：
```markdown
## 用户
问了关于API设计的问题

## 助手
给出了设计方案，详见 design.md
```

**正确示例**（✅ 必须）：
```markdown
## 用户
帮我设计用户屏蔽的 API，需要支持：
1. 屏蔽某个用户
2. 取消屏蔽
3. 查询我的屏蔽列表

## 助手
好的，我来设计这个 API：

### 1. 屏蔽用户
POST /api/v1/block
Content-Type: application/json

{
  "targetUserId": 12345,
  "scenario": 1
}

### 2. 取消屏蔽
DELETE /api/v1/block/{targetUserId}

### 3. 查询屏蔽列表
GET /api/v1/block/list?page=1&size=20

需要我继续细化吗？
```

**7.4 保存 metadata.json**

```json
{
  "sessionId": "session-002",
  "createdAt": "2026-01-30T10:00:00Z",
  "globalRange": [16, 25],
  "turnCount": 10,
  "phase": "design",
  "summary": {
    "topics": ["API设计", "数据库"],
    "decisions": ["使用RESTful风格"],
    "files": ["design.md"]
  }
}
```

---

### 第八步：更新清单文件

```json
{
  "specId": "block-user-personal-chat",
  "lastUpdated": "2026-01-30T10:00:00Z",
  
  "turnWatermark": {
    "lastChunkedTurn": 15,
    "lastSessionEndTurn": 25
  },
  
  "semanticChunks": [
    {
      "chunkId": "chunk-001",
      "turnRange": [1, 15],
      "file": "semantic-chunks/chunk-001.md"
    }
  ],
  
  "recentSessions": [
    {
      "sessionId": "session-002",
      "globalRange": [16, 25],
      "status": "active"
    }
  ]
}
```

---

### 第九步：输出结果

```markdown
✅ **保存完成！**

| 项目 | 值 |
|------|-----|
| 功能 | block-user-personal-chat |
| 会话 | session-002 |
| 保存轮数 | 10 轮 |
| 全局编号 | 第16-25轮 |
| 摘要 | ✅ 已生成 chunk-001 (第1-15轮) |

**建议**：新开窗口执行恢复继续工作。
```

---


### 保留的对话

- 包含技术讨论
- 包含代码
- 包含设计决策
- 用户问题 + AI 回答

---

## 摘要触发条件

```
条件：总轮数 >= 20 且 有旧会话可压缩

计算：
  new_total = 保存后的总轮数
  unchunked = new_total - lastChunkedTurn
  
  如果 unchunked >= 20：
      压缩范围 = (lastChunkedTurn + 1) ~ (lastSessionEndTurn)
      即：压缩旧会话，保留当前会话
```

**示例**：

| 操作 | 保存后总数 | 已压缩 | 未压缩 | 触发？ |
|------|-----------|--------|--------|--------|
| 首次保存 10 轮 | 10 | 0 | 10 | ❌ |
| 第二次保存 12 轮 | 22 | 0 | 22 | ✅ 压缩第1-10轮 |
| 第三次保存 8 轮 | 30 | 10 | 20 | ✅ 压缩第11-22轮 |
| 第四次保存 5 轮 | 35 | 22 | 13 | ❌ |

---

## 目录结构

```
<工作空间>/
└── .claude/
    └── spec-workflow/
        └── specs/
            └── {功能名}/
                ├── requirements.md
                ├── design.md
                ├── tasks.md
                └── .context/
                    ├── manifest.json
                    └── conversations/
                        ├── semantic-chunks/
                        │   ├── chunk-001.md    # 第1-10轮摘要
                        │   └── chunk-002.md    # 第11-22轮摘要
                        └── sessions/
                            └── session-003/    # 当前活跃 (第23-30轮)
                                ├── metadata.json
                                ├── turn-001.md
                                ...
                                └── turn-008.md
```

> 📌 **注意**：所有路径都是相对于工作空间根目录，确保跨环境一致性。

---

## 验证清单

- [ ] 保存了当前窗口所有的对话，不能有任何遗漏
- [ ] 对话文件包含原始内容（非摘要）
- [ ] 全局编号正确递增
- [ ] 清单文件已更新
- [ ] 如果 >= 20 轮，摘要已生成
