package service.api_gateway.threadpool;

import service.api_gateway.logger.ServiceLogger;

public class ClientRequestQueue {
    private ListNode head;
    private ListNode tail;

    public ClientRequestQueue() {
        head = tail = null;
    }

    public synchronized void enqueue(ClientRequest clientRequest) {
        ServiceLogger.LOGGER.info("Adding to queue: " + clientRequest.toString());
        ListNode node = new ListNode(clientRequest, null);
        if (isEmpty()) {
            head = tail = node;
            return;
        }
        this.tail.setNext(node);
        this.tail = node;
        notify();
    }

    public synchronized ClientRequest dequeue() {

        if (isEmpty()) {
            try
            {
                wait();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        ListNode node = head;
        head = head.getNext();

        if (head == null) {
            tail = null;
        }
        return node.getClientRequest();
    }

    boolean isEmpty() {
        return head == null;
    }

    boolean isFull() {
        return head != null && tail != null;
    }
}
