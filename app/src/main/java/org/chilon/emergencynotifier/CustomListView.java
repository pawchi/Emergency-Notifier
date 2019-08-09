package org.chilon.emergencynotifier;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListView extends ArrayAdapter<String> {
    private String[] actionNames;
    private String[] itemColor;
    private Integer[] images;
    private Activity context;

    public CustomListView(Activity context, String[] actionNames, String[] itemColor, Integer[] images) {
        super(context, R.layout.action_button_layout, actionNames);
        this.actionNames = actionNames;
        this.itemColor = itemColor;
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r = convertView;
        ViewHolder viewHolder = null;
        if (r==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.action_button_layout, null, true);
            //viewHolder = new RecyclerView.ViewHolder(r);
        }

        return super.getView(position, convertView, parent);
    }

    class ViewHolder{
        TextView title;


        ViewHolder(View v){
            //title = (TextView)
        }
    }
}
