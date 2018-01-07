package sensor.steve.pers.smartphonesimple;

import java.util.EventListener;

/**
 * SensorDataListener
 * @param <T> DataElement type.
 */
public interface SensorDataListener<T extends SensorDataElement> extends EventListener{
    public void SensorDataEvent(DataEvent<T> event) throws InterruptedException;
}
