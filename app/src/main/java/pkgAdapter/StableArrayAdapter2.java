package pkgAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import group2.schoolproject.a02soccer.R;
import pkgData.Participation;
import pkgData.Player;
import pkgData.PlayerPosition;
import pkgData.Team;
import pkgDatabase.Database;

/**
 * Created by Raphael on 28.05.2017.
 *
 */

    public class StableArrayAdapter2 extends ArrayAdapter<Player> implements AdapterView.OnItemSelectedListener {
    private ArrayList<Player> values = new ArrayList<>();
    private View.OnTouchListener mTouchListener;
    private final Context context;
    private SpinnerAdapter spinnerAdapter;
    private int color = Color.WHITE;
    private TreeMap<Player,Integer> playerwithSpinnerPos;
    private Team team;


    public void add(@Nullable Player object) {
        super.add(object);
        values.add(object);
    }

    public void addWithPosition(Participation part){
        super.add(part.getPlayer());
        values.add(part.getPlayer());
        playerwithSpinnerPos.put(part.getPlayer(),positionToInt(part.getPlayer(),part.getPosition()));
    }

    public Participation removeWithPos(Player p){
        Participation part = new Participation();
        part.setPlayer(p);
        part.setPosition(intToPosition(p,playerwithSpinnerPos.get(p)));
        remove(p);
        return part;
    }

    public void setColor(int color){
        this.color = color;
    }


    public void remove(@Nullable Player object) {
        super.remove(object);
        values.remove(object);
    }


    public StableArrayAdapter2(Context context, List<Player> objects, View.OnTouchListener listener,Team team) {
        super(context, -1, objects);
        this.context = context;
        mTouchListener = listener;
        values.addAll(objects);
        playerwithSpinnerPos = new TreeMap<>();
        this.team = team;
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
        View linea = inflater.inflate(R.layout.swipe_list, parent, false);
        linea.setBackgroundColor(color);
        View linea2 = linea.findViewById(R.id.linearLayout2);
        TextView test = (TextView) linea2.findViewById(R.id.listItem);
        Spinner spinner = (Spinner) linea2.findViewById(R.id.spinner);
        if (values != null && values.size() >= 0) {
            test.setText(values.get(position).toString());
            spinner = createSpinner(spinner, values.get(position).getPositions(),values.get(position));
            spinner.setOnItemSelectedListener(this);
        }
        linea.setOnTouchListener(mTouchListener);
        return linea;
    }

    private Spinner createSpinner(Spinner s,TreeSet<PlayerPosition> positions, Player player) {
        List<String> spinnerArray = new ArrayList<>();
        for (PlayerPosition pos : positions) {
            spinnerArray.add(EnumToString(pos));
        }
        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        s.setAdapter(spinnerAdapter);
        if(playerwithSpinnerPos.containsKey(player)) {
            s.setSelection(playerwithSpinnerPos.get(player));
        }
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

    public void setItem(int position){
        playerwithSpinnerPos.remove(values.get(position));
    }

    public ArrayList<Participation> getParticipations(){
        ArrayList<Participation> participations = new ArrayList<>();
        for (Player p : values) {
            ArrayList<PlayerPosition> newList = new ArrayList<>(p.getPositions());
            Participation temp = new Participation(p,team,newList.get(playerwithSpinnerPos.get(p)));
            participations.add(temp);
        }
        return participations;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout linea = (LinearLayout) parent.getParent();
        TextView t = (TextView) linea.getChildAt(0);
        Spinner s = (Spinner) linea.getChildAt(1);
        System.out.println(position);
        s.setSelection(position);
        String username = t.getText().toString().split("\n")[1].trim();
        username = username.substring(1);
        Player p = Database.getInstance().getPlayerByUsername(username);
        if(playerwithSpinnerPos.containsKey(p));
        {
            playerwithSpinnerPos.remove(p);
        }
        playerwithSpinnerPos.put(p,position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private PlayerPosition intToPosition(Player p, int pos){
        ArrayList<PlayerPosition> newList = new ArrayList<>(p.getPositions());
        return newList.get(playerwithSpinnerPos.get(p));
    }

    private int positionToInt(Player p, PlayerPosition pos){
        int count = 0;
        ArrayList<PlayerPosition> newList = new ArrayList<>(p.getPositions());
        for(PlayerPosition position : newList){
            if(pos != position){
                count++;
            }
            else {
                break;
            }
        }
        return count;
    }
}
