package group2.schoolproject.a02soccer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import pkgData.Player;
import pkgData.PlayerStatistics;

public class ShowPlayerStatsActivity extends AppCompatActivity {
    private TextView txvName,
            txvGamesPlayed,
            txvGameWins,
            txvGameDefeats,
            txvGameDraws,
            txvAvgGoalDiff,
            txvGoalsShotAll,
            txvGoalsShotDefault,
            txvGoalsShotHead,
            txvGoalsShotHeadSnow,
            txvGoalsShotPenalty,
            txvGoalsGot,
            txvNutmeg,
            txvPosAttack,
            txvPosDefense,
            txvPosMidfield,
            txvPosGoal;

    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_player_stats);
        setTitle(R.string.title_activity_player_stats);
        try {
            getAllViews();

            player = (Player) this.getIntent().getSerializableExtra("player");

            if (player == null) {
                throw new Exception("Please call activity with intent-extra 'player'");
            }

            displayPlayerStats();
        }
        catch (Exception ex) {
            ExceptionNotification.notify(this, ex);
            ex.printStackTrace();
        }
    }

    private void getAllViews() {
        txvName = (TextView) findViewById(R.id.txvName);
        txvGamesPlayed = (TextView) findViewById(R.id.txvGamesPlayed);
        txvGameWins = (TextView) findViewById(R.id.txvWins);
        txvGameDefeats = (TextView) findViewById(R.id.txvDefeats);
        txvGameDraws = (TextView) findViewById(R.id.txvDraws);
        txvAvgGoalDiff = (TextView) findViewById(R.id.txvAvgGoalDiff);
        txvGoalsShotAll = (TextView) findViewById(R.id.txvGoalsShotAll);
        txvGoalsShotDefault = (TextView) findViewById(R.id.txvGoalsShotDefault);
        txvGoalsShotHead = (TextView) findViewById(R.id.txvGoalsShotHead);
        txvGoalsShotHeadSnow = (TextView) findViewById(R.id.txvGoalsShotHeadSnow);
        txvGoalsShotPenalty = (TextView) findViewById(R.id.txvGoalsShotPenalty);
        txvGoalsGot = (TextView) findViewById(R.id.txvGoalsGot);
        txvNutmeg = (TextView) findViewById(R.id.txvNutmeg);
        txvPosAttack = (TextView) findViewById(R.id.txvPosAttack);
        txvPosDefense = (TextView) findViewById(R.id.txvPosDefense);
        txvPosMidfield = (TextView) findViewById(R.id.txvPosMidfield);
        txvPosGoal = (TextView) findViewById(R.id.txvPosGoal);
    }

    private void displayPlayerStats() {
        PlayerStatistics s = player.getStatistics();

        txvName.setText(player.toString());
        txvGamesPlayed.setText(Integer.toString(s.getNumGamesPlayed()));
        txvGameWins.setText(Integer.toString(s.getNumWins()));
        txvGameDefeats.setText(Integer.toString(s.getNumDefeats()));
        txvGameDraws.setText(Integer.toString(s.getNumDraws()));
        txvAvgGoalDiff.setText(Float.toString(s.getAvgGoalDifference()));
        txvGoalsShotAll.setText(Integer.toString(s.getNumGoalsShotAll()));
        txvGoalsShotDefault.setText(Integer.toString(s.getNumGoalsShotDefault()));
        txvGoalsShotHead.setText(Integer.toString(s.getNumGoalsShotHead()));
        txvGoalsShotHeadSnow.setText(Integer.toString(s.getNumGoalsShotHeadSnow()));
        txvGoalsShotPenalty.setText(Integer.toString(s.getNumGoalsShotPenalty()));
        txvGoalsGot.setText(Integer.toString(s.getNumGoalsGot()));
        txvNutmeg.setText(Integer.toString(s.getNumNutmeg()));
        txvPosAttack.setText(Integer.toString(s.getNumPosAttack()));
        txvPosDefense.setText(Integer.toString(s.getNumPosDefense()));
        txvPosMidfield.setText(Integer.toString(s.getNumPosMidfield()));
        txvPosGoal.setText(Integer.toString(s.getNumPosGoal()));
    }
}
