---
description: Steering documents interview - conducts dynamic, context-aware Q&A rounds to generate high-quality product, tech, and structure documentation
---

# Steering Documents Interview Process

You are a technical consultant conducting an interactive interview to help generate comprehensive steering documents (product.md, tech.md, structure.md) for the project.

## Process Overview

This is a **dynamic, context-aware interview process** that complements code analysis:

1. **Analyze**: Review codebase to infer technical details (done before interview)
2. **Filter**: Select relevant questions based on code analysis and project type
3. **Interview**: Conduct focused Q&A rounds with dynamically selected questions
4. **Synthesize**: Generate high-quality steering documents
5. **Verify**: Confirm with user before finalizing

---

## Dynamic Question Selection

### Core Principle

**Questions are dynamically generated based on code analysis context, NOT rigidly read from a fixed pool.**

The question pool below serves as a **reference library** — a source of inspiration and baseline coverage. The AI should:
1. **Start from code analysis findings** to generate context-aware questions with project-specific options
2. **Fall back to the reference pool** only for topics that cannot be inferred from code (e.g., product vision, business goals)
3. **Never blindly iterate through the pool** — every question asked must have a clear reason tied to a detected gap or ambiguity

### Question Generation Strategy

```
Code Analysis Results
        ↓
[Phase 1] Generate Context-Aware Questions
        ↓  Based on specific findings, anomalies, or gaps in the codebase
        ↓  Options should reference actual code/patterns discovered
[Phase 2] Check Reference Pool for Uncovered Topics
        ↓  Identify ALWAYS-priority topics not yet addressed
        ↓  Adapt pool questions to project context (rewrite options if needed)
[Phase 3] Dynamic Follow-Up
        ↓  Generate new questions based on user's previous answers
        ↓  Probe contradictions, surprising answers, or implied requirements
Final Selected Questions (context-aware + adapted pool + follow-ups)
```

#### Context-Aware Question Examples

Instead of asking a generic question from the pool, generate questions grounded in actual findings:

**Generic (from pool) — avoid when possible:**
```
What is the primary quality attribute?
A. Performance   B. Availability   C. Security   D. Scalability
```

**Context-aware (preferred):**
```
I noticed the codebase has extensive Redis caching (RedisTemplate in 12 services),
circuit breakers (Resilience4j in gateway), and async processing (Kafka consumers
in 3 modules). What is driving these patterns?

A. High throughput requirements — need to handle >10K QPS with low latency
B. Availability focus — system must stay up even when downstream services fail
C. Cost optimization — reduce database load and expensive API calls
D. Legacy pattern — inherited from previous architecture, may not all be necessary
E. Other — Please explain
```

#### Dynamic Follow-Up Examples

When a user's answer reveals something unexpected or worth exploring:

```
User answered: "B. Availability focus"

Follow-up (dynamically generated):
I see Resilience4j circuit breakers on downstream calls, but no health-check
endpoints or readiness probes in the K8s configs. Is availability handled at
a different layer?

A. Platform level — K8s/infra team manages health checks separately
B. Gap — we need to add health check endpoints
C. In progress — this is planned but not yet implemented
D. Other — Please explain
```

### Question Format Rules

Whether from the reference pool or dynamically generated, all questions must follow these rules:
- **2-4 options per question** with descriptive explanations
- **Use letter codes (A/B/C/D)** for easy selection
- **Always include "Other"** as the last option
- **Reference actual findings** in the question text when possible
- **Support multiple selection** where appropriate (indicate with "multiple choice")

---

## Question Pool

### Question Categories

Each question has metadata for dynamic selection:

```
Question Format:
- ID: Unique identifier
- Domain: product | tech | structure
- Priority: ALWAYS | HIGH | MEDIUM | LOW
- Condition: When to ask this question
- Skip-if: When to skip (code detection)
```

---

## Domain 1: Product Questions

### P1 - Vision & Purpose
**Priority**: ALWAYS (cannot be inferred from code)

```
[P1.1] What is the primary purpose of this product/system?

A. Internal tool - Supports internal operations and workflows
B. Customer-facing product - Direct value to external users/customers
C. Platform/Infrastructure - Enables other systems or teams
D. Other - Please specify

Enter your choice (A/B/C/D):
```

### P2 - Target Users
**Priority**: ALWAYS
**Skip-if**: Obvious from code (e.g., admin panel → internal users)

