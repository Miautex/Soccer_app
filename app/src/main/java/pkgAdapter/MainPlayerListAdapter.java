package pkgAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import group2.schoolproject.a02soccer.MainActivity;
import group2.schoolproject.a02soccer.R;
import pkgData.Player;

/**
 * @author Elias Santner
 */

public class MainPlayerListAdapter extends ArrayAdapter<Player> {
    private final Context context;
    private ArrayList<Player> values;
    private MainActivity activity;
    private boolean isAdminView;

    private TextView txvName = null;
    private ImageButton btnEdit = null,
                        btnDelete = null;

    public MainPlayerListAdapter(Context context, ArrayList<Player> values, MainActivity activity, boolean isAdminView) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.activity = activity;
        this.isAdminView = isAdminView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_main_player, parent, false);
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
        txvName = (TextView) parent.findViewById(R.id.txvName);
        btnEdit = (ImageButton) parent.findViewById(R.id.btnEdit);
        btnDelete = (ImageButton) parent.findViewById(R.id.btnDelete);
    }

    private void setContent(int position) {
        Player player = values.get(position);
        txvName.setText(player.toString());
        if (player.isLocallySavedOnly()) {
            txvName.setTextColor(getContext().getResources().getColor(R.color.redLogo));
        }
    }

    private void setListeners(final int position) {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    activity.onEditPlayer(values.get(position));
                } catch (Exception e) {
                    activity.showMessage(e.getMessage());
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    activity.onDeletePlayer(values.get(position));
                } catch (Exception e) {
                    activity.showMessage(e.getMessage());
                }
            }
        });
    }
}