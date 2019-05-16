package ir.joboona.Models;

import Solutions.Data.Annotations.CollectionTable;
import Solutions.Data.Annotations.Id;

import Solutions.Data.Annotations.ManyToOne;
import Solutions.Data.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.joboona.Exceptions.BidExceptions.BudgetOverflow;
import ir.joboona.Exceptions.BidExceptions.InsufficientSkill;

import java.util.*;
import java.util.function.Function;

import static java.lang.Math.round;
import static java.lang.StrictMath.pow;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toMap;

public class Project implements Entity {

    @Id
    private String id;

    private String title;

    private String description;

    private String imageUrl;

    @CollectionTable(name="ProjectSkill",joinColumn="project")
    private Set<ProjectSkill> skills;

    @CollectionTable(name="Bid",joinColumn="project")
    private Set<Bid> bids;

    @JsonProperty(required = true)
    private Integer budget;

    private Long deadline;

    @ManyToOne
    private User winner;

    private Long creationDate;


    /**
     * Evaluates a set of skills based on project required skills.
     */
    private Integer evaluateSkillsAndOffers(Set<UserSkill> actualSkills, Integer offer) {

        Map<Knowledge, UserSkill> skillMap = actualSkills.stream().collect(toMap(UserSkill::getKnowledge, Function.identity()));

        double result = 0;
        for (ProjectSkill skill : this.getSkills()) {
            Integer required = skill.getPoint();
            Integer actual = skillMap.get(skill.getKnowledge()).getPoint();
            result += 10000 * pow((actual - required), 2);
        }
        result += budget - offer;
        return (int) round(result);
    }

    public boolean sufficientSkills(Set<UserSkill> actualSkills) {

        Map<Knowledge, UserSkill> skillMap = actualSkills.stream().collect(toMap(UserSkill::getKnowledge, Function.identity()));

        return this.getSkills().stream().allMatch(
                skill -> skillMap.containsKey(skill.getKnowledge()) &&
                skillMap.get(skill.getKnowledge()).getPoint() >= skill.getPoint()
        );
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        Project project = (Project) o;
        return Objects.equals(title, project.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<ProjectSkill> getSkills() {
        return skills;
    }

    public void setSkills(Set<ProjectSkill> skills) {
        this.skills = skills;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public void addBid(Bid bid) {
        if (!bid.getProject().sufficientSkills(bid.getBiddingUser().getSkills()))
            throw new InsufficientSkill();

        if (bid.getBidAmount() > bid.getProject().getBudget())
            throw new BudgetOverflow();
        this.getBids().add(bid);
    }

    public void performAuction() {
        Optional<Bid> winningBid =  getBids().stream().max(comparing(bid ->
                this.evaluateSkillsAndOffers(bid.getBiddingUser().getSkills(), bid.getBidAmount())
        ));
        if (winningBid.isPresent() && this.getWinner() == null)
            this.winner = winningBid.get().getBiddingUser();
    }
}
