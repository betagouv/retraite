# Docs divers pour Fluentd

## Installations

### FluentD

Sur Mac OSX, [voir cette page](http://docs.fluentd.org/articles/install-by-dmg) qui 
contient toutes les explications, ainsi que les scripts de start et stop du daemon
fluentd.

### Plugin elasticsearch

Commande pour Mac OSX : 

```
  sudo /opt/td-agent/usr/sbin/td-agent-gem install fluent-plugin-elasticsearch
```

## Configuration

Voir fichier `td-agent.conf` ci-joint, et notamment la section suivantes :

```
    <match retraite.**>
      type stdout
      type elasticsearch
      hosts <URL_ELK>
      logstash_format true
      logstash_prefix logstash-retraite
    </match>

```
URL_ELK est l'URL vers le serveur ELK, de la forme `https://<user>:<mdp>@<host>:<port>`.