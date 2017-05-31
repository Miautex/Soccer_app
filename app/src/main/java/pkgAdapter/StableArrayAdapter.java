package pkgAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import group2.schoolproject.a02soccer.R;

/**
 * Created by Raphael on 28.05.2017.
 *
 */

    public class StableArrayAdapter extends ArrayAdapter<String> {
    private ArrayList<String> values = new ArrayList<>();
    private View.OnTouchListener mTouchListener;
    private final Context context;

    @Override
    public void add(@Nullable String object) {
        super.add(object);
        values.add(object);
    }

    @Override
    public void remove(@Nullable String object) {
        super.remove(object);
        values.remove(object);
    }

    public StableArrayAdapter(Context context, List<String> objects, View.OnTouchListener listener) {
        super(context, -1, objects);
        this.context = context;
        mTouchListener = listener;
        values.addAll(objects);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View linea = inflater.inflate(R.layout.opaque_text_view, parent, false);
        TextView test = (TextView) linea.findViewById(R.id.listItem);
        if (values != null && values.size() >= 0) {
            test.setText(values.get(position));
        }
        linea.setOnTouchListener(mTouchListener);
        return linea;
    }
}
