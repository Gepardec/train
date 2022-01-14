All about Variables
###############################

All variables that you should change can be set in :code:`workdir/variables.tfvars`. Let's go through each variable in detail.

---------

aws_region
------------

The entire workshop infrastructure (e.g. us-west-1) will be provisioned in the AWS region specified by :code:`aws_region = "us-west-1"`.

---------

bootstrap_file
---------------

Each EC2 instance will execute the file (e.g. helloworld.sh) specified by :code:`bootstrap_file = "helloworld.sh"`. The bootstrap file needs to exist in :code:`terraform/files/bootstrap/` and has the execution flag set. More on bootstrap files in "A new Workshop".

---------

instance_replica
-----------------

defines the number of EC2 instances that shall be created by terrafrom in the specified region.

---------

instance_type
---------------

EC2 instances are provisined based on the instance type (e.g. t2.micro) specified by :code:`instance_type = "t2.micro"`.

---------

owner
--------------

All resource associated with the workshop are tagged with an :code:`owner` tag containing the name or shorthand of the person running the workshop environment.

---------

resource_prefix
---------------
All resources are named based on the :code:`resource_prefix` specified in :code:`workdir/variables.tfvars`. E.g. the VPC name with :code:`resource_prefix = "helloworld"` is :code:`helloworld_vpc`. This increases transparency and in some cases resource names need to be unique.


-----------

ami_name
---------------
The Amazon Machine Image Name, or partial name with regex, used to boot the ec2 instance(s). Do not change this unless you know what you are doing.

-----------

ami_owners
---------------

The owner ids of allowed AMIs. Do not change this unless you know what you are doing.