package pkgAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeMap;

import group2.schoolproject.a02soccer.R;

import pkgData.Player;
import pkgDatabase.Database;

/**
 * @author Raphael Moser
 */

public class QRListAdapter extends ArrayAdapter<Player> implements View.OnClickListener{
    private final Context context;
    private TreeMap<Integer,Player> players;
    private ArrayList<Integer> ids;
    private Drawable drawable;


    public QRListAdapter(@NonNull Context context) {
        super(context, -1);
        players = new TreeMap<>();
        ids = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.player_qr_list, parent, false);
        TextView id = (TextView) rowView.findViewById(R.id.playerId);
        System.out.println(id.toString());
        TextView playerName = (TextView) rowView.findViewById(R.id.playerData);
        ImageButton btnRemove = (ImageButton) rowView.findViewById(R.id.removePlayer);
        btnRemove.setOnClickListener(this);
        id.setText(String.valueOf(ids.get(position)));
        playerName.setText(players.get(ids.get(position)).toString());
        rowView.setBackground(context.getResources().getDrawable(R.drawable.tablelayoutred));
        return rowView;
    }

    public void setBackground(Drawable drawable){
        this.drawable = drawable;
    }
    @Override
    public void add(@Nullable Player object) {
        super.add(object);
        ids.add(object.getId());
        players.put(object.getId(),object);
    }

    public boolean contains(int id){
        return players.containsKey(id);
    }

    @Override
    public void remove(@Nullable Player object) {
        super.remove(object);
        ids.remove(object.getId());
        players.remove(object.getId());
    }

    @Override
    public void onClick(View v) {
        try {
            LinearLayout l = (LinearLayout) v.getParent();
            int i = Integer.parseInt(((TextView) l.getChildAt(0)).getText().toString());
            remove(Database.getInstance().getPlayerByID(i));
        }
        catch (Exception e){
            System.out.println("nix");
        }
    }

    public ArrayList<Player> getAllPlayers(){
        ArrayList<Player> retVal = new ArrayList<>();
        retVal.addAll(players.values());
        return retVal;
    }
}
