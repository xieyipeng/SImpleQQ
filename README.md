# 软件大型实验周
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
    path('get_test/', views.get_test),  # 响应get文本请求
    path('post_test/', views.posy_test),  # 响应post文本请求
    path('post_file_test/', views.upload_file)  # 响应get文件请求
]
```
* 4、逻辑[`../myApp/views.py`]：
```python
import json
import os

from django.http import HttpResponse
from login.models import User


def get_test(request):
    pass


def posy_test(request):
    pass


def upload_file(request):
    pass

```

**注意：Android模拟器下 `127.0.0.1` 或者`localhost`访问的是模拟器本机。**

**如果想要访问电脑，使用`10.0.2.2`，**

**`10.0.2.2` 是 Android 模拟器设置的特定 ip，是本机电脑的 alias - （别名）**

## 2、文本类型请求

* 发送get请求：


Django端[`../myApp/views.py`]：
```python
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
```


android端get请求：

```java
   /**
     * 发送get请求
     * 
     * @param url eg: http://10.0.2.2:8000/get_test/
     * @return 返回服务器的响应
     */
    public static String sendGetRequest(String url) {
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        StringBuilder result = null;
        try {
            URL realUrl = new URL(url);
            //打开链接
            connection = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestMethod("GET");
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                //若链接正常（ResponseCode == 200）
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

        } catch (IOException e) {
            Log.e(TAG, "sendGetRequest: error: " + e.getMessage());
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

android端点击事件：
```java
 getRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                  TODO: 子线程中访问网络
                 */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = "http://10.0.2.2:8000/get_test/";
                        final String get = GetPostUtil.sendGetRequest(url);

                        /*
                          TODO: 子线程中更新UI
                         */
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getTV.setText(get);
                            }
                        });
                    }
                }).start();
            }
        });
```



* 发送post请求：

Django端：
```python
def posy_test(request):
    if request.method == 'POST':
        print(request.POST)
        req = request.POST.get('v1')  # 获取post请求中的v1所对应的值
        print('post请求')
        print(req)
        return HttpResponse(req)  # 返回v1所对应的值 -- 只是用来测试代码，实际逻辑按自己要求来
    else:
        return HttpResponse("none")
```

android端post请求

```java
    /**
     * 发送post请求
     *
     * @param url   发送请求的 URL
     * @param data 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return json数据包
     */
    public static String sendPostRequest(String url, String data) {
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
            Log.e(TAG, "sendPostRequest: Post Request Successful");

            // TODO: 定义BufferedReader输入流来读取URL的响应
            result = new StringBuilder();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            Log.e(TAG, "sendPostRequest: " + e.getMessage());
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

android端点击事件：
```java
 postRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = "http://10.0.2.2:8000/post_test/";
                        String data = "v1=v&v2=v";
                        final String get = GetPostUtil.sendPostRequest(url, data);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                postTV.setText(get);
                            }
                        });
                    }
                }).start();
            }
        });
