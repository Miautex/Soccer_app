package pkgTab;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

import group2.schoolproject.a02soccer.BaseActivity;
import group2.schoolproject.a02soccer.R;
import pkgData.Participation;
import pkgData.Player;
import pkgData.PlayerPosition;
import pkgListeners.OnScoreChangedListener;
import pkgMisc.PxDpConverter;


public class TabAddGameEnterData extends Fragment implements View.OnFocusChangeListener, View.OnKeyListener, View.OnClickListener {
    private TableLayout table_PlayersData = null;
    private View view = null;
    private TextView txtPosition = null;

    private ImageButton icGoalShot = null;
    private ImageButton icGoalGot = null;
    private ImageButton icPenalty = null;
    private ImageButton icGoalHead = null;
    private ImageButton icGoalSnow = null;
    private ImageButton icNutmeg = null;

    private OnScoreChangedListener scoreChangedListener = null;

    private HashMap<Integer, Participation> hmParticipations;
    private boolean isEditable = true;


    public TabAddGameEnterData() {
        super();
        hmParticipations = new HashMap<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_game_enter_data,container,false);

        getAllViews();
        registrateEventHandlers();

        ArrayList<Participation> participations = (ArrayList<Participation>) this.getArguments().getSerializable("participations");

        //if value isn't passed, enable editing
        try {
            isEditable = (Boolean) this.getArguments().getSerializable("isEditable");
        }
        catch (Exception ex) {
            isEditable = true;
        }

        setParticipations(participations);

