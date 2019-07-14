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
        print('receive: ' + text_data)
        text_data_json = json.loads(text_data)
        sender = ''
        send_to = ''
        message = text_data_json['message']
        room = text_data_json['room']
        try:
            sender = text_data_json['my_sender']
        except Exception as e:
            print(e.__doc__)

        try:
            send_to = text_data_json['send_to']
        except Exception as e:
            print(e.__doc__)

        print('to ' + send_to)

        print('my_sender ' + sender)
        if sender and send_to:
            print('sender is not none ---- qq')
            await self.channel_layer.group_send(
                self.room_group_name,
                {
                    'message': message,
                    'my_type': TYPE.MESSAGE_TYPE_QQ,
                    'room': room,
                    'send_to': send_to,
                    'my_sender': sender,
                    'type': 'chat_message_QQ',
                }
            )
        else:
            print('sender is None ---- ww')
            await self.channel_layer.group_send(
                self.room_group_name,
                {
                    'message': message,
                    'my_type': TYPE.MESSAGE_TYPE_WW,
                    'room': room,
                    'send_to': 's_u_o_y_o_u_r_e_n_++==',
                    'my_sender': 'w_a_n_g_y_e',
                    'type': 'chat_message_WW',
                }
            )

    # Receive message from room group
    # TODO: 对type的定义
    async def chat_message_WW(self, event):
        message = event['message']
        room = event['room']
        # Send message to WebSocket
        # 这个send 函数影响浏览器之间交换信息的显示与否

        await self.send(text_data=json.dumps({
            'message': message,
            'my_type': TYPE.MESSAGE_TYPE_WW,
            'room': room,
            'send_to': 's_u_o_y_o_u_r_e_n_++==',
            'my_sender': 'w_a_n_g_y_e_++==',
            'type': 'chat_message_WW',
        }))

    async def chat_message_QQ(self, event):
        print('qq event: ')
        message = event['message']
        room = event['room']
        send_to = event['send_to']
        sender = event['my_sender']
        # Send message to WebSocket
        # 这个send 函数影响浏览器之间交换信息的显示与否

        await self.send(text_data=json.dumps({
            'message': message,
            'my_type': TYPE.MESSAGE_TYPE_WW,
            'room': room,
            'send_to': send_to,
            'my_sender': sender,
            'type': 'chat_message_QQ',
        }))

    # async def chat_message(self, event):
    #     message = event['message']
    #     room = event['room']
    #     print('type: chat_message --    message: ' + message + ' room: ' + room)
    #     room = event['room']
    #     print('chat')
    #
    #     # Send message to WebSocket
    #     # 这个send 函数影响浏览器之间交换信息的显示与否
    #     await self.send(text_data=json.dumps({
    #         'type': 'chat_message',
    #         'my_type': TYPE.MESSAGE_TYPE_WQ_NO,
    #         'message': message,
    #         'room': room
    #     }))
