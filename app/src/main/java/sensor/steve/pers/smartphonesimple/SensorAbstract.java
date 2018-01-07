package sensor.steve.pers.smartphonesimple;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.SynchronousQueue;

/**
 * @param <DataElementType>
 * @param <DataInterface>
 */
public abstract class SensorAbstract<DataElementType extends SensorDataElement, DataInterface> {

    SensorAbstract(){
        setSensorName("AbstractSensor");
    }

    SensorAbstract(String sensorName){
        this.sensorName = sensorName;
    }

    protected boolean sensorRunningFlag = false; // flag for sensor thread which read data from hardware.
    protected boolean fileoutRunningFlag = false; // flag for file output thread which write data to file.
    protected boolean guiRunningFlag = false; // flag for gui thread which show the data online.

    protected DataInterface dataInterfaceTool = null;
    protected HashSet<SensorDataListener<DataElementType>> listenerHashSet = null; // save listeners.


    protected SensorDataListener<DataElementType> tmpSensorListern;

    private String sensorName;



    protected File dataSaveFile=null;// file use to save


    public File getDataSaveFile() {
        return dataSaveFile;
    }

    public void setDataSaveFile(File dataSaveFile) {
        System.out.println("set File:"+dataSaveFile.toString());
        this.dataSaveFile = dataSaveFile;
    }


    protected boolean addDataListener(SensorDataListener<DataElementType> listener) {
        try {
            if (listenerHashSet == null) {
                listenerHashSet = new HashSet<SensorDataListener<DataElementType>>();
            }
            listenerHashSet.add(listener);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean removeDataListener(SensorDataListener<DataElementType> listener) {
        try {
            if (listenerHashSet != null) {
                listenerHashSet.remove(listener);
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }




    /**
     * notify all listeners in listenerHashSet.
     *
     * @param event
     * @return
     */
    public boolean notifyListeners(DataEvent<DataElementType> event) {
        try {
            if (null == listenerHashSet) {
                return false;
            }
            Iterator iter = listenerHashSet.iterator();
            while (iter.hasNext()) {
                SensorDataListener<DataElementType> listener =
                        (SensorDataListener<DataElementType>) iter.next();
                Runnable task = () -> {
                    try {
                        listener.SensorDataEvent(event);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                };
                Thread t = new Thread(task);
                t.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    /**
     * Start a sensor and add listener for basic output
     * @param listener
     * @return
     */
    public abstract boolean startSensor(SensorDataListener<DataElementType> listener);

    /**
     * Stop a sensor and remove the listener from HashSet
     * @return
     */
    public abstract boolean stopSensor();



    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }
}
