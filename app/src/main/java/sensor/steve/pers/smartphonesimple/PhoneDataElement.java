package sensor.steve.pers.smartphonesimple;

public class PhoneDataElement extends IMUDataElement {
    public int getStep_flag() {
        return step_flag;
    }

    public void setStep_flag(int step_flag) {
        this.step_flag = step_flag;
    }

    protected int step_flag = 0;

    public PhoneDataElement(){
        dataname="PhoneData";
    }

    /**
     * Convert Data to String(with '\n' at the end)
     */
    @Override
    public String convertDatatoString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%15.03f,%15.03f", time_stamp, system_time_stamp));
        stringBuilder.append(",");
        stringBuilder.append(super.darray2String(acc, 3));
        stringBuilder.append(super.darray2String(gyr, 3));
        stringBuilder.append(super.darray2String(mag, 3));
        stringBuilder.append(super.darray2String(angle, 3));
        stringBuilder.append(super.darray2String(pressure, 1));
        stringBuilder.append(super.darray2String(heigh, 1));
        stringBuilder.append(super.darray2String(Temp, 1));
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
        stringBuilder.append("\n");


        return stringBuilder.toString();
    }
}