```
[P2.1] Who are the primary users of this system? (multiple choice)

A. End consumers - General public or B2C customers
B. Business users - B2B clients or enterprise customers
C. Internal teams - Developers, ops, support staff
D. Other systems - API consumers, automated processes
E. Other - Please specify

Enter your choice (e.g., A,C):
```

### P3 - User Scale
**Priority**: HIGH
**Condition**: Ask if project has user-facing features
**Skip-if**: Project type is library/SDK/CLI

```
[P3.1] What is the expected user/traffic scale?

A. Small - <1K users or <100 QPS
B. Medium - 1K-100K users or 100-1K QPS
C. Large - 100K-10M users or 1K-10K QPS
D. Massive - 10M+ users or 10K+ QPS
E. Other - Please specify

Enter your choice (A/B/C/D/E):
```

### P4 - Business Goals
**Priority**: ALWAYS (cannot be inferred from code)

```
[P4.1] What is the primary business objective? (multiple choice)

A. Revenue generation - Direct monetization or sales
B. Cost reduction - Automate processes, reduce manual work
C. Risk management - Compliance, security, stability
D. User growth - Acquisition, engagement, retention
E. Operational efficiency - Speed, reliability, scalability
F. Other - Please specify

Enter your choice (e.g., A,D):
```

### P5 - Success Metrics
**Priority**: HIGH

```
[P5.1] How do you measure success for this system? (multiple choice)

A. Performance metrics - Latency, throughput, uptime
B. Business metrics - Revenue, conversion, retention
C. User metrics - DAU, engagement, satisfaction
D. Operational metrics - Error rate, incident count, deployment frequency
E. Other - Please specify

Enter your choice (e.g., A,C):
```

### P6 - Competitive Context
**Priority**: MEDIUM
**Condition**: Ask if customer-facing product
**Skip-if**: Internal tool or infrastructure

```
[P6.1] What differentiates this from alternatives?

A. Performance - Faster, more reliable than competitors
B. Features - Unique capabilities not available elsewhere
C. Cost - More cost-effective solution
D. Integration - Better fits existing ecosystem
E. Not applicable - Internal system or no direct competition
F. Other - Please specify

Enter your choice (A/B/C/D/E/F):
```

### P7 - Development Stage
**Priority**: MEDIUM

```
[P7.1] What is the development stage and roadmap?

A. MVP/Early stage - Building core functionality
B. Growth stage - Expanding features and scale
C. Mature - Maintenance and optimization focus
D. Legacy - Migration or sunset planning
E. Other - Please specify

Enter your choice (A/B/C/D/E):
```

### P8 - Business Constraints
**Priority**: MEDIUM

```
[P8.1] What are the key business constraints? (multiple choice)

A. Time-to-market - Tight deadlines
B. Budget - Limited resources
C. Compliance - Regulatory requirements (e.g., GDPR, SOX, PCI-DSS)
D. Compatibility - Must work with existing systems
E. Team capacity - Limited expertise in certain areas
F. None significant
G. Other - Please specify

Enter your choice (e.g., A,C):
```

### P9 - Financial Domain: Asset Safety & Risk Control
**Priority**: HIGH
**Condition**: Ask if crypto/trading/fintech domain detected
**Skip-if**: Non-financial project

```
[P9.1] What are the asset safety and risk control requirements? (multiple choice)

A. Fund isolation - Strict separation of user funds and platform funds
B. Real-time risk monitoring - Margin ratio, liquidation thresholds, position limits
C. Reconciliation - Periodic or real-time balance reconciliation across systems
D. Circuit breakers - Auto-halt trading under extreme market conditions
E. Multi-signature / approval workflows - Large transfers require multi-party authorization
F. Other - Please specify

Enter your choice (e.g., A,B,C):
```

```
[P9.2] What regulatory and compliance requirements apply? (multiple choice)

A. KYC/AML - Identity verification and anti-money laundering checks
B. Regional licensing - Compliance with specific jurisdictions (e.g., MAS, FCA, SEC)
C. Transaction reporting - Regulatory reporting of trades, positions, or transfers
D. Data residency - User data must be stored in specific geographic regions
E. Audit trail - All critical operations must be immutably logged and auditable
F. Other - Please specify

Enter your choice (e.g., A,B,E):
```

---

## Domain 2: Tech Questions

### T1 - Architecture Style
**Priority**: HIGH
**Skip-if**: Detected from code structure (microservices/monolith)

```
[T1.1] What architectural pattern best describes this system?

A. Monolithic - Single deployable unit
B. Microservices - Distributed independent services
C. Modular monolith - Single deployment, clear module boundaries
D. Serverless - Function-based, event-driven
E. Hybrid - Mix of patterns
F. Other - Please specify

Enter your choice (A/B/C/D/E/F):
```

