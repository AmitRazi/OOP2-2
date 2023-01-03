
import java.sql.SQLOutput;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class CustomExecutor {

    private final PriorityBlockingQueue<Task> queue;
    private final ThreadGroup threadGroup = new ThreadGroup("MyThreadGroup");
    private boolean stopped = false;
    private final int availableCPU = Runtime.getRuntime().availableProcessors();
    private int ThreadCount = 0;
    private int workerID = 0;
    private AtomicInteger  AssignedThreads = new AtomicInteger(0);


    public CustomExecutor() {
        queue = new PriorityBlockingQueue<>(availableCPU / 2);
        for (int i = 0; i < availableCPU / 2; i++) {
            createWorker(threadGroup, "worker " + workerID++);
        }
    }

    public void submit(RunnableTask task) {
        CPUandHeapCheck();
        queue.add(task);
    }

    public <T> Future<T> submit(CallableTask<T> task) {
        CPUandHeapCheck();
        queue.add(task);
        return task;
    }

    public void submit(Runnable op) {
        CPUandHeapCheck();
        submit(TaskFactory.createTask(op));
    }

    public void submit(Runnable op, TaskType type) {
        CPUandHeapCheck();
        submit(TaskFactory.createTask(op,type));
    }

    public <T> Future<T> submit(Callable<T> op) {
        CPUandHeapCheck();
       return submit(TaskFactory.createTask(op));
    }

    public <T> Future<T> submit(Callable<T> op, TaskType type) {
        CPUandHeapCheck();
        return submit(TaskFactory.createTask(op,type));
    }

    private void CPUandHeapCheck(){
        if(AssignedThreads.equals(ThreadCount) && ThreadCount < availableCPU -1){
            createWorker(threadGroup,"worker " + workerID++);
        }
    }

    private void createWorker(ThreadGroup threadGroup, String name) {
        new Worker(threadGroup, name).start();
        ThreadCount++;
    }

    public void shutdown() {
      while(true){
          if(queue.isEmpty() && Integer.compare(AssignedThreads.get(),0) == 0){
              try {
                  sleep(20);
              } catch (InterruptedException e) {
                  throw new RuntimeException(e);
              }
              this.stopped=true;
              this.threadGroup.interrupt();
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
            while (stopped == false && !interrupted() && !timeout) {
                long time = System.currentTimeMillis() - start;
                if ((time > 300) && (ThreadCount > availableCPU / 2)){
                    timeout = true;
                }
                try {
                    if (!queue.isEmpty()) {
                        final Runnable job = (Runnable) queue.take();
                        AssignedThreads.incrementAndGet();
                        job.run();
                        AssignedThreads.decrementAndGet();
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
