package group2.schoolproject.a02soccer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;

import pkgData.Player;
import pkgDatabase.Database;

import static group2.schoolproject.a02soccer.R.id.lsvPlayersGames;

public class ScoreboardActivity extends BaseActivity implements AdapterView.OnItemSelectedListener{

    private Spinner choice = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        final ListView listview = (ListView) findViewById(R.id.lsv_Score);
        choice = (Spinner) findViewById(R.id.spSorting);

        ArrayList<Player> test = null;
        try {
            test = Database.getInstance().getAllPlayers();
        } catch (Exception e) {
            System.out.println("warum zum teufel braucht man da a catch");
        }


        final ArrayList<PlayerWithScore> list = new ArrayList<>();
        for (int i = 0; i < test.size(); ++i) {
            Player p = test.get(i);
            list.add(new PlayerWithScore(p.toString(), p.getStatistics().getNumWins()));
        }

        try {
            final ScoreboardListAdapter adapter = (new ScoreboardListAdapter(this, list));
            listview.setAdapter(adapter);

        } catch (Exception e) {
            System.out.println("warum zum teufel bracuht man da a catch");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
