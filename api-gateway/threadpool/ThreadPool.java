package service.api_gateway.threadpool;

public class ThreadPool {
    private int numWorkers;
    private Worker[] workers;
    private ClientRequestQueue queue;

    public ThreadPool(int numWorkers) {
        this.queue = new ClientRequestQueue();
        workers = new Worker[numWorkers];
        for (int i = 0; i < numWorkers; i++) {
            workers[i] = Worker.CreateWorker(i, this);
            workers[i].start();
        }
    }

    public void add(ClientRequest clientRequest) {
        queue.enqueue(clientRequest);
    }

    public ClientRequest remove() {
        return queue.dequeue();
    }

    public ClientRequestQueue getQueue() {
        return queue;
    }
}

