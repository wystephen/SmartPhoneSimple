package sensor.steve.pers.smartphonesimple;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorEvent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by steve on 18-1-7.
 */

public class BLEReader extends SensorAbstract {


    protected BluetoothManager bluetoothManager;
    protected BluetoothAdapter bluetoothAdapter;
    protected BluetoothLeScanner leScanner;

    ScanSettings.Builder scanSettingsBuilder;

    public ScanCallback scanCallback;

    public BLEReader(BluetoothManager bluetoothManager) {
        super("BLE");

        this.bluetoothManager = bluetoothManager;
        bluetoothAdapter = this.bluetoothManager.getAdapter();
        leScanner = bluetoothAdapter.getBluetoothLeScanner();

        scanSettingsBuilder = new ScanSettings.Builder();
        scanSettingsBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);

        System.out.print(System.currentTimeMillis());

    }


    /**
     * Start a sensor and add listener for basic output
     *
     * @param listener
     * @return
     */
    @Override
    public boolean startSensor(SensorDataListener listener) {
        tmpSensorListern = listener;
        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                BluetoothDevice device = result.getDevice();
                int rssi = result.getRssi();
//                        int txPower = result.getTxPower();


                DataEvent<WirelessDataElement> dataEvent = new DataEvent<>(this);
                WirelessDataElement wirelessDataElement = new WirelessDataElement();
                dataEvent.setSensorData(wirelessDataElement);

                double[] m = new double[2];
                m[0] = (double) rssi;
                m[1] = 0;


                dataEvent.getSensorData().setTime_stamp(((double) System.currentTimeMillis()) / 1000.0);
                dataEvent.getSensorData().addMeasurement(device.getAddress(), m);
                notifyListeners(dataEvent);

            }
        };
        addDataListener(tmpSensorListern);
        leScanner.startScan(null,
                scanSettingsBuilder.build(), scanCallback);
        return true;
    }

    /**
     * Stop a sensor and remove the listener from HashSet
     *
     * @return
     */
    @Override
    public boolean stopSensor() {
//        leScanner.stopScan();
        leScanner.stopScan(scanCallback);
        removeDataListener(tmpSensorListern);

        return true;
    }


}
