---
description: "Design reference document for the module detection engine. Not directly invoked — each workflow phase uses inline condensed instructions instead."
---

# Module Resolver — Complete Design Reference

## Purpose
Module detection engine that determines the target module for the current operation. All spec and bug workflow phases call this at the start.

## Pre-check
- Check if `module-map.yaml` exists
- If NOT → single-module mode, skip all resolution

## Resolution Priority

### Priority 1: Explicit Specification
- Parse user input for `{module-name}/{feature-or-bug-name}` pattern
- If found → return module-name

### Priority 2: Recent File Context
- Get the most recently edited file path in the IDE
- Match file's parent directories against module-map repos
- If matched → return corresponding module
- If no recent edit history → skip

### Priority 3: AI Semantic Inference
- Compare user's command text against each module's name + description + repo names
- Confidence heuristics:
  - HIGH: user input directly contains module name or repo name → use directly
  - HIGH: user input keywords strongly correlate with only one module's description → use directly
  - MEDIUM: input may relate to 2 modules with clear primary → use primary, inform user
  - LOW: input relates to multiple modules, cannot distinguish → fall through to Priority 5
- If high/medium confidence → return module

### Priority 4: Single Module Check
- If module-map has only one module → return it

### Priority 5: Interactive Selection
- Present all modules as options (A/B/C/D format)
- Wait for user selection → return selected module

## Post-Resolution: Steering Check
- After module is resolved, check if `steering/{module}/` exists
- If NOT initialized → warn user with choice:
  A. Pause to initialize module steering first
  B. Continue with _global context only (degraded mode)

**Note**: `spec-requirements.md` has an additional **mandatory user confirmation** step after module resolution ("Detected target module: X. Is this correct? Y/N") to prevent misrouted requirements. Other phases (design, tasks, run, bugs) rely on inference confidence levels without explicit confirmation.

## Unified Strategy
All workflows (spec and bug) use the same priority order. No per-scenario adjustments.
