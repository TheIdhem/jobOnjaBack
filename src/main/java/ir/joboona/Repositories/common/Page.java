package ir.joboona.Repositories.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;

public class Page<T> {

    private Collection<T> results;
    private Pageable pageable;
    private Integer count;

    public Page() {
    }

    public Page(Collection<T> results, Pageable pageable, Integer count) {
        this.results = results;
        this.pageable = pageable;
        this.count = count;
    }

    public Collection<T> getResults() {
        return results;
    }

    public Boolean isHasNext() {
        return pageable.getNextOffset() < count;
    }

    @JsonIgnore
    public Pageable getPageable() {
        return pageable;
    }

    @JsonIgnore
    public Integer getCount() {
        return count;
    }
}
