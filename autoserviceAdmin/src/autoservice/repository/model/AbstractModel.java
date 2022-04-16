package autoservice.repository.model;

public abstract class AbstractModel {
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
