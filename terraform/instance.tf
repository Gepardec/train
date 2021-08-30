data "aws_ami" "main" {
  most_recent = true

  filter {
    name   = "name"
    values = var.ami_name
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }

  owners = var.ami_owners
}

resource "aws_instance" "main" {
  ami             = data.aws_ami.main.id
  count           = var.instance_replica
  instance_type   = var.instance_type
  key_name        = "${var.resource_prefix}_${count.index}"
  subnet_id       = aws_subnet.main.id
  user_data       = file("files/bootstrap/${var.bootstrap_file}")
  vpc_security_group_ids = [aws_security_group.main.id]
  
  root_block_device {
    delete_on_termination = true
  }

  metadata_options {
      http_endpoint = "enabled"
      http_tokens = "required"
  }
  
  tags = {
    Name    = "${var.resource_prefix}_${count.index}"
    created = timestamp()
    owner = var.owner
  }
}
