package com.learning.android.movieman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.learning.android.movieman.R;

/**
 * Created by adrianrusu on 3/16/15.
 */
public class NavbarRecyclerAdapter extends RecyclerView.Adapter<NavbarRecyclerAdapter.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    private String[] navTitles;
    private int[] icons;

    Context context;

    public NavbarRecyclerAdapter(String[] navTitles, int[] icons, Context context) {
        this.navTitles = navTitles;
        this.icons = icons;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item,parent,false);

            ViewHolder vhItem = new ViewHolder(v, viewType, context);

            return vhItem;
        } else if (viewType == TYPE_HEADER) {
            
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header,parent,false);

            ViewHolder vhHeader = new ViewHolder(v, viewType, context);

            return vhHeader;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(holder.viewHolderType == TYPE_ITEM) {
            holder.textView.setText(navTitles[position - 1]);
            holder.imageView.setImageResource(icons[position -1]);
        }
        else{
        }
    }

    @Override
    public int getItemCount() {
        return navTitles.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        int viewHolderType;

        TextView textView;
        ImageView imageView;

        Context context;

        public ViewHolder(View itemView, int viewType, Context context) {
            super(itemView);
            this.context = context;
            itemView.setClickable(true);
            itemView.setOnClickListener(this);

            if (viewType == TYPE_ITEM) {
                viewHolderType = viewType;
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
            } else {

            }
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "The item clicked is: " + getAdapterPosition(), Toast.LENGTH_LONG).show();
        }
    }
}