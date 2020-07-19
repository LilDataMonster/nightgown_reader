import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import tinyb.BluetoothDevice;
import tinyb.BluetoothGattCharacteristic;
import tinyb.BluetoothGattDescriptor;
import tinyb.BluetoothGattService;
import tinyb.BluetoothManager;
import tinyb.BluetoothNotification;


//class ANotification implements BluetoothNotification<Map<Short, byte[]>> {
//
//	@Override
//	public void run(Map<Short, byte[]> arg0) {
//		System.out.println("output: " + arg0);
//	}
//
//}
//
//class BNotification implements BluetoothNotification<Map<String, byte[]>> {
//
//	@Override
//	public void run(Map<String, byte[]> arg0) {
//		System.out.println("output: " + arg0);
//	}
//
//}

public class NightgownReader {

	public static void main(String[] args) {
        BluetoothManager manager = BluetoothManager.getBluetoothManager();
        boolean discoveryStarted = manager.startDiscovery();

        System.out.println("The discovery started: " + (discoveryStarted ? "true" : "false"));
//        String device = "D8:A0:1D:5C:AA:96";
        String device = "24:6F:28:02:B0:3A";
        BluetoothDevice sensor = null;
        
        boolean once = true;
        while(true) {
	        try {
				sensor = getDevice(device);
			} catch (InterruptedException e) {
				e.printStackTrace();
	        	continue;
			}
	
	        if (sensor == null) {
	            System.err.println("No sensor found with the provided address.");
	            continue;
	        }
	        
	        if(once) {
		        System.out.print("Found device: ");
		        printDevice(sensor);
		        once = false;
	        }
	        
	        Map<Short, byte[]> mnData = sensor.getManufacturerData();
	        for(Map.Entry<Short, byte[]> sh : mnData.entrySet()) {
	        	Short key = sh.getKey();
	        	String keyHex = Integer.toHexString(key & 0xffff).toUpperCase();
//	        	System.out.println("Key: " + keyHex + ", Value: " + 
//	        			String.format("%X", ByteBuffer.wrap(sh.getValue()).getShort()));
	        	
	        	short values = ByteBuffer.wrap(sh.getValue()).getShort();
	        	int temperature = (values & 0xFF00) >> 8;
	        	int humidity = (values & 0xFF);
	        	System.out.println("Temperature: " + temperature);
	        	System.out.println("Humidity: " + humidity);
	        }
	        
	        try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
	}
	
    static boolean running = true;
    /*
     * After discovery is started, new devices will be detected. We can get a list of all devices through the manager's
     * getDevices method. We can the look through the list of devices to find the device with the MAC which we provided
     * as a parameter. We continue looking until we find it, or we try 15 times (1 minutes).
     */
    static BluetoothDevice getDevice(String address) throws InterruptedException {
        BluetoothManager manager = BluetoothManager.getBluetoothManager();
        BluetoothDevice sensor = null;
        for (int i = 0; (i < 15) && running; ++i) {
            List<BluetoothDevice> list = manager.getDevices();
            if (list == null)
                return null;

            for (BluetoothDevice device : list) {
//                printDevice(device);
                /*
                 * Here we check if the address matches.
                 */
                if (device.getAddress().equals(address))
                    sensor = device;
            }

            if (sensor != null) {
                return sensor;
            }
            Thread.sleep(4000);
        }
        return null;
    }

    static void printDevice(BluetoothDevice device) {
        
        System.out.println("sensor name: " + device.getName());
        for(String s : device.getUUIDs()) {
        	System.out.println("UUID: " + s);
        }
//        device.enableManufacturerDataNotifications(new ANotification());
//        device.enableServiceDataNotifications(new BNotification());

        System.out.println("Size: " + device.getManufacturerData().size() + ", Data: " + device.getManufacturerData());
        System.out.println("Size: " + device.getServiceData().size() + ", Data: " + device.getServiceData());
        
        System.out.println("Address: " + device.getAddress());
        System.out.println("Alias: " + device.getAlias());
        System.out.println("Adapter: " + device.getAdapter());
        System.out.println("Appearance: " + device.getAppearance());
        System.out.println("Blocked: " + device.getBlocked());
        System.out.println("BluetoothClass: " + device.getBluetoothClass());
        System.out.println("BluetoothType: " + device.getBluetoothType());
        System.out.println("Connected: " + device.getConnected());
        System.out.println("Icon: " + device.getIcon());
        System.out.println("LegacyPairing: " + device.getLegacyPairing());
        System.out.println("ManufacturerData: " + device.getManufacturerData());
        System.out.println("Modalias: " + device.getModalias());
        System.out.println("Name: " + device.getName());
        System.out.println("Paired: " + device.getPaired());
        System.out.println("RSSI: " + device.getRSSI());
        System.out.println("ServiceData: " + device.getServiceData());
        System.out.println("Services: " + device.getServices());
        System.out.println("ServicesResolved: " + device.getServicesResolved());
        System.out.println("Trusted: " + device.getTrusted());
        System.out.println("TxPower: " + device.getTxPower());
        System.out.println("UUIDs: " + Arrays.toString(device.getUUIDs()));

        for(BluetoothGattService service : device.getServices()) {
        	System.out.println("\tBluetoothType: " + service.getBluetoothType());
        	System.out.println("\tPrimary: " + service.getPrimary());
        	System.out.println("\tUUID: " + service.getUUID());
        	System.out.println("\tCharacteristics: " + service.getCharacteristics());
    		for(BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
				System.out.println("\t\tBluetoothType: " + characteristic.getBluetoothType());
    			System.out.println("\t\tUUID: " + characteristic.getUUID());
//    			System.out.println("\t\tValue: " + characteristic.readValue());
    			System.out.println("\t\tNotifying: " + characteristic.getNotifying());
    			System.out.println("\t\tDescriptors: " + characteristic.getDescriptors());
    			for(BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
					System.out.println("\t\t\tBluetoothType: " + descriptor.getBluetoothType());
    				System.out.println("\t\t\tUUID:" + descriptor.getUUID());
//					System.out.println("\t\t\tValue:" + descriptor.getValue());
    			}
    		}
        }
    }
	
}
