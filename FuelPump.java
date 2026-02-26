import java.math.*;

public class FuelPump {
    long workTime;
    int id;
    int openedAtDay;
    double flowRate;
    boolean busy;
    Car currentCar;

    public FuelPump(int cur_id, int cur_openedAtDay, double cur_flowRate) {
        id = cur_id;
        openedAtDay = cur_openedAtDay;
        flowRate = cur_flowRate;
        busy = false;
        currentCar = null;
        workTime = 0;
    }

    public void setBusy(boolean set_busy) {
        busy = set_busy;
    }

    public void setCurrentCar(Car set_currentCar) {
        currentCar = set_currentCar;
    }

        public boolean GetBusy() {
        return busy;
    }

    public long getServiceTime() {
        double ret = Math.ceil(currentCar.getDesiredFuel() / flowRate);
        return (long) ret; //convert to long
    }

    public long getWorkTime() {
        return workTime;
    }

    public void addWorkTime(long added_time) {
        workTime += added_time;
    }

    public boolean isServiceEnded(long time) {
        return !busy || time >= currentCar.getEndServiceTime();
    }
}
