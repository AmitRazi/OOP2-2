
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;

public class CustomExecutor{

    private final PriorityBlockingQueue<Task> heap;
    private final ThreadGroup threadGroup = new ThreadGroup("MyThreadGroup");
    private boolean stopped = false;

    public CustomExecutor(){
        int availableCPU = Runtime.getRuntime().availableProcessors()/2;
        heap = new PriorityBlockingQueue<>(availableCPU);
        for(int i = 0 ; i < availableCPU ; i++){
            Worker worker = new Worker(threadGroup,"worker "+i);
            worker.start();
        }
    }
    public void submit(RunnableTask task){
        heap.add(task);
    }
  public <T> Future<T> submit(CallableTask<T> task){
        heap.add(task);
        return task;
  }

  public void shutdown(){
        this.stopped = true;
        this.threadGroup.interrupt();
  }

    private class Worker extends Thread{
        public Worker(ThreadGroup threadGroup, String name) {
            super(threadGroup, name);
        }

        @Override
        public void run(){
            while(stopped == false && !interrupted()){
                try{
                    final Runnable job = (Runnable) heap.take();
                    job.run();
                } catch (InterruptedException e){
                    this.interrupt();
                }
            }
        }
    }
}
