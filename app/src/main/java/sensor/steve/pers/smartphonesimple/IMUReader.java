package sensor.steve.pers.smartphonesimple;

import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import static java.lang.Math.abs;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;

/**
 * Created by steve on 18-1-7.
 */

public class IMUReader extends SensorAbstract implements SensorEventListener {

    SensorManager sm;

    public IMUReader(SensorManager sm) {
        this.sm = sm;

    }


    /**
     * Start a sensor and add listener for basic output
     *
     * @param listener
     * @return
     */
    @Override
    public boolean startSensor(SensorDataListener listener) {

//        sm.registerListener(this,
//                sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                sm.SENSOR_DELAY_FASTEST);
        tmpSensorListern = listener;
        addDataListener(tmpSensorListern);


        sm.registerListener(this,
                sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                sm.SENSOR_DELAY_FASTEST);
        sm.registerListener(this,
                sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                sm.SENSOR_DELAY_FASTEST);
        sm.registerListener(this,
                sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                sm.SENSOR_DELAY_FASTEST);
        sm.registerListener(this,
                sm.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
                sm.SENSOR_DELAY_NORMAL, 1);
        sm.registerListener(this,
                sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                sm.SENSOR_DELAY_FASTEST,1);
        sm.registerListener(this,
                sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                sm.SENSOR_DELAY_FASTEST);
        return true;
    }

    /**
     * Stop a sensor and remove the listener from HashSet
     *
     * @return
     */
    @Override
    public boolean stopSensor() {
        sm.unregisterListener(this);
        removeDataListener(tmpSensorListern);
        return true;
    }

    IMUDataElement imuDataElement = null;
    int counter = 0;

    /**
     * Convert quaternion to euler angle.
     * @param euler euler angle represent by rad
     * @param w w in quaternion
     * @param x x in quaternion
     * @param y y in quaternion
     * @param z z in quaternion
     */
    public void QuaterniontoEuler(double[] euler,
                                  double w,double x,double y,double z){
        if(euler.length != 3){
            Log.d("Q:",
                    "size of array to save euler angle should be 3");
        }
        double sinr = 2.0 * (w * x + y * z);
        double cosr = 1.0 - 2.0 * (x * x + y * y);
        euler[0] = atan2(sinr,cosr); // euler angle of x-axis

        double sinp = 2.0 * (w * y - z * x);
        if(sinp > 1.0){
            sinp = 1.0;
        }
        if(sinp < -1.0){
            sinp = -1.0;
        }
        euler[1] = asin(sinp); // euler angle of y-axis

        double siny = 2.0 * (w * z + x * y);
        double cosy = 1.0 - 2.0 * (y * y + z * z);
        euler[2] = atan2(siny,cosy); // euler angle of z-axis

        return;

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        synchronized (this) {
            int sensorType = sensorEvent.sensor.getType();
            if (imuDataElement == null) {
                imuDataElement = new IMUDataElement();
                counter = 0;

            }
            double[] d = null;
            switch (sensorType) {
                case Sensor.TYPE_ALL:
//                    double[] d = new double[3];
//                    for (int i = 0; i < 3; ++i) {
//                        d[i] = sensorEvent.values[i];
//                    }
//                    imuDataElement.setAcc(d);


                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    d = new double[3];
                    for (int i = 0; i < 3; ++i) {
                        d[i] = sensorEvent.values[i];
                    }
                    imuDataElement.setAcc(d);
                    counter++;

                    break;

                case Sensor.TYPE_GYROSCOPE:
                    d = new double[3];
                    for (int i = 0; i < 3; ++i) {
                        d[i] = sensorEvent.values[i];
                    }
                    imuDataElement.setGyr(d);
                    counter++;

//                    System.out.print("gyr");
//                    for (float v : sensorEvent.values) {
//                        System.out.print("," + v);
//                    }
//                    System.out.println();
                    break;

                case Sensor.TYPE_MAGNETIC_FIELD:
                    d = new double[3];
                    for (int i = 0; i < 3; ++i) {
                        d[i] = sensorEvent.values[i];
                    }
                    imuDataElement.setMag(d);
                    counter++;
//                    Log.d("step","mag");
//                    System.out.print("mag");
//                    for (float v : sensorEvent.values) {
//                        System.out.print("," + v);
//                    }
//                    System.out.println();
                    break;
                case Sensor.TYPE_STEP_DETECTOR:
//                    System.out.print("step detected");
//                    Toast.makeText(get)

                    Log.d("step", "step detected");
                    break;

                case Sensor.TYPE_STEP_COUNTER:

                    Log.d("step", "COUNTER"+Integer.toString((int)sensorEvent.values[0]));
                    break;

                case Sensor.TYPE_ROTATION_VECTOR:
                    d = new double[3];
//                    double sin_half_theta = sqrt(1.0 - (sensorEvent.values[3]*sensorEvent.values[3]));

//                    d[0] = sensorEvent.values
//                    for(int i=0;i<3;++i){
//                        d[i] = sensorEvent.values[i]/sin_half_theta;
//                    }
                    QuaterniontoEuler(d,sensorEvent.values[3],
                            sensorEvent.values[0],
                            sensorEvent.values[1],
                            sensorEvent.values[2]);
                    imuDataElement.setAngle(d);

                    break;

                default:
                    System.out.println(sensorEvent.toString());
                    break;
            }
            if (counter >= 3) {
                imuDataElement.setTime_stamp(((double) System.currentTimeMillis()) / 1000.0);
                DataEvent<IMUDataElement> imuDataElementDataEvent = new DataEvent<>(this);
                imuDataElementDataEvent.setSensorData(imuDataElement);
                Log.d("IMUDATA",imuDataElement.convertDatatoString());

                notifyListeners(imuDataElementDataEvent);
                imuDataElement = null;

                counter = 0;
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
