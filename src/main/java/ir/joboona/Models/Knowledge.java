package ir.joboona.Models;


import Solutions.Data.Annotations.Id;
import Solutions.Data.Entity;
import Solutions.Presentation.Parsers.EntityObjectIdResolver;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.Objects;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "name", scope = Knowledge.class, resolver = EntityObjectIdResolver.class)
public class Knowledge implements Entity {

    @Id
    private String name;

    public Knowledge() {
    }

    public Knowledge(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Knowledge knowledge = (Knowledge) o;
        return Objects.equals(name, knowledge.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
