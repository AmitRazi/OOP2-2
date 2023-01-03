import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public class Main {

    public static void main(String[] args) throws InterruptedException {


        RunnableTask runnabletask = new RunnableTask(() -> System.out.println("Hello from" + Thread.currentThread().getName()));
        CallableTask<Integer> task = TaskFactory.createTask(() -> {
            int sum = 0;
            for (int i = 0; i < 10999990; i++) {
                sum += i;
            }
            return sum;
        });
        CallableTask<Integer> task3 = TaskFactory.createTask(() -> {
            int sum = 0;
            for (int i = 0; i < 10999990; i++) {
                sum += i;
            }
            return sum;
        });
        CallableTask<String> task2 = TaskFactory.createTask(() -> {
            sleep(1000);
            return null;
        });
        CallableTask<String> task4 = TaskFactory.createTask(() -> {
            sleep(1000);
            return null;
        });
        CallableTask<String> task5 = TaskFactory.createTask(() -> {
            sleep(1000);
            return null;
        });
        CallableTask<String> task6 = TaskFactory.createTask(() -> {
            sleep(1000);
            return null;
        });
        CustomExecutor service = new CustomExecutor();

        Future<Integer> sumbit = service.submit(task);
        TaskType t = TaskType.COMPUTATIONAL;
        t.setTypePriority(10);

        Future<String> sumbit2 = service.submit(task2);
        Future<String> sumbit4 = service.submit(task4);
        Future<Integer> sumbit3 = service.submit(task3);
        Future<String> sumbit6 = service.submit(task6);
        Future<String> sumbit5 = service.submit(task5);

        service.submit(new RunnableTask(() -> System.out.println("hello")));
        service.submit(new RunnableTask(() -> System.out.println("hello")));

        service.submit(new RunnableTask(() -> System.out.println("hello")));
        service.submit(new RunnableTask(() -> System.out.println("hello")));
        service.submit(new RunnableTask(() -> System.out.println("hello")));
        service.submit(new RunnableTask(() -> System.out.println("hello")));
        service.submit(new RunnableTask(() -> System.out.println("hello")));

        service.submit(new RunnableTask(() -> System.out.println("hello")));
        try {
            System.out.println(sumbit.get());
            System.out.println(sumbit2.get());
            System.out.println(sumbit3.get());
            System.out.println(sumbit4.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
       sumbit2 = service.submit(task2);
        sumbit4 = service.submit(task4);
         sumbit3 = service.submit(task3);
         sumbit6 = service.submit(task6);
         sumbit5 = service.submit(task5);


        service.shutdown();
    }
}