

public class RunnableTask implements Task, Runnable, Comparable<Task> {
    private final Runnable op;
    private final TaskType type;

    public RunnableTask(Runnable op) {
        this.op = op;
        type = TaskType.OTHER;
        type.setTypePriority(2);
    }

    public RunnableTask(Runnable op, TaskType type) {
        this.op = op;
        this.type = type;
    }

    @Override
    public int getTaskPriority() {
        return type.getTypePriority();
    }


    @Override
    public int compareTo(Task o) {
        return Integer.compare(this.getTaskPriority(),o.getTaskPriority());
    }

    @Override
    public void run() {
        this.op.run();
    }
}
