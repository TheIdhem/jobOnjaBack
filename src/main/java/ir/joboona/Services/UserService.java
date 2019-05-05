package ir.joboona.Services;

import Solutions.Data.EntityManager;
import ir.joboona.Models.Knowledge;
import ir.joboona.Models.User;
import ir.joboona.Models.UserSkill;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class UserService {

    private static UserService instance;

    private final EntityManager entityManager = EntityManager.getInstance();

    public static UserService getInstance() {
        if (instance == null)
            instance = new UserService();
        return instance;
    }

    public User create(User user) throws Exception {
        return entityManager.save(user);
    }


    public Set<Knowledge> retardKnowledge(User user) throws Exception {

        Set<Knowledge> retardSkills = new HashSet<>(entityManager.getAll(Knowledge.class));
        retardSkills.removeAll(user.getSkills().stream().map(UserSkill::getKnowledge).collect(toSet()));
        return retardSkills;
    }
}
