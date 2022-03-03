# Workshop Environment

Providing a frictionless experience for workshop participants with hands on, technical sections is key to focus on what is important - hands on learning and experimenting with new technology. 

It should be easy to work through the hands on labs no matter the operating system or hardware specification of your system. So, how can we provide this magical environment? Find out on our github page at [https://gepardec.github.io/train/](https://gepardec.github.io/train/)


## Troubleshooting VM install
edit workdir/variables.tfvars set instance_replica
cd terraform
terraform apply -var-file ../workdir/variables.tfvars
yes

wait

edit files/inventory set hosts with result from terraform
cd files
ansible-playbook -i inventory playbook_ec2.yml --tags=reconfigure

Links zu nomachine teilen.
