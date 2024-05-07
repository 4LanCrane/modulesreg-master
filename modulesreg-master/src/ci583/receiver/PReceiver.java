package ci583.receiver;
/**
 * The Priority Receiver. This receiver takes the next process from the head of a priority queue
 * (an instance of java.util.PriorityQueue, allows it to run then puts it back into the queue (unless
 * the state of process is TERMINATED). Thus, the Priority Receiver is identical to the Round Robin
 * receiver apart from the fact that processes have a priority (HIGH, MED or LOW) and are held in a
 * priority queue.
 *
 * @author Jim Burton
 */
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class PReceiver extends ModRegReceiver implements Comparator<ModuleRegister> {

    private PriorityQueue<ModuleRegister> queue;// the priority queue

    /**
     * Constructs a new Priority Scheduler. The constructor needs to call the constructor of the
     * superclass then initialise the priority queue. To initialise the priority queue, first define
     * a Comparator that compares two processes. The comparator should return -1 if two processes have
     * the same priority. This is so that when a process is added to the queue it ends up behind any
     * other processes with the same priority. If the two priorities are not equal, the Comparator should
     * return -1 if p1 is less than p2, and 1 if p1 is greater than p2.
     * @param quantum
     */
    public PReceiver(long quantum) {
        super(quantum);
        queue = new PriorityQueue<>(this);
    }


    @Override
    public int compare(ModuleRegister m1, ModuleRegister m2) {
        if (m1.getPriority() == m2.getPriority()) {
            return -1;
        }else if (m1.getPriority() < m2.getPriority()) {
            return -1;
        }else {
            return 1;
        }

    }



    @Override
    public void enqueue( ModuleRegister m) {
        queue.add(m);
    }

    /**
     * Schedule the processes. This method needs to:
     * + create an empty list which will hold the completed processes. This will be the
     * return value of the method.
     * + while the queue is not empty:
     * - use the priority queue's `poll' method to take the next process from the queue and get its State.
     * - if the state is NEW, start the process then sleep for QUANTUM milliseconds
     * then put the process at the back of the queue.
     * - if the state is TERMINATED, add it to the results list.
     * - if the state is anything else then interrupt the process to wake it up then
     * sleep for QUANTUM milliseconds, then put the process at the back of the queue.
     * + when the queue is empty, return the list of completed processes.
     *
     * @return
     */
    @Override
    public List<ModuleRegister> startRegistration() {
        List<ModuleRegister> results = new ArrayList<>();
        while (!queue.isEmpty()) {
            ModuleRegister m = queue.poll();
            switch (m.getState()) {
                case NEW:
                    m.start();
                    try {
                        m.sleep(QUANTUM);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    queue.add(m);
                    break;
                case TERMINATED:
                    results.add(m);
                    break;
                default:
                    m.interrupt();
                    try {
                        m.sleep(QUANTUM);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    queue.add(m);
            }
        }
        return results;
    }



}
