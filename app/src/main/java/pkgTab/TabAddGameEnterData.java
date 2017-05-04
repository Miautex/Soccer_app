package pkgTab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
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
import pkgData.Player;
import pkgListeners.OnScoreChangedListener;


public class TabAddGameEnterData extends Fragment implements View.OnFocusChangeListener {
    TableLayout table_PlayersData = null;
    private View view = null;

    OnScoreChangedListener scoreChangedListener = null;

    private HashMap<Integer, Participation> hmParticipations;

    public TabAddGameEnterData() {
        super();
    }

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

    public void setOnScoreChangedListener(OnScoreChangedListener listener) {
        scoreChangedListener = listener;
    }

    public void updateParticipations(Collection<Participation> participations) throws Exception {
        /*if (!participations.equals(hmParticipations.values())) {
            throw new IllegalArgumentException("The passed participations are not " +
                    "equal to to already displayed ones. Please use setParticipations");
        }*/

        Participation[] partArray = participations.toArray(new Participation[0]);

        for (int i=0; i<partArray.length; i++) {
            setParticipationForRow(i, partArray[i]);
        }

    }

    public void setParticipations(Collection<Participation> participations) {
        try {
            updateParticipations(participations);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            try {
                hmParticipations.clear();
                for (Participation p : participations) {
                    hmParticipations.put(p.getPlayer().getId(), p);
                }

                displayPlayersInTable(participations);
            }
            catch (Exception e) {
                ExceptionNotification.notify(this.getContext(), e);
            }
        }
    }

    private void setParticipationForRow(int index, Participation p) throws Exception {
        TableRow row = null;
        TextView[] viewTexts = new TextView[8];

        if (index>=table_PlayersData.getChildCount()) {
            throw new IllegalArgumentException("Index out of bound");
        }

        row = (TableRow) table_PlayersData.getChildAt(index);

        for (int i=0; i<viewTexts.length; i++) {
            viewTexts[i] = (TextView) row.getChildAt(i);
        }

        viewTexts[0].setText(p.getPlayer().getName());
        viewTexts[1].setText(Integer.toString(p.getNumGoalsShotDefault()));
        viewTexts[2].setText(Integer.toString(p.getNumGoalsShotHead()));
        viewTexts[3].setText(Integer.toString(p.getNumGoalsShotHeadSnow()));
        viewTexts[4].setText(Integer.toString(p.getNumGoalsShotPenalty()));
        viewTexts[5].setText(Integer.toString(p.getNumGoalsGot()));
        viewTexts[6].setText(Integer.toString(p.getNumNutmeg()));
        viewTexts[7].setText(Integer.toString(p.getPlayer().getId()));
    }

    private Participation getParticipationFromRow(int index) throws Exception {
        TableRow row = null;
        TextView[] editTexts = new TextView[7];

        if (index>=table_PlayersData.getChildCount()) {
            throw new IllegalArgumentException("Index out of bound");
        }

        row = (TableRow) table_PlayersData.getChildAt(index);

        for (int i=0; i<editTexts.length; i++) {
            editTexts[i] = (TextView) row.getChildAt(i+1);
            if (editTexts[i].getText().length() == 0) {
                editTexts[i].setText("0");
            }
        }

        Participation p = new Participation();
        p.setNumGoalsShotDefault(Integer.parseInt(editTexts[0].getText().toString()));
        p.setNumGoalsShotHead(Integer.parseInt(editTexts[1].getText().toString()));
        p.setNumGoalsShotHeadSnow(Integer.parseInt(editTexts[2].getText().toString()));
        p.setNumGoalsShotPenalty(Integer.parseInt(editTexts[3].getText().toString()));
        p.setNumGoalsGot(Integer.parseInt(editTexts[4].getText().toString()));
        p.setNumNutmeg(Integer.parseInt(editTexts[5].getText().toString()));
        p.setPlayer((new Player(Integer.parseInt(editTexts[6].getText().toString()), "", "", false)));

        return p;
    }

    private void displayPlayersInTable(Collection<Participation> participations) throws Exception {
        table_PlayersData.removeAllViews();

        for (Participation p: participations) {

            TableRow row = new TableRow(this.getContext());
            TextView txvName = new TextView(this.getContext());
            TableRow.LayoutParams lptr = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);

            row.addView(txvName);
            txvName.setLayoutParams(lptr);
            txvName.setText(p.getPlayer().getName());

            EditText[] editTexts = new EditText[7];

            for (int i=0; i<editTexts.length; i++) {
                EditText et = new EditText(this.getContext());

                et.setLayoutParams(lptr);
                et.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                et.setLongClickable(false);
                et.setGravity(Gravity.RIGHT);
                et.setOnFocusChangeListener(this);
                et.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                row.addView(et);
                editTexts[i] = et;
            }

            editTexts[0].setText(Integer.toString(p.getNumGoalsShotDefault()));
            editTexts[1].setText(Integer.toString(p.getNumGoalsShotHead()));
            editTexts[2].setText(Integer.toString(p.getNumGoalsShotHeadSnow()));
            editTexts[3].setText(Integer.toString(p.getNumGoalsShotPenalty()));
            editTexts[4].setText(Integer.toString(p.getNumGoalsGot()));
            editTexts[5].setText(Integer.toString(p.getNumNutmeg()));

            editTexts[6].setText(Integer.toString(p.getPlayer().getId()));
            editTexts[6].setVisibility(View.GONE);

            table_PlayersData.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    public ArrayList<Participation> getParticipationsFromTable() {
        ArrayList<Participation> participations = new ArrayList<Participation>();

        for (int i=0; i<table_PlayersData.getChildCount(); i++) {
            Participation tmpP = null;
            try {
                tmpP = getParticipationFromRow(i);

                Participation p = hmParticipations.get(tmpP.getPlayer().getId());
                p.setNumGoalsShotDefault(tmpP.getNumGoalsShotDefault());
                p.setNumGoalsShotHead(tmpP.getNumGoalsShotHead());
                p.setNumGoalsShotHeadSnow(tmpP.getNumGoalsShotHeadSnow());
                p.setNumGoalsShotPenalty(tmpP.getNumGoalsShotPenalty());
                p.setNumGoalsGot(tmpP.getNumGoalsGot());
                p.setNumNutmeg(tmpP.getNumNutmeg());

                participations.add(p);
            }
            catch (Exception ex) {
                ExceptionNotification.notify(this.getContext(), ex);
                ex.printStackTrace();
            }
        }

        return participations;
    }

    private int calcuteSumOfGoals() {
        int sum = 0;

        for (Participation p: getParticipationsFromTable()) {
            sum += p.getNumGoalsShotDefault();
            sum += p.getNumGoalsShotHead();
            sum += p.getNumGoalsShotHeadSnow();
            sum += p.getNumGoalsShotPenalty();
        }

        return sum;
    }

    private void informOnScoreChangedListener(int newScore) {
        if (scoreChangedListener != null) {
            scoreChangedListener.onScoreUpdated(newScore, this);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        EditText edt = (EditText) v;

        if (!hasFocus) {
            if (edt.getText().length() == 0) {
                edt.setText("0");
            }
            informOnScoreChangedListener(calcuteSumOfGoals());
        }
    }
}