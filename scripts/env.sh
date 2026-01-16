# env.sh
# Centralized environment variables for operational scripts

#!/usr/bin/env bash
set -euo pipefail

# Fail if this file is sourced incorrectly
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
  echo "This file must be sourced, not executed."
  exit 1
fi

# Namespaces
APP_NAMESPACE="default"
MONITORING_NAMESPACE="monitoring"

# Helm release names
MONITORING_RELEASE="monitoring"

# Helm chart info
PROMETHEUS_REPO_NAME="prometheus-community"
PROMETHEUS_REPO_URL="https://prometheus-community.github.io/helm-charts"
PROMETHEUS_CHART="kube-prometheus-stack"

# Timeout settings
ROLLOUT_TIMEOUT="120s"
