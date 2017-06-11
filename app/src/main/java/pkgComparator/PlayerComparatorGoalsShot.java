package pkgComparator;

import java.util.Comparator;

import pkgData.Player;

/**
 * @author Raphael Moser
 */

public class PlayerComparatorGoalsShot implements Comparator<Player> {
    @Override
    public int compare(Player o1, Player o2) {
        int retVal = o2.getStatistics().getNumGoalsShotAll() - o1.getStatistics().getNumGoalsShotAll();
        if (retVal == 0) {
            retVal = o1.compareTo(o2);
        }
        return retVal;
    }
}