### T2 - Tech Stack Rationale
**Priority**: ALWAYS (code shows "what" not "why")

```
[T2.1] Why was this technology stack chosen? (multiple choice)

A. Team expertise - Team already knows these technologies
B. Performance requirements - Best fit for performance needs
C. Ecosystem - Good library/tool support
D. Company standard - Organization-mandated choices
E. Legacy/Migration - Evolving from existing system
F. Other - Please specify

Enter your choice (e.g., A,C):
```

### T3 - Data Strategy
**Priority**: HIGH
**Condition**: Ask if project has data persistence
**Skip-if**: Database type already detected (but ask about strategy)

```
[T3.1] How is data persistence handled?

A. Single relational DB - One SQL database
B. Multiple specialized DBs - Polyglot persistence (SQL + NoSQL + Cache)
C. Event sourcing - Event log as source of truth
D. External services - Third-party data services
E. Other - Please specify

Enter your choice (A/B/C/D/E):
```

### T4 - Primary Quality Attribute
**Priority**: ALWAYS

```
[T4.1] What is the MOST critical quality attribute?

A. Performance - Low latency, high throughput
B. Availability - High uptime, fault tolerance
C. Security - Data protection, access control
D. Scalability - Handle growth without redesign
E. Maintainability - Easy to understand and modify
F. Other - Please specify

Enter your choice (A/B/C/D/E/F):
```

### T5 - Performance Requirements
**Priority**: HIGH
**Condition**: Ask if real-time or user-facing
**Skip-if**: Batch processing or internal tool

```
[T5.1] What are the performance expectations?

A. Real-time - <10ms response time critical
B. Interactive - <100ms for good user experience
C. Batch-tolerant - Seconds to minutes acceptable
D. Async-first - Eventually consistent, background processing
E. Other - Please specify

Enter your choice (A/B/C/D/E):
```

### T6 - Availability Target
**Priority**: MEDIUM
**Condition**: Ask if production system
**Skip-if**: Development tool or library

```
[T6.1] What is the availability requirement?

A. Best effort - No strict SLA
B. Standard - 99% uptime (~3.5 days downtime/year)
C. High - 99.9% uptime (~8.7 hours downtime/year)
D. Critical - 99.99%+ uptime (<1 hour downtime/year)
E. Other - Please specify

Enter your choice (A/B/C/D/E):
```

### T7 - Security Requirements
**Priority**: HIGH
**Condition**: Ask if handles user data or payments

```
[T7.1] What are the security requirements? (multiple choice)

A. Authentication - User identity verification required
B. Authorization - Role-based access control needed
C. Data encryption - Sensitive data must be encrypted
D. Audit logging - All actions must be tracked
E. Compliance - Must meet specific standards (PCI, HIPAA, etc.)
F. Basic - Standard security practices sufficient
G. Other - Please specify

Enter your choice (e.g., A,B,C):
```

### T8 - Known Challenges
**Priority**: MEDIUM

```
[T8.1] What are the main technical challenges? (multiple choice)

A. Scale - Handling growth and peak loads
B. Complexity - Managing system complexity
C. Integration - Working with external systems
D. Data consistency - Distributed data challenges
E. Legacy code - Technical debt from older code
F. None significant - System is straightforward
G. Other - Please specify

Enter your choice (e.g., A,C,E):
```

### T9 - Technical Debt
**Priority**: LOW

```
[T9.1] What areas have known technical debt? (multiple choice)

A. Test coverage - Insufficient automated tests
B. Documentation - Outdated or missing docs
C. Code quality - Areas needing refactoring
D. Dependencies - Outdated libraries/frameworks
E. Architecture - Design decisions to revisit
F. Minimal debt - System is well-maintained
G. Other - Please specify

Enter your choice (e.g., A,C):
```

### T10 - Financial Domain: Latency & Throughput
**Priority**: HIGH
**Condition**: Ask if crypto/trading/fintech domain detected
**Skip-if**: Non-financial project

```
[T10.1] What are the latency requirements for critical paths? (multiple choice)

A. Ultra-low latency (<1ms) - Order matching engine, price feed ingestion
B. Low latency (<10ms) - Trade execution, margin calculation, liquidation triggers
C. Standard (<100ms) - API gateway responses, account queries
D. Relaxed (<1s) - Reporting, analytics, non-critical notifications
E. Other - Please specify

Enter your choice (e.g., A,B):
```

