package ir.joboona.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Auction {

    private Project project;

    @JsonProperty(value = "project")
    public Project getProject() {
        return project;
    }

    @JsonProperty(value = "projectId", required = true)
    public void setProject(Project project) {
        this.project = project;
    }
}
