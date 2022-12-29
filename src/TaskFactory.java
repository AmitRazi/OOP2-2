import java.util.concurrent.Callable;

public class TaskFactory<T> {

    public static <T> CallableTask<T> createTask(Callable<T> op){
        return new CallableTask(op);
    }
    public static <T> CallableTask<T> createTask(Callable<T> op, TaskType t){
        return new CallableTask(op,t);
    }
}
