# chat/consumers.py
from channels.generic.websocket import AsyncWebsocketConsumer
import json


class TYPE:
    MESSAGE_TYPE_WW = 'web-web'
    MESSAGE_TYPE_WQ_NO = 'web-qq-no'
    MESSAGE_TYPE_WQ_OK = 'web-qq-ok'
    MESSAGE_TYPE_QW = 'qq-web'
    MESSAGE_TYPE_QQ = 'qq-qq'


class ChatConsumer(AsyncWebsocketConsumer):

    async def connect(self):
        self.room_name = self.scope['url_route']['kwargs']['room_name']
        self.room_group_name = 'chat_%s' % self.room_name

        # Join room group
        await self.channel_layer.group_add(
            self.room_group_name,
            self.channel_name
        )

        await self.accept()

    async def disconnect(self, close_code):
        # Leave room group
        await self.channel_layer.group_discard(
            self.room_group_name,
            self.channel_name
        )

    # Receive message from WebSocket
    async def receive(self, text_data):
        print('receive text_data: ' + text_data)
        text_data_json = json.loads(text_data)
        message = text_data_json['message']
        room = text_data_json['room']
        type = text_data_json['my_type']

        # Send message to room group
        # await self.channel_layer.group_send(
        #     self.room_group_name,
        #     {
        #         'type': 'chat_message_WW',
        #         'my_type': TYPE.MESSAGE_TYPE_WW,
        #         'message': message,
        #         'room': room
        #     }
        # )

        # TODO: 处理 web-web 端的数据交互
        if type==TYPE.MESSAGE_TYPE_WW:
            await self.channel_layer.group_send(
                self.room_group_name,
                {
                    'type': 'chat_message_WW',
                    'my_type': TYPE.MESSAGE_TYPE_WW,
                    'message': message,
                    'room': room
                }
            )

        # TODO: 处理 qq-web 端的数据交互
        if type==TYPE.MESSAGE_TYPE_QW:
            await self.channel_layer.group_send(
                self.room_group_name,
                {
                    'type': 'chat_message',
                    'my_type': TYPE.MESSAGE_TYPE_WW,
                    'message': message,
                    'room': room
                }
            )



    # Receive message from room group
    # TODO: 对type的定义
    async def chat_message(self, event):
        message = event['message']
        room = event['room']
        print('type: chat_message --    message: ' + message + ' room: ' + room)
        room = event['room']
        print('chat')

        # Send message to WebSocket
        # 这个send 函数影响浏览器之间交换信息的显示与否
        await self.send(text_data=json.dumps({
            'type': 'chat_message',
            'my_type': TYPE.MESSAGE_TYPE_WQ_NO,
            'message': message,
            'room': room
        }))

    async def chat_message_WW(self, event):
        message = event['message']
        room = event['room']
        print('type: chat_message --    message: ' + message + ' room: ' + room)
        room = event['room']
        print('chat ww')

        # Send message to WebSocket
        # 这个send 函数影响浏览器之间交换信息的显示与否
        await self.send(text_data=json.dumps({
            'type': 'chat_message',
            'my_type': TYPE.MESSAGE_TYPE_WQ_OK,
            'message': message,
            'room': room
        }))
