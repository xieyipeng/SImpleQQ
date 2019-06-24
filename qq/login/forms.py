from django import forms


class UploadFileForm(forms.Form):
    title = forms.CharField(max_length=50)
    file = forms.FileField()


# class VediosForm(forms.Form):
#     # form的定义和model类的定义很像
#     type = forms.CharField(label="视频类型")
#     headImg = forms.FileField(label="文件路径")
