package ir.joboona.Services;

import Solutions.Data.Exceptions.EntityNotFound;
import ir.joboona.Models.Bid;
import ir.joboona.Models.Project;
import ir.joboona.Repositories.BidRepository;
import ir.joboona.Repositories.ProjectRepository;

import java.util.Set;

import static java.util.Comparator.comparing;

public class ProjectService {

    private static ProjectService instance;
    private ProjectRepository projectRepository = ProjectRepository.getInstance();
    private BidRepository bidRepository = BidRepository.getInstance();

    private ProjectService() {
    }

    public static ProjectService getInstance() {
        if (instance == null)
            instance = new ProjectService();
        return instance;
    }

    public Project create(Project project) {
        return projectRepository.save(project);
    }

    public Bid performAuction(Project project) {

        Set<Bid> bids = bidRepository.findAllByProject(project);

        return bids.stream().max(comparing(bid ->
                project.evaluateSkillsAndOffers(bid.getBiddingUser().getSkills(), bid.getBidAmount())
        )).orElseThrow(() -> new EntityNotFound(Project.class, project.getId()));
    }

}
