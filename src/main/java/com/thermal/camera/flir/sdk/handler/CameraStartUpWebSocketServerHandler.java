package com.thermal.camera.flir.sdk.handler;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.flir.thermalsdk.live.CommunicationInterface;
import com.flir.thermalsdk.live.discovery.DiscoveryFactory;
import com.thermal.camera.flir.sdk.thermal.camera.CameraDiscovery;
import com.thermal.camera.flir.sdk.thermal.camera.CameraHandler;


public class CameraStartUpWebSocketServerHandler implements WebSocketHandler {
	
	private Map<WebSocketSession, Timer> timerMap = new ConcurrentHashMap<>();

	@Autowired
	CameraHandler cameraHandler;
	
	@Autowired
	DiscoveryFactory discoveryFactory;
	
	@Autowired
	CameraDiscovery cameraDiscovery;


	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		new Thread(()->{
			discoveryFactory.scan(cameraDiscovery, CommunicationInterface.NETWORK);
		}).start();


	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		System.out.println(String.format("message: %s", message));
		System.out.println(String.format("session: %s", session));
		
		if(message.getPayload().toString().equals("startRecieve")) {
			startSendingImages(session);
		}else if(message.getPayload().toString().equals("stopRecieve")){
			closeSendingImages(session);
		}

	}

	private void closeSendingImages(WebSocketSession session) {
		Timer imageTimer = timerMap.get(session);
		if (imageTimer != null) {
		    imageTimer.cancel();
		    imageTimer = null;
		}
	}



	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		timerMap.keySet().forEach(v -> closeSendingImages(v));
			
		cameraHandler.disconnect();
		discoveryFactory.stop();

		System.out.println(String.format("connected closed: %s", closeStatus));

	}

	@Override
	public boolean supportsPartialMessages() {
		// TODO Auto-generated method stub
		return false;
	}


	private void startSendingImages(WebSocketSession session) {
		Timer imageTimer = new Timer();

		imageTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					session.sendMessage(new BinaryMessage(CameraHandler.cameraPhotoQueue.peek()));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}, 0, 20);

		timerMap.put(session, imageTimer);

	}
}
