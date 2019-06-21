# SSM
软件大型实验周
*django*

# [Django 使用步骤](http://www.liujiangblog.com/course/django/84)
## 1、虚拟环境创建工程
![](http://liujiangblog.com/static/images/course/103-3.png)
* 注意venv这个虚拟环境目录，以及我们额外创建的templats目录
## 2、创建APP
* **检查环境：**
pycharm下方terminal 输入：`where python` 和 `python -V`
```java
(venv) E:\SSM\LoginAndRegistration>where python
E:\SSM\LoginAndRegistration\venv\Scripts\python.exe
D:\python\python.exe

(venv) E:\SSM\LoginAndRegistration>python -V
Python 3.6.8
```

* **创建login这个app：** terminal 输入：`python manage.py startapp login`
## 3、设置时区、语言
* **项目settings文件中：**
```java
# Internationalization
# https://docs.djangoproject.com/en/1.11/topics/i18n/

LANGUAGE_CODE = 'en-us'

TIME_ZONE = 'UTC'

USE_I18N = True

USE_L10N = True

USE_TZ = True
```
改为：
```java
# Internationalization
# https://docs.djangoproject.com/en/1.11/topics/i18n/

LANGUAGE_CODE = 'zh-hans'     # 这里修改了

TIME_ZONE = 'Asia/Shanghai'    # 这里修改了

USE_I18N = True

USE_L10N = True

USE_TZ = False    # 这里修改了
```
## 4、启动开发服务器
* 在Pycharm的Run/Debug Configurations配置界面里，将HOST设置为127.0.0.1，Port保持原样的8000，确定后,运行。

![](http://liujiangblog.com/static/images/course/103-7.png)

## 5、设计数据模型
* 数据库模型设计（model）
* 设置数据库后端
```java
# TODO: 连接数据库
import pymysql

pymysql.install_as_MySQLdb()

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.mysql',
        'NAME': 'login',
        'HOST': '127.0.0.1',
        'USER': 'root',
        'PASSWORD': '270030',
        'PORT': '3306',
    }
}
```
* 注册app
```java
# Application definition

INSTALLED_APPS = [
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    'login',
]
```
* 创建记录和数据表

1、创建数据库 - login

2、修改`E:\SSM\LoginAndRegistration\venv\Lib\site-packages\django\db\backends\mysql`目录下`base.py`和`operations.py`两个文件

base.py：注释掉两行
```python
if version < (1, 3, 13):
    raise ImproperlyConfigured('mysqlclient 1.3.13 or newer is required; you have %s.' % Database.__version__)
```

operations.py：修改decode为encode
```python
    def last_executed_query(self, cursor, sql, params):
        # With MySQLdb, cursor objects have an (undocumented) "_executed"
        # attribute where the exact query sent to the database is saved.
        # See MySQLdb/cursors.py in the source distribution.
        query = getattr(cursor, '_executed', None)
        if query is not None:
            query = query.encode(errors='replace')
        return query
```

3、app中的models建立好了后，并不会自动地在数据库中生成相应的数据表，需要你手动创建。进入Pycharm的terminal终端，执行下面的命令：
```java
python manage.py makemigrations
python manage.py migrate
```