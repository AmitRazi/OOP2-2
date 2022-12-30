
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;

public class CustomExecutor {

    private final PriorityBlockingQueue<Task> heap;
    private final ThreadGroup threadGroup = new ThreadGroup("MyThreadGroup");
    private boolean stopped = false;
    private final int availableCPU = Runtime.getRuntime().availableProcessors();
    private int ThreadCount = 0;
    private int workerID = 0;
    private int AssignedThreads = 0;


    public CustomExecutor() {
        heap = new PriorityBlockingQueue<>(availableCPU / 2);
        for (int i = 0; i < availableCPU / 2; i++) {
            createWorker(threadGroup, "worker " + workerID++);
        }
    }

    public void submit(RunnableTask task) {
        CPUandHeapCheck();
        heap.add(task);
    }

    public <T> Future<T> submit(CallableTask<T> task) {
        CPUandHeapCheck();
        heap.add(task);
        return task;
    }

    public void submit(Runnable op) {
        CPUandHeapCheck();
        heap.add(TaskFactory.createTask(op));
    }

    public void submit(Runnable op, TaskType type) {
        CPUandHeapCheck();
        heap.add(TaskFactory.createTask(op, type));
    }

    public <T> void submit(Callable<T> op) {
        CPUandHeapCheck();
        heap.add(TaskFactory.createTask(op));
    }

    public <T> void submit(Callable<T> op, TaskType type) {
        CPUandHeapCheck();
        heap.add(TaskFactory.createTask(op, type));
    }

    private void CPUandHeapCheck(){
        if(AssignedThreads == ThreadCount && ThreadCount < availableCPU -1){
            createWorker(threadGroup,"worker " + workerID++);
        }
    }

    private void createWorker(ThreadGroup threadGroup, String name) {
        new Worker(threadGroup, name).start();
        ThreadCount++;
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
            boolean timeout = false;
            long start = System.currentTimeMillis();
            while (stopped == false && !interrupted() && !timeout) {
                long time = System.currentTimeMillis() - start;
                if ((time > 300) && (ThreadCount == availableCPU - 1)) timeout = true;
                try {
                    if (!heap.isEmpty()) {
                        final Runnable job = (Runnable) heap.take();
                        AssignedThreads++;
                        System.out.println(AssignedThreads);
                        job.run();
                        AssignedThreads--;
                        start = System.currentTimeMillis();
                    }
                } catch (InterruptedException e) {
                    this.interrupt();
                }
            }
            ThreadCount--;
        }
    }
}
