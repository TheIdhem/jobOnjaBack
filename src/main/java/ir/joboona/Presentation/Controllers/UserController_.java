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

@WebServlet(value = "/user/profile/*")
public class UserController_ extends HttpServlet {

    private final UserRepository userRepo = UserRepository.getInstance();


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String userId = this.tokenizerWithDelimeter(request.getRequestURI(), "user/profile");
            Optional<User> user = userRepo.getById(userId);
            request.setAttribute("user", user.get());
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/user.jsp");
            dispatcher.forward(request, response);
        } catch (Exception ex) {
            this.badRequest(request, response);
        }
    }

    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            String method = request.getParameter("method");
            String userId = request.getParameter("userId");
            User user = userRepo.getById(userId).get();
            if (method.equals("deleteSkill")) {

                String point = request.getParameter("point");
                String skillName = request.getParameter("name");

                Knowledge knowledge = new Knowledge(skillName);
                Skill skill = new Skill(knowledge, Integer.parseInt(point));

                user.deleteSkill(skill);
                response.sendRedirect("/user/profile/" + userId);

            } else if (method.equals("addSkill")) {
                String knowledgeName = request.getParameter("knowledge");
                Knowledge knowledge = new Knowledge(knowledgeName);
                Skill skill = new Skill(knowledge, 0);
                user.addSkill(skill);
                response.sendRedirect("/user/profile/" + userId);
            } else if (method.equals("endorseSkill")) {
                String point = request.getParameter("point");
                String skillName = request.getParameter("name");

                Knowledge knowledge = new Knowledge(skillName);
                Skill skill = new Skill(knowledge, Integer.parseInt(point));
                user.endorseSkill(skill, user);
                response.sendRedirect("/user?userId=" + userId);
            }
        }catch (Exception ex){
            this.badRequest(request,response);
        }


    }


    private String tokenizerWithDelimeter(String buffer, String delimeter) {
        StringTokenizer tokenizer = new StringTokenizer(buffer, delimeter);
        return tokenizer.nextToken();
    }

    private void badRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/bad_request.jsp");
        dispatcher.forward(request, response);
    }

}
