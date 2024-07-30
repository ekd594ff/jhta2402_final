import React, { useEffect, useState } from 'react';
import WebSocketService from './WebSocketService';

const ChatComponent = ({ currentUser, receiver }) => {
    const [messages, setMessages] = useState([]);
    const [messageContent, setMessageContent] = useState('');

    useEffect(() => {
        WebSocketService.connect();

        WebSocketService.subscribeToPrivateChat(currentUser, (message) => {
            setMessages((prevMessages) => [...prevMessages, message]);
        });

        return () => {
            WebSocketService.disconnect();
        };
    }, [currentUser]);

    const sendMessage = () => {
        const chatMessage = {
            sender: currentUser,
            receiver: receiver,
            content: messageContent,
            timestamp: new Date(),
        };
        WebSocketService.sendMessage(chatMessage);
        setMessages((prevMessages) => [...prevMessages, chatMessage]);
        setMessageContent('');
    };

    return (
        <div>
            <div>
                {messages.map((msg, index) => (
                    <div key={index}>
                        <strong>{msg.sender}:</strong> {msg.content}
                    </div>
                ))}
            </div>
            <div>
                <input
                    type="text"
                    value={messageContent}
                    onChange={(e) => setMessageContent(e.target.value)}
                />
                <button onClick={sendMessage}>Send</button>
            </div>
        </div>
    );
};

export default ChatComponent;
