from django.contrib.auth.models import AbstractUser
from django.db import models


# Create your models here.

class MyUser(AbstractUser):
    username = models.CharField(max_length=150, primary_key=True)
    headImg = models.FileField(upload_to='./uploads', default='./uploads/default/headImg.jpg')
    isOnline = models.BooleanField(default=False)

    def __str__(self):
        return self.username


class Test(models.Model):
    name = models.CharField(max_length=128)
    headImg = models.FileField(upload_to='./uploads')  # 文件名

    def __str__(self):
        return self.name


class Friend(models.Model):
    friend_one = models.ForeignKey(
        "MyUser",
        on_delete=models.CASCADE,
        related_name='me'
    )

    friend_other = models.ForeignKey(
        "MyUser",
        on_delete=models.CASCADE,
        related_name='other'
    )

    def __str__(self):
        return self.friend_one.username + ' - ' + self.friend_other.username
