#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=env.sh
source "${SCRIPT_DIR}/env.sh"

echo "Warning!!! This will remove monitoring resources from the cluster."
read -rp "Are you sure you want to continue? (yes/no): " CONFIRM

if [[ "${CONFIRM}" != "yes" ]]; then
  echo "Cleanup aborted."
  exit 0
fi

echo "Removing monitoring Helm release..."

helm uninstall "${MONITORING_RELEASE}" \
  -n "${MONITORING_NAMESPACE}" || true

echo "Deleting monitoring namespace..."

kubectl delete namespace "${MONITORING_NAMESPACE}" || true

echo "Cleanup completed."
