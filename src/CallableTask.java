
import java.util.concurrent.*;
import static java.lang.Thread.sleep;


public class CallableTask<T> implements Taskable, Future<T>, Runnable, Comparable<Taskable> {
    private final Callable<T> op;
    private final TaskType type;
    private boolean done = false;
    private boolean cancel = false;
    private T res;

    public CallableTask(Callable<T> op) {
        this.op = op;
        this.type = TaskType.IO;
        type.setTypePriority(2);
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
        long start = System.currentTimeMillis();
        long time = 0;
        while(isDone() == false){
           time = System.currentTimeMillis()-start;
           if(unit.convert(time, TimeUnit.MILLISECONDS) >= timeout){
               System.err.println(type.getType()+" task with priority: "+type.getTypePriority()+" timed out");
               this.done = true;
               return null;
           }
        }

        return this.res;
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
    public int compareTo(Taskable o) {
        return Integer.compare(this.getTaskPriority(),o.getTaskPriority());
    }

    @Override
    public int hashCode() {
        int result = (getTaskPriority() ^ (getTaskPriority() >>> 32));
        result = 31 * result + type.hashCode();
        result = 31 * result + op.hashCode();
        return result;
    }
}

