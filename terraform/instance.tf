data "aws_ami" "centos" {
  most_recent = true
  filter {
    name   = "name"
    values = ["CentOS Linux 7 x86_64 HVM *"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }

  owners = ["679593333241"] # CentOS
}

resource "aws_instance" "main" {
  ami             = "ami-05f145d9b6c677dbb"
  count           = var.instance_replica
  instance_type   = var.instance_type
  key_name        = "fedoraec2"
  subnet_id       = aws_subnet.main.id
  #user_data       = file("files/bootstrap/${var.bootstrap_file}")
  vpc_security_group_ids = ["${aws_security_group.main.id}"]
  
  root_block_device {
    delete_on_termination = true
  }

  tags = {
    Name    = "${var.resource_prefix}_${count.index}"
    created = timestamp()
    owner = var.owner
  }
}
