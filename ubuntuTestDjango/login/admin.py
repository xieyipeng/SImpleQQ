from django.contrib import admin

# Register your models here.
from login.models import User

admin.site.register(User)