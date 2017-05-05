package group2.schoolproject.a02soccer;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableRow;
import android.widget.TextView;

import pkgTab.SectionsPageAdapter;
import pkgTab.TeamDivisionTab1;
import pkgTab.TeamDivisionTab2;

public class TeamManagmentActivity extends AppCompatActivity {

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_managment);

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tablayout = (TabLayout) findViewById(R.id.tabs);
        tablayout.setupWithViewPager(mViewPager);

        addPlayer(false);
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = mSectionsPageAdapter;
        adapter.addFragment(new TeamDivisionTab1(),"TEAM1");
        adapter.addFragment(new TeamDivisionTab2(),"TEAM2");
        viewPager.setAdapter(adapter);
    }

    public void addPlayer(Boolean b){
        if(b){
            SectionsPageAdapter adapter = (SectionsPageAdapter) mViewPager.getAdapter();
            TeamDivisionTab1 test = (TeamDivisionTab1) adapter.getItem(0);
            // create a new TableRow
            try {
                TableRow row = new TableRow(this);
                // create a new TextView for showing xml data
                TextView t = new TextView(this);
                // set the text to "text xx"
                t.setText( "Santner");
                // add the TextView  to the new TableRow
                row.addView(t);
                test.addPlayer();
            }
            catch (Exception e) {
                ExceptionNotification.notify(this, e);
                e.printStackTrace();
            }
        }
    }
}
