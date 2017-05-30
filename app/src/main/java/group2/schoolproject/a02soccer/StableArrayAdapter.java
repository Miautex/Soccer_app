package group2.schoolproject.a02soccer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pkgData.PlayerWithScore;

/**
 * Created by Raphael on 28.05.2017.
 */

    public class StableArrayAdapter extends ArrayAdapter<String> {

    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
    ArrayList<String> values = new ArrayList<>();
    View.OnTouchListener mTouchListener;
    private final Context context;



    public StableArrayAdapter(Context context,List<String> objects, View.OnTouchListener listener) {
        super(context, -1, objects);
        this.context=context;
        mTouchListener = listener;
        /*for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }*/
        values.addAll(objects);
    }

    @Override
    public long getItemId(int position) {
        values.get(position);
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View linea = inflater.inflate(R.layout.opaque_text_view, parent, false);
        TextView test = (TextView) linea.findViewById(R.id.listItem);
        if(values != null && values.size() > 0){
            test.setText(values.get(position));
            linea.setOnTouchListener(mTouchListener);
        }


        return linea;
        /*System.out.println(position);
        System.out.println("dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd " +convertView);
        System.out.println(parent);
        View view = super.getView(position, convertView, (ViewGroup) parent.getChildAt(position));
        if (view != convertView) {
            // Add touch listener to every new view to track swipe motion
            view.setOnTouchListener(mTouchListener);
        }
        return view;*/
    }

}
