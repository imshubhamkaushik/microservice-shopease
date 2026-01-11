#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"

# shellcheck source=env.sh
source "${SCRIPT_DIR}/env.sh"

echo "Deploying monitoring stack using Helm..."

# Create namespace if not exists
if ! kubectl get namespace "${MONITORING_NAMESPACE}" >/dev/null 2>&1; then
  echo "Creating namespace: ${MONITORING_NAMESPACE}"
  kubectl create namespace "${MONITORING_NAMESPACE}"
  echo "Namespace '${MONITORING_NAMESPACE}' created"
else
  echo "Namespace '${MONITORING_NAMESPACE}' already exists"
fi

# Add Prometheus Helm  repo
helm repo add "${PROMETHEUS_REPO_NAME}" "${PROMETHEUS_REPO_URL}" >/dev/null 2>&1 || true
helm repo update

# Deploy or upgrade monitoring stack
helm upgrade --install "${MONITORING_RELEASE}" \
  "${PROMETHEUS_REPO_NAME}/${PROMETHEUS_CHART}" \
  --namespace "${MONITORING_NAMESPACE}" \
  --values "${ROOT_DIR}/monitoring/values.yaml"

echo "Monitoring stack deployed successfully."
