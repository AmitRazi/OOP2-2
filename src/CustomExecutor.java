
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;

public class CustomExecutor {

    private final PriorityBlockingQueue<Task> heap;
    private final ThreadGroup threadGroup = new ThreadGroup("MyThreadGroup");
    private boolean stopped = false;

    public CustomExecutor() {
        int availableCPU = Runtime.getRuntime().availableProcessors() / 2;
        heap = new PriorityBlockingQueue<>(availableCPU);
        for (int i = 0; i < availableCPU; i++) {
            Worker worker = new Worker(threadGroup, "worker " + i);
            worker.start();
        }
    }

    public void submit(RunnableTask task) {
        heap.add(task);
    }

    public <T> Future<T> submit(CallableTask<T> task) {
        heap.add(task);
        return task;
    }

    public void submit(Runnable op) {
        heap.add(TaskFactory.createTask(op));
    }

    public void submit(Runnable op, TaskType type) {
        heap.add(TaskFactory.createTask(op, type));
    }

    public <T> void submit(Callable<T> op) {
        heap.add(TaskFactory.createTask(op));
    }

    public <T> void submit(Callable<T> op, TaskType type) {
        heap.add(TaskFactory.createTask(op, type));
    }

    public void shutdown() {
        this.stopped = true;
        this.threadGroup.interrupt();
    }

    private class Worker extends Thread {
        public Worker(ThreadGroup threadGroup, String name) {
            super(threadGroup, name);
        }

        @Override
        public void run() {
            while (stopped == false && !interrupted()) {
                try {
                    final Runnable job = (Runnable) heap.take();
                    job.run();
                } catch (InterruptedException e) {
                    this.interrupt();
                }
            }
        }
    }
}
