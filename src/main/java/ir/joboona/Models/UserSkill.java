package ir.joboona.Models;

import Solutions.Data.Annotations.CollectionTable;
import Solutions.Data.Annotations.ManyToOne;
import Solutions.Data.Entity;
import Solutions.Data.Annotations.Id;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Set;

public class UserSkill implements Entity {

    @Id
    private Integer id;

    @ManyToOne
    @JsonProperty(required = true, value = "name")
    @JsonIdentityReference(alwaysAsId = true)
    private Knowledge knowledge;

    private Integer point = 0;

    @ManyToOne
    @JsonIgnore
    private User user;

    @JsonIgnore
    @CollectionTable(name = "Endorsement", joinColumn = "userSkill")
    private Set<Endorsement> endorsements;

    public Set<Endorsement> getEndorsements() {
        return endorsements;
    }

    public void setEndorsements(Set<Endorsement> endorsements) {
        this.endorsements = endorsements;
    }

    public void addEndorser(User user){
        if(this.getEndorsements().add(new Endorsement(user)))
            point++;
    }

    public void removeEndorser(User user){
        if(this.getEndorsements().remove(new Endorsement(user)))
            point--;
    }

    public UserSkill() {
    }

    public UserSkill(Integer id, Knowledge knowledge, Integer point) {
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSkill)) return false;
        UserSkill skill = (UserSkill) o;
        return Objects.equals(getKnowledge(), skill.getKnowledge());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKnowledge(), getUser());
    }
}
