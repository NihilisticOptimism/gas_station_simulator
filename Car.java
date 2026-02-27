import java.util.Random;

public class Car {
    int id;
    double tankCapacity;
    double fuelLevel; 
    double desiredFuel;
    long arrivalTime; 
    long startServiceTime; 
    long endServiceTime;
    public Car(int get_id, long get_arrivalTime, Random random)
    {
        id = get_id;
        arrivalTime = get_arrivalTime;
        
        tankCapacity = 10 * (4 + random.nextInt(5));
        fuelLevel = (10 + random.nextInt(81)) / 100.0 * tankCapacity;
        desiredFuel = 4 + random.nextFloat() * (tankCapacity - fuelLevel - 4);
    }

    public void setStartServiceTime(long get_startServiceTime) {
        startServiceTime = get_startServiceTime;
    }

    public void setEndServiceTime(long get_endServiceTime) {
        endServiceTime = get_endServiceTime;
    }

    public double getDesiredFuel() {
        return desiredFuel;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }
    
    public long getStartServiceTime() {
        return startServiceTime;
    }

    public long getEndServiceTime() {
        return endServiceTime;
    }
}