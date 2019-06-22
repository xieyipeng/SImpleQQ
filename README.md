# SSM
软件大型实验周
*django*

# 一、[Django 使用步骤](http://www.liujiangblog.com/course/django/84)
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
4、创建管理员账户
```java
python manage.py createsuperuser
```

# 二、与android虚拟机的链接

## 1、服务端设置：
基本配置就不重复说了。

* 1、settings中注释掉一行：（具体原因未知：正常在android虚拟机中发送get请求完全正确，但是发送post请求出现错误，具体原因貌似是什么csrf的检查，加密啥的，自己也不明白，只知道注释掉之后，post请求发送的数据可以显示在pycharm的terminal里面了）
```java
MIDDLEWARE = [
    'django.middleware.security.SecurityMiddleware',
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    #'django.middleware.csrf.CsrfViewMiddleware',        # 这一行
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
]
```


* 2、创建模型类[`../myApp/models.py`]：
```python
class User(models.Model):
    gender = (
        ('male', '男'),
        ('female', '女')
    )

    name = models.CharField(max_length=128)
    sex = models.CharField(max_length=32, choices=gender, default='男')
    c_time = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.name

    class Meta:
        ordering = ["-c_time"]
        verbose_name = "用户"
        verbose_name_plural = "用户"
```
* 3、设置路由[`../qq/qq/urls.py`]：
```python
from django.contrib import admin
from django.urls import path

from login import views

urlpatterns = [
    path('admin/', admin.site.urls),
    path('get_test/', views.get_test),
    path('post_test/', views.posy_test),
]
```
* 4、逻辑[`../myApp/views.py`]：
```python
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
```


* 发送get请求：

Android模拟器下 `127.0.0.1` 或者`localhost`访问的是模拟器本机。

如果想要访问电脑，使用`10.0.2.2`，

`10.0.2.2` 是 Android 模拟器设置的特定 ip，是本机电脑的 alias - （别名）
```java
   /**
     * 发送get请求
     *
     * @param url   http://localhost:6144/Home/RequestString/
     * @param param key=123 & v=456
     * @return json数据包
     */
    public static String sendGetRequest(String url, String param) {
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        StringBuilder result = null;
        try {
            String urlString = url + "?" + param;
            URL realUrl = new URL(urlString);
            connection = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            connection.setRequestMethod("GET");
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                Log.e(TAG, "sendGetRequest: Get Request Successful");
                InputStream inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                result = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
            } else {
                Log.e(TAG, "sendGetRequest: Get Request Failed");
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "sendGetRequest: MU " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "sendGetRequest: IO " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }
```

* 发送post请求：
```java
    /**
     * 发送post请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return json数据包
     */
    public static String sendPostRequest(String url, String param) {
        PrintWriter printWriter = null;
        StringBuilder result = null;
        BufferedReader bufferedReader = null;
        try {
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();

            // TODO: 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // TODO: 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // TODO: 获取URLConnection对象对应的输出流
            printWriter = new PrintWriter(connection.getOutputStream());

            // TODO: 发送请求参数
            // TODO: flush输出流的缓冲
            printWriter.print(param);
            printWriter.flush();

            // TODO: 定义BufferedReader输入流来读取URL的响应
            result = new StringBuilder();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }
```