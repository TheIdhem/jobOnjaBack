package Solutions.Core;

import Solutions.Core.ApplicationRunner.ApplicationRunner;
import Solutions.Core.Dispatcher.Dispatcher;
import com.sun.net.httpserver.HttpServer;
import org.reflections.Reflections;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class SolutionsApplication {

    private static SolutionsApplication instance;

    public static SolutionsApplication getInstance() {
        if (instance == null)
            instance = new SolutionsApplication();
        return instance;
    }

    public void run(String[] args) throws Exception {

        ApplicationProperties.init();
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
            instance.run();
    }
}
