import json

from django.http import HttpResponse

from login.models import User


def getAllUser(request):
    list = User.objects.all()
    users = []
    for var in list:
        user = {}
        user['id'] = var.id
        user['name'] = var.name
        user['ctime'] = str(var.c_time)
        user['address'] = var.sex

        users.append(user)

    # 将集合或字典转换成json 对象
    # c = json.dumps(list3)
    return HttpResponse(json.dumps(users))
