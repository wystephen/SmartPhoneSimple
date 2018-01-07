package sensor.steve.pers.smartphonesimple;

import java.util.HashMap;

/**
 * WirelessDataElement designed for save data of wireless sensor in positioning system.
 * The most important  member variables are (MAC(String)->  value(Measure(double),...)) which indicated a map.
 */
public class WirelessDataElement extends SensorDataElement {


    protected HashMap<String, double[]> measurmentsMap = new HashMap<>(4);


    WirelessDataElement() {
        dataname = "WirelessSensor";
    }

    /**
     * Convert Data to String( with '\n' at the end).
     *
     * @return
     */
    @Override
    public String convertDatatoString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%.03f,%15.03f,", time_stamp, system_time_stamp));

        if (measurmentsMap != null) {
            for (String key : measurmentsMap.keySet()) {

                sb.append(String.format("{%s:%s},", key, convertValuetoString(measurmentsMap.get(key))));
            }

        } else {
            sb.append(" Measurements is empty");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append("\n");
        return sb.toString();

    }

    public boolean addMeasurement(String mac_address, double[] data){
        try{

            measurmentsMap.put(mac_address,data);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }


    /**
     * Converte value[] to string. Never use out side.
     * @param v double[], value of measurements.
     * @return
     */
    private String convertValuetoString(double[] v) {
        StringBuilder sb = new StringBuilder();
        for (double value : v) {
            sb.append(value);
            sb.append(",");
        }
        return sb.toString();

    }

}
