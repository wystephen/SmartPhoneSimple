package sensor.steve.pers.smartphonesimple;

/**
 * Common IMU Data Container.
 * acc: x,y,z
 * gyr: x,y,z
 * mag: x,y,z
 * angle: x,y,z
 * pressure: p
 * height: H
 * temporary: T.
 */
public class IMUDataElement extends SensorDataElement {

    protected double[] acc = new double[3];// acc
    protected double[] gyr = new double[3];//gyr
    protected double[] mag = new double[3];//mag
    protected double[] angle = new double[3];//angle
    protected double[] pressure = new double[1];// pressuer
    protected double[] heigh = new double[1];//height
    protected double[] Temp = new double[1];//Temporary


    public IMUDataElement() {
        dataname = "IMUData";
    }


    /**
     * Convert Data to String( with '\n' at the end).
     *
     * @return
     */
    @Override
    public String convertDatatoString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%15.03f,%15.03f", time_stamp, system_time_stamp));
        stringBuilder.append(",");
        stringBuilder.append(darray2String(acc, 3));
        stringBuilder.append(darray2String(gyr, 3));
        stringBuilder.append(darray2String(mag, 3));
        stringBuilder.append(darray2String(angle, 3));
        stringBuilder.append(darray2String(pressure, 1));
        stringBuilder.append(darray2String(heigh, 1));
        stringBuilder.append(darray2String(Temp, 1));
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
        stringBuilder.append("\n");


        return stringBuilder.toString();
    }


    private String darray2String(double[] a, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; ++i) {
            sb.append(a[i]);
            sb.append(",");
        }

        return sb.toString();
    }

    public double[] getHeigh() {
        return heigh;
    }

    public void setHeigh(double[] heigh) {
        this.heigh = heigh;
    }

    public double[] getAcc() {
        return acc;
    }

    public void setAcc(double[] acc) {
        this.acc = acc;
    }

    public double[] getGyr() {
        return gyr;
    }

    public void setGyr(double[] gyr) {
        this.gyr = gyr;
    }

    public double[] getMag() {
        return mag;
    }

    public void setMag(double[] mag) {
        this.mag = mag;
    }

    public double[] getPressure() {
        return pressure;
    }

    public void setPressure(double[] pressure) {
        this.pressure = pressure;
    }

    public double[] getTemp() {
        return Temp;
    }

    public void setTemp(double[] temp) {
        Temp = temp;
    }

    public double[] getAngle() {
        return angle;
    }

    public void setAngle(double[] angle) {
        this.angle = angle;
    }
}
