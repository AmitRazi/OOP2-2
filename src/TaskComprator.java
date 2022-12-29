import java.util.Comparator;

public class TaskComprator implements Comparator<ConcreteTask> {

    @Override
    public int compare(ConcreteTask o1, ConcreteTask o2) {
        if(o1.getTaskPriority() > o2.getTaskPriority()) return 1;
        if(o1.getTaskPriority() < o2.getTaskPriority()) return -1;
        return 0;
    }
}
