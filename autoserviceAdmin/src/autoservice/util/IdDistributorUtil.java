package autoservice.util;

public class IdDistributorUtil {
    private static final IdDistributorUtil instance = new IdDistributorUtil();
    private static Long lastId = 0L;

    private IdDistributorUtil() {}

    public static IdDistributorUtil getInstance() {
        return instance;
    }

    public static Long getId() {
        lastId++;
        return lastId;
    }
}
