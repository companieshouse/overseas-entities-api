#!/bin/bash
#  
# Start script for overseas-entities-api 
 
PORT=8080

exec java -jar -Dserver.port="${PORT}" "overseas-entities-api.jar"