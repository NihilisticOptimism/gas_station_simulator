import java.util.List;

public class Statistics {
    Config conf;
    double sumFuelRemaining = 0;
    double minFuelRemaining = Double.POSITIVE_INFINITY;
    int arrived = 0; 
    int serviced = 0;
    int refused = 0;

    int maxQueueLen = 0;
    int resultPumpsCount = 0;
    int deliveryCount = 0;

    int startPumpsCount = 0;

    long sumWaitingTime = 0;
    long maxWaitingTime = 0;
    long sumServiceTime = 0;
    long sumTotalServiceTime = 0;

    public Statistics(Config cur_conf)
    {
        conf = cur_conf;
        startPumpsCount = conf.getInitialPumps();
        minFuelRemaining = conf.getMaxFuelCapacity();
    }

    public void setarrived(int status_arrived) {
        arrived = status_arrived;
    }

    public void addServiced() {
        serviced++;
    }

    public void addRefused()
    {
        refused++;
    }

    public void compareMaxQueueLen(int cur_maxQueueLen) {
        maxQueueLen = Math.max(maxQueueLen, cur_maxQueueLen);
    }

    public void setResultPumpsCount(int set_resultPumpsCount) {
        resultPumpsCount = set_resultPumpsCount;
    }

    public void setDeliveryCount(int set_deliveryCount) {
        deliveryCount = set_deliveryCount;
    }

    public void addFuelRemaining(double fuelRemaining) {
        sumFuelRemaining += fuelRemaining;
    }

    public void compareMinFuelRemaining(double cur_minFuelRemaining) {
        minFuelRemaining = Math.min(minFuelRemaining, cur_minFuelRemaining);
    }

    public void addWaitingTime(long waitingTime) {
        sumWaitingTime += waitingTime;
    }

    public void compareMaxWaitingTime(long cur_maxWaitingTime) {
        maxWaitingTime = Math.max(maxWaitingTime, cur_maxWaitingTime);
    }

    public void addServiceTime(long serviceTime) {
        sumServiceTime += serviceTime;
    }

    public void addTotalServiceTime(long cur_totalServiceTime) {
        sumTotalServiceTime += cur_totalServiceTime;
    }

    public void display_stats(List<FuelPump> pumps, long simulationTime)
    {
        System.out.println("Всего прибыло автомобилей: " + arrived); 
        System.out.println("Обслужено автомобилей: " + serviced); 
        System.out.println("Отказов (нет топлива): " + refused);

        double average_wait_time = Math.round(sumWaitingTime/serviced /60.0 * 10) / 10.0;
        System.out.println("Ср. ожидание в очереди: " + average_wait_time + " мин"); 

        double max_wait_time = Math.round(maxWaitingTime / 60.0 * 10) / 10.0;
        System.out.println("Макс. ожидание: " + max_wait_time + " мин"); 

        System.out.println("Максимальная длина очереди: " + maxQueueLen); 

        double average_service_time = Math.round(sumServiceTime / serviced / 60.0 * 10) / 10.0;
        System.out.println("Среднее время работы: " + average_service_time + " мин"); 

        double average_total_service_time = Math.round(sumTotalServiceTime / serviced / 60.0 * 10) / 10.0;
        System.out.println("Среднее время обслуживания: " + average_total_service_time + " мин");
        
        System.out.println("Колонок в начале: " + startPumpsCount); 
        System.out.println("Колонок в конце: " + resultPumpsCount); 

        int opened_pumps = resultPumpsCount - startPumpsCount;
        System.out.println("Количество открытых дополнительных колонок: " + opened_pumps); 

        System.out.println("Доставок топлива: " + deliveryCount);

        double minimum_fuel_left = Math.round(minFuelRemaining);
        System.out.println("Минимальный остаток топлива: " + minimum_fuel_left + " л"); 

        double average_fuel_left = Math.round(sumFuelRemaining/deliveryCount);
        System.out.println("Средний остаток топлива: " + average_fuel_left +" л");

        System.out.println("Загрузка колонок:");
        for (int i =0; i < pumps.size(); i++)
            System.out.println((i + 1) + ": " + pumps.get(i).getWorkTime() * 100 / simulationTime + "%");
    }
}