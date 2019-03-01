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

@WebServlet(value = "/user/*")
public class UserController_ extends HttpServlet {

    private final UserRepository userRepo = UserRepository.getInstance();


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String userId = this.tokenizerWithDelimeter(request.getRequestURI(), "/user");
            Optional<User> user = userRepo.getById(userId);
            request.setAttribute("user", user.get());
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/user.jsp");
            dispatcher.forward(request, response);
        } catch (Exception ex) {
            Set<User> users = userRepo.getAll();
            request.setAttribute("users",users);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/user_info.jsp");
            dispatcher.forward(request, response);
        }
    }

    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        String method = request.getParameter("method");
        String userId = request.getParameter("userId");
        User user = userRepo.getById(userId).get();
        if (method.equals("deleteSkill")) {

            String point = request.getParameter("point");
            String skillName = request.getParameter("name");

            Knowledge knowledge = new Knowledge(skillName);
            Skill skill = new Skill(knowledge, Integer.parseInt(point));

            user.deleteSkill(skill);

        } else if (method.equals("addSkill")) {
            String knowledgeName = request.getParameter("knowledge");
            Knowledge knowledge = new Knowledge(knowledgeName);
            Skill skill = new Skill(knowledge, 0);
            user.addSkill(skill);
        }
        response.sendRedirect("/user/" + userId);

    }


    private String tokenizerWithDelimeter(String buffer, String delimeter) {
        StringTokenizer tokenizer = new StringTokenizer(buffer, delimeter);
        return tokenizer.nextToken();
    }

}