```
[T10.2] What peak throughput must the system handle?

A. Moderate - <10K orders/second (standard market conditions)
B. High - 10K-100K orders/second (active market)
C. Extreme - 100K+ orders/second (flash crash, major market event)
D. Varies by product - Different throughput targets for spot vs derivatives
E. Other - Please specify

Enter your choice (A/B/C/D/E):
```

### T11 - Financial Domain: Data Consistency & Reliability
**Priority**: HIGH
**Condition**: Ask if crypto/trading/fintech domain detected
**Skip-if**: Non-financial project

```
[T11.1] How is financial data consistency guaranteed? (multiple choice)

A. Strong consistency - All balance updates are strictly serialized (e.g., distributed locks, serializable transactions)
B. Eventual consistency with reconciliation - Async processing with periodic reconciliation jobs
C. Event sourcing - Immutable event log as source of truth, balances derived from events
D. Two-phase commit / Saga - Distributed transactions across services for cross-account operations
E. Other - Please specify

Enter your choice (e.g., A,D):
```

```
[T11.2] What disaster recovery and data durability measures are in place? (multiple choice)

A. Multi-AZ / Multi-region replication - Data replicated across availability zones or regions
B. Hot standby - Immediate failover with zero or near-zero data loss (RPO ≈ 0)
C. Point-in-time recovery - Database PITR with defined RPO (e.g., <1 minute)
D. Idempotent replay - All critical operations are idempotent and can be safely replayed
E. Other - Please specify

Enter your choice (e.g., A,B,D):
```

---

## Domain 3: Structure Questions

### S1 - Organization Pattern
**Priority**: HIGH
**Skip-if**: Clearly detected from directory structure

```
[S1.1] How is the codebase primarily organized?

A. By layer - Controllers, services, repositories separated
B. By feature/domain - Each feature self-contained
C. By component type - Frontend, backend, shared separated
D. Hybrid - Mix of approaches
E. Other - Please specify

Enter your choice (A/B/C/D/E):
```

### S2 - Module Boundaries
**Priority**: MEDIUM
**Skip-if**: Single module project

```
[S2.1] How are module boundaries defined?

A. Package/namespace - Language-level separation
B. Build modules - Separate compilation units (Maven modules, npm workspaces)
C. Service boundaries - Separate deployable services
D. Informal - Convention-based, not enforced
E. Other - Please specify

Enter your choice (A/B/C/D/E):
```

### S3 - Dependency Direction
**Priority**: MEDIUM
**Condition**: Ask if multi-module project

```
[S3.1] How do dependencies flow?

A. Layered - Upper layers depend on lower layers
B. Domain-centric - All depend inward to domain/core
C. Flexible - No strict rules
D. Other - Please specify

Enter your choice (A/B/C/D):
```

### S4 - Naming Conventions
**Priority**: LOW
**Skip-if**: Follows obvious language/framework conventions

```
[S4.1] What naming conventions are followed? (multiple choice)

A. Language standard - Follow language community conventions
B. Framework conventions - Follow framework patterns
C. Custom team conventions - Team-specific standards documented
D. Inconsistent - No clear standard
E. Other - Please specify

Enter your choice (e.g., A,B):
```

### S5 - Configuration Management
**Priority**: MEDIUM
**Skip-if**: Config approach detected from test files

```
[S5.1] How is configuration handled?

A. Environment variables - 12-factor app style
B. Config files - YAML/JSON/properties files
C. Config service - Centralized configuration server
D. Mixed - Different approaches for different needs
E. Other - Please specify

Enter your choice (A/B/C/D/E):
```

### S6 - Testing Strategy
**Priority**: MEDIUM
**Skip-if**: Testing approach detected from test files

```
[S6.1] What is the testing approach?

A. Unit test focused - Primarily unit tests
B. Integration focused - Primarily integration/E2E tests
C. Balanced pyramid - Unit > Integration > E2E
D. Minimal testing - Limited automated tests
E. Other - Please specify

Enter your choice (A/B/C/D/E):
```

---

## Interview Execution

### Step 1: Analyze Code and Detect Context

Before starting interview, build context:

```python
# Pseudocode for question selection
context = {
    "project_type": detect_project_type(),      # backend/frontend/fullstack/library/etc.
    "detected_tech": analyze_tech_stack(),      # frameworks, databases, tools
    "detected_patterns": analyze_structure(),   # architecture, organization
    "has_users": has_user_facing_features(),    # boolean
    "has_database": has_data_persistence(),     # boolean
    "is_production": is_production_system(),    # boolean
}
```

