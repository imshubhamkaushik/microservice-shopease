#!/usr/bin/env bash
# Simple Trivy wrapper to scan local docker images.
# Usage: ./trivy-scan.sh <image1> <image2> ...

if [ "$#" -lt 1 ]; then
  echo "Usage: $0 <image1> [image2 ...]"
  exit 2
fi

IMAGES=("$@")
EXIT_CODE=0

for IMG in "${IMAGES[@]}"; do
  echo "Scanning image: ${IMG}"

  docker run --rm \
    -v /var/run/docker.sock:/var/run/docker.sock \
    aquasec/trivy:latest image \
    --severity HIGH,CRITICAL \
    --ignore-unfixed \
    "${IMG}"

  CODE=$?
  if [ "$CODE" -ne 0 ]; then
    echo "❌ HIGH/CRITICAL vulnerabilities found in ${IMG}"
    EXIT_CODE=1
  else
    echo "✔ No major vulnerabilities in ${IMG}"
  fi

done

exit $EXIT_CODE
