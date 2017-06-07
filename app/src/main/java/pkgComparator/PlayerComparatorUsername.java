package pkgComparator;

import java.io.Serializable;
import java.util.Comparator;
import pkgData.Player;

/**
 * Created by Elias on 01.06.2017.
 */

public class PlayerComparatorUsername implements Comparator<Player>, Serializable {
    @Override
    public int compare(Player o1, Player o2) {
        return o1.getUsername().toLowerCase().compareTo(o2.getUsername().toLowerCase());
    }
}