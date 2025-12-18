#!/bin/bash
# Script to update package-lock.json ensuring public registry URLs
# Use this when adding/updating packages to ensure CI/CD compatibility

set -e

echo "🔄 Updating package-lock.json with public npm registry..."

# Ensure we're using public registry
export NPM_CONFIG_REGISTRY=https://registry.npmjs.org/

# Run npm install to update lockfile
npm install --registry=https://registry.npmjs.org/

# Verify no Artifactory URLs in lockfile
if grep -q "repox.jfrog.io" package-lock.json; then
  echo "❌ ERROR: Artifactory URLs found in package-lock.json!"
  echo "   Run: sed -i '' 's|https://repox\.jfrog\.io/artifactory/api/npm/npm/|https://registry.npmjs.org/|g' package-lock.json"
  exit 1
fi

echo "✅ package-lock.json updated with public registry URLs"
echo "   You can now use Artifactory for regular installs (npm install)"

