.. # define a hard line break for HTML
.. |br| raw:: html

   <br />

.. image:: https://img.shields.io/badge/license-GPL%20v3.0-brightgreen.svg?style=flat-square
   :alt: licence badge
   :align: left

.. image:: https://img.shields.io/maintenance/yes/2022?style=flat-square
   :alt: maintenance badge
   :align: left

|br|
 
Welcome to our workshop infra docs!
=======================================================

Providing a frictionless experience for workshop participants with hands on, technical sections is key to focus on what is important - hands on learning and experimenting with new technology. 

It should be easy to work through the hands on labs no matter the operating system or hardware specification of your device. 

First steps
----------------

.. toctree::
   :maxdepth: 1
   :hidden:
   :caption: First Steps

   first_steps/provision
   first_steps/access
   first_steps/variables


In our :doc:`Getting Started <first_steps/provision>` guide you will learn how to manage the lifecycle of a workshop environment on your own, which workshops currently exists and what those pesky variables that you need to configure do.

---------

.. Available Workshops - Start

.. toctree::
   :maxdepth: 1
   :hidden:
   :caption: Available Workshops

   workshop_list

.. Available Workshops - END

A new Workshop
-------------------------

.. toctree::
   :maxdepth: 1
   :hidden:
   :caption: A new Workshop

   workshop_create

You want to do a workshop but could not find a readiliy available bootstrap script that fits your need? Don't worry, :doc:`here <workshop_create>`  we cover how you can create a new bootstrap file containing all the provision steps you desire. Please consider adding it to our repo once you are happy with it so all can benefit from your work.

---------

Architecture
---------------------

.. toctree::
   :maxdepth: 1
   :hidden:
   :caption: Architecture

   architecture

In :doc:`Architecture <architecture>` we cover which resource get provision in AWS and a bit more... go check it out.

---------

Misc
---------------------

.. toctree::
   :maxdepth: 1
   :hidden:
   :caption: Misc

   misc

You want to know how we ended up with terraform?! Why not packer, vagrant, or a vm image? In our :doc:`Why Terraform?! <misc>` section you will find all the answers you did not know you wanted.

---------

Contribution Guidelines
------------------------

.. toctree::
   :maxdepth: 1
   :hidden:
   :caption: Contribution Guideline

   contribute

We need to be able to understand the technology we work with. In addition to the general awareness we want to make it simple for new colleagues to get started with the tools that we have in place. Hence documentation is key. Here is how you can contribute to our repository.