package org.chilon.emergencynotifier;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListView extends ArrayAdapter<String> {
    private String[] actionNames;
    private String[] itemColor;
    private Integer[] images;
    private Activity context;

    public CustomListView(Activity context, String[] actionNames, String[] itemColor, Integer[] images) {
        super(context, R.layout.button_list_view_layout, actionNames);
        this.actionNames = actionNames;
        this.itemColor = itemColor;
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        ViewHolder viewHolder = null;
        if (view==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.button_list_view_layout, null, true);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.title.setText(actionNames[position]);
        viewHolder.colorsTest.setText(itemColor[position]);
        viewHolder.waitedMessagesCircle.setImageResource(images[position]);

        return view;
    }

    class ViewHolder{
        TextView title;
        ImageView waitedMessagesCircle;
        TextView colorsTest;

        ViewHolder(View v){
            title = (TextView) v.findViewById(R.id.actiontype);
            colorsTest = (TextView) v.findViewById(R.id.waited_messages);
            waitedMessagesCircle = (ImageView) v.findViewById(R.id.image_circle);
        }
    }
}
