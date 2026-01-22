#!/bin/bash

echo ">>> Starting Vault dev server with fixed root token..."

# Kill any running Vault dev instance (opcional)
pkill -f "vault server -dev" 2>/dev/null

# Start Vault in dev mode with fixed token, in background
vault server -dev -dev-root-token-id="my-root-token" > vault.log 2>&1 &
VAULT_PID=$!

# Wait for Vault to become ready (very basic wait)
sleep 2

# Export the fixed token so vault CLI can use it
export VAULT_TOKEN="my-root-token"
echo ">>> Exported VAULT_TOKEN=$VAULT_TOKEN"

# Write the JWT secret to Vault
echo ">>> Writing JWT secret to Vault..."
vault kv put secret/security security.jwt.secret="uma-super-secret-key-com-pelo-menos-32-caracteres"

# Confirm it was written
vault kv get secret/security

echo ">>> Vault is ready with JWT secret injected!"
echo ">>> Root token: $VAULT_TOKEN"
echo ">>> Logs available in vault.log"
echo ">>> To stop Vault: kill $VAULT_PID"

