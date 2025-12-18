# GitHub Pages Documentation Deployment

**Date:** January 2025  
**Status:** ✅ Configured

---

## Overview

Documentation is automatically built and published to GitHub Pages whenever you push to the main branch. The workflow:

1. **Builds the project** → Generates JavaDoc and TypeDoc in `target/` folders
2. **Collects the docs** → Copies them to a staging directory
3. **Deploys to GitHub Pages** → Publishes to `https://your-username.github.io/sonar-demo/`

**Key Point:** The generated docs are **never committed to your main branch**. They're generated during CI/CD and pushed to a separate GitHub Pages branch automatically.

---

## How It Works

### The Workflow

The GitHub Action (`.github/workflows/docs.yml`) runs on:
- Push to `main` or `master` branch
- Pull requests (builds but doesn't deploy)
- Manual trigger via GitHub Actions UI

### Step-by-Step Process

1. **Checkout Code**
   ```yaml
   - uses: actions/checkout@v4
   ```

2. **Set Up Build Environment**
   - Java 21 (for Maven/JavaDoc)
   - Node.js 24 (for TypeDoc)
   - Cached dependencies for faster builds

3. **Build and Generate Docs**
   ```bash
   mvn clean install -DskipTests
   ```
   This generates:
   - `backend/target/site/apidocs/` (JavaDoc)
   - `frontend/target/site/typedoc/` (TypeDoc)

4. **Prepare Documentation Site**
   - Creates a `docs-site/` directory
   - Copies both documentation sets
   - Creates an index page with links to both

5. **Deploy to GitHub Pages**
   - Uploads `docs-site/` as a Pages artifact
   - GitHub automatically serves it at your Pages URL

---

## Where Are The Docs?

### In Your Repository

- **Source code:** `main` branch (clean, no generated files)
- **Generated docs:** `gh-pages` branch (auto-created by GitHub Actions)
  - You never manually touch this branch
  - It's automatically updated on each deployment

### On GitHub Pages

Your documentation will be available at:
```
https://your-username.github.io/sonar-demo/
```

Or if using a custom domain:
```
https://yourdomain.com/
```

---

## Repository Structure

```
sonar-demo/
├── .github/
│   └── workflows/
│       └── docs.yml          ← GitHub Actions workflow
├── backend/
│   └── target/
│       └── site/
│           └── apidocs/      ← Generated during build (NOT in git)
├── frontend/
│   └── target/
│       └── site/
│           └── typedoc/       ← Generated during build (NOT in git)
└── docs/                      ← Source documentation (IN git)
    ├── api-spec.md
    ├── business-logic.md
    └── ...
```

**Important:**
- `target/` folders are in `.gitignore` ✅
- Source docs in `docs/` are committed ✅
- Generated docs are only in CI/CD and GitHub Pages ✅

---

## Enabling GitHub Pages

### First Time Setup

1. **Push the workflow file** to your repository:
   ```bash
   git add .github/workflows/docs.yml
   git commit -m "Add GitHub Pages documentation deployment"
   git push
   ```

2. **Enable GitHub Pages in repository settings:**
   - Go to: **Settings** → **Pages**
   - **Source:** Select "GitHub Actions"
   - Save

3. **Wait for the workflow to run:**
   - Go to: **Actions** tab
   - Watch the "Build and Deploy Documentation" workflow
   - Once complete, your docs will be live!

### Verify It's Working

After the first deployment:
- Check the **Actions** tab for a green checkmark ✅
- Visit your GitHub Pages URL
- You should see the documentation index page

---

## Accessing Your Documentation

### Main Index
```
https://your-username.github.io/sonar-demo/
```
Shows a landing page with links to both documentation sets.

### Backend JavaDoc
```
https://your-username.github.io/sonar-demo/backend/
```
Direct link to Java API documentation.

### Frontend TypeDoc
```
https://your-username.github.io/sonar-demo/frontend/
```
Direct link to TypeScript/React documentation.

---

## Manual Deployment

You can manually trigger a documentation rebuild:

1. Go to **Actions** tab
2. Select **Build and Deploy Documentation**
3. Click **Run workflow**
4. Select branch (usually `main`)
5. Click **Run workflow**

---

## Troubleshooting

### Docs Not Appearing

**Check:**
1. Is GitHub Pages enabled? (Settings → Pages)
2. Is the workflow running? (Actions tab)
3. Did the build succeed? (Check workflow logs)

**Common Issues:**

**"No documentation found"**
- Check that `mvn clean install` completed successfully
- Verify `target/site/` directories were created

**"404 on GitHub Pages"**
- Ensure Pages source is set to "GitHub Actions"
- Wait a few minutes after deployment (propagation delay)

**"Workflow failing"**
- Check the Actions tab for error messages
- Verify Java and Node.js versions match your setup

---

## Best Practices

✅ **DO:**
- Keep generated docs in `target/` (excluded from git)
- Let CI/CD handle documentation deployment
- Update source code comments to improve generated docs
- Use manual trigger for testing workflow changes

❌ **DON'T:**
- Commit `target/` folders to git
- Manually edit the `gh-pages` branch
- Include generated docs in pull requests
- Hardcode documentation URLs in code

---

## Advantages of This Approach

1. **Clean Repository**
   - Main branch only has source code
   - No merge conflicts from generated files
   - Smaller repository size

2. **Always Up-to-Date**
   - Docs automatically reflect latest code
   - No manual deployment steps
   - Versioned with your code (via workflow)

3. **Professional**
   - Public documentation site
   - Easy to share with team/stakeholders
   - Searchable and indexed by search engines

4. **CI/CD Integration**
   - Documentation is part of your build pipeline
   - Can be extended with additional checks
   - Works with any GitHub repository

---

## Customization

### Change Deployment Branch

Edit `.github/workflows/docs.yml`:
```yaml
on:
  push:
    branches:
      - main
      - develop  # Add more branches
```

### Add More Documentation

To include additional docs in the site:
1. Add them to `docs-site/` in the "Prepare documentation site" step
2. Update the index.html to link to them

### Custom Domain

1. Add a `CNAME` file to `docs-site/`
2. Configure custom domain in GitHub Pages settings

---

*Last Updated: January 2025*

