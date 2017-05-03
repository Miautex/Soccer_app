package group2.schoolproject.a02soccer;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import group2.schoolproject.a02soccer.ExceptionNotification;
import group2.schoolproject.a02soccer.R;
import pkgData.Player;
import pkgTab.SectionsPageAdapter;
import pkgTab.Tab1Fragment;
import pkgTab.Tab2Fragment;

public class TeamManagmentActivity extends AppCompatActivity {

    private static final String TAG = "TeamMangamnet";

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

        addPlayer();

    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(),"TEAM1");
        adapter.addFragment(new Tab2Fragment(),"TEAM2");
        viewPager.setAdapter(adapter);
    }

    public void addPlayer(){
        SectionsPageAdapter adapter = (SectionsPageAdapter) mViewPager.getAdapter();
        Tab1Fragment test = (Tab1Fragment) adapter.getItem(0);
        /*// create a new TableRow
        TableRow row = new TableRow(this);
        // create a new TextView for showing xml data
        TextView t = new TextView(this);
        // set the text to "text xx"
        t.setText( "Santner");
        // add the TextView  to the new TableRow
        row.addView(t);
        test.addPlayer(row);*/
        try {
            test.addPlayerTest(new Player("santnere", "Santner", true));
        } catch (Exception e) {
            ExceptionNotification.notify(this, e);
            e.printStackTrace();
        }
    }



}
