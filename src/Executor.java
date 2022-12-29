import java.util.concurrent.Future;

public interface Executor {

    <T> Future<T> submit(Task<T> task);

    void shutdown();

}
