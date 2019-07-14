# chat/routing.py
from django.conf.urls import url
from django.urls import path

from . import consumers

websocket_urlpatterns = [
    # url(r'^ws/chat/(?P<room_name>[^/]+)/$', consumers.ChatConsumer),
    path('ws/chat/<str:room_name>/', consumers.ChatConsumer),
]
