package ir.joboona.Presentation.Controllers;

import Solutions.Core.Dispatcher.RequestMethod;
import Solutions.Presentation.Controller.RequestBody;
import Solutions.Presentation.Controller.RequestMapping;
import Solutions.Presentation.Controller.RequestParam;
import Solutions.Presentation.Controller.RestController;
import ir.joboona.Models.Knowledge;
import ir.joboona.Models.Skill;
import ir.joboona.Models.User;
import ir.joboona.Services.UserService;

import java.util.Set;

@RestController(basePath = "/user/skill")
public class SkillController {

    private final UserService userService;

    public SkillController() {
        userService = UserService.getInstance();
    }


    @RequestMapping(method = RequestMethod.POST)
    public User addSkill(@RequestBody Skill skill,
                         @RequestParam(value = "userId", required = true) User user){

        user.addSkill(skill);
        return user;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public User removeSkill(@RequestBody Skill skill,
                            @RequestParam(value = "userId", required = true) User user){

        user.deleteSkill(skill);
        return user;
    }

    @RequestMapping(path = "/retard",method = RequestMethod.GET)
    public Set<Knowledge> getRetardKnowledge(@RequestParam(value = "userId", required = true) User user) {
        return userService.retardKnowledge(user);
    }
}
