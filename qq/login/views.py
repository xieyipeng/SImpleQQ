import json
import os

from django.http import HttpResponse
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
    return HttpResponse(json.dumps(users))  # 返回user数据给安卓端


def posy_test(request):
    if request.method == 'POST':
        print(request.POST)
        req = request.POST.get('v1')  # 获取post请求中的v1所对应的值
        print('post请求')
        print(req)
        return HttpResponse(req)  # 返回v1所对应的值 -- 只是用来测试代码，实际逻辑按自己要求来
    else:
        return HttpResponse("none")


def upload_file(request):
    if request.method == 'POST':
        temp_file = request.FILES
        if not temp_file:
            print("文件传输失败")
            return HttpResponse('upload failed!')
        else:
            print("文件传输成功")

            # 自行理解下面输出的内容
            # print(request.POST)
            # print(temp_file)
            # print(temp_file.get('file'))
            # print(temp_file.get('file').name)
        # TODO: 获取图片并存储在./uploads目录下 -- 实际逻辑按自己要求来
        destination = open(os.path.join("./uploads", temp_file.get('file').name), 'wb+')
        for chunk in temp_file.get('file').chunks():
            destination.write(chunk)
        destination.close()
    return HttpResponse('upload success!')
