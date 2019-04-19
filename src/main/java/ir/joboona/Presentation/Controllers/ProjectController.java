package ir.joboona.Presentation.Controllers;

import Solutions.Core.Dispatcher.RequestMethod;
import Solutions.Presentation.Controller.RestController;
import Solutions.Presentation.Controller.PathVariable;
import Solutions.Presentation.Controller.RequestMapping;
import Solutions.Presentation.Controller.RequestParam;
import ir.joboona.Exceptions.BidExceptions.Common.Unauthorized;
import ir.joboona.Models.Bid;
import ir.joboona.Models.Project;
import ir.joboona.Models.User;
import ir.joboona.Presentation.Controllers.Presentation.Dtos.ProjectDto;
import ir.joboona.Repositories.ProjectRepository;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@RestController(basePath = "/project")
public class ProjectController {

    private final ProjectRepository projectRepository = ProjectRepository.getInstance();

    @RequestMapping(method = RequestMethod.GET)
    public Set<ProjectDto> show(@RequestParam(value = "userId", required = true) User user) {

        return projectRepository.getAll().stream()
                .filter(project -> project.sufficientSkills(user.getSkills()))
                .map(p -> new ProjectDto(isUserBidding(p, user), p))
                .collect(toSet());
    }


    @RequestMapping(path = "/{projectId}", method = RequestMethod.GET)
    public ProjectDto get(@PathVariable(value = "projectId") Project project,
                          @RequestParam(value = "userId", required = true) User user) {

        if (!project.sufficientSkills(user.getSkills()))
            throw new Unauthorized("شما مهارتهای لازم برای مشاهده این پروژه را ندارید.");

        return new ProjectDto(isUserBidding(project, user), project);
    }

    @RequestMapping(path = "/{projectId}/bid",method = RequestMethod.POST)
    public Bid bid(@PathVariable("projectId") Project project, @RequestParam("userId") User user,
                   @RequestParam(value = "amount", required = true) Integer amount){

        Bid bid = new Bid(user, project, amount);
        project.addBid(bid);
        return bid;
    }

    private boolean isUserBidding(Project project, User user){
        return project.getBids().stream().anyMatch(bid -> bid.getBiddingUser().equals(user));
    }

}
