package group2.schoolproject.a02soccer;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

class TeamDivision2 extends BaseActivity implements View.OnTouchListener {

    float historicX = Float.NaN, historicY = Float.NaN;
    static final int DELTA = 50;
    ListView lv3;
    int position = 100;



    enum Direction {LEFT, RIGHT;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_division2);
        ArrayList<String> test = new ArrayList<>();
        test.add("a");
        test.add("b");
        test.add("c");
        test.add("d");
        test.add("e");
        test.add("f");

        ArrayAdapter<String> list = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, test);
        ListView lv1 = (ListView) findViewById(R.id.lvGrey);
        ListView lv2 = (ListView) findViewById(R.id.lvRed);
        lv1.setAdapter(list);
        lv2.setAdapter(list);
        lv3 =  (ListView) findViewById(R.id.lvBlau);
        lv3.setBackgroundResource(R.drawable.tablelayout2);
        lv3.setAdapter(list);
        lv3.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int pos = ((ListView)v).pointToPosition((int)event.getX(), (int)event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                position = ((ListView)v).pointToPosition((int)event.getX(), (int)event.getY());
                historicX = event.getX();
                historicY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (event.getX() - historicX < -DELTA) {
                    showMessage("left " + ((ListView) v).getItemAtPosition(position));
                    return true;
                } else if (event.getX() - historicX > DELTA) {
                    showMessage("right " + ((ListView) v).getItemAtPosition(position));
                    return true;
                }
                break;

            default:
                return false;
        }
        return false;
    }

}
