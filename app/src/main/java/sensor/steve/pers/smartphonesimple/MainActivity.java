package sensor.steve.pers.smartphonesimple;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    //--- ble
    TextView bleResText = (TextView) findViewById(R.id.bleResText);
    Button bleButton = (Button) findViewById(R.id.bleButton);

    //-- imu
    Spinner sensorSpeedSpinner=(Spinner) findViewById(R.id.sensorSpeed);
    TextView sensorResultText = (TextView) findViewById(R.id.sensorResultText);
    Button imuButton = (Button) findViewById(R.id.imuButton);


    //-- tcp
    EditText ipText = (EditText) findViewById(R.id.ipText);
    EditText portText = (EditText) findViewById(R.id.portText);
    Button tcpButton = (Button) findViewById(R.id.tcpButton);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
