package ir.joboona.Repositories;

import Solutions.Data.BeanMapper;
import Solutions.Data.EntityManager;
import ir.joboona.Models.User;
import ir.joboona.Repositories.common.Page;
import ir.joboona.Repositories.common.Pageable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class UserRepository {

    private static UserRepository instance;
    private final EntityManager entityManager = EntityManager.getInstance();

    public static UserRepository getInstance() {
        if (instance == null)
            instance = new UserRepository();
        return instance;
    }

    public Page<User> getAllUsers(Pageable pageable) throws Exception {

        return entityManager.queryForObject(connection -> {
            PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) FROM User");
            Integer count = countStatement.executeQuery().getInt(1);

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM User LIMIT ?, ?");
            statement.setInt(1, pageable.getBaseOffset());
            statement.setInt(2, pageable.getSize());
            ResultSet resultSet = statement.executeQuery();
            BeanMapper beanMapper = new BeanMapper();
            Set<User> users = new HashSet<>();
            while (resultSet.next())
                users.add(beanMapper.getForObject(resultSet, "User", User.class));
            resultSet.close();

            return new Page<>(users, pageable, count);

        });
    }

    public Page<User> getAllUsersLike(Pageable pageable, String q) throws Exception {

        return entityManager.queryForObject(connection -> {
            PreparedStatement countStatement = connection
                    .prepareStatement("SELECT COUNT(*) FROM User WHERE firstName LIKE ? OR lastName LIKE ?");
            countStatement.setString(1, "%" + q + "%");
            countStatement.setString(2, "%" + q + "%");
            Integer count = countStatement.executeQuery().getInt(1);

            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM User WHERE firstName LIKE ? OR lastName LIKE ? LIMIT ?,?");
            statement.setString(1, "%" + q + "%");
            statement.setString(2, "%" + q + "%");
            statement.setInt(3, pageable.getBaseOffset());
            statement.setInt(4,pageable.getSize());
            ResultSet resultSet = statement.executeQuery();
            BeanMapper beanMapper = new BeanMapper();
            Set<User> users = new HashSet<>();
            while (resultSet.next())
                users.add(beanMapper.getForObject(resultSet, "User", User.class));
            resultSet.close();

            return new Page<>(users, pageable, count);

        });
    }
}
