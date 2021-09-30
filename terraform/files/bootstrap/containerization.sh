#!/bin/bash

# install ansible
yum update -y
yum install -y epel-release
yum install -y ansible

# install roles
ansible-galaxy install ckaserer.bashrc
ansible-galaxy install ckaserer.timezone
ansible-galaxy install ckaserer.users
ansible-galaxy install geerlingguy.docker
ansible-galaxy install grog.sudo
ansible-galaxy install grog.package

cat << EOF > /tmp/playbook.yml
---
- hosts: localhost
  become: true
  tasks:
    - include_role:
        name: grog.package
      vars:
        package_list:
          - name: git
          - name: vim
          - name: htop
          - name: net-tools
          - name: wget
          - name: nano
          - name: gcc
          - name: httping
          - name: iscsi-initiator-utils
          - name: nfs-utils
    - include_role:
        name: ckaserer.bashrc
      vars: 
        systemwide: true
    - include_role:
        name: ckaserer.users
      vars:
        users:
          - centos
    - include_role:
        name: grog.sudo
      vars:
        sudo_list:
          - name: centos
            sudo:
              hosts: ALL
              as: ALL:ALL
              nopasswd: yes
              commands: ALL
    - include_role:
        name: ckaserer.timezone
    - include_role:
        name: geerlingguy.docker
      vars: 
        docker_users:
          - centos
...
EOF

# run playbook
ansible-playbook /tmp/playbook.yml 2>&1 | tee /tmp/playbook.log

# set SELinux in permissive mode (effectively disabling it)
sudo setenforce 0
sudo sed -i 's/^SELINUX=enforcing$/SELINUX=permissive/' /etc/selinux/config

# install kubelet, kubeadm and kubectl
cat <<EOF | sudo tee /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://packages.cloud.google.com/yum/repos/kubernetes-el7-\$basearch
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://packages.cloud.google.com/yum/doc/yum-key.gpg https://packages.cloud.google.com/yum/doc/rpm-package-key.gpg
exclude=kubelet kubeadm kubectl
EOF

sudo yum install -y jq kubelet kubeadm kubectl --disableexcludes=kubernetes

sudo systemctl enable --now kubelet