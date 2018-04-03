package sensor.steve.pers.smartphonesimple;


import java.util.EventObject;

/**
 * Generic class for events of data which is processed received.
 *
 * @param <T> DataElement
 */
public class DataEvent<T extends SensorDataElement> extends EventObject {


    protected T sensorData; // Sensor data saved here.


    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public DataEvent(Object source) {
        super(source);
    }

    public T getSensorData() {
        return sensorData;
    }


    public void setSensorData(T sensorData) {
        this.sensorData = sensorData;
    }
}
