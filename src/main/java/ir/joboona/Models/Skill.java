package ir.joboona.Models;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Skill {

    @JsonProperty(required = true, value = "name")
    @JsonIdentityReference(alwaysAsId = true)
    private Knowledge knowledge;

    @JsonProperty(required = true)
    private Integer point;

    public Skill() {
    }

    public Skill(Knowledge knowledge, Integer points) {
        this.knowledge = knowledge;
        this.point = points;
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
