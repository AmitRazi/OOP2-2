
import java.util.concurrent.*;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;


public class CallableTask<T> implements Task, Future<T>, Runnable, Comparable<Task> {
    private final Callable<T> op;
    private final TaskType type;
    private boolean done = false;
    private boolean cancel = false;
    private T res;

    public CallableTask(Callable<T> op) {
        this.op = op;
        this.type = TaskType.IO;
        type.setTypePriority(5);
    }

    public CallableTask(Callable<T> op, TaskType type) {
        this.op = op;
        this.type = type;
    }

    public int getTaskPriority() {
        return type.getTypePriority();
    }

    public TaskType getType() {
        return type.getType();
    }


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (isDone()) return false;
        Thread.currentThread().interrupt();
        return true;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public boolean isDone() {
        return this.done;
    }

    @Override
    public T get() throws InterruptedException {
        while (isDone() == false) {
            sleep(10);
        }
        return this.res;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    @Override
    public void run() {
        try {
            this.res = this.op.call();
            this.done = true;

        } catch (Exception e) {
            this.cancel = true;
        }
    }

    @Override
    public int compareTo(Task o) {
        return Integer.compare(this.getTaskPriority(),o.getTaskPriority());
    }
}
