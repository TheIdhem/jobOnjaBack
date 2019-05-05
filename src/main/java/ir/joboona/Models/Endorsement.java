package ir.joboona.Models;

import Solutions.Data.Annotations.ManyToOne;

import java.util.Objects;

public class Endorsement {

    public Endorsement() {
    }

    public Endorsement(User endorser) {
        this.endorser = endorser;
    }

    @ManyToOne
    private User endorser;

    public User getEndorser() {
        return endorser;
    }

    public void setEndorser(User endorser) {
        this.endorser = endorser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Endorsement)) return false;
        Endorsement that = (Endorsement) o;
        return Objects.equals(getEndorser(), that.getEndorser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEndorser());
    }
}