```

## 图片（文件）请求
* 发送post请求

Django端：
```python
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
```
android端post请求：
```java
    /**
     * 发送post请求上传图片
     * 
     * @param actionUrl url
     * @param inputStream 图片的流
     * @param fileName name
     * @return 服务器响应
     */
    public static String upLoadFiles(String actionUrl, InputStream inputStream, String fileName) {
        StringBuffer result = new StringBuffer();
        OutputStream outputStream = null;
        DataInputStream dataInputStream = null;
        try {
            final String newLine = "\r\n"; // 换行符
            final String boundaryPrefix = "--"; //边界前缀
            final String boundary = String.format("=========%s", System.currentTimeMillis()); // 定义数据分隔线
            // 连接
            URL url = new URL(actionUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            // 设置请求头参数
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Charsert", "UTF-8");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            // 获取输出流
            outputStream = new DataOutputStream(connection.getOutputStream());
            // 文件参数
            // 参数头设置完以后需要两个换行，然后才是参数内容
            String stringBuilder = boundaryPrefix +
                    boundary +
                    newLine +
                    "Content-Disposition: form-data;name=\"file\";filename=\"" + fileName + "\"" + newLine +
                    "Content-Type:application/octet-stream" +
                    newLine +
                    newLine;
            // 将参数头的数据写入到输出流中
            outputStream.write(stringBuilder.getBytes());
            // 数据输入流,用于读取文件数据
            dataInputStream = new DataInputStream(inputStream);
            byte[] bufferOut = new byte[1024];
            int bytes = 0;
            // 每次读1KB数据,并且将文件数据写入到输出流中
            while ((bytes = dataInputStream.read(bufferOut)) != -1) {
                outputStream.write(bufferOut, 0, bytes);
            }
            // 最后添加换行
            outputStream.write(newLine.getBytes());
            //关闭流
            inputStream.close();
            dataInputStream.close();
            // 定义最后数据分隔线，即--加上boundary再加上--。
            byte[] end_data = (newLine + boundaryPrefix + boundary + boundaryPrefix + newLine).getBytes();
            // 写上结尾标识
            outputStream.write(end_data);
            outputStream.flush();
            outputStream.close();
            // 定义BufferedReader输入流来读取服务器的响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            Log.e(TAG, "upLoadFiles: 响应： " + result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "uploadFiles: " + e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "upLoadFiles: " + e.getMessage());
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "upLoadFiles: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "upLoadFiles: " + e.getMessage());
                }
            }
        }
        return result.toString();
    }
```

android端点击事件：
```java
 postFileRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String fileName = "IMG.JPG";
                        // 获取工程目录下Assets目录里面的只读资源文件
                        // 也可获取相机里的文件流，同理
                        AssetManager assetManager = MainActivity.resources.getAssets();
                        try {
                            InputStream inputStream = assetManager.open(fileName);
                            final String get = GetPostUtil.upLoadFiles("http://10.0.2.2:8000/post_file_test/", inputStream, fileName);
                            /*
                             TODO: 子线程中更新UI
                             */
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    postFileTV.setText(get);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e(TAG, "run: " + e.getMessage());
                        }
                    }
                }).start();
            }
        });
```

* 获取图片资源（nginx）

道理同url获取图片资源，`eg：http://pic37.nipic.com/20140113/8800276_184927469000_2.png`，反向代理。

1、首先下载nginx

网址：http://nginx.org/en/download.html

选择 `stable version` -- `win10`

2、 安装

加压后直接双击根目录下nginx.exe即可运行

任务管理器中看到两个nginx的进程即可

3、 修改config

http包下的servcer包内，修改listen属性为81（防止别的资源抢占80端口），charset改为UTF-8，root改为自己的./upload目录：
```python
http {
    include       mime.types;
    default_type  application/octet-stream;

    sendfile        on;

    keepalive_timeout  65;

    #gzip  on;

    server {
        listen       80;
        server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            root   html;
            index  index.html index.htm;
        }

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }
```

修改后：
```python
http {
    include       mime.types;
    default_type  application/octet-stream;

    sendfile        on;

    keepalive_timeout  65;

    #gzip  on;

    server {
        listen       81;
        server_name  localhost;

        charset UTF-8;

        #access_log  logs/host.access.log  main;

        location / {
            root   "E:\\SSM\\qq\\uploads"; # 根据自己的情况而定
            index  index.html index.htm;
        }

        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }
```

4、 验证

直接在浏览器输入`127.0.0.1:81/img.jpg` 即可。(当然，你的./upload路径下要有该img文件)

5、android端访问

**注意**：用`10.0.2.2:81`访问

导入Glide依赖
```java
    implementation 'com.github.bumptech.glide:glide:4.5.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.5.0'
```

```java
    getFileRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://10.0.2.2:81/微信图片_20190609175306.jpg";
                Glide.with(context)
                        .load(url)
                        .into(imageView);
            }
        });
```