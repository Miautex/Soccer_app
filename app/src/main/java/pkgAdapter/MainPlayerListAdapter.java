package pkgAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import group2.schoolproject.a02soccer.R;
import pkgData.Player;

public class MainPlayerListAdapter extends ArrayAdapter<Player> {
    private final Context context;
    private ArrayList<Player> values;

    public MainPlayerListAdapter(Context context, ArrayList<Player> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_main_player, parent, false);
        TextView text = (TextView) rowView.findViewById(R.id.text);
        text.setText(values.get(position).toString());
        return rowView;
    }
}