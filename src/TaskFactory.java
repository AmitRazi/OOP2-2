import java.util.concurrent.Callable;

public class TaskFactory<T> {

    public static <T> ConcreteTask<T> createTask(Callable<T> op){
        return new ConcreteTask(op);
    }
    public static <T> ConcreteTask<T> createTask(Callable<T> op,TaskType t){
        return new ConcreteTask(op,t);
    }
}
