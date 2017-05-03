package pkgTab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import group2.schoolproject.a02soccer.ExceptionNotification;
import group2.schoolproject.a02soccer.R;
import pkgData.Participation;


public class TabAddGameEnterData extends Fragment {
    TableLayout table_PlayersData = null;
    private View view = null;

    private HashMap<Integer, Participation> hmParticipations;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_game_enter_data,container,false);
        hmParticipations = new HashMap<>();
        getAllViews();

        return view;
    }

    private void getAllViews() {
        table_PlayersData = (TableLayout) view.findViewById(R.id.table_PlayersData);
    }

    public void setParticipations(Collection<Participation> participations) {

        try {
            hmParticipations.clear();
            for (Participation p : participations) {
                hmParticipations.put(p.getPlayer().getId(), p);
            }

            displayPlayersInTable(hmParticipations.values());
        }
        catch (Exception ex) {
            ExceptionNotification.notify(this.getContext(), ex);
            ex.printStackTrace();
        }
    }

    private void displayPlayersInTable(Collection<Participation> participations) throws Exception {
        table_PlayersData.removeAllViews();

        for (Participation p: participations) {

            TableRow row = new TableRow(this.getContext());

             TextView txvName = new TextView(this.getContext());
             EditText txvGoalsShot = new EditText(this.getContext()),
                     txvGoalsShotHead = new EditText(this.getContext()),
                     txvGoalsShotSnow = new EditText(this.getContext()),
                     txvGoalsShotPenalty = new EditText(this.getContext()),
                     txvGoalsGot = new EditText(this.getContext()),
                     txvNutmeg = new EditText(this.getContext()),
                     txvId = new EditText(this.getContext());

            txvGoalsShot.setRawInputType(InputType.TYPE_CLASS_NUMBER);
            txvGoalsShotHead.setRawInputType(InputType.TYPE_CLASS_NUMBER);
            txvGoalsShotSnow.setRawInputType(InputType.TYPE_CLASS_NUMBER);
            txvGoalsShotPenalty.setRawInputType(InputType.TYPE_CLASS_NUMBER);
            txvGoalsGot.setRawInputType(InputType.TYPE_CLASS_NUMBER);
            txvNutmeg.setRawInputType(InputType.TYPE_CLASS_NUMBER);

            txvName.setText(p.getPlayer().getName());
            txvGoalsShot.setText(Integer.toString(p.getNumGoalsShotDefault()));
            txvGoalsShotHead.setText(Integer.toString(p.getNumGoalsShotHead()));
            txvGoalsShotSnow.setText(Integer.toString(p.getNumGoalsShotHeadSnow()));
            txvGoalsShotPenalty.setText(Integer.toString(p.getNumGoalsShotPenalty()));
            txvGoalsGot.setText(Integer.toString(p.getNumGoalsGot()));
            txvNutmeg.setText(Integer.toString(p.getNumNutmeg()));

            txvId.setText(Integer.toString(p.getPlayer().getId()));
            txvId.setVisibility(View.GONE);

            row.addView(txvName);
            row.addView(txvGoalsShot);
            row.addView(txvGoalsShotHead);
            row.addView(txvGoalsShotSnow);
            row.addView(txvGoalsShotPenalty);
            row.addView(txvGoalsGot);
            row.addView(txvNutmeg);
            row.addView(txvId);

            table_PlayersData.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

        }
    }

    public ArrayList<Participation> getParticipationsFromTable() {
        ArrayList<Participation> participations = new ArrayList<>();
        TableRow row;
        EditText txvGoalsShot,
                 txvGoalsShotHead,
                 txvGoalsShotSnow,
                 txvGoalsShotPenalty,
                 txvGoalsGot,
                 txvNutmeg,
                 txvId;

        for (int i=0; i<table_PlayersData.getChildCount(); i++) {
            row = (TableRow) table_PlayersData.getChildAt(i);

            txvGoalsShot = (EditText) row.getChildAt(1);
            txvGoalsShotHead = (EditText) row.getChildAt(2);
            txvGoalsShotSnow = (EditText) row.getChildAt(3);
            txvGoalsShotPenalty = (EditText) row.getChildAt(4);
            txvGoalsGot = (EditText) row.getChildAt(5);
            txvNutmeg = (EditText) row.getChildAt(6);
            txvId = (EditText) row.getChildAt(7);

            Participation p = hmParticipations.get(Integer.parseInt(txvId.getText().toString()));
            p.setNumGoalsShotDefault(Integer.parseInt(txvGoalsShot.getText().toString()));
            p.setNumGoalsShotHead(Integer.parseInt(txvGoalsShotHead.getText().toString()));
            p.setNumGoalsShotHeadSnow(Integer.parseInt(txvGoalsShotSnow.getText().toString()));
            p.setNumGoalsShotPenalty(Integer.parseInt(txvGoalsShotPenalty.getText().toString()));
            p.setNumGoalsGot(Integer.parseInt(txvGoalsGot.getText().toString()));
            p.setNumNutmeg(Integer.parseInt(txvNutmeg.getText().toString()));

            participations.add(p);
        }

        return participations;
    }
}