Available Workshops
#######################

* containerization
* helloworld
* troubleshooting (WIP)

---------

containerization
------------------------

The containerization workshop is designed to experiment with container technology such as docker and docker-compose. Provisioning a workshop with :code:`bootstrap_file = "containerization.sh"` in your :code:`workdir/variables.tfvars` will 

* update the OS
* install epel-release
* install ansible
* run the docker ansible role to

    * install docker
    * install docker-compose 
    * sets docker usage rights (docker group) to the default VM user (centos). 

This bootstrap script is preparing the host to run ansible and continues execute more complex steps via ansible.

------------

helloworld
------------

The helloworld workshop is a exactly what it sounds like - a hello world showcasing how to set up a new workshop from scratch. Provisioning a workshop with :code:`bootstrap_file = "helloworld.sh"` in your :code:`workdir/variables.tfvars` will 

* update the OS
* install httpd
* serve a hello world message on Port 80

This is a simple and lightweight way to start experimenting with the infrastructure code in order to get familiar with terraform and how we use it in the context of workshops.

---------

troubleshooting (WIP)
------------------------

Disclaimer: The troubleshooting workshop is not readily available yet! Hence the WIP in brackets (Work in Progress)

Contact: wirnse