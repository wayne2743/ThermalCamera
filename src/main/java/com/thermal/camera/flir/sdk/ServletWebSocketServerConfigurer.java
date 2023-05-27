package com.thermal.camera.flir.sdk;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.thermal.camera.flir.sdk.handler.CameraImageReciverSocketServerHandler;
import com.thermal.camera.flir.sdk.handler.CameraStartUpWebSocketServerHandler;

@Configuration
@EnableWebSocket
public class ServletWebSocketServerConfigurer implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

		registry.addHandler(cameraStartUpWebSocketServerHandler(), "/websocket").setAllowedOrigins("*")
				.addInterceptors(cameraStartUpWebSocketHandshakeInterceptor());

	}

	@Bean
	public CameraStartUpWebSocketServerHandler cameraStartUpWebSocketServerHandler() {
		return new CameraStartUpWebSocketServerHandler();
	}

	public CameraImageReciverSocketServerHandler cameraImageReciverSocketServerHandler() {
		return new CameraImageReciverSocketServerHandler();
	}

	@Bean
	public CmaeraStartUpWebSocketHandshakeInterceptor cameraStartUpWebSocketHandshakeInterceptor() {
		return new CmaeraStartUpWebSocketHandshakeInterceptor();
	}

	public class CmaeraStartUpWebSocketHandshakeInterceptor implements HandshakeInterceptor {

		@Override
		public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
				WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
			System.out.println("Before handshake");

			return true;
		}

		@Override
		public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
				Exception exception) {
			System.out.println("After handshake");

		}

	}

//	public class PrefixUrlPathHelper extends UrlPathHelper {
//
//		@Override
//		public String resolveAndCacheLookupPath(HttpServletRequest request) {
//
//            String path = super.resolveAndCacheLookupPath(request);
//            System.out.println(String.format("path: %s", path));
//			return path;
//		}
//		
//	}

}
