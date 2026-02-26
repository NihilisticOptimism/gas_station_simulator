import java.util.*;

public class CarGenerator {
    Random random;

    public CarGenerator(Random cur_random)
    {
        random = cur_random;
    }
    
    double getLambda(long time) {
        double la;
        long mins = time / 60;
        mins = mins % (24 * 60) / 60;
        if (mins < 6)
            la = 0.03;
        else if (mins < 10)
            la = 0.25;
        else if (mins < 16)
            la = 0.12;
        else if (mins < 20)
            la = 0.3;
        else
            la = 0.08;
        return la;
    }

    long getDeltaTime(long time) {
        return Math.round(-Math.log(random.nextDouble()) / getLambda(time)) * 60;
    }

    public int generate(Queue<Event> events, long simulationTime) {
        int count = 0;
        for (long time = 0; time < simulationTime; time += getDeltaTime(time)) {
            events.add(new Event(EventType.ARRIVAL, time));
            count++;
        }
        return count;
    }
}