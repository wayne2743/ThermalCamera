package com.thermal.camera.flir.sdk.thermal.camera;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import com.flir.thermalsdk.ErrorCode;
import com.flir.thermalsdk.image.ImageBuffer;
import com.flir.thermalsdk.image.Renderer;
import com.flir.thermalsdk.image.ThermalImage;
import com.flir.thermalsdk.image.ThermalValue;
import com.flir.thermalsdk.live.Camera;
import com.flir.thermalsdk.live.CommunicationInterface;
import com.flir.thermalsdk.live.ConnectParameters;
import com.flir.thermalsdk.live.Identity;
import com.flir.thermalsdk.live.connectivity.ConnectionStatusListener;
import com.flir.thermalsdk.live.connectivity.Connector;
import com.flir.thermalsdk.live.connectivity.ConnectorFactory;
import com.flir.thermalsdk.live.connectivity.VisualChannelListener;
import com.flir.thermalsdk.live.discovery.DiscoveredCamera;
import com.flir.thermalsdk.live.discovery.DiscoveryEventListener;
import com.flir.thermalsdk.live.remote.OnReceived;
import com.flir.thermalsdk.live.remote.OnRemoteError;
import com.flir.thermalsdk.live.remote.StoredImage;
import com.flir.thermalsdk.live.streaming.Stream;
import com.flir.thermalsdk.live.streaming.ThermalStreamer;
import com.flir.thermalsdk.utils.Pair;

