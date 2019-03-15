package ir.joboona.Services;

import ir.joboona.Models.Knowledge;
import ir.joboona.Models.Skill;
import ir.joboona.Models.User;
import ir.joboona.Repositories.KnowledgeRepository;
import ir.joboona.Repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class UserService {

    private static UserService instance;

    private UserService(){
        userRepository = UserRepository.getInstance();
        knowledgeRepository = KnowledgeRepository.getInstance();
    }

    private final UserRepository userRepository;
    private final KnowledgeRepository knowledgeRepository;

    public static UserService getInstance() {
        if (instance == null)
            instance = new UserService();
        return instance;
    }

    public User create(User user){
        return userRepository.save(user);
    }


    public Set<Knowledge> retardKnowledge(User user) {

        Set<Knowledge> retardSkills = new HashSet<>(knowledgeRepository.getAll());
        retardSkills.removeAll(user.getSkills().stream().map(Skill::getKnowledge).collect(toSet()));
        return retardSkills;
    }
}
