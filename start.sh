#!/bin/bash
#
# Start script for overseas-entities-api

export APP_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROXY_ARGS=""

if [[ -z "${MESOS_SLAVE_PID}" ]]; then
    echo "Vagrant not supported, please use Docker"

else

    PORT="$1"
    CONFIG_URL="$2"
    ENVIRONMENT="$3"
    APP_NAME="$4"

    source /etc/profile

    echo "Downloading environment from: ${CONFIG_URL}/${ENVIRONMENT}/${APP_NAME}"
    wget -O "${APP_DIR}/private_env" "${CONFIG_URL}/${ENVIRONMENT}/private_env"
    wget -O "${APP_DIR}/global_env" "${CONFIG_URL}/${ENVIRONMENT}/global_env"
    wget -O "${APP_DIR}/app_env" "${CONFIG_URL}/${ENVIRONMENT}/${APP_NAME}/env"
    source "${APP_DIR}/private_env"
    source "${APP_DIR}/global_env"
    source "${APP_DIR}/app_env"

fi

exec java ${JAVA_MEM_ARGS} -jar ${PROXY_ARGS} -Dserver.port="${PORT}" -Dspring.data.mongodb.uri="$MONGODB_URL" "${APP_DIR}/overseas-entities-api.jar"
