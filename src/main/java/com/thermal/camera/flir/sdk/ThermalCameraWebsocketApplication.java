package com.thermal.camera.flir.sdk;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.flir.thermalsdk.javasdk.ThermalSdkJava;
import com.flir.thermalsdk.live.discovery.DiscoveryFactory;
import com.thermal.camera.flir.sdk.thermal.camera.CameraHandler;
import com.thermal.camera.flir.sdk.thermal.camera.CameraDiscovery;

@SpringBootApplication
public class ThermalCameraWebsocketApplication {

	public static void main(String[] args) {

		SpringApplication.run(ThermalCameraWebsocketApplication.class, args);
		
	}
	
	@Bean
	public CameraHandler cameraHandler() {
		return new CameraHandler();
	}
	
	
	@Bean
	public CameraDiscovery myDiscovery() {
		return new CameraDiscovery();
	}
	
	@Bean
	public DiscoveryFactory discoveryFactory() {
		ThermalSdkJava.init(new File("C:\\Users\\Wego Lin\\eclipse-workspace\\ThermalCamera\\tmp2"));
		return DiscoveryFactory.getInstance();
	}
}
