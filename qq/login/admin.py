from django.contrib import admin

from login.models import MyUser, Test,Friend
from . import models


# Register your models here.


class MyUserAdmin(admin.ModelAdmin):
    # fields = ['pub_date', 'question_text']
    # fieldsets = [
    #     (None, {'fields': ['question_text']}),
    #     ('Date information', {'fields': ['pub_date'], 'classes': ['collapse']}),
    # ]
    # list_display = ('name', 'sex', 'c_time', 'headImg')

    search_fields = ['name']


admin.site.register(MyUser, MyUserAdmin)
admin.site.register(Test)
admin.site.register(Friend)

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
