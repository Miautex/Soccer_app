package pkgAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pkgMisc.PlayerWithScore;
import group2.schoolproject.a02soccer.R;

/**
 * Created by Raphael on 20.05.2017.
 *
 */

public class ScoreboardListAdapter extends ArrayAdapter<PlayerWithScore> {
    private final Context context;
    private ArrayList<PlayerWithScore> values;

    public ScoreboardListAdapter (Context context, ArrayList<PlayerWithScore> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_scoreboard, parent, false);
        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView score = (TextView) rowView.findViewById(R.id.score);
        name.setText(values.get(position).getName());
        score.setText(values.get(position).getScore());
        return rowView;
    }
}
