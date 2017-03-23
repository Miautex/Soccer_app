package pkgData;

import java.io.Serializable;

/**
 * Created by Elias on 23.03.2017.
 */

public abstract class DatabaseObject implements Comparable, Serializable {
    private int id;

    public abstract int getId();
}
