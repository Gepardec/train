Provision a Workshop
#################################

Requirements
---------------

* AWS IAM with programatic access
* docker

IAM programatic access
------------------------

You need to create an AWS IAM user with programatic access and store the credentials on your workstation. For the rest of the doc we will assume that your aws credentials file exists as :code:`~/.aws/credentials`.

Provision a Workshop
------------------------

Create a new folder, let's name it :code:`workdir` for the remainder of the section.

.. code-block:: bash
   
   mkdir workdir

In our :code:`workdir` we create a new file named :code:`variables.tfvars` with the workshop specification we want to provision.

Next we need to populate our file. Let's say I, :code:`Jon Doe <jdoe>`, want to provision a workshop for :code:`1` participant(s) with :code:`t2.micro` instances in :code:`us-west-1`. In order to avoid resource naming conflicts we prefix our resource with :code:`helloworld`. This would result in

.. code-block:: bash

   aws_region       = "us-west-1"
   bootstrap_file   = "helloworld.sh"
   instance_replica = 1
   instance_type    = "t2.micro"
   owner            = "jdoe"
   resource_prefix  = "helloworld"

If you want a bit more explaination in regard to what those variable are for you can check out :doc:`Variables <variables>`.

Now we can create our workshop environment via

.. code-block:: bash

   docker run --rm -it \
     -v $(echo ~)/.aws:/root/.aws:ro \
     -v $(pwd)/workdir:/opt/train/workdir \
      ghcr.io/gepardec/train:latest apply

Resulting in an output similar to this

.. code-block:: bash

   # terraform init -backend-config='key=train/jdoe_helloworld_us-west-1' terraform

   Initializing the backend...

   Successfully configured the backend "s3"! Terraform will automatically
   use this backend unless the backend configuration changes.

   Initializing provider plugins...
   - Checking for available provider plugins...
   - Downloading plugin for provider "aws" (hashicorp/aws) 2.57.0...

   The following providers do not have any version constraints in configuration,
   so the latest version was installed.

   To prevent automatic upgrades to new major versions that may contain breaking
   changes, it is recommended to add version = "..." constraints to the
   corresponding provider blocks in configuration, with the constraint strings
   suggested below.

   * provider.aws: version = "~> 2.57"

   Terraform has been successfully initialized!

   You may now begin working with Terraform. Try running "terraform plan" to see
   any changes that are required for your infrastructure. All Terraform commands
   should now work.

   If you ever set or change modules or backend configuration for Terraform,
   rerun this command to reinitialize your working directory. If you forget, other
   commands will detect it and remind you to do so if necessary.
   # mkdir -p workdir/helloworld/0
   # ssh-keygen -t rsa -b 4096 -f workdir/helloworld/0/access -q -N ''
   # terraform apply -var-file='workdir/variables.tfvars' -auto-approve terraform
   data.aws_ami.centos: Refreshing state...
   aws_vpc.main: Creating...
   aws_key_pair.main[0]: Creating...
   aws_key_pair.main[0]: Creation complete after 4s [id=helloworld_0]
   aws_vpc.main: Still creating... [10s elapsed]
   aws_vpc.main: Creation complete after 11s [id=vpc-04808488d3bebc0ac]
   aws_internet_gateway.main: Creating...
   aws_subnet.main: Creating...
   aws_security_group.main: Creating...
   aws_subnet.main: Creation complete after 6s [id=subnet-068847305a316a713]
   aws_internet_gateway.main: Creation complete after 7s [id=igw-0239c6cc5df123477]
   aws_route_table.main: Creating...
   aws_security_group.main: Still creating... [10s elapsed]
   aws_security_group.main: Creation complete after 11s [id=sg-0fb8b0d5c66886475]
   aws_instance.main[0]: Creating...
   aws_route_table.main: Creation complete after 6s [id=rtb-04ef8a9883b563761]
   aws_route_table_association.main: Creating...
   aws_route_table_association.main: Creation complete after 2s [id=rtbassoc-0f146f884068e3658]
   aws_instance.main[0]: Still creating... [10s elapsed]
   aws_instance.main[0]: Still creating... [20s elapsed]
   aws_instance.main[0]: Still creating... [30s elapsed]
   aws_instance.main[0]: Creation complete after 36s [id=i-01462142ede2b68d3]

   Apply complete! Resources: 8 added, 0 changed, 0 destroyed.

   Outputs:

   instance_ips = [
   [
      "52.53.254.171",
   ],
   ]

