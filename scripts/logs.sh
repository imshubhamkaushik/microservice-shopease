#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=env.sh
source "${SCRIPT_DIR}/env.sh"

if [[ $# -lt 1 ]]; then
  echo "Usage: $0 <deployment-name> [--follow]"
  exit 1
fi

DEPLOYMENT_NAME="$1"
FOLLOW_MODE="${2:-}"

# Assumes deployments use label: app=<deployment-name>
POD_NAME=$(kubectl get pods \
  -n "${APP_NAMESPACE}" \
  -l app="${DEPLOYMENT_NAME}" \
  -o jsonpath='{.items[0].metadata.name}' 2>/dev/null || true)

if [[ -z "${POD_NAME}" ]]; then
  echo "No pod found for deployment: ${DEPLOYMENT_NAME}"
  exit 1
fi

echo "Fetching logs for pod: ${POD_NAME}"

if [[ "${FOLLOW_MODE}" == "--follow" ]]; then
  kubectl logs -n "${APP_NAMESPACE}" -f "${POD_NAME}"
else
  kubectl logs -n "${APP_NAMESPACE}" "${POD_NAME}"
fi
