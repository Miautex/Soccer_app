package pkgComparator;

import java.util.Comparator;

import pkgData.Player;

/**
 * Created by Raphael on 18.05.2017.
 *
 */

public class PlayerComparatorGoalsShot implements Comparator<Player> {
    @Override
    public int compare(Player o1, Player o2) {
        int retVal = o1.getStatistics().getNumGoalsShotAll() - o2.getStatistics().getNumGoalsShotAll();
        if (retVal == 0) {
            retVal = o1.compareTo(o2);
        }
        return retVal;
    }
}
