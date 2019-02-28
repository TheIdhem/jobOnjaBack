package ir.joboona.Seeds;

import Solutions.Core.ApplicationRunner.ApplicationRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import ir.joboona.HttpClient.HttpGetClient;
import ir.joboona.Models.Project;
import ir.joboona.Repositories.ProjectRepository;

import java.util.Set;

public class ProjectSeed implements ApplicationRunner {

    private final ProjectRepository projectRepository = ProjectRepository.getInstance();

    @Override
    public void run() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();

        Set<Project> projects = objectMapper.
                readValue(HttpGetClient.getInstance().getRequest("http://142.93.134.194:8000/joboonja/project", 1000),
                        typeFactory.constructCollectionType(Set.class, Project.class));

        projectRepository.saveAll(projects);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
