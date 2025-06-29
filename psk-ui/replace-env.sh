#!/bin/sh
if [ -z "$API_URL" ]; then
  echo "Error: API_URL environment variable is not set."
  exit 1
fi

sed -i "s|\__API_URL__|$API_URL|g" ./src/environments/environment.prod.ts
echo "API_URL replaced in environment.prod.ts"