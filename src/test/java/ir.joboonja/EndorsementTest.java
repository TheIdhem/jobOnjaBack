package ir.joboonja;

import Solutions.Core.SolutionsApplication;
import ir.joboona.Models.User;
import ir.joboona.Models.UserSkill;
import ir.joboona.Presentation.Controllers.EndorsementController;
import ir.joboona.Repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

public class EndorsementTest {

    private UserRepository userRepository;

    @Before
    public void setup() throws Exception {
        SolutionsApplication.getInstance().run();
        userRepository = UserRepository.getInstance();
    }

    @Test
    public void test1() throws Exception {
        EndorsementController endorsementController = new EndorsementController();

        User endorsee = userRepository.findUserByUsername("mryf").get();
        User endorser = userRepository.findUserByUsername("golnar").get();

        Optional<UserSkill> userSkill = endorsee.getSkills().stream()
                .filter(skill -> skill.getKnowledge().getName().equals("Java")).findFirst();
        endorsementController.endorse(userSkill.get(), endorser);

    }
}
