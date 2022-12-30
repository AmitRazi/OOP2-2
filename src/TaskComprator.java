import java.util.Comparator;

public class TaskComprator implements Comparator<CallableTask> {

    @Override
    public int compare(CallableTask o1, CallableTask o2) {
        if (o1.getTaskPriority() > o2.getTaskPriority()) return 1;
        if (o1.getTaskPriority() < o2.getTaskPriority()) return -1;
        return 0;
    }
}
