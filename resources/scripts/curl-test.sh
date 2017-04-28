#!/bin/bash

# Implicit token flow example
# Requires jq

CLIENT_USER_NAME=fooClientIdPassword
CLIENT_USER_PASSWORD=secret
FINAL_USER_NAME=john
FINAL_USER_PASSWORD=123

FILE_OAUTH_TOKEN=./flow-token.json
FILE_OAUTH_RESOURCE=./flow-resource.json
FILE_OAUTH_REFRESH=./flow-refresh.json
FILE_OAUTH_RESOURCE_REFRESH=./flow-resource-refresh.json
FILE_OAUTH_RESOURCE_BAR=./flow-resource-bar.json
FILE_ACTUATOR_MAPPINGS=./actuator-mappings.json


rm ./*.json

# Retreive token

curl \
  --silent \
  -o $FILE_OAUTH_TOKEN \
  -u $CLIENT_USER_NAME:$CLIENT_USER_PASSWORD \
  --header "accept: application/json, text/plain, */*" \
  --header "content-type: application/x-www-form-urlencoded;charset=utf-8" \
  --data "client_id=$CLIENT_USER_NAME&grant_type=password&username=$FINAL_USER_NAME&password=$FINAL_USER_PASSWORD" \
  http://localhost:8081/spring-security-oauth-server/oauth/token

echo "Token:"
echo "---"
cat $FILE_OAUTH_TOKEN
echo
echo "---"

ACCESS_TOKEN=$(jq -r '.access_token' $FILE_OAUTH_TOKEN)
REFRESH_TOKEN=$(jq -r '.refresh_token' $FILE_OAUTH_TOKEN)

echo "Access token :" $ACCESS_TOKEN
echo "Refresh token:" $REFRESH_TOKEN

# Consume resource

curl \
  --silent \
  -o $FILE_OAUTH_RESOURCE \
  --header "Authorization: Bearer $ACCESS_TOKEN" \
  http://localhost:8082/spring-security-oauth-resource/foos/1

echo "Resource:"
echo "---"
cat $FILE_OAUTH_RESOURCE
echo
echo "---"

# Refresh token

curl \
  --silent \
  -u $CLIENT_USER_NAME:$CLIENT_USER_PASSWORD \
  -o $FILE_OAUTH_REFRESH \
  --data "grant_type=refresh_token&refresh_token=$REFRESH_TOKEN" \
  http://localhost:8081/spring-security-oauth-server/oauth/token

echo "Refresh:"
echo "---"
cat $FILE_OAUTH_REFRESH
echo
echo "---"

ACCESS_TOKEN=$(jq -r '.access_token' $FILE_OAUTH_REFRESH)
REFRESH_TOKEN=$(jq -r '.refresh_token' $FILE_OAUTH_REFRESH)

# Consume resource with new token

curl \
  --silent \
  -o $FILE_OAUTH_RESOURCE_REFRESH \
  --header "Authorization: Bearer $ACCESS_TOKEN" \
  http://localhost:8082/spring-security-oauth-resource/foos/1

echo "Resource:"
echo "---"
cat $FILE_OAUTH_RESOURCE_REFRESH
echo
echo "---"

# Insufficient scope call (bar)

curl \
  --silent \
  -o $FILE_OAUTH_RESOURCE_BAR \
  --header "Authorization: Bearer $ACCESS_TOKEN" \
  http://localhost:8082/spring-security-oauth-resource/bars/1

echo "Resource without privileges:"
echo "---"
cat $FILE_OAUTH_RESOURCE_BAR
echo
echo "---"

# Actuator

curl \
  --silent \
  -o $FILE_ACTUATOR_MAPPINGS \
  -u $FINAL_USER_NAME:$FINAL_USER_PASSWORD \
  http://localhost:8081/spring-security-oauth-server/actuator/mappings
