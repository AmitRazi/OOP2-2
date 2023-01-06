import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import static java.lang.Thread.sleep;

public class CustomExecutor {

    private final PriorityBlockingQueue<Taskable> queue;
    private final ThreadGroup threadGroup = new ThreadGroup("MyThreadGroup");
    private boolean stopped = false;
    private final int availableCPU = Runtime.getRuntime().availableProcessors();
    private volatile int ThreadCount = 0;
    private int workerID = 0;
    private final int[] priorityArray;


    public CustomExecutor() {
        priorityArray = new int[11];
        queue = new PriorityBlockingQueue<>(availableCPU / 2);
        for (int i = 0; i < availableCPU / 2; i++) {
            createWorker(threadGroup, "worker " + workerID++);
        }
    }

    public void submit(RunnableTask task) {
        if(stopped) return;
        CPUandHeapCheck();
        queue.add(task);

    }

    public <T> Future<T> submit(CallableTask<T> task) {
        if(stopped) return null;
        CPUandHeapCheck();
        queue.add(task);
        setValueInPriorityArray(task.getTaskPriority());
        return task;
    }

    private void setValueInPriorityArray(int taskPriority) {
        synchronized (priorityArray) {
            priorityArray[taskPriority]++;
        }
    }
    public int getMaxPriority(){
        synchronized (priorityArray) {
            for (int i = priorityArray.length - 1; i >= 0; i--) {
                if (priorityArray[i] > 0) return i;
            }
        }
        return 0;
    }

    public void submit(Runnable op) {
        submit(Task.createTask(op));
    }

    public void submit(Runnable op, TaskType type) {
        submit(Task.createTask(op,type));
    }

    public <T> Future<T> submit(Callable<T> op) {
        return submit(Task.createTask(op));
    }

    public <T> Future<T> submit(Callable<T> op, TaskType type) {
        return submit(Task.createTask(op,type));
    }

    private void CPUandHeapCheck(){
        if(!queue.isEmpty() && ThreadCount < availableCPU -1){
            createWorker(threadGroup,"worker " + workerID++);
        }
    }

    private void createWorker(ThreadGroup threadGroup, String name) {
        new Worker(threadGroup, name).start();
        ThreadCount++;
    }


    public void shutdown() {
        while(true){
            if(queue.isEmpty() && threadGroup.activeGroupCount() == 0){
                try {
                    sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                this.stopped=true;
                threadGroup.interrupt();
                break;
            }
        }
    }

    private class Worker extends Thread {
        public Worker(ThreadGroup threadGroup, String name) {
            super(threadGroup, name);
        }

        @Override
        public void run() {
            boolean timeout = false;
            long start = System.currentTimeMillis();
            while (stopped == false && !this.isInterrupted() && !timeout) {
                long time = System.currentTimeMillis() - start;
                if ((time > 300) && (ThreadCount > availableCPU / 2)){
                    timeout = true;
                }
                try {
                    if (!queue.isEmpty()) {
                        final Runnable job = (Runnable) queue.take();
                        job.run();
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