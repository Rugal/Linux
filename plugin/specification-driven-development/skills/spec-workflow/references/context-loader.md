---
description: "Design reference document for layered context loading. Not directly invoked — each workflow phase uses inline condensed instructions instead."
---

# Context Loader — Complete Design Reference

## Purpose
Layered loading of steering documents and conventions, providing complete context for any workflow phase.

## Mode Detection
- If module is null → single-module mode
- If module is not null → multi-module mode

## Single-Module Mode (backward compatible)
- Load `steering/product.md`, `steering/tech.md`, `steering/structure.md`
- Load all `.md` files from `conventions/`

## Multi-Module Mode
1. Load _global steering (OPTIONAL — only if `steering/_global/` exists and was created during init):
   - `steering/_global/product.md`
   - `steering/_global/tech.md`
2. Load module steering (if `steering/{module}/` exists):
   - `steering/{module}/product.md`
   - `steering/{module}/tech.md`
   - `steering/{module}/structure.md`
3. Load conventions (global, shared by all modules):
   - All `.md` from `conventions/`

## Merge Strategy
- Product context = _global product (background) + module product (focus)
- Tech context = _global tech (shared base) + module tech (specific additions)
- Structure context = module structure only (not in _global)
- Conventions = global conventions only (not split by module)

## Ordering
- Global content first, module content second
- Module content receives higher attention weight from LLM due to recency

## Degraded Mode
- If _global was not created during init (user chose to skip):
  - Only module steering + conventions are loaded
  - No global background context
- If module steering not initialized but user chose to continue:
  - Load _global steering (if exists) + conventions only
  - Skip module steering entirely
- If neither _global nor module steering exist:
  - Load conventions only
