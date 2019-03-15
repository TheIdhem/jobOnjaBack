package ir.joboona.Models;

import Solutions.Data.Entity;
import Solutions.Presentation.Parsers.EntityObjectIdResolver;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;



@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = User.class, resolver = EntityObjectIdResolver.class)

public class User implements Entity {

    private String id;

    private Set<Skill> skills;

    private String firstName;

    private String lastName;

    private String jobTitle;

    private String profilePictureURL;

    private String bio;

    public User() {
        this.skills = new HashSet<>();
    }

    public User(String id, String firstName, String lastName, Set<Skill> skills, String jobTitle, String bio,String profilePictureURL) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.skills = skills;
        this.jobTitle = jobTitle;
        this.bio = bio;
        this.profilePictureURL = profilePictureURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void deleteSkill(Skill skill) {
        this.skills.remove(skill);
    }

    public void addSkill(Skill skill) {
        this.skills.add(skill);
    }
}