        return view;
    }

    private void getAllViews() {
        table_PlayersData = (TableLayout) view.findViewById(R.id.table_PlayersData);
        icGoalShot = (ImageButton) view.findViewById(R.id.icGoalShot);
        icGoalGot = (ImageButton) view.findViewById(R.id.icGoalGot);
        icGoalHead = (ImageButton) view.findViewById(R.id.icGoalHead);
        icGoalSnow = (ImageButton) view.findViewById(R.id.icGoalSnow);
        icPenalty = (ImageButton) view.findViewById(R.id.icPenalty);
        icNutmeg = (ImageButton) view.findViewById(R.id.icNutmeg);
        txtPosition = (TextView) view.findViewById(R.id.txtPosition);
    }

    private void registrateEventHandlers(){
        icGoalShot.setOnClickListener(this);
        icGoalGot.setOnClickListener(this);
        icGoalHead.setOnClickListener(this);
        icGoalSnow.setOnClickListener(this);
        icPenalty.setOnClickListener(this);
        icNutmeg.setOnClickListener(this);
        txtPosition.setOnClickListener(this);
    }

    public void setOnScoreChangedListener(OnScoreChangedListener listener) {
        scoreChangedListener = listener;
    }

    public void setParticipations(Collection<Participation> participations) {
        try {
            hmParticipations.clear();
            for (Participation p : participations) {
                hmParticipations.put(p.getPlayer().getId(), p);
            }

            displayParticipationsInTable(new TreeSet<Participation>(participations));
        }
        catch (Exception e) {
            showMessage(getString(R.string.Error) + ": " + e.getMessage());
        }
    }

    private Participation getParticipationFromRow(int index) throws Exception {
        TableRow row = null;
        TextView[] editTexts = new TextView[7];

        if (index>=table_PlayersData.getChildCount()) {
            throw new IllegalArgumentException("Index out of bound");
        }

        row = (TableRow) table_PlayersData.getChildAt(index);

        for (int i=0; i<editTexts.length; i++) {
            editTexts[i] = (TextView) row.getChildAt(i+2);
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
        p.setPlayer((new Player(Integer.parseInt(editTexts[6].getText().toString()))));

        return p;
    }

    private String getPositionAbbreviation(PlayerPosition pos) {
        String retVal = null;

        switch (pos) {
            case ATTACK:
                retVal = getString(R.string.PosAtkAbbrev);
                break;
            case DEFENSE:
                retVal = getString(R.string.PosDefAbbrev);
                break;
            case MIDFIELD:
                retVal = getString(R.string.PosMidAbbrev);
                break;
            case GOAL:
                retVal = getString(R.string.PosGoalAbbrev);
                break;
        }

        return retVal;
    }

    private void displayParticipationsInTable(Collection<Participation> participations) throws Exception {
        table_PlayersData.removeAllViews();

        for (Participation p: participations) {

            TableRow row = new TableRow(this.getContext());
            TextView txvName = new TextView(this.getContext()),
                    txvPos = new TextView(this.getContext());
            TableRow.LayoutParams lptr = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);

            row.addView(txvName);
            row.addView(txvPos);

            txvName.setText(p.getPlayer().getName());
            txvName.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2));
            txvName.setPadding(0, 0, PxDpConverter.toDp(5, this.getContext()),0);

            txvPos.setText(getPositionAbbreviation(p.getPosition()));
            txvPos.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.5f));
            txvPos.setGravity(Gravity.CENTER_VERTICAL);
            txvPos.setTypeface(null, Typeface.BOLD);
            txvPos.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

            TextView[] editTexts = new TextView[7];

            for (int i=0; i<editTexts.length; i++) {
                TextView et;

                if (isEditable) {
                    et = new EditText(this.getContext());
                }
                else {
                    et = new TextView(this.getContext());
                    et.setGravity(Gravity.CENTER);
                    et.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                }

                et.setBackgroundResource(R.drawable.tablelayout2);
                et.setLayoutParams(lptr);
                et.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                et.setLongClickable(false);
                et.setOnFocusChangeListener(this);
                et.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                et.setOnKeyListener(this);
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

            //border-lines
            row.setBackgroundResource(R.drawable.tablelayout2);
            row.setPadding(0, 0, 0, 0);
            txvPos.setBackgroundResource(R.drawable.tablelayout2);
            txvName.setBackgroundResource(R.drawable.tablelayout2);
            txvName.setPadding(PxDpConverter.toDp(5,this.getContext()), 0, 0, 0);


            txvName.setOnClickListener(this);
            txvPos.setOnClickListener(this);

            ((TextView) row.getChildAt(0)).setGravity(Gravity.CENTER_VERTICAL);
            for (int i=1; i<row.getChildCount(); i++) {
                ((TextView) row.getChildAt(i)).setGravity(Gravity.CENTER);
            }

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
                showMessage(getString(R.string.Error) + ": " + ex.getMessage());
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
            //if invalid/no text has been entered, set it to 0 on focus lost
            if (edt.getText().length() == 0) {
                edt.setText("0");
            }
            informOnScoreChangedListener(calcuteSumOfGoals());
        }
        else {
            //if text==0, clear field on selection to make entering easier
            if (edt.getText().toString().equals("0")) {
                edt.setText("");
            }
        }
    }

    public void forceScoreRecalculation() {
        informOnScoreChangedListener(calcuteSumOfGoals());
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (event.getAction() == KeyEvent.ACTION_DOWN)
        {
            switch (keyCode)
            {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    forceScoreRecalculation();
                    return true;
                default:
                    break;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        try {
            if (v.getId() == R.id.icGoalShot) {
                showMessage(getString(R.string.GoalsShotDefault));
            } else if (v.getId() == R.id.icGoalGot){
                showMessage(getString(R.string.GoalsGot));
            } else if (v.getId() == R.id.icGoalHead){
                showMessage(getString(R.string.GoalsShotHead));
            } else if (v.getId() == R.id.icGoalSnow){
                showMessage(getString(R.string.GoalsShotHeadSnow));
            } else if (v.getId() == R.id.icPenalty){
                showMessage(getString(R.string.GoalsShotPenalty));
            } else if (v.getId() == R.id.icNutmeg){
                showMessage(getString(R.string.Nutmeg));
            } else if (v.getId() == R.id.txtPosition){
                showMessage(getString(R.string.Positions));
            }
            else if (v.getClass().equals(TextView.class)) {
                TableRow row = (TableRow) v.getParent();

                Participation clickedParticipation = hmParticipations
                        .get(Integer.parseInt(((TextView) row.getChildAt(8)).getText().toString()));

                showMessage(clickedParticipation.getPlayer().toString());
            }

        } catch (Exception e) {
            showMessage(getString(R.string.Error) + ": " + e.getMessage());
        }
    }

    private void showMessage(String msg) {
        //Dirty, but works
        ((BaseActivity) scoreChangedListener).showMessage(msg);
    }
}