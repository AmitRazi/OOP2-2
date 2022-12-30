import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public class Main {

    public static void main(String[] args) {


        RunnableTask runnabletask = new RunnableTask(() -> System.out.println("Hello from"+Thread.currentThread().getName()));
        CallableTask<Integer> task = TaskFactory.createTask(() -> {
            int sum = 0;
            for(int i = 0 ; i < 100 ; i++){
                sum+=i;
            }
            return sum;
        });
        CallableTask<String> task2 = TaskFactory.createTask(() -> {
            sleep(1000);
            return null;
        });
        CustomExecutor service = new CustomExecutor();

        Future<Integer> sumbit = service.submit(task);
        TaskType t = TaskType.COMPUTATIONAL;
        t.setTypePriority(1);
        RunnableTask task3 = TaskFactory.createTask(new RunnableTask(() -> {
            for(int i = 0 ; i < 10000 ; i++){
                System.out.println(i);
            }
        }),t);
        service.submit(task3);
        Future<String> sumbit2 = service.submit(task2);
        try {
            System.out.println(sumbit.get());
            System.out.println(sumbit2.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        service.shutdown();
    }
}