class WebSocketClient {
	constructor(url) {
		this.socket = new WebSocket(url);

		this.socket.onopen = this.onOpen.bind(this);
		this.socket.onmessage = this.onMessage.bind(this);
		this.socket.onclose = this.onClose.bind(this);
		this.socket.onerror = this.onError.bind(this);
	}

	onOpen(event) {
		console.log('WebSocket 连接已打开');
	}

	onMessage(event) {
		console.log('收到消息:', event);
		if (event.data instanceof Blob) {
			const reader = new FileReader();
			reader.onload = function(e) {
				const imageContainer = document.getElementById('imageContainer');
				imageContainer.src = e.target.result;
			};
			reader.readAsDataURL(event.data);
		}
	}

	onClose(event) {
		console.log('WebSocket 连接已关闭');
	}

	onError(error) {
		console.error('WebSocket 错误:', error);
	}

	send(message) {
		this.socket.send(message);
	}

	stop() {
		this.socket.close();
	}
	
	status() {
		return this.socket.readyState;
	}

	sendMessageToServer(message) {
		if (this.socket.readyState === WebSocket.OPEN) {
			this.socket.send(message);
		} else {
			console.log('WebSocket connection is not open. Message not sent.');
		}
	}
}

// 创建 WebSocketClient 对象并与服务器建立连接
let cameraSocket = new WebSocketClient('ws://localhost:8081/websocket');




const sendButton = document.getElementById('connectToCamera');
console.log(sendButton);
sendButton.addEventListener('click', function() {
	console.log("socket ready state :" + cameraSocket.status() + ": " + WebSocket.CLOSED);
	if (cameraSocket.status() === WebSocket.CLOSED) {
		cameraSocket = new WebSocketClient('ws://localhost:8081/websocket');
	}
});

const stopButton = document.getElementById("disconnectCamera");
stopButton.addEventListener('click', function() {
	console.log("socket ready state :" + cameraSocket.status());
	cameraSocket.stop();

});


const startReceiveImages = document.getElementById('startReceiveImages');
console.log(startReceiveImages);
startReceiveImages.addEventListener('click', function() {
	console.log("socket ready state :" + cameraSocket.status() + ": " + WebSocket.CLOSED);
	cameraSocket.sendMessageToServer('startRecieve');
});

const stopReceiveImages = document.getElementById("stopReceiveImages");
stopReceiveImages.addEventListener('click', function() {
	console.log("socket ready state :" + cameraSocket.status());
	cameraSocket.sendMessageToServer('stopRecieve');

});
