import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        ConcreteTask<Integer> task = TaskFactory.createTask(() -> {
            int sum = 0;
            for(int i = 0 ; i < 100 ; i++){
                sum+=i;
            }
            return sum;
        });
        ConcreteTask<String> task2 = TaskFactory.createTask(() -> {
            return "HELLO ITS ME";
        });
        CustomExecutor service = new CustomExecutor();
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