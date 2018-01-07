package sensor.steve.pers.smartphonesimple;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.ContactsContract;

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

//                    System.out.print("acc:");
//                    for (float v : sensorEvent.values) {
//                        System.out.print("," + v);
//                    }
//                    System.out.println();
//                    break;

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

//                    System.out.print("mag");
//                    for (float v : sensorEvent.values) {
//                        System.out.print("," + v);
//                    }
//                    System.out.println();
                    break;

                default:
                    System.out.println(sensorEvent.toString());
                    break;
            }
            if(counter>=3){
                imuDataElement.setTime_stamp(((double)sensorEvent.timestamp)/1000.0);
                DataEvent<IMUDataElement> imuDataElementDataEvent = new DataEvent<>(this);
                imuDataElementDataEvent.setSensorData(imuDataElement);

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
