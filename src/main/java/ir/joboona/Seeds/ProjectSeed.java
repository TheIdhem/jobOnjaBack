package ir.joboona.Seeds;

import Solutions.Core.ApplicationRunner.ApplicationRunner;
import Solutions.Data.EntityManager;
import Solutions.Schedule.Schedulable;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import ir.joboona.HttpClient.HttpGetClient;
import ir.joboona.Models.Project;

import java.util.Set;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toSet;

public class ProjectSeed implements Schedulable, ApplicationRunner {

    private final EntityManager entityManager = EntityManager.getInstance();
    private long lastExecution = Long.MIN_VALUE;

    @Override
    public void start() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        Set<Project> projects = objectMapper.
                readValue(HttpGetClient.getInstance().getRequest("http://142.93.134.194:8000/joboonja/project", 1000),
                        typeFactory.constructCollectionType(Set.class, Project.class));

        Set<Project> newlyAdded = projects.stream()
            .filter(project -> project.getCreationDate() > lastExecution).collect(toSet());
        lastExecution = projects.stream().map(Project::getCreationDate)
                .reduce(Long::max).orElse(lastExecution);
        entityManager.saveAll(newlyAdded);
        System.out.println(String.format("ProjectSeed: %d projects added.", newlyAdded.size()));

    }


    @Override
    public String getCron() {
        return "*/5 * * * *";
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public void run() {
        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
