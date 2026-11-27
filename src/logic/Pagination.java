package logic;

/**
 * Pagination
 * utility class to handle query pagination
 */
public class Pagination {

    // default page size if null is passed to constructor
    private static final int DEFAULT_PAGE_SIZE = 100;
    private final int INIT_COUNT = 1;// count init index

    private final int PAGE_SIZE;// configurable max page size
    private boolean isPrinting; // true of page is currently print | false if waiting for user promprt
    private boolean isTerminated; // true if user terminates the query result
    private int count;// number of rows printed in current page

    /**
     * constructor to initialize pagination
     * 
     * @param pageSize desired page size | null for default size (300)
     */
    public Pagination(Integer pageSize) {
        this.PAGE_SIZE = pageSize != null ? pageSize : DEFAULT_PAGE_SIZE;
        this.isPrinting = true;
        this.isTerminated = false;
        this.count = INIT_COUNT;
    }

    /**
     * check if query printing is at page limit
     * 
     * @return true if row printing should continue otherwise false
     */
    public boolean isPrinting() {

        // if it page limit, set to false
        if (count % this.PAGE_SIZE == 0) {
            this.isPrinting = false;
            this.count = INIT_COUNT;
        } else
            this.count += 1;
        return this.isPrinting;
    }

    // getters
    public int getPAGE_SIZE() {
        return PAGE_SIZE;
    }

    public boolean isTerminated() {
        return isTerminated;
    }

    // setters
    public void setTerminated(boolean isTerminated) {
        this.isTerminated = isTerminated;
    }
}
