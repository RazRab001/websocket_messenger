let currentUser = null;
let selectedChatId = null;
let stompClient = null;
let chatSubscription = null; // Храним текущую подписку

// Инициализация чата
document.addEventListener('DOMContentLoaded', () => {
    console.log("Start chat init");

    const user = JSON.parse(localStorage.getItem('currentUser'));
    if (!user || !user.id) {
        console.error("Current user is not properly set.");
        window.location.href = 'login.html';
    } else {
        initializeChat(user);
    }
});

// Инициализация чата с загрузкой списка чатов
function initializeChat(user) {
    console.log("Start chat init");
    currentUser = user;

    if (!currentUser || !currentUser.id) {
        console.error("Current user ID is missing.");
        return;
    }

    document.getElementById('user-login').innerText = `Logged in as ${currentUser.login}`;
    loadChats();
    connectWebSocket();
}

// WebSocket подключение
function connectWebSocket() {
    const socket = new SockJS('http://localhost:8080/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
        console.log('Connected to WebSocket');
        
        // Подписываемся на все чаты, которые уже выбраны
        if (selectedChatId) {
            subscribeToChat(selectedChatId);
        }
    }, (error) => {
        console.error('WebSocket connection error:', error);
    });
}

// Подписка на чат по его ID
function subscribeToChat(chatId) {
    if (chatSubscription) {
        chatSubscription.unsubscribe();
    }

    chatSubscription = stompClient.subscribe(`/topic/chat/${chatId}`, (message) => {
        console.log("New message received:", message.body);
        const receivedMessage = JSON.parse(message.body);
        displayMessage(receivedMessage);
    });

    console.log(`Subscribed to /topic/chat/${chatId}`);
}

// Загрузка списка чатов
async function loadChats() {
    try {
        const response = await fetch(`http://localhost:8080/api/v1/chat/of/${currentUser.id}`);
        if (!response.ok) {
            console.error('Failed to load chats:', response.statusText);
            return;
        }

        const chats = await response.json();
        const chatsContainer = document.getElementById('chats');
        chatsContainer.innerHTML = '';

        chats.forEach(chat => {
            const chatElement = document.createElement('div');
            chatElement.className = 'chat-item';
            chatElement.innerText = `Chat with ${chat.members.map(member => member.login).join(', ')}`;
            chatElement.addEventListener('click', () => {
                selectChat(chat.id);
            });
            chatsContainer.appendChild(chatElement);
        });
    } catch (error) {
        console.error('Error loading chats:', error);
    }
}

// Выбор чата и загрузка сообщений
async function selectChat(chatId) {
    selectedChatId = chatId;

    try {
        const response = await fetch(`http://localhost:8080/api/v1/chat/${chatId}`);
        if (!response.ok) {
            console.error('Failed to load chat:', response.statusText);
            return;
        }

        const chat = await response.json();
        const messagesContainer = document.getElementById('messages');
        messagesContainer.innerHTML = '';

        chat.messages.forEach(message => {
            displayMessage(message);
        });

        // Подписываемся на новые сообщения
        subscribeToChat(chatId);
    } catch (error) {
        console.error('Error selecting chat:', error);
    }
}

// Создание нового чата
document.getElementById('create-chat-btn').addEventListener('click', async () => {
    const newMember = document.getElementById('new-member').value;
    if (newMember) {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/chat`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    memberIds: [newMember],
                    creatorId: currentUser.id
                })
            });

            if (response.ok) {
                loadChats();
                document.getElementById('new-member').value = '';
            } else {
                alert('Error creating chat');
            }
        } catch (error) {
            console.error('Error creating chat:', error);
            alert('Error creating chat');
        }
    }
});

// Отправка сообщения через WebSocket
document.getElementById('send-message-btn').addEventListener('click', () => {
    const messageContent = document.getElementById('message-input').value;

    if (!selectedChatId) {
        alert('Please select a chat first.');
        return;
    }

    if (messageContent && stompClient) {
        const message = {
            senderId: currentUser.id,
            chatId: selectedChatId,
            text: messageContent
        };

        stompClient.send(`/app/chat.sendMessage`, {}, JSON.stringify(message));
        document.getElementById('message-input').value = '';
    }
});

async function getUserById(userId) {
    const response = await fetch(`http://localhost:8080/api/v1/user/id/${currentUser.id}/${userId}`);
        if (!response.ok) {
            console.error('Failed to load user:', response.statusText);
            return 'Unknown User';
        }
        const user = await response.json();
        return user.login;
}

// Отображение сообщений
async function displayMessage(message) {
    const messagesContainer = document.getElementById('messages');
    //console.log(message)
    const senderName = await getUserById(message.sender);

    const messageElement = document.createElement('li');
    messageElement.className = 'message-item';
    messageElement.innerHTML = `<strong>${senderName}:</strong> ${message.text}`;
    messagesContainer.appendChild(messageElement);

    // Прокрутка к последнему сообщению
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
}