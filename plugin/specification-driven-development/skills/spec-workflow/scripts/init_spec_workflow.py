#!/usr/bin/env python3
"""
Initialize spec-workflow project structure.

This script:
1. Creates the user-templates directory if it doesn't exist
2. Copies missing templates from skill's assets/templates/ to project
3. Creates conventions directory for coding standards
4. Creates steering directory for project documentation
5. Reports what was done
"""

import os
import shutil
import sys
from pathlib import Path


def init_templates(workspace_root, multi_module=False):
    """
    Initialize user templates directory for spec-workflow.
    
    Args:
        workspace_root: Root directory of the workspace
        multi_module: If True, create additional multi-module directories and files
        
    Returns:
        dict: Summary of actions taken
    """
    # Template files to check
    template_files = [
        # Steering templates
        'product-template.md',
        'tech-template.md',
        'structure-template.md',
        # Spec workflow templates
        'design-template.md',
        'requirements-template.md',
        'tasks-template.md',
        'implementation-log.md',
        # Bug workflow templates
        'bug-report-template.md',
        'bug-analysis-template.md',
        'bug-verification-template.md',
        'test-plan-template.md',
    ]
    
    # Directories
    workspace_root = Path(workspace_root).resolve()
    spec_workflow_root = workspace_root / '.claude' / 'spec-workflow'
    user_templates_dir = spec_workflow_root / 'user-templates'
    conventions_dir = spec_workflow_root / 'conventions'
    steering_dir = spec_workflow_root / 'steering'
    
    # Use skill's built-in templates as the primary source
    skill_templates_dir = Path(__file__).parent.parent / 'assets' / 'templates'
    
    # Results tracking
    results = {
        'created_dirs': [],
        'existing_dirs': [],
        'copied_files': [],
        'skipped_files': [],
        'missing_source': [],
        'errors': [],
        'source_dir': str(skill_templates_dir)
    }
    
    # Create required directories
    directories = [
        ('user-templates', user_templates_dir, 'User templates directory'),
        ('conventions', conventions_dir, 'Conventions directory (coding standards, guidelines)'),
        ('steering', steering_dir, 'Steering directory (product, tech, structure docs)')
    ]
    
    for dir_name, dir_path, description in directories:
        if not dir_path.exists():
            try:
                dir_path.mkdir(parents=True, exist_ok=True)
                results['created_dirs'].append(dir_name)
                print(f"✓ Created {dir_name}: {dir_path}")
                print(f"  Purpose: {description}")
            except Exception as e:
                results['errors'].append(f"Failed to create {dir_name}: {e}")
                return results
        else:
            results['existing_dirs'].append(dir_name)
            print(f"✓ Directory already exists: {dir_path}")
    
    # Verify skill templates directory exists
    if not skill_templates_dir.exists():
        results['errors'].append(f"Skill templates directory not found: {skill_templates_dir}")
        return results
    
    print(f"✓ Using templates from: {skill_templates_dir}")
    
    # Copy missing templates
    for template_file in template_files:
        source_file = skill_templates_dir / template_file
        dest_file = user_templates_dir / template_file
        
        # Check if destination already exists
        if dest_file.exists():
            results['skipped_files'].append(template_file)
            print(f"  ⊝ Skipped (already exists): {template_file}")
            continue
        
        # Check if source exists
        if not source_file.exists():
            results['missing_source'].append(template_file)
            print(f"  ⚠ Source not found: {template_file}")
            continue
        
        # Copy the file
        try:
            shutil.copy2(source_file, dest_file)
            results['copied_files'].append(template_file)
            print(f"  ✓ Copied: {template_file}")
        except Exception as e:
            results['errors'].append(f"Failed to copy {template_file}: {e}")
            print(f"  ✗ Error copying {template_file}: {e}")
    
    # Multi-module mode: create additional directories and files
    if multi_module:
        # Copy module-map template
        module_map_source = skill_templates_dir / 'module-map-template.yaml'
        module_map_dest = spec_workflow_root / 'module-map.yaml'
        if not module_map_dest.exists():
            if module_map_source.exists():
                try:
                    shutil.copy2(module_map_source, module_map_dest)
                    results['copied_files'].append('module-map.yaml')
                    print(f"  ✓ Copied: module-map.yaml (module registry template)")
                except Exception as e:
                    results['errors'].append(f"Failed to copy module-map.yaml: {e}")
            else:
                results['missing_source'].append('module-map-template.yaml')
                print(f"  ⚠ Source not found: module-map-template.yaml")
        else:
            results['skipped_files'].append('module-map.yaml')
            print(f"  ⊝ Skipped (already exists): module-map.yaml")

    return results


def print_summary(results):
    """Print a summary of actions taken."""
    print("\n" + "="*60)
    print("SPEC-WORKFLOW INITIALIZATION SUMMARY")
    print("="*60)
    
    if results['created_dirs']:
        print(f"✓ Created {len(results['created_dirs'])} director(y/ies):")
        for d in results['created_dirs']:
            print(f"  - {d}")
    
    if results['existing_dirs']:
        print(f"⊝ {len(results['existing_dirs'])} director(y/ies) already existed")
    
    if results['copied_files']:
        print(f"✓ Copied {len(results['copied_files'])} template(s):")
        for f in results['copied_files']:
            print(f"  - {f}")
    
    if results['skipped_files']:
        print(f"⊝ Skipped {len(results['skipped_files'])} existing template(s)")
    
    if results['missing_source']:
        print(f"⚠ {len(results['missing_source'])} template(s) not found in source")
    
    if results['errors']:
        print(f"\n✗ {len(results['errors'])} error(s) occurred:")
        for error in results['errors']:
            print(f"  - {error}")
        return False
    
    print("\n" + "="*60)
    print("DIRECTORY USAGE:")
    print("="*60)
    print("• user-templates/  - Customizable templates for your project")
    print("• conventions/     - Add coding standards and guidelines")
    print("• steering/        - Product, tech, and structure documentation")
    # Check if multi-module directories were created
    all_dirs = results.get('created_dirs', []) + results.get('existing_dirs', [])
    all_files = results.get('copied_files', []) + results.get('skipped_files', [])
    if 'module-map.yaml' in all_files:
        print("• module-map.yaml  - Module registry (module-to-repo mapping)")
    print("\n✓ Spec-workflow initialization completed successfully!")
    return True


def main():
    """Main entry point."""
    if len(sys.argv) < 2:
        print("Usage: python init_spec_workflow.py <workspace_root> [--multi-module]")
        print("\nExample:")
        print("  python init_spec_workflow.py /path/to/workspace")
        print("  python init_spec_workflow.py /path/to/workspace --multi-module")
        sys.exit(1)

    workspace_root = sys.argv[1]
    multi_module = '--multi-module' in sys.argv

    if not os.path.exists(workspace_root):
        print(f"✗ Error: Workspace directory does not exist: {workspace_root}")
        sys.exit(1)

    print(f"Initializing templates for workspace: {workspace_root}")
    if multi_module:
        print("Mode: Multi-module")
    print("-" * 60)

    results = init_templates(workspace_root, multi_module=multi_module)
    success = print_summary(results)

    sys.exit(0 if success else 1)


if __name__ == '__main__':
    main()
