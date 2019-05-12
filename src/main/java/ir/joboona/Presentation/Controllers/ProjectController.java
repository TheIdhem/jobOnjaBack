package ir.joboona.Presentation.Controllers;

import Solutions.Core.Dispatcher.RequestMethod;
import Solutions.Data.EntityManager;
import Solutions.Presentation.Controller.*;
import ir.joboona.Exceptions.Forbidden;
import ir.joboona.Models.Bid;
import ir.joboona.Models.Project;
import ir.joboona.Models.User;
import ir.joboona.Repositories.ProjectRepository;
import ir.joboona.Repositories.common.Page;
import ir.joboona.Presentation.Dtos.ProjectDto;
import ir.joboona.Repositories.common.Pageable;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController(basePath = "/project")
public class ProjectController {

    private final EntityManager entityManager = EntityManager.getInstance();
    private final ProjectRepository projectRepository = ProjectRepository.getInstance();

    @RequestMapping(method = RequestMethod.GET)
    public Page<ProjectDto> show(@RequestAttribute("principal") User user,
                                 @RequestParam("q") String q,
                                 @RequestParam(value = "page", required = true) Integer page,
                                 @RequestParam(value = "size", required = true) Integer size) throws Exception {

        Pageable pageable = new Pageable(page, size);
        Page<Project> projectPage = projectRepository.allQualifingProjectsLikeOrderedByCreationDate(pageable, q, user);

        List<ProjectDto> results = projectPage.getResults().stream()
                .map(p -> new ProjectDto(isUserBidding(p, user), p)).collect(toList());

        return new Page<>(results, projectPage.getPageable(), projectPage.getCount());
    }


    @RequestMapping(path = "/{projectId}", method = RequestMethod.GET)
    public ProjectDto get(@PathVariable(value = "projectId") Project project, @RequestAttribute("principal") User user) {

        if (!project.sufficientSkills(user.getSkills()))
            throw new Forbidden("شما مهارتهای لازم برای مشاهده این پروژه را ندارید.");

        return new ProjectDto(isUserBidding(project, user), project);
    }

    @RequestMapping(path = "/{projectId}/bid",method = RequestMethod.POST)
    public Bid bid(@PathVariable("projectId") Project project, @RequestAttribute("principal") User user,
                   @RequestParam(value = "amount", required = true) Integer amount) throws Exception {

        Bid bid = new Bid(user, project, amount);
        project.addBid(bid);
        entityManager.save(project);
        return bid;
    }

    private boolean isUserBidding(Project project, User user){
        return project.getBids().stream().anyMatch(bid -> bid.getBiddingUser().equals(user));
    }

}
