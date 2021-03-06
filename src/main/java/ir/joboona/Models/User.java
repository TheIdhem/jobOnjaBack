package ir.joboona.Models;

import Solutions.Data.*;
import Solutions.Data.Annotations.Id;
import Solutions.Data.Annotations.JoinColumn;
import Solutions.Data.Annotations.OneToMany;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;



public class User implements Entity{

    @Id
    @AutoIncrement
    private Integer id;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.DELETE})
    @JoinColumn(name="user", referencedColumnName="id")
    private Set<UserSkill> skills;

    private String firstName;

    private String lastName;

    private String jobTitle;

    private String profilePictureURL;

    private String bio;

    public User() {
    }

    public User(String username, String password, String firstName, String lastName, Set<UserSkill> skills, String jobTitle, String bio, String profilePictureURL) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        skills.forEach(skill -> skill.setUser(this));
        this.skills = skills;
        this.jobTitle = jobTitle;
        this.bio = bio;
        this.profilePictureURL = profilePictureURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public Set<UserSkill> getSkills() {
        return skills;
    }

    public void setSkills(Set<UserSkill> skills) {
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

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void deleteSkill(UserSkill skill) {
        this.getSkills().remove(skill);
    }

    public void addSkill(UserSkill skill) {
        skill.setUser(this);
        skill.setEndorsements(new HashSet<>());
        this.getSkills().add(skill);
    }
}
