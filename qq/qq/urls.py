"""qq URL Configuration

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
from django.urls import path
from django.conf.urls import include

from login import views, views_login, views_web

urlpatterns = [
    path('admin/', admin.site.urls),
    # TODO: test
    path('get_test/', views.get_test),  # 响应get文本请求
    path('post_test/', views.post_test),  # 响应post文本请求
    path('post_file_test/', views.upload_file),  # 响应get文件请求
    path('test_file/', views.test_file),

    # TODO: chat
    path('', include('chat.urls')),

    # TODO: register
    path('user_register/', views_login.register),
    path('user_login/', views_login.user_login),
    path('user_logout/', views_login.user_logout),

    # TODO: 添加显示好友
    path('check_is_friend/', views_login.check_is_friend),
    path('add_friend/', views_login.add_friend),
    path('get_all_friend/', views_login.get_all_friend),
    path('get_all_friend_message/', views_login.get_all_friend_message),

    # TODO: 好友动态
    path('add_dynamic/', views_login.add_dynamic),
    path('get_all_dynamic/', views_login.get_all_dynamic),
    path('delete_dynamic/', views_login.delete_dynamic),


    # TODO：web
    path('index/', views_web.index),
    path('login/', views_web.login),
    path('register/', views_web.register),
    path('logout/', views_web.logout),

    # TODO: 修改信息
    path('change_head_image/', views_login.change_head_image),

]
