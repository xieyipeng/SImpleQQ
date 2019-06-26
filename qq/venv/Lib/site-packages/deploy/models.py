#!/usr/bin/python
# coding: utf8

# Deploy

import os
import datetime


from deploy.conf import APPS_HOME, SOCKS_HOME, GIT_HOME, SUPERVISOR_CONFD, NGINX_SITES, NGINX_SITES_ENABLED, HOLDERS_HOME, BACKUP_DIR


class App:
    def __init__(self, name, domains=None):
        name = name.replace("-", "_").replace(".", "__")
        self.name = name
        self.holder = os.path.join(HOLDERS_HOME, name)
        self.user = "deploy-app-%s" % name
        self.home = os.path.join(APPS_HOME, name)
        self.git = os.path.join(GIT_HOME, "%s.git"%name)
        self.sock = os.path.join(SOCKS_HOME, "%s.sock"%name)
        self.supervisor_conf = os.path.join(SUPERVISOR_CONFD, "%s.conf"%name)
        self.static = os.path.join(self.home, "static")
        self.nginx_conf = os.path.join(NGINX_SITES, name)
        self.nginx_enabled = os.path.join(NGINX_SITES_ENABLED, name)
        self.post_update_hook = os.path.join(self.git, "hooks/post-update")

        if not domains:
            domain = self.name.replace("__", ".").replace("_", "-")
            self.domains = "%s www.%s" % (domain, domain)
        else:
            self.domains = domains

    def is_exists(self):
        return os.path.exists(self.holder)

    backup_dir = property(lambda self: os.path.join(BACKUP_DIR, "%s_%s"%(datetime.datetime.now().strftime("%Y-%m-%d-%H-%M-%S"), self.name)))
