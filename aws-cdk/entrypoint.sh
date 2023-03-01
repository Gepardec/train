#!/bin/sh

NUMBER_REGEX='^[0-9]{1,2}'
let INSTANCE_COUNT=$(jq '.instanceCount' ${CONFIGURATION_DIR}/configuration.json)

if [[ ${INSTANCE_COUNT} =~ ${NUMBER_REGEX} ]]; then
  echo "Generating ssh key-pairs for instanceCount '${INSTANCE_COUNT}'"
  let counter=0
  while [[ counter -lt ${INSTANCE_COUNT} ]]; do
      ssh-keygen -t rsa -b 4096 -f ${CONFIGURATION_DIR}/id_rsa_${counter} -q -N ''
      echo "Generating ssh key-pair for instance '${counter}'"
      counter=$((counter + 1))
  done
else
  echo "InstanceCount '${INSTANCE_COUNT}' is invalid, is the configuration.json mapped to '${CONFIGURATION_DIR}/configuration.json'"
fi

cd ${EXECUTION_DIR}
cdk --require-approval never ${1}