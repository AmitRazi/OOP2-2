

public class RunnableTask implements Taskable, Runnable, Comparable<Taskable> {
    private final Runnable op;
    private final TaskType type;

    public RunnableTask(Runnable op) {
        this.op = op;
        type = TaskType.OTHER;
        type.setTypePriority(3);
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
    public int compareTo(Taskable o) {
        return Integer.compare(this.getTaskPriority(),o.getTaskPriority());
    }

    public int hashCode() {
        int result = (getTaskPriority() ^ (getTaskPriority() >>> 32));
        result = 31 * result + type.hashCode();
        result = 31 * result + op.hashCode();
        return result;
    }

    @Override
    public void run() {
        this.op.run();
    }
}
