package ir.joboona.Presentation.Controllers;

import Solutions.Core.Dispatcher.RequestMethod;
import Solutions.Data.EntityManager;
import Solutions.Presentation.Controller.*;
import ir.joboona.Models.User;
import ir.joboona.Presentation.Dtos.UserDto;
import ir.joboona.Repositories.UserRepository;
import ir.joboona.Repositories.common.Page;
import ir.joboona.Repositories.common.Pageable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@RestController(basePath = "/user")
public class UserController {

    private final UserRepository userRepository = UserRepository.getInstance();

    @RequestMapping(method = RequestMethod.GET)
    public Page<UserDto> show(@RequestParam("userId") User visitor,
                             @RequestParam("q") String q,
                             @RequestParam(value = "page", required = true) Integer page,
                             @RequestParam(value = "size", required = true) Integer size) throws Exception {

        Pageable pageable = new Pageable(page, size);

        Page<User> userPage ;
        if (q == null || q.isEmpty())
            userPage = userRepository.getAllUsers(pageable);
        else
            userPage = userRepository.getAllUsersLike(pageable, q);

        List<UserDto> userDtos = userPage.getResults().stream()
                .map(item -> new UserDto(item, visitor)).collect(toList());

        return new Page<>(userDtos, pageable, userPage.getCount());
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    public UserDto get(@PathVariable(value = "userId") User user, @RequestParam("userId") User visitor) {
        return new UserDto(user, visitor);
    }



}
