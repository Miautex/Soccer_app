package pkgAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import group2.schoolproject.a02soccer.MainActivity;
import group2.schoolproject.a02soccer.R;
import pkgData.Game;
import pkgMisc.LocalizedDateFormatter;

/**
 * @author Elias Santner
 */

public class MainGameListAdapter extends ArrayAdapter<Game> {
    private final Context context;
    private ArrayList<Game> values;
    private MainActivity activity;
    private boolean isAdminView;

    private TextView txvDate = null,
                     txvScore = null;
    private ImageButton btnEdit = null,
                        btnDelete = null;

    public MainGameListAdapter(Context context, ArrayList<Game> values, MainActivity activity, boolean isAdminView) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.activity = activity;
        this.isAdminView = isAdminView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_main_game, parent, false);
        getAllViews(rowView);
        setContent(position);
        setListeners(position);
        setAdminFeatures();
        return rowView;
    }

    private void setAdminFeatures() {
        if (!isAdminView) {
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }
    }

    private void getAllViews(View parent) {
        txvDate = (TextView) parent.findViewById(R.id.txvDate);
        txvScore = (TextView) parent.findViewById(R.id.txvScore);
        btnEdit = (ImageButton) parent.findViewById(R.id.btnEdit);
        btnDelete = (ImageButton) parent.findViewById(R.id.btnDelete);
    }

    private void setContent(int position) {
        Game game = values.get(position);
        txvDate.setText(LocalizedDateFormatter.format(game.getDate(), Locale.getDefault()));
        txvScore.setText(game.getScoreTeamA() + ":" + game.getScoreTeamB());
    }

    private void setListeners(final int position) {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    activity.onEditGame(values.get(position));
                } catch (Exception e) {
                    activity.showMessage(e.getMessage());
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    activity.onDeleteGame(values.get(position));
                } catch (Exception e) {
                    activity.showMessage(e.getMessage());
                }
            }
        });
    }
}