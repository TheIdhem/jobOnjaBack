package ir.joboona.Services;

import ir.joboona.Models.User;
import ir.joboona.Repositories.UserRepository;

public class UserService {

    private static UserService instance;
    private  UserService(){}
    private UserRepository userRepository = UserRepository.getInstance();

    public static UserService getInstance() {
        if (instance == null)
            instance = new UserService();
        return instance;
    }

    public User create(User user){
        return userRepository.save(user);
    }
}
