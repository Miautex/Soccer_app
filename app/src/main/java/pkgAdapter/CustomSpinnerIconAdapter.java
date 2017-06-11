package pkgAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import group2.schoolproject.a02soccer.R;
import pkgMisc.PxDpConverter;

/**
 * Created by Elias on 09.06.2017.
 */

public class CustomSpinnerIconAdapter<T> extends ArrayAdapter<T> {
    private final Context context;
    private ArrayList<Drawable> icons;
    private ArrayList<T> values;

    private TextView txvText = null;
    private ImageView iconView = null;

    /**
     *
     * @param values ArrayList of all objects which are displayed in spinner (via toString())
     * @param icons ArrayList of all icons displayed left to a spinner item (in order according to values)
     *              There must an icon for every spinner. If no icon shall be displayed,
     *              set the according item of the ArrayList to null
     */
    public CustomSpinnerIconAdapter(Context context, ArrayList<T> values, ArrayList<Drawable> icons) {
        super(context, -1, values);
        if (values.size() != icons.size()) {
            throw new IllegalArgumentException("There must be an icon assigned to each value");
        }
        else {
            this.context = context;
            this.values = values;
            this.icons = icons;
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = getCustomView(position, parent);
        view.setBackground(context.getResources().getDrawable(R.drawable.buttonshape));
        view.setPadding(0, PxDpConverter.toDp(15, context), 0, PxDpConverter.toDp(15, context));

        return view;
    }

    private void getAllViews(View parent) {
        txvText = (TextView) parent.findViewById(R.id.txvText);
        iconView = (ImageView) parent.findViewById(R.id.imgIcon);
    }

    private void setContent(int position) {
        Drawable icon = icons.get(position);
        T genericObject = values.get(position);

        if (icon != null) {
            iconView.setImageDrawable(icon);
        }
        if (genericObject != null) {
            txvText.setText(genericObject.toString());
        }
    }

    private View getCustomView(int position, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_icon_spinner, parent, false);
        getAllViews(rowView);
        setContent(position);
        return rowView;
    }
}