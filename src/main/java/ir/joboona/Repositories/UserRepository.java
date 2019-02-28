package ir.joboona.Repositories;

import Solutions.Data.EntityRepository;
import ir.joboona.Models.User;

import java.util.HashSet;
import java.util.Set;

public class UserRepository implements EntityRepository<User, String> {

    private static UserRepository instance;
    private Set<User> users;

    private UserRepository(){
        users = new HashSet<>();
    }

    public static UserRepository getInstance(){
        if (instance == null)
            instance = new UserRepository();
        return instance;
    }

    @Override
    public User save(User user){

        users.add(user);
        return user;
    }

    @Override
    public Set<User> getAll() {
        return users;
    }

}
