'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;
var credentials = null;
var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    username = document.querySelector('#username').value.trim();
    credentials = document.querySelector('#credentials').value.trim();

    if(username) {
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.debug = null;
        stompClient.connect({}, onConnected, onError);
    }
    /*event.preventDefault();*/
}

function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify(
        {
         sender: username,
         type: 'JOIN',
         credentials: credentials
        })
    )

    connectingElement.classList.add('hidden');
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            credentials: credentials,
            content: messageInput.value,
            type: 'CHAT'
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function banUser(event) {
    if(stompClient) {
        var credentials = $( "#userToBeBannedSelect option:selected" ).text();
        var username = $( "#userToBeBannedSelect option:selected" ).val();
        var banLength = $( "#banLengthInput").val();
        var banMessage = {
            content: username + ',' + banLength + ',' + credentials,
            type: 'BAN_REQUEST'
        };
        stompClient.send("/app/chat.banUser", {}, JSON.stringify(banMessage));
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    if (message.type !== 'BANNED'){
        var messageElement = document.createElement('li');
        if(message.type === 'JOIN') {
            messageElement.classList.add('event-message');
            message.content = message.credentials + ' dołączył!';
        } else if (message.type === 'LEAVE') {
            messageElement.classList.add('event-message');
            message.content = message.credentials + ' wyszedł!';
        } else if (message.type === 'BAN_REQUEST'){
            messageElement.classList.add('event-message');
            // in ban request credentials and ban length is stored in content of message
            const content = message.content;
            const contentList = content.split(',');
            message.content = contentList[2] + ' został zbanowany na ' + contentList[1] + ' minut!';
        } else {
            createCredentialsSection(message, messageElement);
        }
        appendMessageContext(message, messageElement);
    } else {
        alert('Zostałeś tymczasowo zablokowany.')
    }
}

function createCredentialsSection(message, messageElement){
   messageElement.classList.add('chat-message');

   var avatarElement = document.createElement('i');
   var avatarText = document.createTextNode(message.credentials[0]);
   avatarElement.appendChild(avatarText);
   avatarElement.style['background-color'] = getAvatarColor(message.credentials);

   messageElement.appendChild(avatarElement);

   var credentialsElement = document.createElement('span');
   var credentialsText = document.createTextNode(message.credentials);
   credentialsElement.appendChild(credentialsText);
   messageElement.appendChild(credentialsElement);
}

function loadPreviousMessages(message){
   var messageElement = document.createElement('li');
   createCredentialsSection(message, messageElement);
   appendMessageContext(message, messageElement);
}

function appendMessageContext(message,messageElement){
   var textElement = document.createElement('p');
   var messageText = document.createTextNode(message.content);
   textElement.appendChild(messageText);

   messageElement.appendChild(textElement);

   messageArea.appendChild(messageElement);
   messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

function displayBanModal(){
    $('.banModal').modal('show');
}

$(document).ready(connect);
messageForm.addEventListener('submit', sendMessage, true);
banForm.addEventListener('submit', banUser, true );

/*
$(document).ready(function() {
    $(".ban_icon-container").hover(
        function () {
            $(".banIcon").removeAttribute('hidden');
        },
        function () {
           $(".banIcon").setAttribute('hidden','');
        }
    );
});*/
