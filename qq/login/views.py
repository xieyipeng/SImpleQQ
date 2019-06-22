import json

from django.http import HttpResponse
from django.shortcuts import render

# Create your views here.
from login.models import User


def get_test(request):
    users = []
    user = {}
    list = User.objects.all()
    if request.method == 'GET':
        for var in list:
            user['id'] = var.id
            user['name'] = var.name
            user['ctime'] = str(var.c_time)
            user['sex'] = var.sex
            users.append(user)
    return HttpResponse(json.dumps(users))


def posy_test(request):
    if request.method == 'POST':
        req = request.POST.get('v1')
        print('post请求')
        print(req)
        return HttpResponse(req)
    else:
        return HttpResponse("")
