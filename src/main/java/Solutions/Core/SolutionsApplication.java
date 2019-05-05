package Solutions.Core;

import Solutions.Core.ApplicationRunner.ApplicationRunner;
import Solutions.Data.EntityManager;
import Solutions.Schedule.ScheduleManager;
import org.reflections.Reflections;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class SolutionsApplication {

    private static SolutionsApplication instance;
    private ScheduleManager scheduleManager;

    public static SolutionsApplication getInstance() {
        if (instance == null)
            instance = new SolutionsApplication();
        return instance;
    }

    public void run(String[] args) throws Exception {

        ApplicationProperties.init();
        EntityManager.init();
        this.runApplicationRunners();

    }

    private void runApplicationRunners() throws Exception {

        String basePackage = ApplicationProperties.getInstance()
                .getProperty("solutions.application_runner.base_package");
        Set<Class<? extends ApplicationRunner>> runners =
                new Reflections(basePackage).getSubTypesOf(ApplicationRunner.class);

        List<ApplicationRunner> instances = new ArrayList<>();

        for (Class<? extends ApplicationRunner> runner : runners)
            instances.add(runner.getDeclaredConstructor().newInstance());

        AnnotationAwareOrderComparator.sort(instances);

        for (ApplicationRunner instance : instances)
            instance.start();

        scheduleManager = ScheduleManager.init();
    }


    public void tearDown(){
        scheduleManager.tearDown();
    }
}
