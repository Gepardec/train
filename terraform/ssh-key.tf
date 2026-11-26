locals {
  # Wird wegen ignore_changes nie angewendet oder verglichen.
  key_placeholder = "placeholder"
}


resource "aws_key_pair" "main" {
  count = var.instance_replica
  key_name   = "${var.resource_prefix}_${count.index}"
  public_key = try(file("../workdir/${var.resource_prefix}/${count.index}/access.pub"), local.key_placeholder)

  tags = {
    owner = var.owner
  }

  lifecycle {
    ignore_changes = [public_key]
  }
}