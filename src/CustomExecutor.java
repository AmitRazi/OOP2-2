
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;

public class CustomExecutor{

    private PriorityBlockingQueue<Task> queue;
    private ThreadGroup threadGroup = new ThreadGroup("MyThreadGroup");
    private boolean stopped = false;

    public CustomExecutor(){
        queue = new PriorityBlockingQueue<>(5);
        for(int i = 0 ; i < 5 ; i++){
            Worker worker = new Worker(threadGroup,"worker "+i);
            worker.start();
        }
    }
    public void submit(RunnableTask task){
        queue.add(task);
    }
  public <T> Future<T> submit(CallableTask<T> task){
        queue.add(task);
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
                    final Runnable job = (Runnable) queue.take();
                    job.run();
                } catch (InterruptedException e){
                    this.interrupt();
                }
            }
        }
    }
}
