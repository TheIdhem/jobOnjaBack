package ir.joboona.Exceptions;

import java.util.Set;

public class DuplicateItemException extends RuntimeException{

    private Class<?> type;
    private Set<String> fields;

    public DuplicateItemException() {
    }

    public DuplicateItemException(Class<?> type, Set<String> fields) {
        this.type = type;
        this.fields = fields;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Set<String> getFields() {
        return fields;
    }

    public void setFields(Set<String> fields) {
        this.fields = fields;
    }
}
