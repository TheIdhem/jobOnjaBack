package ir.joboona.Models;

import Solutions.Data.Annotations.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class ProjectSkill {

    @ManyToOne
    @JsonProperty(required = true, value = "name")
    @JsonIdentityReference(alwaysAsId = true)
    private Knowledge knowledge;

    private Integer point = 0;

    public ProjectSkill() {
    }

    public ProjectSkill(Knowledge knowledge, Integer point) {
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
        if (!(o instanceof ProjectSkill)) return false;
        ProjectSkill skill = (ProjectSkill) o;
        return Objects.equals(getKnowledge(), skill.getKnowledge());
    }

    @Override
    public int hashCode() {
        return Objects.hash(knowledge);
    }
}
