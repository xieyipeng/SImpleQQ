from django.contrib.auth.hashers import check_password
from django.shortcuts import render, redirect

from login import forms
from login.models import MyUser, Dynamic, Friend, Love


def index(request):
    users = MyUser.objects.all()
    dynamics = Dynamic.objects.all()
    friends = Friend.objects.all()
    loves = Love.objects.all()
    return render(request, 'login/index.html', {
        'users': users,
        'dynamics': dynamics,
        'friends': friends,
        'loves': loves,
    })


def login(request):
    if request.session.get('is_login', None):  # 不允许重复登录
        return redirect('/index/')
    if request.method == 'POST':
        login_form = forms.UserForm(request.POST)
        message = '请检查填写的内容！'
        if login_form.is_valid():
            username = login_form.cleaned_data.get('username')
            password = login_form.cleaned_data.get('password')

            try:
                user = MyUser.objects.get(username=username)
            except:
                message = '用户不存在！'
                return render(request, 'login/login.html', locals())

            if not user.is_staff:
                message = '非职员！'
                return render(request, 'login/login.html', locals())

            if check_password(password, user.password):
                request.session['is_login'] = True
                request.session['user_name'] = user.username
                return redirect('/index/')
            else:
                message = '密码不正确！'
                return render(request, 'login/login.html', locals())
        else:
            return render(request, 'login/login.html', locals())

    login_form = forms.UserForm()
    return render(request, 'login/login.html', locals())


def register(request):
    print('nihao')
    if request.session.get('is_login', None):
        return redirect('/index/')
    if request.method == 'POST':
        register_form = forms.RegisterForm(request.POST)
        message = "请检查填写的内容！"
        if register_form.is_valid():
            username = register_form.cleaned_data.get('username')
            password1 = register_form.cleaned_data.get('password1')
            password2 = register_form.cleaned_data.get('password2')

            if password1 != password2:
                message = '两次输入的密码不同！'
                return render(request, 'login/register.html', locals())
            else:
                same_name_user = MyUser.objects.filter(username=username)
                if same_name_user:
                    message = '用户名已经存在'
                    return render(request, 'login/register.html', locals())
                new_user = MyUser()
                new_user.username = username
                new_user.password = password1
                new_user.save()
                return redirect('/login/')
        else:
            return render(request, 'login/register.html', locals())
    register_form = forms.RegisterForm()
    return render(request, 'login/register.html', locals())


def logout(request):
    if not request.session.get('is_login', None):
        # 如果本来就未登录，也就没有登出一说
        return redirect("/login/")
    request.session.flush()
    # 或者使用下面的方法
    # del request.session['is_login']
    # del request.session['user_id']
    # del request.session['user_name']
    return redirect("/login/")
