package ir.joboona.Presentation.Controllers;

import ir.joboona.Models.Bid;
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
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/project")
public class ProjectController_ extends HttpServlet {

    private final ProjectRepository projectRepository = ProjectRepository.getInstance();
    private final UserRepository userRepo = UserRepository.getInstance();


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Project project = projectRepository.getById(request.getParameter("projectId")).get();
            User user = userRepo.getById(request.getParameter("userId")).get();
            Set<User> usersEndorsed = project.getBids().stream().map(bid -> bid.getBiddingUser()).collect(Collectors.toSet());
            Boolean isBided = usersEndorsed.contains(user);
            request.setAttribute("project", project);
            request.setAttribute("user", user);
            request.setAttribute("isBided", isBided);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/project.jsp");
            dispatcher.forward(request, response);
        } catch (Exception ex) {
            this.badRequest(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Project project = projectRepository.getById(request.getParameter("projectId")).get();
            Integer bidAmount = Integer.parseInt(request.getParameter("bid"));
            User user = userRepo.getById(request.getParameter("userId")).get();
            Bid bid = new Bid(user, project, bidAmount);
            project.addBid(bid);
            response.sendRedirect("/project?projectId=" + project.getId() + "&userId=" + user.getId());
        } catch (Exception ex) {
            this.badRequest(request, response);
        }
    }

    private void badRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/bad_request.jsp");
        dispatcher.forward(request, response);
    }
}
