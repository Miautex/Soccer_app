package pkgComparator;

import java.util.Comparator;

import pkgData.Player;

/**
 * @author Raphael Moser
 */

public class PlayerComparatorGoalsGot implements Comparator<Player> {
    @Override
    public int compare(Player o1, Player o2) {
        int retVal = o2.getStatistics().getNumGoalsGot() - o1.getStatistics().getNumGoalsGot();
        if (retVal == 0) {
            retVal = o1.compareTo(o2);
        }
        return retVal;
    }
}
