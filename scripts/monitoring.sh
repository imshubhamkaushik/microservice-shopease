#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "=== Shopease Monitoring Setup ==="

echo
echo "Step 1: Pre-flight checks"
"${SCRIPT_DIR}/check-cluster.sh"

echo
echo "Step 2: Deploy monitoring stack"
"${SCRIPT_DIR}/deploy-monitoring.sh"

echo
echo "Step 3: Show monitoring status"
"${SCRIPT_DIR}/show-monitoring-info.sh"

echo
echo "Monitoring setup completed successfully."
