package pkgAdapter;

import android.content.Context;
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
import java.util.TreeSet;

import group2.schoolproject.a02soccer.R;
import pkgData.Player;
import pkgData.PlayerPosition;

/**
 * Created by Raphael on 28.05.2017.
 *
 */

    public class StableArrayAdapter extends ArrayAdapter<Player> {
    private ArrayList<Player> values = new ArrayList<>();
    private View.OnTouchListener mTouchListener;
    private final Context context;

    @Override
    public void add(@Nullable Player object) {
        super.add(object);
        values.add(object);
    }

    @Override
    public void remove(@Nullable Player object) {
        super.remove(object);
        values.remove(object);
    }

    public StableArrayAdapter(Context context, List<Player> objects, View.OnTouchListener listener) {
        super(context, -1, objects);
        this.context = context;
        mTouchListener = listener;
        values.addAll(objects);
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
        View linea = inflater.inflate(R.layout.opaque_text_view, parent, false);
        TextView test = (TextView) linea.findViewById(R.id.listItem);
        Spinner test2 = (Spinner) linea.findViewById(R.id.spinner);
        if (values != null && values.size() >= 0) {
            test.setText(values.get(position).toString());
            createSpinner(test2,values.get(position).getPositions());
        }
        linea.setOnTouchListener(mTouchListener);
        return linea;
    }

    private Spinner createSpinner(Spinner s,TreeSet<PlayerPosition> positions) {
        List<String> spinnerArray = new ArrayList<>();
        for (PlayerPosition pos : positions) {
            spinnerArray.add(EnumToString(pos));
        }
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        s.setAdapter(spinnerAdapter);
        return s;
    }

    private String EnumToString(PlayerPosition position){
        String retVal = "";
        if(position == PlayerPosition.ATTACK){
            retVal =  context.getString(R.string.PosAtk);
        }
        else if (position == PlayerPosition.DEFENSE){
            retVal = context.getString(R.string.PosDef);
        }
        else if (position == PlayerPosition.GOAL){
            retVal = context.getString(R.string.PosGoal);
        }
        else if (position == PlayerPosition.MIDFIELD){
            retVal = context.getString(R.string.PosMid);
        }
        return retVal;
    }

    private PlayerPosition StringToEnum (String position){
        PlayerPosition retVal = null;
        if (position.equals(context.getString(R.string.PosAtk))){
            retVal = PlayerPosition.ATTACK;
        }
        else if (position.equals(context.getString(R.string.PosDef))){
            retVal = PlayerPosition.DEFENSE;
        }
        else if (position.equals(context.getString(R.string.PosMid))){
            retVal = PlayerPosition.MIDFIELD;
        }
        else if (position.equals(context.getString(R.string.PosGoal))){
            retVal = PlayerPosition.GOAL;
        }
        return retVal;
    }

}