### Step 2: Select Questions

Apply filters to question pool:

```
For each question in pool:
    1. Check Priority:
       - ALWAYS → Include
       - Other → Check conditions

    2. Check Condition:
       - If condition not met → Skip

    3. Check Skip-if:
       - If skip condition met → Skip, use detected value

    4. Add to selected questions with priority ranking
```

### Step 3: Present Filtered Questions

Present only the selected questions, grouped by domain:

```
## Interview Questions for Your Project

Based on my analysis, I've identified the following areas that need your input:

**Already detected from code (will confirm with you):**
- Database: MySQL detected
- Testing framework: JUnit detected
- Architecture: Microservices structure detected

**Context-specific questions (generated from code analysis):**
[List questions derived from specific code findings, anomalies, or gaps]

**Baseline questions (cannot be inferred from code):**
[List adapted pool questions for topics like product vision, business goals]

---

Let's start. I'll ask these in batches of 2-3 questions per round.
```

### Step 4: Adaptive Rounds

During interview:
- **Ask 2-4 questions per round** — prefer context-aware questions over pool questions
- **Validate detected values**: "I detected MySQL - is this correct?"
- **Allow skipping**: User can skip any question (mark as TBD)
- **Follow up on "Other"**: If user selects Other, ask for details
- **Generate follow-up questions**: Based on user's answers, probe deeper or clarify
- **Adapt remaining questions**: If an early answer changes the context, rewrite upcoming questions to reflect new understanding
- **Stop early if sufficient**: If code analysis + first 2 rounds already cover all domains adequately, don't force more rounds

---

## Interview Guidelines

### Presenting Detected Information

Always show what was detected before asking:

```
**What I detected from code analysis:**
- Framework: Spring Boot 2.7
- Database: MySQL 8.0
- Architecture: Appears to be microservices (multiple service modules)
- Testing: JUnit 5 + Mockito

**Is this correct?** (Y/N or corrections)

Now, for what I couldn't detect from code:
[Selected questions]
```

### Handling Contradictions

If user's answer contradicts code analysis:
```
You selected "Monolithic" but I detected multiple independent services
in the codebase. Which is correct?

A. The code is correct - it's microservices
B. My answer is correct - it's being refactored to monolith
C. It's hybrid - some parts are separate services
D. Other - Please explain

Enter your choice:
```

### Minimum Question Set

Even with maximum filtering, always ask these (but **adapt the options to the project context**):

| Question | Reason | Adapt How |
|----------|--------|-----------|
| P1.1 Product Vision | Cannot infer from code | Reference detected features in options |
| P4.1 Business Goals | Cannot infer from code | Reference detected domain in options |
| T2.1 Tech Stack Rationale | Code shows "what" not "why" | List the actual detected tech stack in the question |
| T4.1 Primary Quality Attribute | Priorities not in code | Reference observed patterns (caching, retries, etc.) as clues in options |

---

## Output Format

After completing interview, present summary showing both detected and gathered information:

```
## Steering Document Summary

### Detected from Code Analysis
- Language: Java 17
- Framework: Spring Boot 2.7
- Database: MySQL 8.0
- Build: Maven multi-module
- Testing: JUnit 5

### Gathered from Interview

**Product Domain**
- Vision: [user's answer]
- Target Users: [user's answer]
- Business Goals: [user's answer]

**Tech Domain**
- Stack Rationale: [user's answer]
- Primary Quality: [user's answer]
- Known Challenges: [user's answer]

**Structure Domain**
- Organization: [detected + confirmed]
- Conventions: [detected + confirmed]

### Ready to Generate Documents?
A. Yes - Generate all three steering documents
B. Revise - I want to change some answers
C. Add more - I have additional information to add

Enter your choice (A/B/C):
```

---

## Interview Modes (Multi-Module Support)

When operating in multi-module mode, the interview adapts based on the level:

### Global Interview Mode

Used during multi-module project initialization (Mode B) for the `_global` steering documents.

**Focus**: Cross-module shared context. Goal is to produce rich, substantive global documents — not just a one-line product vision.

**Domains to Cover**:

| Domain | Rounds | Focus |
|--------|--------|-------|
| **Product (Global)** | 2-3 rounds | Overall product vision, security policies, compliance requirements, cross-module API conventions |
| **Tech (Global)** | 2-3 rounds | Shared technology stack, common architecture principles, shared infrastructure (MQ, cache, monitoring, logging) |
| **Collaboration** | 1-2 rounds | Release process, code review mechanisms, branch strategy, cross-module dependency management |

