import java.sql.SQLOutput;

import static java.lang.Thread.currentThread;


public class ConcreteTask<T> implements Task<T> {
    private Operation<T> op;
     private TaskType type;

    public ConcreteTask(Operation<T> op){
         this.op = op;
    }
    public ConcreteTask(Operation<T> op, TaskType type){
        this.op = op;
        this.type = type;
    }

    public int getTaskPriority(){
        return type.getTypePriority();
    }

    public TaskType getType(){
        return type.getType();
    }


    @Override
    public T call() throws Exception {
        return op.Operation();
    }
}
