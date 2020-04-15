resource "aws_internet_gateway" "main" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name    = "${var.resource_prefix}_gw"
    Created = timestamp()
    Owner   = var.owner
  }
}
