from django.contrib import admin

from login.models import MyUser, Test, Friend, Dynamic, Love
from . import models


# Register your models here.


class MyUserAdmin(admin.ModelAdmin):
    # fields = ['username', 'isOnline', 'last_login', 'date_joined', 'headImg']
    # fieldsets = [
    #     (None, {'fields': ['question_text']}),
    #     ('Date information', {'fields': ['pub_date'], 'classes': ['collapse']}),
    # ]
    list_display = ('username', 'isOnline', 'is_staff', 'is_active', 'last_login', 'date_joined', 'headImg')
    search_fields = ['name']


class DynamicAdmin(admin.ModelAdmin):
    # fields = ['id', 'username', 'c_time', 'context', 'img']
    list_display = ('id', 'username', 'c_time', 'context', 'img')


admin.site.register(MyUser, MyUserAdmin)
admin.site.register(Test)
admin.site.register(Friend)
admin.site.register(Dynamic, DynamicAdmin)
admin.site.register(Love)

# class QuestionAdmin(admin.ModelAdmin):
#     # fields = ['pub_date', 'question_text']
#     fieldsets = [
#         (None, {'fields': ['question_text']}),
#         ('Date information', {'fields': ['pub_date'], 'classes': ['collapse']}),
#     ]
#     inlines = [ChoiceInline]
#     list_display = ('question_text', 'pub_date', 'was_published_recently')
#
#     list_filter = ['pub_date']
#     search_fields = ['question_text']
#
#
# admin.site.register(Question, QuestionAdmin)
