package ci583.receiver;
/**
 * The Round Robin Module Registration Receiver. This receiver takes the next process from the head of a list,
 * allows it to run then puts it back at the end of the list (unless the state of process is
 * TERMINATED).
 *
 * @author Jim Burton
 */

import java.util.ArrayList;
import java.util.List;

public class RRReceiver extends ModRegReceiver {

    private ArrayList<ModuleRegister> queue; // the list of processes
    /**
     * Create a new RRReceiver with the given quantum. The constructor needs to call the constructor
     * of the superclass, then initialise the list of processes.
     * @param quantum amount of time to run RRReceiver
     */
    public RRReceiver(long quantum) {
      super(quantum); // call the constructor of the superclass
      queue = new ArrayList<>(); // initialise the list of processes
    }

    /**
     * Add a ModuleRegister process to the queue, to be scheduled for registration
     */
    @Override
    public void enqueue(ModuleRegister m) {
        queue.add(m);
    }

    /**
     * Schedule the processes, start registration. This method needs to:
     * + create an empty list which will hold the completed processes. This will be the
     *   return value of the method.
     * + while the queue is not empty:
     *   - take the next process from the queue and get its State.
     *   - if the state is NEW, start the process then sleep for QUANTUM milliseconds
     *     then put the process at the back of the queue.
     *   - if the state is TERMINATED, add it to the results list.
     *   - if the state is anything else then interrupt the process to wake it up then
     *     sleep for QUANTUM milliseconds, then put the process at the back of the queue.
     *  + when the queue is empty, return the list of completed processes.
     * @return
     */
    @Override
    public List<ModuleRegister> startRegistration() {
        ArrayList<ModuleRegister> results = new ArrayList<>(); // create an empty list which will hold the completed processes
        while (!queue.isEmpty()) {
            ModuleRegister m = queue.remove(0);
            switch (m.getState()) {// take the next process from the queue and get its State
                case NEW:
                    m.start(); // start the process
                    try {
                        m.sleep(QUANTUM); // then sleep for QUANTUM milliseconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    queue.add(m); // then put the process at the back of the queue
                    break;
                case TERMINATED:
                    results.add(m); // add it to the results
                    break;
                default:
                    m.interrupt(); // wake it up
                    try {
                        m.sleep(QUANTUM); // sleep for QUANTUM milliseconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    queue.add(m); // put it at the back of the queue
                    break;
            }
        }
        return results;
    }
}
