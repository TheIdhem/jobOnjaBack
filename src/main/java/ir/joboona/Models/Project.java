package ir.joboona.Models;

import Solutions.Data.Entity;
import Solutions.Presentation.Parsers.EntityObjectIdResolver;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.*;
import java.util.function.Function;

import static java.lang.Math.round;
import static java.lang.StrictMath.pow;
import static java.util.stream.Collectors.toMap;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = Project.class, resolver = EntityObjectIdResolver.class)


public class Project implements Entity {

    private String id;

    private String title;

    private String description;

    private String imageUrl;

    private Set<Skill> skills = new HashSet<>();

    private List<Bid> bids = new ArrayList<>();

    @JsonProperty(required = true)
    private Integer budget;

    private long deadline;

    private User winner;


    /**
     * Evaluates a set of skills based on project required skills.
     */
    public Integer evaluateSkillsAndOffers(Set<Skill> actualSkills, Integer offer) {

        Map<Knowledge, Skill> skillMap = actualSkills.stream().collect(toMap(Skill::getKnowledge, Function.identity()));

        double result = 0;
        for (Skill skill : this.skills) {
            Integer required = skill.getPoints();
            Integer actual = skillMap.get(skill.getKnowledge()).getPoints();
            result += 10000 * pow((actual - required), 2);
        }
        result += budget - offer;
        return (int) round(result);
    }

    public boolean sufficientSkills(Set<Skill> actualSkills) {

        if (!actualSkills.containsAll(this.skills))
            return false;

        Map<Knowledge, Skill> skillMap = actualSkills.stream().collect(toMap(Skill::getKnowledge, Function.identity()));

        return this.skills.stream().allMatch(skill ->
                skillMap.get(skill.getKnowledge()).getPoints() >= skill.getPoints()
        );

    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
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

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
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

    public void addBid(Bid bid) {
        bids.add(bid);
    }

    public void setId(String id) {
        this.id = id;
    }
}
