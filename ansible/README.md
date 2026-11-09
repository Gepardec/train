# Infra Roles 
## acme_cert
Rolls out certificates. Needs EAB (External Account Binding) credentials from a ACME provider. Tested with ZeroSSL and Google Public CA.

Needed vars - put them into your inventory
```txt
[all:vars]
acme_eab_hmac_key="__secret_here__"
acme_eab_kid="__secret_here__"
acme_email="valid.email@gepardec.com"
acme_directory="https://acme.zerossl.com/v2/DV90"
```