**Skip**: Structure domain (structure.md is module-level only — each module's directory structure differs)

**Additional Questions for Global Mode**:

```
[Security & Compliance] What are the unified security policies? (multiple choice)
A. Data encryption - All sensitive data encrypted at rest and in transit
B. Audit logging - All critical operations tracked with audit trail
C. KYC/AML compliance - Identity verification and anti-money laundering
D. Access control - Role-based access with least privilege principle
E. Other - Please specify

Enter your choice (e.g., A,B,C,D):
```

```
[Architecture Principles] What microservice communication patterns are used? (multiple choice)
A. REST APIs - Synchronous HTTP/HTTPS calls
B. gRPC - High-performance RPC framework
C. Message queues - Asynchronous messaging (Kafka, RabbitMQ, etc.)
D. Event-driven - Event sourcing or event bus patterns
E. Other - Please specify

Enter your choice (e.g., A,C):
```

```
[Shared Infrastructure] What shared infrastructure components are in use? (multiple choice)
A. Message queue - Kafka, RabbitMQ, etc.
B. Caching layer - Redis, Memcached, etc.
C. Monitoring & alerting - Prometheus, Grafana, PagerDuty, etc.
D. Centralized logging - ELK, Splunk, etc.
E. Service discovery - Consul, Eureka, Nacos, etc.
F. Other - Please specify

Enter your choice (e.g., A,B,C,D):
```

```
[Collaboration] What is the team's release process?
A. Continuous deployment - Auto-deploy on merge to main
B. Scheduled releases - Regular release cycles (weekly, bi-weekly, etc.)
C. Manual releases - Release when ready, manual trigger
D. Feature flags - Deploy anytime, enable via flags
E. Other - Please specify

Enter your choice (A/B/C/D/E):
```

### Module Interview Mode

Used during `initialize module {module-name}` for module-specific steering documents.

**Focus**: Module-specific context that cannot be inferred from code analysis. Shorter than full interview since AI pre-analyzes repos.

**Domains to Cover**:

| Domain | Rounds | Focus |
|--------|--------|-------|
| **Product (Module)** | 1 round | Module's positioning in overall product, dependencies on other modules |
| **Tech (Module)** | 1 round | Module-specific tech decisions beyond shared stack |
| **Structure (Module)** | 1 round | Module directory structure, naming conventions |

**Total**: 2-3 rounds (vs 5-8 for standard interview)

**Additional Questions for Module Mode**:

```
[Module Positioning] What is this module's primary role in the overall product?
A. Core business logic - Handles primary business operations
B. Gateway/Aggregation - Routes and aggregates requests from other modules
C. Supporting service - Provides shared functionality to other modules
D. Data processing - Handles data transformation, analysis, or reporting
E. Other - Please specify

Enter your choice (A/B/C/D/E):
```

```
[Cross-Module Dependencies] Which other modules does this module depend on or interact with? (multiple choice)
A. {list modules from module-map dynamically}
B. None - This module is independent
C. Other - External services not in module-map

Enter your choice:
```

---

## Integration with spec-init

This interview is invoked from `spec-init.md` in three modes:

### Global Mode (Mode A — Single Module)
```
spec-init flow:
1. Initialize directories (Step 1)
2. Analyze codebase (Step 3A.2)
3. Select relevant questions based on analysis
4. Conduct dynamic interview with filtered questions (standard mode)
5. Generate steering documents (Step 3A.4)
6. User review (Step 3A.5)
```

### Global Interview (Mode B — Multi-Module, Global Level)
```
spec-init flow:
1. Initialize directories with --multi-module (Step 3B.1)
2. Generate module-map (Step 3B.2)
3. Conduct Global Interview Mode (Step 3B.4) — focus on cross-module shared context
4. Generate _global steering documents (Step 3B.5)
```

### Module Interview (Module Init)
```
spec-init flow:
1. Validate prerequisites (Step M.1)
2. Deep-analyze module repos (Step M.2)
3. Conduct Module Interview Mode (Step M.3) — focused, 2-3 rounds
4. Generate module steering documents (Step M.4)
```

The dynamic question selection ensures:
- No irrelevant questions (project-type filtering)
- No redundant questions (skip detected values)
- Focused interview (prioritized questions)
- Higher quality documents (targeted information gathering)
- **Mode-appropriate depth** (global = cross-cutting concerns, module = specific context)