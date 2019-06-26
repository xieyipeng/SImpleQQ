from datetime import timezone

from django.db import models


# Create your models here.
class User(models.Model):
    gender = (
        ('male', '男'),
        ('female', '女')
    )

    name = models.CharField(max_length=128)
    sex = models.CharField(max_length=32, choices=gender, default='男')
    c_time = models.DateTimeField(auto_now_add=True)
    # headImg = models.FileField('文件', upload_to='./uploads/%Y/%m/%d/')
    headImg = models.FileField('头像', upload_to='./uploads')  # 文件名

    def __str__(self):
        return self.name

    class Meta:
        ordering = ["-c_time"]
        verbose_name = "用户"
        verbose_name_plural = "用户"


# class Vedios(models.Model):
#     types = (('run', "跑步"), ('yoga', "瑜伽"), ('exercise', "健身"), ('balls', "球类"))
#
#     headImg = models.FileField('文件', upload_to='./upload')  # 文件名
#     type = models.CharField(choices=types, default="跑步", max_length=32)
#
#     class Meta:
#         verbose_name = "视频课程"
#         verbose_name_plural = "视频课程"

class Test(models.Model):
    name = models.CharField(max_length=128)
    headImg = models.FileField(upload_to='./uploads')  # 文件名

    def __str__(self):
        return self.name
