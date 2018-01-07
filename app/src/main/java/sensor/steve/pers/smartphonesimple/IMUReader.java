package sensor.steve.pers.smartphonesimple;

/**
 * Created by steve on 18-1-7.
 */

public class IMUReader extends SensorAbstract {

    public IMUReader() {

    }


    /**
     * Start a sensor and add listener for basic output
     *
     * @param listener
     * @return
     */
    @Override
    public boolean startSensor(SensorDataListener listener) {
        return false;
    }

    /**
     * Stop a sensor and remove the listener from HashSet
     *
     * @return
     */
    @Override
    public boolean stopSensor() {
        return false;
    }
}
