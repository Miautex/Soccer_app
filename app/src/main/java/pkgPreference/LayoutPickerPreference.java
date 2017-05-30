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
import pkgMisc.AssignmentLayout;

/**
 * @author Marco Wilscher
 */

public class LayoutPickerPreference extends DialogPreference implements View.OnClickListener {
    private TreeMap<Integer, OptionEntity<ImageButton, AssignmentLayout>> optionButtons;
    private SharedPreferences preferences;
    private OptionEntity<ImageButton, AssignmentLayout> selectedOption;

    public LayoutPickerPreference (Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(false);
        setDialogLayoutResource(R.layout.layout_picker_preference);
        optionButtons = new TreeMap<>();
    }

    @Override
    protected void onBindDialogView (View view) {
        super.onBindDialogView(view);
        optionButtons.put(R.id.optionLayout1, new OptionEntity<>((ImageButton) view.findViewById(R.id.optionLayout1), AssignmentLayout.TAB_LAYOUT));
        optionButtons.put(R.id.optionLayout2, new OptionEntity<>((ImageButton) view.findViewById(R.id.optionLayout2), AssignmentLayout.SLIDE_LAYOUT));
        optionButtons.put(R.id.optionLayout3, new OptionEntity<>((ImageButton) view.findViewById(R.id.optionLayout3), AssignmentLayout.RADIO_LAYOUT));

        optionButtons.get(R.id.optionLayout1).getView().setOnClickListener(this);
        optionButtons.get(R.id.optionLayout2).getView().setOnClickListener(this);
        optionButtons.get(R.id.optionLayout3).getView().setOnClickListener(this);

        preferences = getSharedPreferences();

        AssignmentLayout layout = AssignmentLayout.valueOf(preferences.getString(getKey(), AssignmentLayout.TAB_LAYOUT.toString()));

        selectedOption = getOptionEntityByLayout(layout);
        highlightSelectedOption(selectedOption);
    }

    @Override
    public void onClick (View view) {
        selectedOption = optionButtons.get(view.getId());
        highlightSelectedOption(selectedOption);
    }

    @Override
    protected void onDialogClosed (boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            SharedPreferences.Editor editor = getEditor();
            editor.putString(getKey(), selectedOption.getValue().toString());
            editor.commit();
        }
    }

    private void highlightSelectedOption (OptionEntity<ImageButton, AssignmentLayout> option) {
        for (Map.Entry<Integer, OptionEntity<ImageButton, AssignmentLayout>> entry: optionButtons.entrySet()) {
            if (entry.getKey() == option.getView().getId()) {
                entry.getValue().getView().setColorFilter(Color.RED);
            }
            else {
                entry.getValue().getView().setColorFilter(Color.TRANSPARENT);
            }
        }
    }

    private OptionEntity<ImageButton, AssignmentLayout> getOptionEntityByLayout(AssignmentLayout layout) {
        OptionEntity<ImageButton, AssignmentLayout> entity = null;
        OptionEntity<ImageButton, AssignmentLayout>[] entities = optionButtons.values().toArray(new OptionEntity[0]);
        Boolean found = false;
        for (int i = 0; i < optionButtons.size() && !found; i++) {
            if(entities[i].getValue() == layout){
                entity = entities[i];
            }
        }
        return entity;
    }
}
