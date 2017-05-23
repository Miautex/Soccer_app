package pkgAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import group2.schoolproject.a02soccer.R;
import pkgData.Game;

public class MainGameListAdapter extends ArrayAdapter<Game> {
    private final Context context;
    private ArrayList<Game> values;

    public MainGameListAdapter(Context context, ArrayList<Game> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_main_game, parent, false);
        TextView text = (TextView) rowView.findViewById(R.id.text);
        text.setText(values.get(position).toString());
        return rowView;
    }
}