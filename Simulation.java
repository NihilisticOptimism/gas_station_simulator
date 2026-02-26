import java.io.Console;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class Simulation {
    GasStation station;
    Queue<Event> events;
    Statistics stats;
    Config conf;
    Random rand;
    long simulationTime;

    Simulation(long cur_simulationTime, Statistics cur_stats, Config cur_conf) {
        simulationTime = cur_simulationTime;
        stats = cur_stats;
        conf = cur_conf;

        station = new GasStation(stats, conf);
        events = new PriorityQueue<>((e1, e2) -> (int) (e1.getTime() - e2.getTime()));
        rand = new Random(conf.getRandomSeed());

        CarGenerator carGenerator = new CarGenerator(new Random(conf.getRandomSeed()));
        stats.setarrived(carGenerator.generate(events, simulationTime));

        int deliveryCount = 0;
        for (long time = 0; time < simulationTime; time += conf.getDeliveryInterval() * 60*60) {
            events.add(new Event(EventType.DELIVERY, time + conf.getDeliveryDuration() * 60));
            deliveryCount++;
        }
        stats.setDeliveryCount(deliveryCount);
    }

    public void run() {
        while (events.size() > 0 && step() < simulationTime);
    }

    public long step() {
        Event e = events.poll();
        switch (e.getType()) {
            case ARRIVAL:
                station.addCar(e.getTime(), rand);
                break;
            case SERVICE_END:
                station.checkIfPumpsFree(e.getTime());
                break;
            case DELIVERY:
                station.deliveryFuel();
                break;
            case PUMP_OPEN:
                station.addPump((int) e.getTime() / 60 / 60 / 24, conf.getDefaultFlowRate());
                break;
            default:
                break;
        }
        station.processCars(events, e.getTime());
        return e.getTime();
    }

    public long getSimulationTime(){
        return simulationTime;
    }
}