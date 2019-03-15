package ir.joboona.Presentation.Controllers;

import Solutions.Core.Dispatcher.RequestMethod;
import Solutions.Data.Exceptions.EntityNotFound;
import Solutions.Presentation.Controller.*;
import ir.joboona.Models.Knowledge;
import ir.joboona.Models.Skill;
import ir.joboona.Models.User;

@RestController(basePath = "/user/{endorsee}/endorse")
public class EndorsementController {

    @RequestMapping(method = RequestMethod.POST)
    public Skill endorse(@PathVariable("endorsee") User endorsee, @RequestParam("userId") User endorser,
                        @RequestBody Knowledge knowledge){

        Skill skill = endorsee.getSkills().stream().filter(eachSkill -> eachSkill.getKnowledge().equals(knowledge))
                .findFirst().orElseThrow(EntityNotFound::new);

        skill.addEndorser(endorser);
        return skill;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public Skill unEndorse(@PathVariable("endorsee") User endorsee, @RequestParam("userId") User endorser,
                         @RequestBody Knowledge knowledge){

        Skill skill = endorsee.getSkills().stream().filter(eachSkill -> eachSkill.getKnowledge().equals(knowledge))
                .findFirst().orElseThrow(EntityNotFound::new);

        skill.removeEndorser(endorser);
        return skill;
    }

}
