#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck source=env.sh
source "${SCRIPT_DIR}/env.sh"

echo "Monitoring Status Summary for Namespace: $MONITORING_NAMESPACE"
echo "--------------------------------------------------------"

echo
echo "Pods Status: "
kubectl get pods -n $MONITORING_NAMESPACE

echo
echo "Services: "
kubectl get svc -n $MONITORING_NAMESPACE

echo
echo "Alertmanager Status: "
kubectl get pods -n $MONITORING_NAMESPACE -l app.kubernetes.io/name=alertmanager || echo "No Alertmanager pods found."

echo
echo "How To access:"

echo "Prometheus:"
echo "kubectl port-forward -n $MONITORING_NAMESPACE svc/monitoring-kube-prometheus-sta-prometheus 9090:9090"
echo "Port forwarding established for Prometheus."
echo "We can access Prometheus UI at http://localhost:9090"

echo
echo "Grafana: "
echo "kubectl port-forward -n $MONITORING_NAMESPACE svc/monitoring-grafana 3000:80"
echo "Port forwarding established for Grafana."
echo "We can access Grafana UI at http://localhost:3000 (default credentials: admin/admin)"