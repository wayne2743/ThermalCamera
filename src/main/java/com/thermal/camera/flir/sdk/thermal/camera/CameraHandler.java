package com.thermal.camera.flir.sdk.thermal.camera;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;

import com.flir.thermalsdk.image.ThermalImage;
import com.flir.thermalsdk.live.Camera;
import com.flir.thermalsdk.live.ConnectParameters;
import com.flir.thermalsdk.live.Identity;
import com.flir.thermalsdk.live.connectivity.ConnectionStatusListener;
import com.flir.thermalsdk.live.discovery.DiscoveryFactory;
import com.flir.thermalsdk.live.streaming.ThermalImageStreamListener;

public class CameraHandler {
	
	public static ArrayBlockingQueue<byte[]> cameraPhotoQueue = new ArrayBlockingQueue<>(10);
	
	@Autowired
	DiscoveryFactory discoveryFactory;
	
	private static final String TAG = "Camera handler";

    // Connected FLIR Camera
    private Camera camera;

//    public interface StreamDataListener {
//        void receiveImages(FrameDataHolder dataHolder);
//    }


    /**
     * Possible discovery statuses
     */
    public interface DiscoveryStatus {
        void started();

        void stopped();
    }

    /**
     * Empty constructor for now
     */
    public CameraHandler() {
    }


    /**
     * Connect to a FLIR One camera
     *
     * @param identity                 Identity of a discovered FLIR camera
     * @param connectionStatusListener Event listener
     * @throws IOException 
     */
    public Camera connect(Identity identity, ConnectionStatusListener connectionStatusListener) throws IOException {
        camera = new Camera();
        camera.connect(identity, connectionStatusListener, new ConnectParameters());
        return camera;
    }

    /**
     * Disconnect a FLIR One camera
     */
    public void disconnect() {
        if (camera == null) {
            return;
        }
        if (camera.isGrabbing()) {
            camera.unsubscribeAllStreams();
        }
        camera.disconnect();
        camera = null;
    }

    /**
     * Called when a new image is available (non-UI-thread)
     */
    private final ThermalImageStreamListener thermalImageStreamListener = new ThermalImageStreamListener() {
        @Override
        public void onImageReceived() {
            camera.withImage(this, handleIncomingImage);
        }
    };

    /**
     * Subscribes to camera stream
     */
    public void startStream() {
        camera.subscribeStream(thermalImageStreamListener);
    }

    /**
     * For processing receiveImages and updating UI
     */
    private final Camera.Consumer<ThermalImage> handleIncomingImage = new Camera.Consumer<ThermalImage>() {

        @Override
        public void accept(ThermalImage thermalImage) {
        	
            thermalImage.getImage().with(v-> outputToPhoto(thermalImage.getWidth(), thermalImage.getHeight(), v));
//            thermalImage.getFusion().getPhoto().with(v->outputToPhoto(thermalImage.getFusion().getPhotoWidth(), thermalImage.getFusion().getPhotoHeight(), v));
//            Fusion fusion = thermalImage.getFusion();
//            fusion.setFusionMode(FusionMode.THERMAL_FUSION);
//            System.out.println(String.format("fussion mode : %s", fusion.getCurrentFusionMode()));
//            FileUtils.saveToFile("C:\\Users\\Wego Lin\\eclipse-workspace\\ThermalCamera\\tmp\\test.jpg", thermalImage.getImage().getPixelBuffer());
//            Bitmap firBitmap = BitmapAndroid.createBitmap(thermalImage.getImage()).getBitMap();
//
//            // Get a bitmap with the visual image
//            Bitmap rgbBitmap = BitmapAndroid.createBitmap(Objects.requireNonNull(thermalImage.getFusion().getPhoto())).getBitMap();
//
//            // Add receiveImages to cache
//            streamDataListener.receiveImages(new FrameDataHolder(rgbBitmap, firBitmap));
        }

		private void outputToPhoto(int width, int height, ByteBuffer v) {

			byte[] rgbBytes = new byte[v.remaining()];
			v.get(rgbBytes, 0, rgbBytes.length);
			// Create a new BufferedImage with RGB888 format
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			
			// Set the pixel data from the RGB888 byte array
			byte[] data = ((java.awt.image.DataBufferByte) image.getRaster().getDataBuffer()).getData();
			System.arraycopy(rgbBytes, 0, data, 0, rgbBytes.length);
			
//			System.out.println(String.format("cameraPhotoQueue size: %s", cameraPhotoQueue.size()));
			
			try {
				if(cameraPhotoQueue.remainingCapacity() == 0) {
					cameraPhotoQueue.poll();
				}
				cameraPhotoQueue.offer(toByteArray(image, "jpg"));
	            Thread.sleep(20);
			} catch (IOException | InterruptedException e) {
				System.out.println(String.format("error: %s", e));
			}
			
			// Data export
//			String outputPath = "C:\\Users\\Wego Lin\\eclipse-workspace\\ThermalCamera\\tmp\\"+UUID.randomUUID()+".jpg"; // Specify the output file path
//			File output = new File(outputPath);
//			try {
//			    ImageIO.write(image, "jpg", output);
////			    System.out.println("Image saved successfully.");
//			} catch (IOException e) {
//			    e.printStackTrace();
//			}

		}
		
	    // convert BufferedImage to byte[]
	    public byte[] toByteArray(BufferedImage bi, String format)
	        throws IOException {

	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(bi, format, baos);
	        byte[] bytes = baos.toByteArray();
	        return bytes;

	    }
    };

    
    
}
