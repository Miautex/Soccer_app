package pkgComparator;

import java.util.Comparator;
import pkgData.Player;

public class PlayerComparatorName implements Comparator<Player> {
    @Override
    public int compare(Player o1, Player o2) {
        int retVal = o1.getName().compareTo(o2.getName());
        if (retVal == 0) {
            retVal = o1.getUsername().toLowerCase().compareTo(o2.getUsername().toLowerCase());
        }
        return retVal;
    }
}
