import java.util.concurrent.*;

import static java.lang.Thread.currentThread;

public class Main {

    public static void main(String[] args) {
        TaskType t = TaskType.COMPUTATIONAL;
        t.setTypePriority(8);
        ConcreteTask<Integer> task = TaskFactory.createTask(() ->{
            int sum = 0;
            for(int i = 0 ; i < 100 ;i++){
                sum+=i;
            } return sum;
                },t);
        ExecutorService service = Executors.newFixedThreadPool(1);
        Future<Integer> res = service.submit(task);
        try {
            System.out.println(res.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        service.shutdown();
    }
}