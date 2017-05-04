package pkgComparator;

import java.util.Comparator;

import pkgData.Participation;

public class ParticipationComparatorName implements Comparator<Participation> {
    @Override
    public int compare(Participation o1, Participation o2) {
        PlayerComparatorName playerCN = new PlayerComparatorName();

        return playerCN.compare(o1.getPlayer(), o2.getPlayer());
    }
}
