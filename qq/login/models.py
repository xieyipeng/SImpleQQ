from django.contrib.auth.models import AbstractUser
from django.db import models


# Create your models here.

def head_image_path(MyUser, filename):
    return './uploads/user_{0}/{1}'.format(MyUser.username, filename)


class MyUser(AbstractUser):
    username = models.CharField('用户名', max_length=150, primary_key=True)
    headImg = models.FileField('头像', upload_to=head_image_path, default='./uploads/default/headImg.jpg')
    isOnline = models.BooleanField('是否在线', default=False)

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


def dynamic_upload_path(Dynamic, filename):
    return './uploads/user_{0}/{1}'.format(Dynamic.username, filename)


class Dynamic(models.Model):
    username = models.CharField('用户名', max_length=150)
    c_time = models.DateTimeField('创建时间', auto_now_add=True)
    context = models.CharField('内容', max_length=254)
    img = models.FileField(upload_to=dynamic_upload_path)

    class Meta:
        unique_together = ('username', 'c_time')


class Love(models.Model):
    clicker = models.CharField('点击人', max_length=255)
    clicked = models.CharField('被点击人', max_length=255)
    c_time = models.DateTimeField('创建时间', auto_now_add=True)

    class Meta:
        unique_together = ('clicker', 'clicked', 'c_time')
