/**
 * @author José Marinho
 */

package pt.isec.pd.ex15a;

import java.io.Serializable;

public class RequestToWorker implements Serializable
{
    protected int id;
    protected int nWorkers;
    protected long nIntervals;
    private static final long serialVersionUID = 1L;
    public RequestToWorker(int id, int nWorkers, long nIntervals)
    {
        this.id = id;
        this.nWorkers = nWorkers;
        this.nIntervals = nIntervals; 
    }

    public int getId() {
        return id;
    }

    public long getnIntervals() {
        return nIntervals;
    }

    public int getnWorkers() {
        return nWorkers;
    }
        
}