from django.shortcuts import render

# Create your views here.
import json
import os

from django.http import HttpResponse
from login.models import User


def get_test(request):
    users = []
    list = User.objects.all()
    if request.method == 'GET':  # 测试代码，真正逻辑自己编写
        for var in list:
            user = {}
            user['id'] = var.id
            user['name'] = var.name
            user['ctime'] = str(var.c_time)
            user['sex'] = var.sex
            users.append(user)
    return HttpResponse(json.dumps(users))  # 返回user数据给安卓端


def post_test(request):
    pass


def upload_file(request):
    pass
