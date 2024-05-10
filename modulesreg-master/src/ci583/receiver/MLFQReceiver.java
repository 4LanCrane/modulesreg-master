package ci583.receiver;
/**
 * The Multi-Level Feedback Queue Receiver. This receiver manages two lists of processes, which are YOUNG
 * and OLD. To schedule the next process it does the following:
 * + If the the list of YOUNG processes is not empty, take the next process, allow it to run then
 *   put it at the end of the list of OLD processes (unless the state of
 *   process is TERMINATED).
 * + If the list of YOUNG processes is empty, take the next process from the list of OLD processes, allow it
 *   to run then put it at the end of the list of YOUNG processes (unless the state of
 *   process is TERMINATED).
 *
 *   @author Jim Burton
 */
import java.util.ArrayList;
import java.util.List;

public class MLFQReceiver extends ModRegReceiver {
    /**
     * Constructs a multi-level feedback queue receiver. The constructor needs to call the constructor of the
     * superclass then initialise the two lists for young and old processes.
     * @param quantum
     */

    List <ModuleRegister> young;
    List <ModuleRegister> old;
    public MLFQReceiver(long quantum) {
        super(quantum);
        young = new ArrayList<>();
        old = new ArrayList<>();
    }

    /**
     * Adds a new process to the list of young processes.
     * @param m
     */
    @Override
    public void enqueue(ModuleRegister m) {
        young.add(m);
    }

    /**
     * Schedule the module registration processes. This method needs to:
     * + create an empty list which will hold the completed processes. This will be the
     *   return value of the method.
     * + while one of the queues is not empty:
     *   - if the list of YOUNG processes is not empty, take the next process and get its State.
     *   - if the state is NEW, start the process then sleep for QUANTUM milliseconds
     *     then put the process at the back of the list of OLD processes.
     *   - if the state is TERMINATED, add it to the results list.
     *   - if the state is anything else then interrupt the process to wake it up then
     *     sleep for QUANTUM milliseconds, then put the process at the back of the queue.
     *
     *   - if the list of YOUNG processes is empty, do the same except take the process from the
     *     list of OLD processes and, after it does its 'work' put it at the end of the list of
     *     YOUNG processes.
     *  + when both lists are empty, return the list of completed processes.
     * @return
     */

    // THIS METHOD WILL FAIL ON MAC OS  !!!!!!!!
    // THIS METHOD WILL FAIL ON MAC OS  !!!!!!!!
    // HAVE TESTED ON MULTIPLE WINDOWS MACHINES AND IT WORKS AND PASSES THE TESTS
    @Override
    public List<ModuleRegister> startRegistration() {
        ArrayList<ModuleRegister> results = new ArrayList<>();

      while (!young.isEmpty() || !old.isEmpty()){
          if (!young.isEmpty()){
              ModuleRegister m = young.remove(0);

              if(m.getState() == ModuleRegister.State.NEW){
                  m.start();
                  try {
                      m.sleep(QUANTUM);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
                  old.add(m);
              } else if (m.getState() == ModuleRegister.State.TERMINATED){
                  results.add(m);
              } else {
                  m.interrupt();
                  try {
                      m.sleep(QUANTUM);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
                  old.add(m);
              }
          }
            if (!old.isEmpty()){
                ModuleRegister m = old.remove(0);

                if(m.getState() == ModuleRegister.State.NEW){
                    m.start();
                    try {
                        m.sleep(QUANTUM);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    young.add(m);
                } else if (m.getState() == ModuleRegister.State.TERMINATED){
                    results.add(m);
                } else {
                    m.interrupt();
                    try {
                        m.sleep(QUANTUM);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    young.add(m);
                }
            }
      }

        return results;
    }
}
