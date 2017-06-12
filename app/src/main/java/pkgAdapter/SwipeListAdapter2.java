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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

    public class SwipeListAdapter2 extends ArrayAdapter<Player> implements AdapterView.OnItemSelectedListener {
    private ArrayList<Player> values = new ArrayList<>();
    private View.OnTouchListener mTouchListener;
    private final Context context;
    private int color = Color.WHITE;
    private TreeMap<Player,Integer> playerWithPos;
    private Team team;


    public void add(@Nullable Player player) {
        super.add(player);
        values.add(player);
    }

    public void addWithPosition(Participation part){
        super.add(part.getPlayer());
        values.add(part.getPlayer());
        playerWithPos.put(part.getPlayer(),positionToInt(part.getPlayer(),part.getPosition()));
    }

    public Participation removeWithPos(Player p){
        Participation part = new Participation();
        part.setPlayer(p);
        part.setPosition(intToPosition(p));
        remove(p);
        return part;
    }

    public void setColor(int color){
        this.color = color;
    }


    public void remove(@Nullable Player player) {
        super.remove(player);
        values.remove(player);
    }


    public SwipeListAdapter2(Context context, List<Player> oplayers, View.OnTouchListener listener, Team team) {
        super(context, -1, oplayers);
        this.context = context;
        mTouchListener = listener;
        values.addAll(oplayers);
        playerWithPos = new TreeMap<>();
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
        View conatainer = inflater.inflate(R.layout.swipe_list, parent, false);
        conatainer.setBackgroundColor(color);
        View contentContainer = conatainer.findViewById(R.id.linearLayout2);
        TextView playerName = (TextView) contentContainer.findViewById(R.id.listItem);
        Spinner positionsSpinner = (Spinner) contentContainer.findViewById(R.id.spinner);
        TextView positionsPreview = (TextView) conatainer.findViewById(R.id.posOverview);
        positionsPreview.setVisibility(View.GONE);
        if (values != null && values.size() >= 0) {
            playerName.setText(values.get(position).toString());
            positionsSpinner = createSpinner(positionsSpinner, values.get(position).getPositions(),values.get(position));
            positionsSpinner.setOnItemSelectedListener(this);
        }
        conatainer.setOnTouchListener(mTouchListener);
        return conatainer;
    }

    private Spinner createSpinner(Spinner s,TreeSet<PlayerPosition> positions, Player player) {
        ArrayList<String> spinnerArray = new ArrayList<>();
        for (PlayerPosition pos : positions) {
            spinnerArray.add(EnumToString(pos));
        }
        CustomSpinnerAdapter<String> spinnerAdapter = new CustomSpinnerAdapter<>(getContext(), spinnerArray);
        s.setAdapter(spinnerAdapter);
        if(playerWithPos.containsKey(player)) {
            s.setSelection(playerWithPos.get(player));
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

    public void setItem(int position){
        playerWithPos.remove(values.get(position));
    }

    public ArrayList<Participation> getParticipations(){
        ArrayList<Participation> participations = new ArrayList<>();
        for (Player p : values) {
            ArrayList<PlayerPosition> newList = new ArrayList<>(p.getPositions());
            Participation temp = new Participation(p,team,newList.get(playerWithPos.get(p)));
            participations.add(temp);
        }
        return participations;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout linea = (LinearLayout) parent.getParent();
        TextView t = (TextView) linea.getChildAt(0);
        Spinner s = (Spinner) linea.getChildAt(1);
        s.setSelection(position);
        String username = t.getText().toString().split("\n")[1].trim();
        username = username.substring(1);
        Player p = Database.getInstance().getPlayerByUsername(username);
        if(playerWithPos.containsKey(p)){
            playerWithPos.remove(p);
        }
        playerWithPos.put(p,position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private PlayerPosition intToPosition(Player p){
        ArrayList<PlayerPosition> newList = new ArrayList<>(p.getPositions());
        return newList.get(playerWithPos.get(p));
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
    public void shufflePositions(){
        Random rand = new Random();
        ArrayList<PlayerPosition> positions;
        for (Player p : values){
            positions = new ArrayList<>(p.getPositions());
            playerWithPos.remove(p);
            playerWithPos.put(p,rand.nextInt(positions.size()));
        }
    }
}
