#!/bin/sh

FLAG_DRYRUN=false

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
  echo 'docker run --rm -it -v $(echo ~)/.aws:/root/.aws:ro -v $(pwd):/opt/train/output gepardec/train COMMAND'
  echo "Available commands: valid terraform commands such as apply or destroy"
}

function main {
  # check if aws config is mounted
  if [[ ! -f "$(echo ~)/.aws/credentials" ]]; then
    usage
    echo 
    echo "aws credentials not found. Please mount ~/.aws folder"
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

  # initialize terraform
  execute "terraform init -backend-config='key=${backend_config_key}' terraform"

  # create ssh keys
  num_participants=$((num_participants - 1))
  if [[ "${1}" == "apply" ]]; then 
    while [[ ${num_participants} -ge 0 ]]; do
      execute "mkdir -p workdir/${resource_prefix}/${num_participants}"
      execute "ssh-keygen -t rsa -b 4096 -f workdir/${resource_prefix}/${num_participants}/access -q -N ''"
      num_participants=$((num_participants - 1))
    done
  fi  

  # execute terraform
  execute "terraform $@ -var-file='workdir/variables.tfvars' -auto-approve terraform"
}

main $@