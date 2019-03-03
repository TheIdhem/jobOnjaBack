package ir.joboona.Presentation.Controllers;

import ir.joboona.Models.Knowledge;
import ir.joboona.Models.Skill;
import ir.joboona.Models.User;
import ir.joboona.Repositories.UserRepository;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;

@WebServlet(value = "/users")
public class UsersController extends HttpServlet {

    private final UserRepository userRepo = UserRepository.getInstance();


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = userRepo.getById(request.getParameter("userId")).get();
            Set<User> users = userRepo.getAll();
            request.setAttribute("users", users);
            request.setAttribute("user", user);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/user_info.jsp");
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
