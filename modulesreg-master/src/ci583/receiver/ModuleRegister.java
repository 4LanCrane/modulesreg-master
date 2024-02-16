package ci583.receiver;

/**
 * A class representing a process for the CI583 Modules Registration assignment. Process is a subclass of Thread.
 * When the thread runs, the 'work' that it does is to sleep repeatedly.
 *
 *
 */
public class ModuleRegister extends Thread {
    /** The amount of 'work' this process has to do, in milliseconds. */
    private long work;
    /** The time at which this process was started, in milliseconds. */
    private long timeStarted;
    /** Contains the name and other details of this process. */
    private String status;

    /** An enum containing three priority values, LOW, MEDIUM and HIGH.
     *
     */
    public enum PRIORITY {
        HIGH(1), MED(5), LOW(9);

        private int val;

        PRIORITY(int val) {
            this.val = val;
        }

        public int getVal() {
            return val;
        }
    }

    /**
     * Constructs a new Process with the given name and amount of work to do.
     * @param pid
     * @param work
     */
    public ModuleRegister(String pid, long work) {
        this(pid, work, PRIORITY.MED);
    }

    /**
     * Constructs a new Process with the given name, amount of work to do and priority.
     * @param pid
     * @param work
     * @param p
     */
    public ModuleRegister(String pid, long work, PRIORITY p) {
        this.setName(pid);
        this.work = work;
        timeStarted = System.currentTimeMillis();
        setPriority(p.getVal());
        status = String.format("[%s %d] INCOMPLETE %d", pid, getPriority(), work);
    }

    /**
     * The run method sleeps repeatedly until the 'work' is done.
     */
    public void run() {
        while(workDone() < work) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) { }
        }
        setStatus();
    }

    /**
     * Sets a string describing this process and how long it took to complete.
     */
    private void setStatus() {
        status = String.format("[%s %d] COMPLETE: %d/%d",
                getName(), getPriority(), work, workDone());
    }

    /**
     * Getter for status.
     * @returns status of the process
     */
    public String getStatus() {
        throw new UnsupportedOperationException("Method not implemented");
    }
    //return status;

    /**
     * The length of time, in milliseconds, that this process has been working.
     * @return
     */
    private long workDone() {
      throw new UnsupportedOperationException("Method not implemented");
      //return System.currentTimeMillis() - timeStarted;
    }

    /**
     * Returns the contents of `status'.
     * @return
     */
    public String toString() {
        return status;
    }

}
