#!/bin/bash

instanceCount=$(jq '.instanceCount' ${CONFIGURATION_DIR}/configuration.json)

if [[ "${instanceCount}" =~ ^[0-9]{1,2} ]]
then
  if [ "${1}" != 'destroy' ]
  then
    echo "Generating ssh key-pairs for instanceCount '${instanceCount}'"
    counter=0
    while [[ counter -lt ${instanceCount} ]]; do
      sshFileName="${OUT_DIR}/id_rsa_${counter}"
      if [ -e "${sshFileName}" ] && [ -e "${sshFileName}.pub" ]
      then
        echo "Using existing ssh-key-pair '${sshFileName}'"
      else
        echo "Generating ssh key-pair for instance '${counter}'"
        ssh-keygen -t rsa -b 4096 -f "${sshFileName}" -q -N ''
      fi
      counter=$((counter + 1))
    done
  fi
else
  echo "InstanceCount '${instanceCount}' is invalid, is the configuration.json mapped to '${CONFIGURATION_DIR}/configuration.json'"
fi

if [ "${1}" == 'deploy' ]
then
  cdk deploy --require-approval never --outputs-file "${OUT_DIR}/training-info.json"
elif [ "${1}" == 'destroy' ]
then
  cdk destroy --force
elif [ "${1}" == 'synth' ]
then
  cdk synth
else
  echo "Only [deploy | destroy | synth] commands are supported, and you provided '${1}'"
  exit 1
fi
