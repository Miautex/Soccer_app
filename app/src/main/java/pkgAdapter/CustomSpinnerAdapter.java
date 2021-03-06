package pkgAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import group2.schoolproject.a02soccer.R;
import pkgMisc.PxDpConverter;

/**
 * @author  Elias Santner
 */

public class CustomSpinnerAdapter<T> extends ArrayAdapter<T> {
    private final Context context;
    private ArrayList<T> values;
    private TextView txvText = null;

    /**
     * @param values ArrayList of all objects which are displayed in spinner (via toString())
     */
    public CustomSpinnerAdapter(Context context, ArrayList<T> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
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
        view.setPadding(PxDpConverter.toDp(8, context), PxDpConverter.toDp(8, context),
                PxDpConverter.toDp(8, context), PxDpConverter.toDp(8, context));

        return view;
    }

    private void getAllViews(View parent) {
        txvText = (TextView) parent.findViewById(R.id.txvText);
    }

    private void setContent(int position) {
        T genericObject = values.get(position);

        if (genericObject != null) {
            txvText.setText(genericObject.toString());
        }
    }

    private View getCustomView(int position, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_spinner, parent, false);
        getAllViews(rowView);
        setContent(position);
        return rowView;
    }
}