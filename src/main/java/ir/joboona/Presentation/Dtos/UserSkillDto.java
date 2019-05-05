package ir.joboona.Presentation.Dtos;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import ir.joboona.Models.User;
import ir.joboona.Models.UserSkill;

public class UserSkillDto {

    public UserSkillDto() {
    }

    public UserSkillDto(UserSkill skill, User visitor) {
        this.skill = skill;
        this.visitor = visitor;
    }

    @JsonUnwrapped
    private UserSkill skill;

    private User visitor;

    public boolean isEndorsed() {
        return skill.getEndorsements().stream()
                .anyMatch(endorsement -> endorsement.getEndorser().equals(visitor));
    }
}