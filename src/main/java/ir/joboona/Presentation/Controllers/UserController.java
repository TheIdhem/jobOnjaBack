package ir.joboona.Presentation.Controllers;

import Solutions.Core.Dispatcher.RequestMethod;
import Solutions.Presentation.Controller.*;
import ir.joboona.Models.Knowledge;
import ir.joboona.Models.Skill;
import ir.joboona.Models.User;
import ir.joboona.Repositories.UserRepository;
import ir.joboona.Services.UserService;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Set;


@RestController(basePath = "/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController() {
        userRepository = UserRepository.getInstance();
    }

    @RequestMapping(method = RequestMethod.GET)
    public Set<User> show() {

        return userRepository.getAll();
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    public User get(@PathVariable(value = "userId") User user) {
        return user;
    }



}
