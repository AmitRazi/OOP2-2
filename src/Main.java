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
            sleep(5000);
            return null;
        });
        CustomExecutor service = new CustomExecutor();
        service.submit(runnabletask);
       Future<Integer> sumbit = service.submit(task);
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