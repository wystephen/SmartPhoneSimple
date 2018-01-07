package sensor.steve.pers.smartphonesimple;

import android.Manifest;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
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

public class MainActivity extends AppCompatActivity {

    //--- ble
    TextView bleResText;
    StringBuilder bleSB = new StringBuilder();
    Handler bleHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            bleResText.append(msg.)
            bleResText.setText(bleSB.substring(0));
        }
    };
    Button bleButton;
    boolean bleRunningFlag = false;

    BLEReader bleReader;

    //-- imu
    Spinner sensorSpeedSpinner;
    TextView sensorResultText;
    StringBuilder imuSB = new StringBuilder();
    Handler imuHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            sensorResultText.setText(imuSB.substring(0));

        }
    };

    Button imuButton;
    boolean imuRunningFlag = false;




    //-- tcp
    EditText ipText;
    EditText portText;
    Button tcpButton;
    boolean tcpRunningFlag = false;


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
                            System.out.println(event.getSensorData().convertDatatoString());
//                            bleResText.append(event.getSensorData().convertDatatoString());
//                            bleResText.
//                            Toast.makeText(getApplicationContext(),
//                                    event.getSensorData().convertDatatoString(),Toast.LENGTH_LONG).show();

                            if(bleSB.length()>0){

                                bleSB.delete(0,bleSB.length()-1);
                            }
                            bleSB.append(event.getSensorData().convertDatatoString());
//                            if(bleSB.length()>100){
//                                bleSB.delete(0,bleSB.indexOf("\n"));
//                            }
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


        imuButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imuRunningFlag) {
                    // is running


                    imuRunningFlag = false;
                    imuButton.setText("START");
                } else {
                    // isn't running


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

                    tcpRunningFlag = false;
                    tcpButton.setText("START");
                } else {
                    // isn't running

                    tcpRunningFlag = true;
                    tcpButton.setText("STOP");
                }
            }
        });
    }


}
