Create a new Workshop
#########################

You came to the right place if the current selection of available workshops does not fit your need and you want to create a new one.

Make it work
-------------------------

Create a new bootstrap script in :code:`terraform/files/bootstrap/` with a descriptive name. The file name will later be used to reference the correct bootstrap script via the terraform variable :code:`bootstrap_file`. We will name our example file :code:`myBootStrapFile.sh` for the rest of the documentation.

The bootstrap script itself needs to be executable. You can add the executable flag via

.. code-block:: bash
   
   chmod +x myBootStrapFile.sh

Before we fill our bootstrap script with commands we add `Shebangs <https://linuxize.com/post/bash-shebang/>`_ as first line to our file.

.. code-block:: bash
   
   #!/bin/bash

Once we got the Shebang in place we can add our infrastructure provisioning steps. For little things like our :code:`helloworld.sh` we just use bash to do it all. For more complex scenarios we highly recommend you use ansible instead.

How can you use ansible in your provisioning process? You need make sure ansible is installed and you need to get your playbook from somewhere, preferably a git repo containing your playbook.

Write it down
--------------------------

Please add your workshop to the list of available workshops in :code:`docs/source/workshop_list.rst` with a short summary of your workshop and the packages installed.

To publish the changes you need to build the documentation again via

.. code-block:: bash
   
   cd docs
   make clean && make html

and commit it to git.

Further information how to contribute to our repo can be found in our :doc:`Contribution <contribute>` section.

------------

FAQ
--------

**Q:** Can my bootstrap script call other scripts in the bootstrap folder?

**A:** No, it can't. Only one file can be uploaded to an ec2 instance to bootstrap it. If you really require more files you need to script it in a way that your bootstrap script fetches additional files for you.
