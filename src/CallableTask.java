import java.util.concurrent.*;
import static java.lang.Thread.sleep;

/**
 * This class represents a task that returns a value.
 * The task implements Taskable to allow priority comparisons with other task types such as the RunnableTask.
 * @param <T> - The type of value to be returned.
 */
public class CallableTask<T> implements Taskable, Future<T>, Runnable {
    private final Callable<T> op;
    private final TaskType type;
    private boolean done = false;
    private boolean cancel = false;
    private T res;


    /**
     * Parametrized constructor.
     * This constructor allows the omission of TaskType, it adds a default TaskType.
     * @param op - A callable operation.
     */
    public CallableTask(Callable<T> op) {
        this.op = op;
        this.type = TaskType.IO;
        type.setTypePriority(2);
    }

    /**
     * Parametrized constructor.
     * This constructor takes both the callable operation and a TaskType.
     * @param op - A callable opertion.
     * @param type - TaskType specification
     */
    public CallableTask(Callable<T> op, TaskType type) {
        this.op = op;
        this.type = type;
    }

    /**
     * Implementation of the method in the interface Taskable.
     * @return The priority in TaskType
     */
    @Override
    public int getTaskPriority() {
        return type.getTypePriority();
    }

    /**
     * @return returns the type of task.
     */
    public TaskType getType() {
        return type.getType();
    }


    /**
     * @param mayInterruptIfRunning {@code true} if the thread
     * executing this task should be interrupted (if the thread is
     * known to the implementation); otherwise, in-progress tasks are
     * allowed to complete
     * @return false of the task has already been completed, true otherwise.
     */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (isDone()) return false;
        Thread.currentThread().interrupt();
        return true;
    }

    /**
     * @return true if the task execution was canceled, false otherwise.
     */
    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    /**
     * @return true if the task has finished, false otherwise.
     */
    @Override
    public boolean isDone() {
        return this.done;
    }

    /**
     * @return The results of the callable operation.
     * @throws InterruptedException
     */
    @Override
    public T get() throws InterruptedException {
        while (isDone() == false) {
            sleep(10);
        }
        return this.res;
    }

    /**
     * This method returns the result of the callable operation but wait for a specified amount of times.
     * @param timeout the maximum time to wait
     * @param unit the time unit of the timeout argument
     * @return The results of the callable operation
     * @throws InterruptedException if the current thread was interrupted while waiting
     * @throws ExecutionException if the computation threw an exception
     * @throws TimeoutException if the wait timed out
     */
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
    public int hashCode() {
        int result = (getTaskPriority() ^ (getTaskPriority() >>> 32));
        result = 31 * result + type.hashCode();
        result = 31 * result + op.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CallableTask<?> that)) return false;

        if (done != that.done) return false;
        if (cancel != that.cancel) return false;
        if(((CallableTask<?>) o).getTaskPriority() != that.getTaskPriority()) return false;
        return type == that.type;
    }


    @Override
    public String toString() {
        return "CallableTask{" +
                "type=" + type +
                ", priority=" +getTaskPriority()+
                ", done=" + done +
                ", cancel=" + cancel +
                ", res=" + res +
                '}';
    }
}