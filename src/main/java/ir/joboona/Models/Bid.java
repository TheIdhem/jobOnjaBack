package ir.joboona.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Bid {

    private User biddingUser;

    private Project project;

    private Integer bidAmount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bid bid = (Bid) o;
        return Objects.equals(biddingUser, bid.biddingUser) &&
                Objects.equals(project, bid.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(biddingUser, project);
    }

    public Integer getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(Integer bidAmount) {
        this.bidAmount = bidAmount;
    }

    public User getBiddingUser() {
        return biddingUser;
    }

    public void setBiddingUser(User biddingUser) {
        this.biddingUser = biddingUser;
    }

    @JsonProperty(value = "project")
    public Project getProject() {
        return project;
    }

    @JsonProperty(value = "projectTitle", required = true)
    public void setProject(Project project) {
        this.project = project;
    }
}
