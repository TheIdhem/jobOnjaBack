package ir.joboona.Presentation.Controllers.Presentation.Dtos;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import ir.joboona.Models.Skill;
import ir.joboona.Models.User;

public class SkillDto {
        
        @JsonUnwrapped
        private Skill skill;
        
        private User visitor;
        
        public SkillDto() {
        }

        public SkillDto(Skill skill, User visitor) {
            this.skill = skill;
            this.visitor = visitor;
        }

        public SkillDto(Skill skill) {
            this.skill = skill;
        }
        
        public boolean isEndorsed(){
            return skill.getUsersEndorsed().contains(visitor);
        }
    }