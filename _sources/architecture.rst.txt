Architecture
##############

In the architecture section we will cover which resource get provision in AWS, what those variables in :code:`workdir/variables.tfvars` specify in more details and you get to look at an awesome infrastructure architecture diagram.

Each workshop will be provisioned in a single AWS region (e.g. us-west-1) and is encapseled in a dedicated VPC. The Internet Gateway, Security groups and Route Table in our VPC are configured to allow all ingress / egress traffic for ease of use.

All resource are tagged with a :code:`created` tag containing the terraform creation timestamp and an :code:`owner` tag containing the owner specified in :code:`workdir/variables.tfvars`.

Each instance is accessible with an unique auto generated ssh key which can be found in :code:`workdir/` after you have applied a terraform script.

Terraform state is the only element that is fixed to an S3 bucket named :code:`gepardec-terraform-state` in the :code:`us-east-1` region.

.. image:: _images/architecture.png
   :alt: Architecture Diagram   
