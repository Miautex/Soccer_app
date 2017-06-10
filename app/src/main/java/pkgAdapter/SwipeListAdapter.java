package pkgAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import group2.schoolproject.a02soccer.R;
import pkgData.Player;
import pkgData.PlayerPosition;

/**
 * Created by Raphael on 28.05.2017.
 *
 */

    public class SwipeListAdapter extends ArrayAdapter<Player> {
    private ArrayList<Player> values = new ArrayList<>();
    private View.OnTouchListener mTouchListener;
    private final Context context;
    private int color = Color.WHITE;

    @Override
    public void add(@Nullable Player player) {
        super.add(player);
        values.add(player);
    }

    public void setColor(int color){
        this.color = color;
    }

    @Override
    public void remove(@Nullable Player player) {
        super.remove(player);
        values.remove(player);
    }

    public SwipeListAdapter(Context context, List<Player> players, View.OnTouchListener listener) {
        super(context, -1, players);
        this.context = context;
        mTouchListener = listener;
        values.addAll(players);
    }

    public ArrayList<Player> getFreePlayers(){
        return values;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View container = inflater.inflate(R.layout.swipe_list, parent, false);
        container.setBackgroundColor(color);
        View contentContainer = container.findViewById(R.id.linearLayout2);
        TextView positionPreview = (TextView) contentContainer.findViewById(R.id.posOverview);
        TextView playerName = (TextView) contentContainer.findViewById(R.id.listItem);
        Spinner positionSpinner = (Spinner) contentContainer.findViewById(R.id.spinner);
        if (values != null && values.size() >= 0) {
            playerName.setText(values.get(position).toString());
            positionSpinner.setVisibility(View.GONE);
        }
        positionPreview.setText(getPositionOverview(position));
        container.setOnTouchListener(mTouchListener);
        return container;
    }

    private String getPositionOverview(int position){
        String retVal = "";
        Player p = values.get(position);
        for(PlayerPosition pos : p.getPositions()){
            if (pos == PlayerPosition.ATTACK){
                retVal = retVal +  context.getResources().getString(R.string.PosAtkAbbrev) + "\t" + "\t" ;
            }
            if (pos == PlayerPosition.DEFENSE){
                retVal = retVal + context.getResources().getString(R.string.PosDefAbbrev) + "\t"+ "\t";
            }
            if (pos == PlayerPosition.MIDFIELD){
                retVal = retVal + context.getResources().getString(R.string.PosMidAbbrev) + "\t"+ "\t";
            }
            if (pos == PlayerPosition.GOAL){
                retVal = retVal + context.getResources().getString(R.string.PosGoalAbbrev) + "\t"+ "\t";
            }
        }
        return retVal;
    }






}
