import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

class WebSocketService {
    constructor() {
        this.client = new Client({
            webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
            onConnect: this.onConnect,
            onStompError: this.onError,
        });
    }

    onConnect = () => {
        console.log('Connected to WebSocket');
    };

    onError = (error) => {
        console.error('Error with WebSocket', error);
    };

    connect = () => {
        this.client.activate();
    };

    disconnect = () => {
        this.client.deactivate();
    };

    subscribeToPrivateChat = (user, callback) => {
        this.client.subscribe(`/user/${user}/queue/messages`, (message) => {
            callback(JSON.parse(message.body));
        });
    };

    sendMessage = (chatMessage) => {
        this.client.publish({
            destination: '/app/chat.send',
            body: JSON.stringify(chatMessage),
        });
    };
}

export default new WebSocketService();
