package ir.joboona.Presentation.Controllers;

import ir.joboona.Models.Project;
import ir.joboona.Models.User;
import ir.joboona.Repositories.ProjectRepository;
import ir.joboona.Repositories.UserRepository;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@WebServlet("/projects")
public class ProjectsController extends HttpServlet {

    private final ProjectRepository projectRepository = ProjectRepository.getInstance();
    private final UserRepository userRepo = UserRepository.getInstance();


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            User user = userRepo.getById(request.getParameter("userId")).get();

            Set<Project> projects = projectRepository.getAll().stream()
                    .filter(project -> project.sufficientSkills(user.getSkills())).collect(toSet());
            request.setAttribute("projects", projects);
            request.setAttribute("user", user);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/projects.jsp");
            dispatcher.forward(request, response);
        }catch (Exception ex){
            this.badRequest(request,response);
        }
    }

    private void badRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/bad_request.jsp");
        dispatcher.forward(request, response);
    }
}