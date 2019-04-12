package ir.joboona.Presentation.Controllers;

import Solutions.Core.Dispatcher.RequestMethod;
import Solutions.Presentation.Controller.*;
import ir.joboona.Models.Knowledge;
import ir.joboona.Models.Skill;
import ir.joboona.Models.User;
import ir.joboona.Presentation.Controllers.Presentation.Dtos.UserDto;
import ir.joboona.Repositories.UserRepository;
import ir.joboona.Services.UserService;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Set;
import java.util.stream.Collectors;


@RestController(basePath = "/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController() {
        userRepository = UserRepository.getInstance();
    }

    @RequestMapping(method = RequestMethod.GET)
    public Set<UserDto> show(@RequestParam("userId") User visitor) {

        return userRepository.getAll().stream()
                .map(user -> new UserDto(user, visitor)).collect(Collectors.toSet());
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    public UserDto get(@PathVariable(value = "userId") User user, @RequestParam("userId") User visitor) {
        return new UserDto(user, visitor);
    }



}
