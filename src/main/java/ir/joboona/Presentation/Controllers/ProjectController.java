package ir.joboona.Presentation.Controllers;

import Solutions.Core.Dispatcher.RequestMethod;
import Solutions.Core.Exceptions.UnAuthorized;
import Solutions.Presentation.Controller.RestController;
import Solutions.Presentation.Controller.PathVariable;
import Solutions.Presentation.Controller.RequestMapping;
import Solutions.Presentation.Controller.RequestParam;
import ir.joboona.Models.Bid;
import ir.joboona.Models.Project;
import ir.joboona.Models.User;
import ir.joboona.Repositories.ProjectRepository;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@RestController(basePath = "/project")
public class ProjectController {

    private final ProjectRepository projectRepository = ProjectRepository.getInstance();

    @RequestMapping(method = RequestMethod.GET)
    public Set<Project> show(@RequestParam(value = "userId", required = true) User user) {

        return projectRepository.getAll().stream()
                .filter(project -> project.sufficientSkills(user.getSkills())).collect(toSet());
    }


    @RequestMapping(path = "/{projectId}", method = RequestMethod.GET)
    public Project get(@PathVariable(value = "projectId") Project project,
                    @RequestParam(value = "userId", required = true) User user) {

        if (!project.sufficientSkills(user.getSkills()))
            throw new UnAuthorized("You don't have enough skills.");

        return project;
    }

    @RequestMapping(path = "/{projectId}/bid",method = RequestMethod.POST)
    public Bid bid(@PathVariable("projectId") Project project, @RequestParam("userId") User user,
                   @RequestParam(value = "amount", required = true) Integer amount){

        Bid bid = new Bid(user, project, amount);
        project.addBid(bid);
        return bid;
    }

}
