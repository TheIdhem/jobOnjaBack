package Solutions.Schedule;

import Solutions.Core.ApplicationProperties;
import it.sauronsoftware.cron4j.Scheduler;
import org.reflections.Reflections;


import java.util.Set;

public class ScheduleManager {

    private static ScheduleManager instance;
    private Scheduler scheduler;

    private ScheduleManager() throws Exception {

        scheduler =  new Scheduler();
        String basePackage = ApplicationProperties.getInstance().getProperty("solutions.scheduler.base_package");
        Set<Class<? extends Schedulable>> classes = new Reflections(basePackage).getSubTypesOf(Schedulable.class);
        for (Class< ? extends Schedulable> clazz : classes) {
            Schedulable schedulable = clazz.newInstance();
            scheduler.schedule(schedulable.getCron(), schedulable);
        }
        scheduler.start();

    }

    public static ScheduleManager init() throws Exception {
        instance = new ScheduleManager();
        return instance;
    }
    public static ScheduleManager getInstance() {
        return instance;
    }

    public void tearDown(){
        scheduler.stop();
    }

}
