package sensor.steve.pers.smartphonesimple;

import android.Manifest;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.EventListener;

public class MainActivity extends AppCompatActivity {

    //--- ble
    TextView bleResText;
    StringBuffer bleSB = new StringBuffer();
    Handler bleHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            bleResText.append(msg.)
            synchronized (this) {

                bleResText.setText(bleSB.substring(0));
            }

        }
    };
    Button bleButton;
    boolean bleRunningFlag = false;
    BLEReader bleReader;

    //-- imu
    Spinner sensorSpeedSpinner;
    TextView sensorResultText;
    StringBuffer imuSB = new StringBuffer();
    Handler imuHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            synchronized (this) {

                sensorResultText.setText(imuSB.substring(0));
            }

        }
    };

    Button imuButton;
    boolean imuRunningFlag = false;
    IMUReader imuReader;


    //-- tcp
    EditText ipText;
    EditText portText;
    Button tcpButton;
    boolean tcpRunningFlag = false;
    Socket socket = null;
    SensorDataListener<IMUDataElement> imuTCPListener;
    SensorDataListener<WirelessDataElement> bleTCPListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO: BLE
        bleResText = (TextView) findViewById(R.id.bleResText);
        bleButton = (Button) findViewById(R.id.bleButton);
        //// Getting permission...
        String[] needed_permission = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                Manifest.permission.ACCESS_COARSE_LOCATION

        };
        boolean permission_ok = true;
        for (String permission : needed_permission) {
            if (ContextCompat.checkSelfPermission(this,
                    permission) != PackageManager.PERMISSION_GRANTED) {
                permission_ok = false;
            }
        }
        if (!permission_ok) {
            ActivityCompat.requestPermissions(
                    this,
                    needed_permission,
                    1
            );
        }

        bleReader = new BLEReader((BluetoothManager)
                getSystemService(Context.BLUETOOTH_SERVICE));

        bleButton.setOnClickListener(new Button.OnClickListener() {
            int counter = 0;
            int maxCounter = 5;

            @Override
            public void onClick(View view) {
                if (bleRunningFlag) {
                    // is running.
                    bleReader.stopSensor();


                    bleRunningFlag = false;
                    bleButton.setText("START");

                } else {
                    // isn't running
                    bleReader.startSensor(new SensorDataListener() {
                        @Override
                        public void SensorDataEvent(DataEvent event) throws InterruptedException {

                            if (bleSB.length() > 0) {

                                bleSB.delete(0, bleSB.length() - 1);
                            }
                            bleSB.append(event.getSensorData().convertDatatoString());
                            bleHandler.sendEmptyMessage(0);

                        }
                    });


                    bleRunningFlag = true;
                    bleButton.setText("STOP");
                }

            }
        });

        ///TODO: IMU
        sensorSpeedSpinner = (Spinner) findViewById(R.id.sensorSpeed);
        sensorResultText = (TextView) findViewById(R.id.sensorResultText);
        imuButton = (Button) findViewById(R.id.imuButton);

        imuReader = new IMUReader((SensorManager) getSystemService(SENSOR_SERVICE));

        imuButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imuRunningFlag) {
                    // is running
                    imuReader.stopSensor();

                    imuRunningFlag = false;
                    imuButton.setText("START");
                } else {
                    // isn't running
                    imuReader.startSensor(new SensorDataListener() {
                        @Override
                        public void SensorDataEvent(DataEvent event) throws InterruptedException {
//                            System.out.print(event.getSensorData().convertDatatoString());
                            if (imuSB.length() > 0) {
                                imuSB.delete(0, imuSB.length() - 1);
                            }
                            imuSB.append(event.getSensorData().convertDatatoString());
                            imuHandler.sendEmptyMessage(0);

                        }
                    });


                    imuRunningFlag = true;
                    imuButton.setText("STOP");
                }
            }
        });


        //TODO: tcp
        ipText = (EditText) findViewById(R.id.ipText);
        portText = (EditText) findViewById(R.id.portText);
        tcpButton = (Button) findViewById(R.id.tcpButton);
        tcpButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tcpRunningFlag) {
                    // is running
                    try {


                        imuReader.removeDataListener(imuTCPListener);
                        bleReader.removeDataListener(bleTCPListener);
                        imuTCPListener = null;
                        bleTCPListener = null;

                        if (socket != null) {

                            socket.close();
                            socket = null;
                        }
                        tcpRunningFlag = false;
                        tcpButton.setText("TCPSTART");


                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else {
                    // isn't running
                    new Thread(() -> {
                        try {
                            if (socket == null) {
                                socket = new Socket(ipText.getText().toString(), Integer.valueOf(portText.getText().toString()));
                                socket.setKeepAlive(true);
                                if (socket.isConnected()) {
                                    System.out.println("socket connected");
                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }).start();


                    imuTCPListener = new SensorDataListener<IMUDataElement>() {

                        BufferedWriter writer = null;// new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                        @Override
                        public void SensorDataEvent(DataEvent<IMUDataElement> event) throws InterruptedException {
                            try {
                                if (socket == null) {
                                    return;
                                }
                                if (writer == null) {

                                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                                }
                                if (socket.isConnected()) {
                                    writer.write(event.getSensorData().convertDatatoString());
                                    writer.flush();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    bleTCPListener = new SensorDataListener<WirelessDataElement>() {

                        BufferedWriter writer = null;// new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                        @Override
                        public void SensorDataEvent(DataEvent<WirelessDataElement> event) throws InterruptedException {
                            try {
                                if (socket == null) {
                                    return;
                                }
                                if (writer == null) {

                                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                                }
                                if (socket.isConnected()) {
                                    writer.write(event.getSensorData().convertDatatoString());
                                    writer.flush();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    imuReader.addDataListener(imuTCPListener);
                    bleReader.addDataListener(bleTCPListener);


                    tcpRunningFlag = true;
                    tcpButton.setText("TCPSTOP");


                }
            }
        });
    }


}
