import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class GasStation {
    Statistics stats;
    Config conf;
    List<FuelPump> pumps;
    Queue<Car> q;
    double fuelReserve, maxFuelCapacity, deliveryVolume;

    Integer findFreePumpIndx() {
        for (int i = 0; i < pumps.size(); i++)
            if (!pumps.get(i).GetBusy())
                return i;
        return null;
    }

    Event serviceCar(Car car, Integer pumpIndx, long time) {
        if (car != null && fuelReserve >= car.getDesiredFuel() && pumpIndx != null) {
            FuelPump pump = pumps.get(pumpIndx);
            pump.setBusy(true);
            pump.setCurrentCar(car);
            car.setStartServiceTime(time);
            long serviceTime = pump.getServiceTime();
            car.setEndServiceTime(time + serviceTime);
            fuelReserve -= car.getDesiredFuel();

            pump.addWorkTime(serviceTime);

            stats.addServiced();
            stats.addWaitingTime(time - car.getArrivalTime());
            stats.compareMaxWaitingTime(time - car.getArrivalTime());
            stats.addServiceTime(serviceTime);
            stats.addTotalServiceTime(serviceTime + time - car.getArrivalTime());
            stats.compareMinFuelRemaining(fuelReserve);

            return new Event(EventType.SERVICE_END, car.getEndServiceTime());
        }
        if (fuelReserve < car.getDesiredFuel())
            stats.addRefused();
        return null;
    }

    public GasStation(Statistics cur_stats, Config cur_conf) {
        conf = cur_conf;
        fuelReserve = conf.getInitialFuel();
        maxFuelCapacity = conf.getMaxFuelCapacity();
        deliveryVolume = conf.getDeliveryVolume();
        pumps = new ArrayList<>();
        q = new PriorityQueue<>((c1, c2) -> (int) (c1.getArrivalTime() - c2.getArrivalTime()));
        stats = cur_stats;
        for (int i = 0; i < conf.getInitialPumps(); i++)
            addPump(0, conf.getDefaultFlowRate());
    }

    public void processCars(Queue<Event> events, long time) {
        stats.compareMaxQueueLen(q.size());

        Integer pumpIndx = findFreePumpIndx();

        while (q.size() > 0 && pumpIndx != null) {
            Car car = q.poll();
            Event result = serviceCar(car, pumpIndx, time);
            if (result != null)
                events.add(result);
            pumpIndx = findFreePumpIndx();

            if (car.getStartServiceTime() - car.getArrivalTime() > conf.getQueueThreshold() * 60
                    && !events.stream().anyMatch(e -> e.getType() == EventType.PUMP_OPEN))
                events.add(
                        new Event(EventType.PUMP_OPEN, time + conf.getExpansionDelay() * 24 * 60 * 60));
        }
    }

    public void addCar(long time, Random rand) {
        q.add(new Car(q.size(), time, rand));
    }

    public void deliveryFuel() {
        stats.addFuelRemaining(fuelReserve);
        stats.compareMinFuelRemaining(fuelReserve);

        fuelReserve = Math.max(conf.getMaxFuelCapacity(),
                Math.min(fuelReserve + deliveryVolume, maxFuelCapacity));
    }

    public void addPump(int openedAtDay, double flowRate) {
        if (pumps.size() >= conf.getMaxPumps())
            return;
        pumps.add(new FuelPump(pumps.size(), openedAtDay, flowRate / 60.0));
        stats.setResultPumpsCount(pumps.size());
    }

    public void checkIfPumpsFree(long time) {
        for (int i = 0; i < pumps.size(); i++)
            if (pumps.get(i).isServiceEnded(time)) {
                pumps.get(i).setBusy(false);
            }
    }

    public List<FuelPump> getPumps() {
        return pumps;
    }
}