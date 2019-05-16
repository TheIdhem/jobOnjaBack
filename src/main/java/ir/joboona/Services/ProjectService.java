package ir.joboona.Services;

import Solutions.Data.EntityManager;
import Solutions.Data.Exceptions.EntityNotFound;
import ir.joboona.Models.Bid;
import ir.joboona.Models.Project;

import static java.util.Comparator.comparing;

public class ProjectService {

    private static ProjectService instance;
    private EntityManager entityManager = EntityManager.getInstance();

    private ProjectService() {
    }

    public static ProjectService getInstance() {
        if (instance == null)
            instance = new ProjectService();
        return instance;
    }

    public Project create(Project project) throws Exception {
        return entityManager.save(project);
    }

}
