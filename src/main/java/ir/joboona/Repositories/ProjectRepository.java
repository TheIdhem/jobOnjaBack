package ir.joboona.Repositories;

import Solutions.Data.BeanMapper;
import Solutions.Data.EntityManager;
import ir.joboona.Models.Project;
import ir.joboona.Models.User;
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

    public Page<Project> allQualifingProjectsLikeOrderedByCreationDate(Pageable pageable, String q, User user) throws Exception {

        return entityManager.queryForObject(connection -> {

            String allowed = "(SELECT p.id AS project FROM Project AS p" +
                    " WHERE p.title LIKE ? OR p.description LIKE ? EXCEPT " +
                    "SELECT p.project FROM ProjectSkill AS p JOIN UserSkill AS u " +
                    "ON p.knowledge = u.knowledge " +
                    "WHERE p.point > u.point EXCEPT " +
                    "SELECT p.project FROM ProjectSkill AS p " +
                    "WHERE p.knowledge NOT IN (SELECT u.knowledge FROM UserSkill AS u WHERE u.user = ?)) AS Allowed";
            PreparedStatement countStatement = connection
                    .prepareStatement("SELECT COUNT(*) FROM " + allowed);
            String search = q == null ? "%%" : "%"+q+"%";
            countStatement.setString(1, search);
            countStatement.setString(2, search);
            countStatement.setInt(3, user.getId());
            Integer count = countStatement.executeQuery().getInt(1);

            PreparedStatement statement = connection
                    .prepareStatement("SELECT Project.* FROM Project JOIN " + allowed + " ON Allowed.project = Project.id "+
                            "ORDER BY creationDate DESC LIMIT ?,?");
            statement.setString(1, search);
            statement.setString(2, search);
            statement.setInt(3, user.getId());
            statement.setInt(4, pageable.getBaseOffset());
            statement.setInt(5, pageable.getSize());
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
