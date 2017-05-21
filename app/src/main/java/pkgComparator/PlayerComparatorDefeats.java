package pkgComparator;

import java.util.Comparator;

import pkgData.Player;

/**
 * Created by Raphael on 21.05.2017.
 */

public class PlayerComparatorDefeats implements Comparator<Player> {
    @Override
    public int compare(Player o1, Player o2) {
        int retVal = o2.getStatistics().getNumDefeats() - o1.getStatistics().getNumDefeats();
        if (retVal == 0) {
            retVal = o1.compareTo(o2);
        }
        return retVal;
    }
}

