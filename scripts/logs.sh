#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=env.sh
source "${SCRIPT_DIR}/env.sh"

if [[ $# -lt 1 ]]; then
  echo "Usage: $0 <app-name> [--follow]"
  exit 1
fi

APP_NAME="$1"
FOLLOW_MODE="${2:-}"

if [[ "${FOLLOW_MODE}" == "--follow" ]]; then
  kubectl logs -n "${APP_NAMESPACE}" -l app="${APP_NAME}" -f
else
  kubectl logs -n "${APP_NAMESPACE}" -l app="${APP_NAME}"
fi