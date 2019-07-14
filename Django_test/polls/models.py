from django.db import models
from django.utils import timezone
import datetime

"""
    # 创建一个新的question对象
    # Django推荐使用timezone.now()代替python内置的datetime.datetime.now()
    # 这个timezone就来自于Django的依赖库pytz
    from django.utils import timezone
    >>> q = Question(question_text="What's new?", pub_date=timezone.now())
"""


# Create your models here.
class Question(models.Model):
    question_text = models.CharField(max_length=200)
    pub_date = models.DateTimeField('data published')

    def __str__(self):
        return self.question_text

    def was_published_recently(self):
        now = timezone.now()
        return now - datetime.timedelta(days=1) <= self.pub_date <= now

    was_published_recently.admin_order_field = 'pub_date'
    was_published_recently.boolean = True
    was_published_recently.short_description = 'Published recently?'


class Choice(models.Model):
    question = models.ForeignKey(Question, on_delete=models.CASCADE)
    choice_text = models.CharField(max_length=200)
    votes = models.IntegerField(default=0)

    def __str__(self):
        return self.choice_text


class MyModel(models.Model):
    # 文件被传至`MEDIA_ROOT/uploads`目录，MEDIA_ROOT由你在settings文件中设置
    upload = models.FileField(upload_to='uploads/')
    # 或者
    # 被传到`MEDIA_ROOT/uploads/2015/01/30`目录，增加了一个时间划分
    upload = models.FileField(upload_to='uploads/%Y/%m/%d/')


def user_directory_path(instance, filename):
    # 文件上传到MEDIA_ROOT/user_<id>/<filename>目录中
    return 'user_{0}/{1}'.format(instance.user.id, filename)


class MyModel(models.Model):
    upload = models.FileField(upload_to=user_directory_path)
