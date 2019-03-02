package ir.joboona.Models;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.jws.soap.SOAPBinding;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Skill {

    @JsonProperty(required = true, value = "name")
    @JsonIdentityReference(alwaysAsId = true)
    private Knowledge knowledge;

    @JsonProperty(required = true)
    private Integer point;

    private Set<User> usersEndorsed;


    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Set<User> getUsersEndorsed() {
        return usersEndorsed;
    }

    public void setUsersEndorsed(Set<User> usersEndorsed) {
        this.usersEndorsed = usersEndorsed;
    }

    public void setUserEndorsed(User user){
        this.usersEndorsed.add(user);
    }

    public void deleteUserEndorsed(User user){
        this.usersEndorsed.remove(user);
    }

    public Skill() {
    }

    public Skill(Knowledge knowledge, Integer points) {
        this.knowledge = knowledge;
        this.point = points;
        this.usersEndorsed = new HashSet<>();
    }

    public Knowledge getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(Knowledge knowledge) {
        this.knowledge = knowledge;
    }

    public Integer getPoints() {
        return point;
    }

    public void setPoints(Integer points) {
        this.point = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return Objects.equals(knowledge, skill.knowledge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(knowledge);
    }
}
