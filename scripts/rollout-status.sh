#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=env.sh
source "${SCRIPT_DIR}/env.sh"

echo "Checking rollout status for deployments in namespace: ${APP_NAMESPACE}"

DEPLOYMENTS=$(kubectl get deployments -n "${APP_NAMESPACE}" -o jsonpath='{.items[*].metadata.name}')

if [[ -z "${DEPLOYMENTS}" ]]; then
  echo "No deployments found in namespace ${APP_NAMESPACE}"
  exit 0
fi

for deploy in ${DEPLOYMENTS}; do
  echo "Deployment: ${deploy}"
  kubectl rollout status deployment "${deploy}" \
    -n "${APP_NAMESPACE}" \
    --timeout="${ROLLOUT_TIMEOUT}"
done

echo "All deployments are successfully rolled out."