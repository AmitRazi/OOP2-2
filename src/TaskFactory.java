public class TaskFactory<T> {

    public static <T> ConcreteTask<T> createTask(Operation<T> op){
        return new ConcreteTask(op);
    }
    public static <T> ConcreteTask<T> createTask(Operation<T> op,TaskType t){
        return new ConcreteTask(op,t);
    }
}
