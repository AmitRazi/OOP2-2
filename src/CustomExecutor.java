import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;

public class CustomExecutor implements Executor{
    PriorityBlockingQueue<Task<?>> queue;
    public void dosomething(){

    }

    @Override
    public <T> Future<T> submit(Task<T> task) {
        Future<T> res
    }

    @Override
    public void shutdown() {

    }
}
