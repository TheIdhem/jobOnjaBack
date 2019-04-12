package ir.joboona.Presentation.Controllers.Presentation.Dtos;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import ir.joboona.Models.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserDto {

    @JsonUnwrapped
    private User targetUser;

    private User visitor;

    public UserDto() {
    }

    public UserDto(User targetUser, User visitor) {
        this.targetUser = targetUser;
        this.visitor = visitor;
    }

    public Set<SkillDto> getSkills(){
        return targetUser.getSkills().stream()
                .map(skill -> new SkillDto(skill, visitor)).collect(Collectors.toSet());
    }


}
