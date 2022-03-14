#!/bin/sh

FLAG_DRYRUN=false

BASE_DIR=$(pwd)
WORKDIR=$BASE_DIR/workdir
CALLBACKS=$WORKDIR/callbacks.sh

# execute $COMMAND [$ALT_TEXT] [$FLAG_DRYRUN=false]
# if command and FLAG_DRYRUN=true are set the command will be execuded
# if command and FLAG_DRYRUN=false (or no 3rd argument is provided) 
# if FLAG_QUIET=true are the command will not be printed to stdout
# if FLAG_QUIET=false (or no 4rd argument is provided) print command prefixed by "# "
# if an ALT_TEXT is provided it will print the alt text instead of the command
# this can be used to mask sensitiv information
# the function will only print the command the command to stdout
function execute () {
  local exec_command=$1
  local alt_text=${2:-${1}}
  local flag_dryrun=${3:-${FLAG_DRYRUN:-false}}
  local flag_quiet=${4:-${FLAG_QUIET:-false}}

  if [[ "${flag_dryrun}" == false ]]; then
    if [[ "${flag_quiet}" == false ]]; then
      echo "${alt_text}" | awk '{$1=$1;print}' | sed 's/^/# /'
    fi
    eval "${exec_command}"
  else
    echo "${exec_command}" | awk '{$1=$1;print}'
  fi
}

function usage {
  echo 
  echo 'docker run --rm -it -v $(echo ~)/.aws:/root/.aws:ro -v $(pwd):/opt/train/workdir gepardec/train COMMAND'
  echo "Available commands: valid terraform commands such as apply or destroy"
}

function create_readme {
  local counter=${1}
  local targetDir=${2}

  echo """IP: "$(terraform output -json instance_public_ips | jq ".[0][${counter}]" | tr -d '"')" """ > ${targetDir}/readme.txt
  echo """DNS: "$(terraform output -json instance_public_dns | jq ".[0][${counter}]" | tr -d '"')" """ >> ${targetDir}/readme.txt
}

function create_ansible_inventory {
  local counter=${1}
  local targetDir=${2}

  echo """${counter} ansible_host="$(terraform output -json instance_public_ips | jq ".[0][${counter}]" | tr -d '"')" ansible_user=TBD ansible_ssh_private_key_file=${counter}/access ansible_ssh_common_args='-o StrictHostKeyChecking=no'""" >> ../${targetDir}/hosts
}

function callbacks_enabled {
  test -f $CALLBACKS
}

function main {
  # check if aws config is mounted
  if [[ ! -f "$(echo ~)/.aws/config" ]]; then
    usage
    echo 
    echo "aws config not found. Please mount ~/.aws folder"
    exit 1
  fi
  # source terraform variables
  export $(cat workdir/variables.tfvars | sed '/^#/d' | tr -d "[:blank:]")

  local aws_region=$(echo ${aws_region} | tr -d '"')
  local owner=$(echo ${owner} | tr -d '"')
  local resource_prefix=$(echo ${resource_prefix} | tr -d '"')
  local backend_config_key="train/${owner}_${resource_prefix}_${aws_region}"
  
  # check if terraform variables are set
  if [[  -z "${owner-unset}" ]] || [[  -z ${resource_prefix-unset} ]]; then
     usage
     echo 
     echo "owner and resource_prefix in workdir/variables.tfvars can't be empty"
     exit 1
  fi
 
  # switch to terraform directory -> current limitation of terraform output command
  cd terraform

  # initialize terraform
  execute "terraform init -backend-config='key=${backend_config_key}'"

  # create ssh keys
  instance_replica=$((instance_replica - 1))
  local counter=${instance_replica}
  if [[ "${1}" == "apply" ]]; then 
    while [[ ${counter} -ge 0 ]]; do
      execute "mkdir -p ../workdir/${resource_prefix}/${counter}"
      execute "ssh-keygen -t rsa -b 4096 -f ../workdir/${resource_prefix}/${counter}/access -q -N ''"
      counter=$((counter - 1))
    done
  fi  

  # execute terraform
  execute "terraform $* -var-file='../workdir/variables.tfvars' -auto-approve"

  # create readme and ansible inventory
  if [[ "${1}" == "apply" ]]; then 
    counter=${instance_replica}
    >../workdir/${resource_prefix}/hosts
    while [[ ${counter} -ge 0 ]]; do
      create_readme ${counter} ../workdir/${resource_prefix}/${counter}
      create_ansible_inventory ${counter} workdir/${resource_prefix}
      counter=$((counter - 1))
    done
    chown -R 1000:1000 ../workdir/*

    callbacks_enabled && train_apply_postprocess
  fi
}

callbacks_enabled && . $CALLBACKS

main "$@"
