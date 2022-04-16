package autoservice.utility;

public class IdDistributor {
    private static final IdDistributor instance = new IdDistributor();
    private static Long lastId = 0L;

    private IdDistributor() {}

    public static IdDistributor getInstance() {
        return instance;
    }

    public static Long getId() {
        lastId++;
        return lastId;
    }
}
