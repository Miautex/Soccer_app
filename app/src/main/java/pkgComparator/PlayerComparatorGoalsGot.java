package pkgComparator;

import pkgData.Player;

/**
 * Created by Raphael on 18.05.2017.
 */

public class PlayerComparatorGoalsGot implements Comparator<Player>{
    @Override
    //// TODO: 18.05.2017  
    public int compare(Player o1, Player o2) {
        int retVal = o1.getStatistics().getNumGoalsShotAll() - o2.getStatistics().getNumGoalsShotAll();

        if (retVal == 0) {
            retVal = o1.getName().compareTo(o2.getName());
        }

        return retVal;
    }
}
