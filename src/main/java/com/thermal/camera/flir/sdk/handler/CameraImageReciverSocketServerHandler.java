package com.thermal.camera.flir.sdk.handler;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.thermal.camera.flir.sdk.thermal.camera.CameraHandler;


public class CameraImageReciverSocketServerHandler implements WebSocketHandler{
	

	private Map<WebSocketSession, Timer> timerMap = new ConcurrentHashMap<>();
	
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		startSendingImages(session);
		
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
			session.sendMessage(new TextMessage(session.getId()));
			session.sendMessage(new BinaryMessage(CameraHandler.cameraPhotoQueue.peek()));
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		System.out.println(String.format("handleTransportError: %s", exception));
		
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		System.out.println(String.format("connected closed: %s", closeStatus));
		timerMap.remove(session);
		
	}

	@Override
	public boolean supportsPartialMessages() {
		System.out.println(String.format("supportsPartialMessages"));
		return false;
	}

	
	private void startSendingImages(WebSocketSession session) {
		Timer imageTimer = new Timer();
//		File dir = new File("C:\\Users\\Wego Lin\\eclipse-workspace\\ThermalCamera\\tmp");
//		Queue<File> queue = new LinkedList<>(Arrays.asList(dir.listFiles()));

		imageTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
//					File current = queue.poll();
//					System.out.println(current);
//					System.out.println(session.isOpen());
//					System.out.println(cameraHandler.getCameraPhotoQueue().peek());
					session.sendMessage(new BinaryMessage(CameraHandler.cameraPhotoQueue.peek()));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}, 0, 20);

		timerMap.put(session, imageTimer);

	}
	
	public Map<WebSocketSession, Timer> getTimerMap() {
		return timerMap;
	}

	public void setTimerMap(Map<WebSocketSession, Timer> timerMap) {
		this.timerMap = timerMap;
	}
}
