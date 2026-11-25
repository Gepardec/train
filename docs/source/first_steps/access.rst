Access a Workshop Instance
##############################

In the previous section we have created a :code:`workdir`. After you have provisioned a workshop you will find a new folder inside that folder named like your prefix. In here you will find a set of numbered folders containing a little readme.txt with the IP and DNS of the ec2 instance.

.. code-block:: bash
   
   workdir/
    ├── helloworld
    │   ├── 0
    │   │   └── readme.txt
    │   └── hosts
    └── variables.tfvars

To access the first VM of our :code:`helloworld` prefixed VM via ssh make sure the private key is loaded by your lcoal SSH agent (via Keeper Settings "Developer"->"Activate SSH Agent") and run

.. code-block:: bash
   
   ssh centos@IP