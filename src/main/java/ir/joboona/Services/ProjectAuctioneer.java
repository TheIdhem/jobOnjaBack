package ir.joboona.Services;

import Solutions.Schedule.Schedulable;
import ir.joboona.Models.Project;
import ir.joboona.Repositories.ProjectRepository;

public class ProjectAuctioneer implements Schedulable {

    private final ProjectRepository projectRepository;

    public ProjectAuctioneer() {
        projectRepository = ProjectRepository.getInstance();
    }

    @Override
    public String getCron() {
        return "*/1 * * * *";
    }

    @Override
    public void run() {
        try {
            projectRepository
                    .getAllProjectsWithDeadlineBeforeAndNotAuctioned(System.currentTimeMillis())
                    .forEach(Project::performAuction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
