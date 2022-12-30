import java.util.concurrent.Callable;

public class TaskFactory {

    public static <T> CallableTask<T> createTask(Callable<T> op){
        return new CallableTask<>(op);
    }
    public static <T> CallableTask<T> createTask(Callable<T> op, TaskType type){
        return new CallableTask<>(op,type);
    }

    public static RunnableTask createTask(Runnable op){
        return new RunnableTask(op);
    }

    public static RunnableTask createTask(Runnable op, TaskType type){
        return new RunnableTask(op,type);
    }
}