---------

Destroy a Workshop
------------------------

.. code-block:: bash

   docker run --rm -it \
     -v $(echo ~)/.aws:/root/.aws:ro \
     -v $(pwd)/workdir:/opt/train/workdir \
     ghcr.io/gepardec/train:latest destroy

Resulting in an output similar to this

.. code-block:: bash


   # terraform init -backend-config='key=train/trainerin_helloworld_us-west-1' terraform

   Initializing the backend...

   Initializing provider plugins...

   The following providers do not have any version constraints in configuration,
   so the latest version was installed.

   To prevent automatic upgrades to new major versions that may contain breaking
   changes, it is recommended to add version = "..." constraints to the
   corresponding provider blocks in configuration, with the constraint strings
   suggested below.

   * provider.aws: version = "~> 2.57"

   Terraform has been successfully initialized!

   You may now begin working with Terraform. Try running "terraform plan" to see
   any changes that are required for your infrastructure. All Terraform commands
   should now work.

   If you ever set or change modules or backend configuration for Terraform,
   rerun this command to reinitialize your working directory. If you forget, other
   commands will detect it and remind you to do so if necessary.
   # terraform destroy -var-file='workdir/variables.tfvars' -auto-approve terraform
   aws_key_pair.main[0]: Refreshing state... [id=helloworld_0]
   data.aws_ami.centos: Refreshing state...
   aws_vpc.main: Refreshing state... [id=vpc-04808488d3bebc0ac]
   aws_internet_gateway.main: Refreshing state... [id=igw-0239c6cc5df123477]
   aws_subnet.main: Refreshing state... [id=subnet-068847305a316a713]
   aws_security_group.main: Refreshing state... [id=sg-0fb8b0d5c66886475]
   aws_route_table.main: Refreshing state... [id=rtb-04ef8a9883b563761]
   aws_instance.main[0]: Refreshing state... [id=i-01462142ede2b68d3]
   aws_route_table_association.main: Refreshing state... [id=rtbassoc-0f146f884068e3658]
   aws_key_pair.main[0]: Destroying... [id=helloworld_0]
   aws_route_table_association.main: Destroying... [id=rtbassoc-0f146f884068e3658]
   aws_instance.main[0]: Destroying... [id=i-01462142ede2b68d3]
   aws_route_table_association.main: Destruction complete after 0s
   aws_route_table.main: Destroying... [id=rtb-04ef8a9883b563761]
   aws_key_pair.main[0]: Destruction complete after 0s
   aws_route_table.main: Destruction complete after 3s
   aws_internet_gateway.main: Destroying... [id=igw-0239c6cc5df123477]
   aws_instance.main[0]: Still destroying... [id=i-01462142ede2b68d3, 10s elapsed]
   aws_internet_gateway.main: Still destroying... [id=igw-0239c6cc5df123477, 10s elapsed]
   aws_internet_gateway.main: Destruction complete after 15s
   aws_instance.main[0]: Still destroying... [id=i-01462142ede2b68d3, 20s elapsed]
   aws_instance.main[0]: Still destroying... [id=i-01462142ede2b68d3, 30s elapsed]
   aws_instance.main[0]: Destruction complete after 33s
   aws_subnet.main: Destroying... [id=subnet-068847305a316a713]
   aws_security_group.main: Destroying... [id=sg-0fb8b0d5c66886475]
   aws_security_group.main: Destruction complete after 1s
   aws_subnet.main: Destruction complete after 1s
   aws_vpc.main: Destroying... [id=vpc-04808488d3bebc0ac]
   aws_vpc.main: Destruction complete after 1s

   Destroy complete! Resources: 8 destroyed.