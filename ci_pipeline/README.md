# Continuous Delivery Pet Project
Playground for setting up a continuous delivery pipeline with Docker.

Uses:
- Jenkins (As container)
- GitLab (As container)

# Notes

## SSH

### Master - Client

Files
* known_hosts
* id_rsa (private key file)

*Copy Slave pub key on authorized_keys on Server*

known_hosts is created on successful ssh manual connection

_Can add `config` file to check ssh connection. Format:
```
Host <alias>
    HostName <ip/domain>
    User <user>
    IdentityFile <path-to-priv-key>
```

### Slave - Server

Files:
* authorized_keys


## Gitlab

Add jenkins url to notify (webhooks)
Copy Account Settings -> Account -> Private Token into Jenkins Gitlab plugin

## Jenkins

Basic tasks
* Configure Gitlab and Test
* Create node (Slave) and configure it to Main node
