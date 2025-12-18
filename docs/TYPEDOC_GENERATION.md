# TypeDoc Generation Guide

**Date:** January 2025  
**Status:** ✅ Ready to Use

---

## Overview

TypeDoc automatically generates API documentation from TypeScript source files. It extracts JSDoc comments and TypeScript type information to create comprehensive documentation.

---

## Prerequisites

1. **Node.js 24+** installed
2. **npm** or **yarn** package manager
3. Frontend dependencies installed

---

## Installation

TypeDoc is already configured as a dev dependency. If you need to install it manually:

```bash
cd frontend
npm install --save-dev typedoc
```

---

## Generating Documentation

### Basic Generation

From the **frontend directory**:

```bash
cd frontend
npm run docs
```

This will:
1. Generate TypeDoc documentation from all TypeScript files in `src/`
2. Output HTML documentation to `frontend/target/site/typedoc/`
3. Exclude test files and configuration files

### Watch Mode (Development)

To automatically regenerate docs when files change:

```bash
cd frontend
npm run docs:serve
```

This runs TypeDoc in watch mode, regenerating documentation on file changes.

---

## Viewing the Documentation

After generation, open the documentation:

```bash
open frontend/target/site/typedoc/index.html
```

Or navigate to: `frontend/target/site/typedoc/index.html` in your browser.

---

## Configuration

TypeDoc is configured via `frontend/typedoc.json`. Current settings:

- **Entry Points:** All `.ts` and `.tsx` files in `src/`
- **Output:** `target/site/typedoc/`
- **Excluded:** Test files, setup files, and type definition files
- **Categorization:** Organized by Components, Services, Hooks, Utils, Types

---

## Adding Documentation Comments

TypeDoc uses JSDoc-style comments. Example:

```typescript
/**
 * User service for managing user operations
 * 
 * @example
 * ```typescript
 * const user = await userService.getUser(1);
 * ```
 */
export class UserService {
  /**
   * Gets a user by ID
   * 
   * @param id - User ID
   * @returns User object or null if not found
   * @throws {Error} If the API request fails
   */
  async getUser(id: number): Promise<User | null> {
    // ...
  }
}
```

### Common JSDoc Tags

- `@param {type} name - Description` - Document parameters
- `@returns {type} Description` - Document return values
- `@throws {Error} Description` - Document exceptions
- `@example` - Provide code examples
- `@deprecated` - Mark as deprecated
- `@since` - Version when added

---

## Documentation Structure

The generated documentation includes:

1. **Modules** - Organized by file structure
2. **Classes** - React components and service classes
3. **Interfaces** - Type definitions
4. **Functions** - Utility functions and hooks
5. **Types** - Type aliases and unions

---

## Integration with Build Process

To generate docs as part of the build:

```bash
# In frontend/package.json, you could add:
"build:docs": "npm run docs && npm run build"
```

---

## Troubleshooting

### Error: "Cannot find module 'typedoc'"

**Solution:** Install TypeDoc:
```bash
cd frontend
npm install --save-dev typedoc
```

### Error: "No entry points found"

**Solution:** Check that `src/` directory contains TypeScript files and verify `typedoc.json` entry points.

### Documentation is empty

**Solution:** 
- Ensure source files have TypeScript types
- Add JSDoc comments to improve documentation
- Check that files aren't excluded in `typedoc.json`

---

## Best Practices

1. **Add JSDoc comments** to all exported functions, classes, and interfaces
2. **Use TypeScript types** - TypeDoc extracts type information automatically
3. **Provide examples** - Use `@example` tags for complex APIs
4. **Document parameters** - Use `@param` tags for all function parameters
5. **Document return values** - Use `@returns` tags

---

## Output Location

- **HTML files:** `frontend/target/site/typedoc/`
- **Index page:** `frontend/target/site/typedoc/index.html`
- **Assets:** `frontend/target/site/typedoc/assets/`

> **Note:** The output directory is `target/site/typedoc/` to align with Maven conventions and ensure the directory is created automatically during the build.

---

## Next Steps

1. **Generate documentation:** `npm run docs`
2. **Review generated docs:** Open `target/site/typedoc/index.html`
3. **Add JSDoc comments** to improve documentation quality
4. **Update as needed** when adding new components or services

---

*Last Updated: January 2025*

