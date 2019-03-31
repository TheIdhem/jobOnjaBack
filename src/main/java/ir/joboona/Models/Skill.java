package ir.joboona.Models;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Skill {

    @JsonProperty(required = true, value = "name")
    @JsonIdentityReference(alwaysAsId = true)
    private Knowledge knowledge;

    private Integer point = 0;

    private Set<User> usersEndorsed = new HashSet<>();

    public Set<User> getUsersEndorsed() {
        return usersEndorsed;
    }

    public void setUsersEndorsed(Set<User> usersEndorsed) {
        this.usersEndorsed = usersEndorsed;
    }

    public void addEndorser(User user){
        if(this.usersEndorsed.add(user))
            point++;
    }

    public void removeEndorser(User user){
        if(this.usersEndorsed.remove(user))
            point--;
    }

    public Skill() {
    }

    public Skill(Knowledge knowledge, Integer point) {
        this.knowledge = knowledge;
        this.point = point;
    }

    public Knowledge getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(Knowledge knowledge) {
        this.knowledge = knowledge;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
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
