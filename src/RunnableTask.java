import java.util.concurrent.CopyOnWriteArrayList;

public class RunnableTask implements Task,Runnable, Comparable<Task>{
    private Runnable op;
    private TaskType type;

    public RunnableTask(Runnable op){
        this.op = op;
        type = TaskType.OTHER;
        type.setTypePriority(2);
    }

    public RunnableTask(Runnable op, TaskType type){
        this.op = op;
        this.type = type;
    }
    @Override
    public int getTaskPriority() {
        return type.getTypePriority();
    }


    @Override
    public int compareTo(Task o) {
        if(this.getTaskPriority() > o.getTaskPriority()) return 1;
        if(this.getTaskPriority() < o.getTaskPriority()) return -1;
        return 0;
    }

    @Override
    public void run() {
        this.op.run();
    }
}
