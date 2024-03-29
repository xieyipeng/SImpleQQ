# Generated by Django 2.2.2 on 2019-07-05 17:58

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('login', '0007_auto_20190704_1740'),
    ]

    operations = [
        migrations.CreateModel(
            name='Love',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('clicker', models.CharField(max_length=255, verbose_name='点击人')),
                ('clicked', models.CharField(max_length=255, verbose_name='被点击人')),
                ('c_time', models.DateTimeField(auto_now_add=True, verbose_name='创建时间')),
            ],
            options={
                'unique_together': {('clicker', 'clicked', 'c_time')},
            },
        ),
    ]