public class CameraDiscovery implements DiscoveryEventListener{
	
	
	@Autowired
	CameraHandler cameraHandler;

	
	@Override
	public void onCameraFound(DiscoveredCamera discoveredCamera) {
		Identity identity = discoveredCamera.getIdentity();
 		System.out.println(identity);
		System.out.println(discoveredCamera.getCameraDetails());
		

		try {
			cameraHandler.connect(identity, new MyConnectionStatusListener());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		camera.glWithThermalImage(v->{
//			System.out.println(v.getImage());
//		});
		cameraHandler.startStream();
		
//		cameraHandler.disconnect();
//		
//		// Camera
//		System.out.println("test");
//    	Camera camera = new Camera();
//    	try {
//        	System.out.println(String.format("Is camera connected: %s", camera.isConnected()));
//			camera.connect(identity, new MyConnectionStatusListener(), new ConnectParameters(10000));
//			camera.authenticate(identity, "testCamera1", 10000);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	
//    	System.out.println(String.format("Is camera connected: %s", camera.isConnected()));
    	
//    	// Remote Control
//    	RemoteControl cameraRemote = camera.getRemoteControl();
//    	Command<StoredImage> command = cameraRemote.getStorage().snapshot();
//    	
//    	System.out.println(String.format("Is command avaliable: %s", command.isAvailable()));
//    	command.execute(new MyOnRecieved(), new MyOnRemoteError());
//    	System.out.print(camera.getRemoteControl().getStorage().lastStoredImage().getSync());
////    	System.out.println(storedImage.visualImage);
//    	
//    	for(int i=0; i<5; i++) {
//        	// thermal range
//        	List<Pair<ThermalValue, ThermalValue>> tempRange = cameraRemote.getTemperatureRange().ranges().getSync();
//        	System.out.println(tempRange);        		
//    	}
    	
//    	cameraRemote.getTemperatureRange().ranges().subscribe(new ThermalRange());
    	
    	
//    	for(int i=0; i<10; i++) {
//        	System.out.println("print temperature ranges");    	
//        	
//        	System.out.println();    		
//    	}
//    	
//    	camera.getStreams()
//    	.forEach(v->System.out.println(String.format("Is streaming: %s, Is thermal: %s", v.isStreaming(), v.isThermal())));
//
//    	List<ThermalStreamer> thermalStreamers = camera.getStreams().stream().filter(Stream::isThermal).map(v->{
//    		try (ThermalStreamer thermalStreamer = new ThermalStreamer(v)) {
//    			 v.start(
//    				     onReceivedResult -> {
//    				         // notify UI or render thread about new frame - don't call thermalStreamer.update() directly on the stream thread
//    				         updateUiOnSeparateThread(thermalStreamer);
//    				     },
//    				     error -> {
//    				         // handle streaming error
//    				    	 System.out.println("Streaming Error");
//    				     });
//    			
//				return thermalStreamer;
//			}
//    	})
//    	.collect(Collectors.toList());
    	
//    	for(ThermalStreamer thermalStreamer : thermalImages) {
//    		System.out.println("in thermal");
//    		System.out.println(thermalStreamer.getImage());
//    	}
    	
//    	
//    	for(Runnable each :lisArrayBlockingQueue) {
//        	  		
//    	}
//    	
//    	while (!lisArrayBlockingQueue.isEmpty()) {
//        	lisArrayBlockingQueue.poll().run();  			
//		}
    	
//    	
//    	thermalImageRunnable.run();
//    	List<ImageBuffer> imageBuffers = thermalStreamers.stream().map(v->v.getImage()).collect(Collectors.toList());
//    	
//    	System.out.println("Result:" + imageBuffers);
//    	
//    	
//    	
//    	List<ImageBuffer> imageBuffers = camera.getStreams().stream()
//    		.map(v-> new ThermalStreamer(v))
//    		.map(v-> v.getImage()).collect(Collectors.toList());
    	
//    	System.out.print(teStreams);
    	
//    	// Connectivity
//    	ConnectorFactory connectorFactory = ConnectorFactory.getInstance();
//    	Connector connector = connectorFactory.create(CommunicationInterface.NETWORK);
//    	
//    	ErrorCode errorCode = connector.connect(identity);
//    	System.out.println(errorCode.getMessage());

    	
	}

	
	
//	private void updateUiOnSeparateThread(ThermalStreamer thermalStreamer) {
//	      // you can also run most of the processing on a background thread and run only the UI-related code on the main/UI thread
//			
//	      System.out.println("in updateUiOnSeparateThread: ");
////	      System.out.println("in updateUiOnSeparateThread: " + thermalStreamer.getImage());
////	      thermalImages.add(thermalStreamer);
//	      
//		  Runnable thermalImageRunnable = () -> {
//		        // update the streamer data with the new frame
//		        thermalStreamer.update();
//		        // for rendering purposes we can read out the colorized pixels easily
//		        ImageBuffer imageBuffer = thermalStreamer.getImage();
//		        System.out.print("ImageBuffer:" + imageBuffer);
//	//		        android.graphics.Bitmap androidBmp = BitmapAndroid.createBitmap(imageBuffer).getBitMap();
//		        // you can use androidBmp instance to render to ImageView for example
//	//		        mImageView.setImageBitmap(androidBmp); // UI thread required
//	
//		        // if you want to access ThermalImage object, use the ThermalStreamer#withThermalImage() helper
//		        thermalStreamer.withThermalImage(thermalImage -> {
//		            // here you can safely read/modify different ThermalImage parameters, like palette, fusion mode, color distribution, measurements, etc.
//		            // for example you can read out the measurements data and draw them on the UI thread on top of the androidBmp as an overlay
//		            // show the current palette name on the UI
//	//		            mTextView.setText(thermalImage.getPalette().name); // UI thread required
//		      	  System.out.print(thermalImage.getPalette().name);
//		        });
//		    };
//		    
//		   lisArrayBlockingQueue.add(thermalImageRunnable);
//	 }



//	private void runOnUiThread(ThermalStreamer thermalStreamer) {
//
//	}



	@Override
	public void onDiscoveryError(CommunicationInterface arg0, ErrorCode arg1) {
		System.out.print("MyDiscovery: onDiscoveryError");
		
	}

	@Override
	public void onCameraLost(Identity identity) {
		// TODO Auto-generated method stub
		DiscoveryEventListener.super.onCameraLost(identity);
		System.out.print("Test" + identity);
	}

	@Override
	public void onDiscoveryFinished(CommunicationInterface communicationInterface) {
		// TODO Auto-generated method stub
		DiscoveryEventListener.super.onDiscoveryFinished(communicationInterface);
		
		System.out.print("Test" + communicationInterface);
	}





	

}

class ThermalRange implements OnReceived<ArrayList<Pair<ThermalValue, ThermalValue>>> {

	@Override
	public void onReceived(ArrayList<Pair<ThermalValue, ThermalValue>> input) {
		System.out.println("ThermalRange: OnReceived");
		System.out.println(input);
		
	}
	
}


class MyRender implements Renderer {

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ImageBuffer getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
}


class MyConnectionStatusListener implements ConnectionStatusListener {

	@Override
	public void onDisconnected(@Nullable ErrorCode arg0) {
		System.out.print("MyConnectionStatusListener: onDisconnected");
		
	}
	
}


class MyConnectorCreator implements ConnectorFactory.ConnectorCreator {

	@Override
	public Connector create() {
		// TODO Auto-generated method stub
		return null;
	}
	
}


class MyVisualChannelListener implements VisualChannelListener {

	@Override
	public void onVisualDataReceived(int width, int height, int yRowStride, int yPixelStride, ByteBuffer arg4, int arg5, int arg6,
			ByteBuffer arg7, int arg8, int arg9, ByteBuffer arg10, long arg11) {
		System.out.print( String.format("In onVisualDataReceived, width: %s, hieght", width, height));
		
	}
	
}



class MyOnRecieved implements OnReceived<StoredImage> {

	@Override
	public void onReceived(StoredImage storedImage) {

//		storedImage.thermalImage.getLocation();
		System.out.println(storedImage.thermalImage);
		System.out.println(storedImage.visualImage);
		
	}
	
}

class MyOnRemoteError implements OnRemoteError {

	@Override
	public void onRemoteError(ErrorCode arg0) {
		System.out.print(String.format("onRemoteError: test"));
		
	}
	
}