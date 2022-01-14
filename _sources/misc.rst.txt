
****************
Why Terraform?!
****************

Providing a frictionless experience for workshop participants with hands on, technical sections is key to focus on what is important - hands on learning and experimenting with new technology. 

It should be easy to work through the hands on labs no matter the operating system or hardware specification of your system. So, how can we privide this magical environment? Let's start by what we care about and why we don't do "x".

We care about
##############

* **up to date software packages** - Outdated software packages are just wrong.
* **standardized environement** - No more "But it works on my machine - look!"
  "On windows you have to do x, on Linux do y and on MacOS do b." 
* **shipability** - In order to run your training environment, go here, download that, go there install this. Now execute x to start...
* **low resource consumption** - No absurd hardware requirements .e.g. "Please participate only if you have at least 16GB RAM."
* **no admin priviledges** - Please install following software on your system. What?! You can't install software on your company notebook? Too bad...
* **maintainability** - All of the above without hours on end stuck with maintainance tasks. 

-------

Why not "x"
##############

As advertised let's go through a few potential scenarios and why they don't fit our requirements.

Let everybody fend for themselves
--------------------------------------

Here is an idea, let everybody install required software and configuration on their own before the workshop and you can start right away! That was easy, right? Well, not so fast. You need to provide installation instructions for all OS that you expect in the workshop and all hands on labs need to reflect the OS specific nuances in order to cover the variety of OS present at a BYOD - "bring your own device" - styled workshop.

How would this approach fare based on our criteria?

| ☑ up to date software packages
| ☐ standardized environement
| ☐ maintainability
| ☐ shipability
| ☐ low resource consumption
| ☐ no admin priviledges

Not well at all. We do not get a standardized environment, the hardware must be able to support it, most likely you will need admin priviledges to install software on your device. In regard to "easily accessible" we could argue based on software package, OS and participant skill level but overall I would not count this approach as easily accessible for the masses. What's the good part then? Well, since everybody will likely install the software just before the workshop, they will most likely end up with a rather new software version. Maintainability from the trainer perspective however can be a bit tricky. You need to check if all installation procedures that you hand out are still up to date before each workshop or risk emberrassing yourself and the company you stand for right from the start. Recovering from such a blunder is tricky if at all possible and should generally be avoided.

-------

DIY VMs
---------------

Okay, we need to streamline our approach a bit. Let's focus on standardization of the lab environment itself for a bit. By utilizing VMs we can standardize the lab environment for our workshop. Just a few things to consider here: "how do we create a VM?", "how do we ship a VM?", "how can everybody run a VM?" 

Let's keep it simple. We can create a VM manually by spinning it up, installing all packages and shuting it down. Next we need to ship the image to our participants. Depending on the restictions of the available internet we could provide a download link, or a USB stick, depending on network security at the workshop location. We created a vm, our participants got a copy of the VM, now they need to run it. With VirtualBox on all participant devices we could do exactly that and call it a day.

How would this approach fare based on our criteria?

| ☑ up to date software packages
| ☑ standardized environement
| ☐ maintainability
| ☐ shipability
| ☐ low resource consumption
| ☐ no admin priviledges

In short, VMs are great for providing a standardized environment. However the overhead of maintaining and updating the VM image and compatibility can be cumbersome and timeconsuming and does not guarantee a seemless user expirience. In addition we have not covered the feasability to get VirtualBox installed on all devices.

-------

Packer
------------

Manual provisioned VMs are like pet, you need to take care of them. You need to do OS updates and keep the required software up to date. Nobody got time for that! In this section we will focus on maintainability.

With packer we are able to automate the provisioning process of VMs. By automating the process it gets easier to maintain an up to date version for workshops. However we still need to regulary build the VM to keep it up to date.

How would this approach fare based on our criteria?

| ☑ up to date software packages
| ☑ standardized environement
| ☑ maintainability
| ☐ shipability
| ☐ low resource consumption
| ☐ no admin priviledges

VMs as standardized environment with up to date software build by packer we can already create a decent solution. However we have not covered all aspects yet.

-------

Vagrant
-------------

Packer can create VMs, but you still have the problem of shiping a rather large VM file to your users. With Vagrant we can define how the VM should look like via a Vagrantfile and only ship the Vagrantfile (a text file) and a readme on how to use it. This can be put in an email attachment. Once a participant has the Vagrantfile, vagrant and a supported hypervisor of their choice the can provision the VM on their device with vagrant. 

How would this approach fare based on our criteria?

| ☑ up to date software packages
| ☑ standardized environement
| ☑ maintainability
| ☑ shipability
| ☐ low resource consumption
| ☐ no admin priviledges

By providing descriptive Vagrantfiles instead of large VM files we can reduce initial friction in the setup process by reduce the file size and VM managment tasks that we need to communicate to each user to a few lines in a Vagrantfile and a few vagrant commands.

-------

Terraform + AWS
---------------------

We still haven't covered the little detail that participants will most likely have a device with limited resources and no admin priviledges to install new software - a typical setup for company issued notebooks of larger enterprises.

To factor in those restrictions we need to literaly think outside the box. We can utilize Terraform to provision the lab environments in a public cloud such as AWS shortly before a workshop. By provisioning the VM in a public cloud instead of the users device (box) we create more resource intesive lab environments to cover more complex hands on scenarios. In addition we do not need to install any software that requires admin priviledges on the participants device. A participant only needs internet access on Port 22 (ssh) to access the VM and an ssh client. For the duration of the workshop each participant gets an ssh key and an IP/DNS to connect to the lab environment and work unrestricted inside the VM. After the workshop we can use terraform to destroy the VMs to keep costs to a minimum.

How would this approach fare based on our criteria?

| ☑ up to date software packages
| ☑ standardized environement
| ☑ maintainability
| ☑ shipability
| ☑ low resource consumption
| ☑ no admin priviledges

That's it. Thanks for reading!

-------

FAQ
####

**Q: Wait!** How much does the AWS approach cost you?!

**A:** Glad you asked. Let's break it down. A rather beefy 16GB, 4vCPU VM in AWS costs 0.192 Dollar per hour (15. Apr 2020). 
For a single day workshop we would spin the VMs up the night before in order to have time to react if an issues occurs. 

This would result in 24h of uptime per VM totaling 0.192$ * 24h = 4.608$ per participant. 

Less than 5$ per participant for a frictionless workshop experience that fits our strict criteria is a bargain if you ask us. Or ask our participants, they will tell how frictionless their expierence was.

.. todo: add: why us region? why not docker / kubernetes

-------

**Source**

* https://www.packer.io/
* https://www.vagrantup.com/
* https://www.terraform.io/

