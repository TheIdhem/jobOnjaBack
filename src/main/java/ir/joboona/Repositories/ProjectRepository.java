package ir.joboona.Repositories;

import Solutions.Data.BeanMapper;
import Solutions.Data.EntityManager;
import ir.joboona.Models.Project;
import ir.joboona.Repositories.common.Page;
import ir.joboona.Repositories.common.Pageable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class ProjectRepository {

    private static ProjectRepository instance;
    private final EntityManager entityManager = EntityManager.getInstance();

    public static ProjectRepository getInstance() {
        if (instance == null)
            instance = new ProjectRepository();
        return instance;
    }

    public Page<Project> allProjectsOrderedByCreationDate(Pageable pageable) throws Exception {

        return entityManager.queryForObject(connection -> {
            PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) FROM Project");
            Integer count = countStatement.executeQuery().getInt(1);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Project ORDER BY creationDate DESC LIMIT ?,?");
            statement.setInt(1, pageable.getBaseOffset());
            statement.setInt(2,pageable.getSize());
            ResultSet rs = statement.executeQuery();
            List<Project> results = new ArrayList<>();
            BeanMapper beanMapper = new BeanMapper();
            while (rs.next())
                results.add(beanMapper.getForObject(rs, "Project", Project.class));
            return new Page<>(results, pageable, count);
        });
    }

    public Page<Project> allProjectsLikeOrderedByCreationDate(Pageable pageable, String q) throws Exception {

        return entityManager.queryForObject(connection -> {
            PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) FROM Project WHERE title LIKE ? OR description LIKE ?");
            countStatement.setString(1, "%" + q + "%");
            countStatement.setString(2, "%" + q + "%");
            Integer count = countStatement.executeQuery().getInt(1);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Project WHERE title LIKE ? OR description LIKE ? ORDER BY creationDate DESC LIMIT ?,?");
            statement.setString(1, "%" + q + "%");
            statement.setString(2, "%" + q + "%");
            statement.setInt(3, pageable.getBaseOffset());
            statement.setInt(4,pageable.getSize());
            ResultSet rs = statement.executeQuery();
            List<Project> results = new ArrayList<>();
            BeanMapper beanMapper = new BeanMapper();
            while (rs.next())
                results.add(beanMapper.getForObject(rs, "Project", Project.class));
            rs.close();
            return new Page<>(results, pageable, count);
        });
    }


}
