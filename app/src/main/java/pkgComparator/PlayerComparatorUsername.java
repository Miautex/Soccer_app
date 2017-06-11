package pkgComparator;

import java.io.Serializable;
import java.util.Comparator;
import pkgData.Player;

/**
 * @author Elias Santner
 */

public class PlayerComparatorUsername implements Comparator<Player>, Serializable {
    @Override
    public int compare(Player o1, Player o2) {
        return o1.getUsername().toLowerCase().compareTo(o2.getUsername().toLowerCase());
    }
}