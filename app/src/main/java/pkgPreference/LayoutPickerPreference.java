package pkgPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import java.util.Map;
import java.util.TreeMap;

import group2.schoolproject.a02soccer.R;

/**
 * Created by Wilscher Marco
 */

public class LayoutPickerPreference extends DialogPreference implements View.OnClickListener {
    private TreeMap<Integer, OptionEntity<ImageButton, String>> optionButtons;
    private SharedPreferences preferences;
    private ImageButton selectedButton;

    public LayoutPickerPreference (Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(false);
        setDialogLayoutResource(R.layout.layout_picker_preference);
        optionButtons = new TreeMap<>();
    }

    @Override
    protected void onBindDialogView (View view) {
        super.onBindDialogView(view);
        optionButtons.put(R.id.optionLayout1, new OptionEntity<>((ImageButton) view.findViewById(R.id.optionLayout1), "layout1"));
        optionButtons.put(R.id.optionLayout2, new OptionEntity<>((ImageButton) view.findViewById(R.id.optionLayout2), "layout2"));
        optionButtons.put(R.id.optionLayout3, new OptionEntity<>((ImageButton) view.findViewById(R.id.optionLayout3), "layout3"));

        optionButtons.get(R.id.optionLayout1).getView().setOnClickListener(this);
        optionButtons.get(R.id.optionLayout2).getView().setOnClickListener(this);
        optionButtons.get(R.id.optionLayout3).getView().setOnClickListener(this);

        preferences = getSharedPreferences();

        String layout = preferences.getString(getKey(), "layout1");

        OptionEntity<ImageButton, String>[] oentities = optionButtons.values().toArray(new OptionEntity[0]);
        Boolean found = false;
        for (int i = 0; i < optionButtons.size() && !found; i++) {
            if(oentities[i].getValue().equals(layout)){
                setSelectedOption(oentities[i].getView().getId());
                found = true;
            }
        }
    }

    @Override
    public void onClick (View view) {
        selectedButton = (ImageButton)view;
        setSelectedOption(selectedButton.getId());
    }

    @Override
    protected void onDialogClosed (boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            SharedPreferences.Editor editor = getEditor();
            editor.putString(getKey(), optionButtons.get(selectedButton.getId()).getValue());
            editor.commit();
        }
    }

    private void setSelectedOption(int resId) {
        for (Map.Entry<Integer, OptionEntity<ImageButton, String>> entry: optionButtons.entrySet()) {
            if (entry.getKey() == resId) {
                entry.getValue().getView().setColorFilter(Color.RED);
            }
            else {
                entry.getValue().getView().setColorFilter(Color.TRANSPARENT);
            }
        }
    }
}
