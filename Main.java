public class Main {

    public static void main(String[] args) {
        Config conf = new Config();  
        Statistics stats = new Statistics(conf);
        Simulation sim = new Simulation(60 * 60 * 24 * conf.getSimulationDays(), stats, conf);
        sim.run();
        long simulationTime = sim.getSimulationTime();
        stats.display_stats(sim.station.getPumps(), simulationTime);
    }  
}  
