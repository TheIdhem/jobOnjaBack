package ir.joboona.Presentation.Controllers;

import Solutions.Core.Dispatcher.RequestMethod;
import Solutions.Data.EntityManager;
import Solutions.Presentation.Controller.*;
import ir.joboona.Models.User;
import ir.joboona.Models.UserSkill;
import ir.joboona.Presentation.Dtos.UserSkillDto;

@RestController(basePath = "/endorse")
public class EndorsementController {

    private EntityManager entityManager = EntityManager.getInstance();

    @RequestMapping(method = RequestMethod.POST)
    public UserSkillDto endorse(@RequestParam("skillId") UserSkill skill, @RequestParam("userId") User endorser) throws Exception {

        skill.addEndorser(endorser);
        entityManager.save(skill);
        return new UserSkillDto(skill, endorser);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public UserSkillDto unEndorse(@RequestParam("skillId") UserSkill skill, @RequestParam("userId") User endorser) throws Exception {

        skill.removeEndorser(endorser);
        entityManager.save(skill);
        return new UserSkillDto(skill, endorser);
    }

}
