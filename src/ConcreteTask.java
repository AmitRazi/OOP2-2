
import java.util.concurrent.*;

import static java.lang.Thread.sleep;


public class ConcreteTask<T> implements Runnable, Future<T>, Comparable<ConcreteTask> {
    private Callable<T> op;
     private TaskType type;
     private boolean done = false;
     private boolean cancel = false;
     private T res;

    public ConcreteTask(Callable<T> op){
         this.op = op;
         this.type = TaskType.IO;
         type.setTypePriority(5);
    }
    public ConcreteTask(Callable<T> op, TaskType type){
        this.op = op;
        this.type = type;
    }

    public int getTaskPriority(){
        return type.getTypePriority();
    }

    public TaskType getType(){
        return type.getType();
    }

    public <T> Callable<T> getOp(){
        return (Callable<T>) this.op;
    }
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if(isDone()) return false;
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
    public T get() throws InterruptedException, ExecutionException {
        while(isDone() == false){
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
            final T result = this.op.call();
            this.res = result;
            this.done = true;

        } catch (Exception e) {
            this.cancel = true;
        }
    }

    @Override
    public int compareTo(ConcreteTask o) {
        if(this.getTaskPriority() > o.getTaskPriority()) return 1;
        if(this.getTaskPriority() < o.getTaskPriority()) return -1;
        return 0;
    }
}
