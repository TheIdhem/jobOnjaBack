package ir.joboona.Models;

import Solutions.Data.Annotations.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Bid {

    @ManyToOne
    private User biddingUser;

    @ManyToOne
    private Project project;

    private Integer bidAmount;

    public Bid() {
    }

    public Bid(User biddingUser, Project project, Integer bidAmount) {
        this.biddingUser = biddingUser;
        this.project = project;
        this.bidAmount = bidAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bid)) return false;
        Bid bid = (Bid) o;
        return Objects.equals(getBiddingUser(), bid.getBiddingUser()) &&
                Objects.equals(getProject(), bid.getProject());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBiddingUser(), getProject());
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
    @JsonIgnore
    public Project getProject() {
        return project;
    }

    @JsonProperty(value = "projectTitle", required = true)
    public void setProject(Project project) {
        this.project = project;
    }
}
