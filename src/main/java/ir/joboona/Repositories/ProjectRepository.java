package ir.joboona.Repositories;

import Solutions.Data.EntityRepository;
import ir.joboona.Models.Project;

import java.util.HashSet;
import java.util.Set;

public class ProjectRepository implements EntityRepository<Project, String> {

    private static ProjectRepository instance;
    private Set<Project> projects;

    private ProjectRepository(){
        projects = new HashSet<>();
    }

    public static ProjectRepository getInstance(){
        if (instance == null)
            instance = new ProjectRepository();
        return instance;
    }

    @Override
    public Project save(Project project) {

        projects.add(project);
        return project;
    }

    public void saveAll(Set<Project> projects) {
        this.projects.addAll(projects);
    }

    @Override
    public Set<Project> getAll() {
        return projects;
    }

}
