#!/usr/bin/python
# coding: utf8

# Deploy

DEPLOY_CONF = "/etc/deploy/deploy"

try:
    execfile(DEPLOY_CONF)
except IOError:
    pass
import conf

APPS_HOME = getattr(conf, "APPS_HOME", "/var/deploy/apps")
SOCKS_HOME = getattr(conf, "SOCKS_HOME", "/var/deploy/sock")
GIT_HOME = getattr(conf, "GIT_HOME", "/var/git")
GIT_USER = getattr(conf, "GIT_USER", "git")
BACKUP_DIR = getattr(conf, "BACKUP_DIR", "/var/deploy/backups/")

SUPERVISOR_CONFD = getattr(conf, "SUPERVISOR_CONFD", "/etc/supervisor/conf.d/")
NGINX_SITES = getattr(conf, "NGINX_SITES", "/etc/nginx/sites-available/")
NGINX_SITES_ENABLED = getattr(conf, "NGINX_SITES_ENABLED", "/etc/nginx/sites-enabled/")

HOLDERS_HOME = getattr(conf, "HOLDERS_HOME", "/etc/deploy/apps")
