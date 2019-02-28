package ir.joboona.Presentation.Controllers;

import Solutions.Core.Dispatcher.RequestMethod;
import Solutions.Core.Exceptions.UnAuthorized;
import Solutions.Presentation.Controller.HtmlController;
import Solutions.Presentation.Controller.PathVariable;
import Solutions.Presentation.Controller.RequestMapping;
import Solutions.Presentation.Controller.RequestParam;
import ir.joboona.Models.Project;
import ir.joboona.Models.User;
import ir.joboona.Repositories.ProjectRepository;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@HtmlController(basePath = "/project")
public class ProjectController {

    private final ProjectRepository projectRepository = ProjectRepository.getInstance();

    @RequestMapping(method = RequestMethod.GET, template = "projects.html")
    public void show(@RequestParam(value = "userId", required = true) User user, Document document) {

        Set<Project> projects = projectRepository.getAll().stream()
                .filter(project -> project.sufficientSkills(user.getSkills())).collect(toSet());

        for (Project p : projects) {

            document.selectFirst("table tbody").append(
                    "<tr>" +
                            "<td>" + p.getId() + "</td>" +
                            "<td>" + p.getTitle() + "</td>" +
                            "<td>" + p.getBudget() + "</td>" +
                            "</tr>"
            );
        }
    }


    @RequestMapping(path = "/{projectId}", method = RequestMethod.GET, template = "project-single.html")
    public void get(@PathVariable(value = "projectId") Project project,
                    @RequestParam(value = "userId", required = true) User user,
                    Document document) {

        if (!project.sufficientSkills(user.getSkills()))
            throw new UnAuthorized("You don't have enough skills.");

        Element ul = document.selectFirst("ul");

        ul.child(0).append(project.getId());
        ul.child(1).append(project.getTitle());
        ul.child(2).append(project.getDescription());
        ul.child(3).child(0).attr("src", project.getImageUrl());
    }

}
