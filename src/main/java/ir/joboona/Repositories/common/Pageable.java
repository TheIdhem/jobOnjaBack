package ir.joboona.Repositories.common;

public class Pageable {
    private Integer page;
    private Integer size;

    public Pageable(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public Integer getNextOffset(){
        return page * size;
    }

    public Integer getBaseOffset(){
        return (page - 1) * size;
    }

    public Integer getSize() {
        return size;
    }
}
