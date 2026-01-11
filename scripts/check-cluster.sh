#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=env.sh
source "${SCRIPT_DIR}/env.sh"

echo "Running Kubernetes pre-flight checks..."

# Check kubectl
if ! command -v kubectl >/dev/null 2>&1; then
  echo "kubectl not found. Please install kubectl."
  exit 1
fi

# Check helm
if ! command -v helm >/dev/null 2>&1; then
  echo "helm not found. Please install Helm."
  exit 1
fi

# Check cluster connectivity
if ! kubectl cluster-info >/dev/null 2>&1; then
  echo "Unable to connect to Kubernetes cluster."
  exit 1
fi

# Check namespaces
for ns in "${APP_NAMESPACE}" "${MONITORING_NAMESPACE}"; do
  if kubectl get namespace "${ns}" >/dev/null 2>&1; then
    echo "Namespace '${ns}' exists"
  else
    echo "Namespace '${ns}' does not exist"
  fi
done

echo "Pre-flight checks completed successfully."
