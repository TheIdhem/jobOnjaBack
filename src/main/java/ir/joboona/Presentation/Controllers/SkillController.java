package ir.joboona.Presentation.Controllers;

import Solutions.Core.Dispatcher.RequestMethod;
import Solutions.Data.EntityManager;
import Solutions.Presentation.Controller.*;
import ir.joboona.Models.Knowledge;
import ir.joboona.Models.User;
import ir.joboona.Models.UserSkill;
import ir.joboona.Services.UserService;

import java.util.Objects;
import java.util.Set;

@RestController(basePath = "/user/skill")
public class SkillController {

    private final UserService userService;
    private final EntityManager entityManager = EntityManager.getInstance();

    public SkillController() {
        userService = UserService.getInstance();
    }


    @RequestMapping(method = RequestMethod.POST)
    public User addSkill(@RequestBody UserSkill skill,
                         @RequestAttribute("principal") User user) throws Exception {
        skill.setId(Objects.hash(user, skill.hashCode()));
        user.addSkill(skill);
        entityManager.save(user);
        return user;
    }

    @RequestMapping(path = "/{skillId}",method = RequestMethod.DELETE)
    public User removeSkill(@PathVariable("skillId") UserSkill skill,
                            @RequestAttribute("principal") User user) throws Exception {

        user.deleteSkill(skill);
        entityManager.save(user);
        return user;
    }

    @RequestMapping(path = "/retard",method = RequestMethod.GET)
    public Set<Knowledge> getRetardKnowledge(@RequestAttribute("principal") User user) throws Exception {
        return userService.retardKnowledge(user);
    }
}
