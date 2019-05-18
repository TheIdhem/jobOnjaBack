package ir.joboona.Presentation.Controllers;

import Solutions.Core.Dispatcher.RequestMethod;
import Solutions.Presentation.Controller.*;
import ir.joboona.Exceptions.DuplicateItemException;
import ir.joboona.Exceptions.Forbidden;
import ir.joboona.Models.User;
import ir.joboona.Presentation.Dtos.UserDto;
import ir.joboona.Repositories.UserRepository;
import ir.joboona.Repositories.common.Page;
import ir.joboona.Repositories.common.Pageable;
import ir.joboona.Services.AuthenticationService;
import ir.joboona.Services.UserService;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;


@RestController(basePath = "/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public UserController() {
        userRepository = UserRepository.getInstance();
        userService = UserService.getInstance();
        authenticationService = AuthenticationService.getInstance();
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<UserDto> show(@RequestAttribute("principal") User visitor,
                             @RequestParam("q") String q,
                             @RequestParam(value = "page", required = true) Integer page,
                             @RequestParam(value = "size", required = true) Integer size) throws Exception {

        Pageable pageable = new Pageable(page, size);

        Page<User> userPage = userRepository.getAllUsersLike(pageable, q);

        List<UserDto> userDtos = userPage.getResults().stream()
                .map(item -> new UserDto(item, visitor)).collect(toList());

        return new Page<>(userDtos, pageable, userPage.getCount());
    }

    @RequestMapping(method = RequestMethod.POST)
    public User create (@RequestBody User user, HttpServletResponse response) throws Exception {
        if(userRepository.findUserByUsername(user.getUsername()).isPresent())
            throw new DuplicateItemException(User.class, singleton("username"));
        String token = authenticationService.generateJWT_Token(user);
        response.addHeader("Authorization", token);
        userService.registerUser(user);
        return user;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/login")
    public User login(@RequestBody User credentials,
                      HttpServletResponse resp) throws Exception{

        Optional<User> user = userRepository.findUserByUsername(credentials.getUsername());
        if(! user.isPresent())
            throw new Forbidden("کاربری با این نام کاربری وجود ندارد.");
        if (!BCrypt.checkpw(credentials.getPassword(), user.get().getPassword()))
            throw new Forbidden("گذرواژه نامعتبر است.");

        String token = authenticationService.generateJWT_Token(user.get());
        resp.addHeader("Authorization", token);
        return user.get();
    }


    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    public UserDto get(@PathVariable(value = "userId") User user, @RequestAttribute("principal") User visitor) {
        return new UserDto(user, visitor);
    }



}
