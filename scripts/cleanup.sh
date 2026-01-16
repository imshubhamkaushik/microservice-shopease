#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=env.sh
source "${SCRIPT_DIR}/env.sh"

echo "WARNING: This will remove monitoring resources from the cluster."
read -rp "Type 'yes' to continue or "no" to abort: " CONFIRM

if [[ "${CONFIRM}" != "yes" ]]; then
  echo "Cleanup aborted."
  exit 0
fi

if kubectl get namespace "${MONITORING_NAMESPACE}" >/dev/null 2>&1; then
  helm uninstall "${MONITORING_RELEASE}" \
    -n "${MONITORING_NAMESPACE}" || true

  kubectl delete namespace "${MONITORING_NAMESPACE}" || true
else
  echo "Monitoring namespace does not exist. Nothing to clean."
fi

echo "Cleanup completed."
