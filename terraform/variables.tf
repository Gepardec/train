variable "aws_region" {
  type = string
  default = "us-west-2"
  description = "The infrastructure will be provisioned in the AWS region specified by aws_region."
}

variable "bootstrap_file" {
  type = string
  default = "helloworld.sh"
  description = "Each EC2 instance will execute the bootstrap file specified by bootstrap_file. The file needs to exist in terraform/files/bootstrap/ and has the execution flag set"
}

variable "instance_replica" {
  type = number
  description = "instance_replica defines the number of EC2 instances that shall be created by terrafrom in the specified region."
}

variable "instance_type" {
  type = string
  default = "t2.micro"
  description = "EC2 instances are provisined with the instance type specified by instance_type"
}

variable "owner" {
  type = string
  description = "All resource associated with the workshop are tagged with an owner tag containing the name or shorthand of the person running the workshop environment."
}

variable "resource_prefix" {
  type = string
  description = "All resources are named based on the resource_prefix. E.g. the VPC name with resource_prefix = 'helloworld' is helloworld_vpc. This increases transparency and in some cases resource names need to be unique."
}
