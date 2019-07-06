# Generated by Django 2.2.2 on 2019-07-02 11:17

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('login', '0005_myuser_isonline'),
    ]

    operations = [
        migrations.CreateModel(
            name='Dynamic',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('username', models.CharField(max_length=150)),
                ('c_time', models.DateTimeField(auto_now_add=True)),
                ('context', models.CharField(max_length=254)),
                ('img', models.FileField(upload_to='./uploads/<django.db.models.fields.CharField>/')),
            ],
            options={
                'unique_together': {('username', 'c_time')},
            },
        ),
    ]