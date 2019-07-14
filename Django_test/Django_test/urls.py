"""Django_test URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/2.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import include, path

"""
    path()方法：
    路由系统中最重要的path()方法可以接收4个参数，其中2个是必须的：route和view，以及2个可选的参数：kwargs和name。
    route：
    route 是一个匹配 URL 的准则（类似正则表达式）。当 Django 响应一个请求时，它会从 urlpatterns 的第一项开始，
按顺序依次匹配列表中的项，直到找到匹配的项，然后执行该条目映射的视图函数或下级路由，其后的条目将不再继续匹配。
因此，url路由的编写顺序非常重要！
需要注意的是，route不会匹配 GET 和 POST 参数或域名。例如，URLconf 在处理请求 https://www.example.com/myapp/时，
它会尝试匹配 myapp/。处理请求 https://www.example.com/myapp/?page=3 时，也只会尝试匹配 myapp/。
    view：
    view指的是处理当前url请求的视图函数。当Django匹配到某个路由条目时，自动将封装的HttpRequest对象作为第一个参数，被
“捕获”的参数以关键字参数的形式，传递给该条目指定的视图view。
    kwargs：
任意数量的关键字参数可以作为一个字典传递给目标视图。
    name：
    对你的URL进行命名，让你能够在Django的任意处，尤其是模板内显式地引用它。这是一个非常强大的功能，相当于给URL取了个
全局变量名，不会将url匹配地址写死。
"""

urlpatterns = [
    path('polls/', include('polls.urls')),
    path('admin/', admin.site.urls),    # 在实际环境中，为了站点的安全性，我们一般不能将管理后台的url随便暴露给他人，不能用/admin/这么简单的路径

]
