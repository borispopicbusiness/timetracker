#!/bin/bash

# Set variables
KEYCLOAK_URL="http://localhost:8080"
REALM="time-tracker-realm"
CLIENT_ID="admin-cli"
USERNAME="admin"
PASSWORD="admin"
CLIENT_ID_FOR_API="tt-fe-client"  # Use the frontend client name here
USER_EMAIL="newuser@example.com"  # Ensure unique email
ROLE_NAME="EMPLOYEE"             # Use role name here

# Function to obtain an access token
get_access_token() {
  echo "Requesting access token..."
  response=$(curl -s -X POST "$KEYCLOAK_URL/realms/master/protocol/openid-connect/token" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -d "grant_type=password&client_id=$CLIENT_ID&username=$USERNAME&password=$PASSWORD")

  access_token=$(echo $response | jq -r '.access_token')

  if [ "$access_token" == "null" ]; then
    echo "Failed to obtain access token. Response:"
    echo $response
    exit 1
  fi

  echo "Access token obtained successfully."
}

# Function to get user ID by email
get_user_id_by_email() {
  echo "Fetching user ID for email $USER_EMAIL..."
  user_id_response=$(curl -s -X GET "$KEYCLOAK_URL/admin/realms/$REALM/users?email=$USER_EMAIL" \
    -H "Authorization: Bearer $access_token")

  USER_ID=$(echo $user_id_response | jq -r '.[0].id')

  if [ "$USER_ID" == "null" ]; then
    echo "User not found with email $USER_EMAIL."
    exit 1
  fi

  echo "User ID retrieved: $USER_ID"
}

# Function to get client ID
get_client_id() {
  echo "Requesting client ID..."
  client_id_response=$(curl -s -X GET "$KEYCLOAK_URL/admin/realms/$REALM/clients?clientId=$CLIENT_ID_FOR_API" \
    -H "Authorization: Bearer $access_token")

  CLIENT_ID=$(echo $client_id_response | jq -r '.[0].id')

  if [ "$CLIENT_ID" == "null" ]; then
    echo "Client ID not found for $CLIENT_ID_FOR_API."
    exit 1
  fi

  echo "Client ID retrieved: $CLIENT_ID"
}

# Function to create a user
create_user() {
  echo "Creating a user..."
  create_user_response=$(curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM/users" \
    -H "Authorization: Bearer $access_token" \
    -H "Content-Type: application/json" \
    -d '{"username": "newuser", "enabled": true, "email": "'$USER_EMAIL'"}')

  if echo $create_user_response | grep -q "User exists with same email"; then
    echo "User already exists with the email $USER_EMAIL."
  else
    echo "Create user response:"
    echo $create_user_response
    get_user_id_by_email  # Retrieve the user ID after creation
  fi
}

# Function to update a user
update_user() {
  if [ -z "$USER_ID" ]; then
    echo "No user ID available for updating."
    return
  fi

  echo "Updating user $USER_ID..."
  update_user_response=$(curl -s -X PUT "$KEYCLOAK_URL/admin/realms/$REALM/users/$USER_ID" \
    -H "Authorization: Bearer $access_token" \
    -H "Content-Type: application/json" \
    -d '{"email": "updateduser@example.com"}')

  if echo $update_user_response | grep -q "User not found"; then
    echo "User not found with ID $USER_ID."
  else
    echo "Update user response:"
    echo $update_user_response
  fi
}

# Function to delete a user
delete_user() {
  if [ -z "$USER_ID" ]; then
    echo "No user ID available for deletion."
    return
  fi

  echo "Deleting user $USER_ID..."
  delete_user_response=$(curl -s -X DELETE "$KEYCLOAK_URL/admin/realms/$REALM/users/$USER_ID" \
    -H "Authorization: Bearer $access_token")

  if echo $delete_user_response | grep -q "User not found"; then
    echo "User not found with ID $USER_ID."
  else
    echo "Delete user response:"
    echo $delete_user_response
  fi
}

# Function to assign a client role to a user
assign_client_role() {
  if [ -z "$USER_ID" ]; then
    echo "No user ID available for role assignment."
    return
  fi

  echo "Assigning role $ROLE_NAME to user $USER_ID..."

  # Get the client ID (if needed, replace CLIENT_ID with the actual value if necessary)
  get_client_id

  # Get the role ID
  role_id_response=$(curl -s -X GET "$KEYCLOAK_URL/admin/realms/$REALM/clients/$CLIENT_ID/roles" \
    -H "Authorization: Bearer $access_token")

  echo "Role ID response:"
  echo $role_id_response  # Debugging: Check if roles are retrieved correctly

  ROLE_ID=$(echo $role_id_response | jq -r ".[] | select(.name==\"$ROLE_NAME\") | .id")

  if [ -z "$ROLE_ID" ]; then
    echo "Role ID for $ROLE_NAME not found."
    exit 1
  fi

  # Assign role to user
  assign_role_response=$(curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM/users/$USER_ID/role-mappings/clients/$CLIENT_ID" \
    -H "Authorization: Bearer $access_token" \
    -H "Content-Type: application/json" \
    -d '[{"id": "'$ROLE_ID'", "name": "'$ROLE_NAME'"}]')

  if echo $assign_role_response | grep -q "User not found"; then
    echo "User not found with ID $USER_ID."
  else
    echo "Assign role response:"
    echo $assign_role_response
  fi
}

# Authenticate and get an access token
get_access_token

# Execute functions
create_user
update_user
assign_client_role
delete_user