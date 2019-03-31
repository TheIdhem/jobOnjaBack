package ir.joboona.Presentation.Controllers.Presentation.Dtos;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import ir.joboona.Models.Project;

public class ProjectDto {

    @JsonUnwrapped
    private Project project;

    private boolean isBidding;

    public ProjectDto(boolean isBidding, Project project) {
        this.isBidding = isBidding;
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    public boolean isBidding() {
        return isBidding;
    }
}
