package com.kshimauchi.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kshimauchi.myapplication.Model.Repository;

import java.util.ArrayList;


/**
 * Created by kshim on 7/11/2017.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.itemHolder> {
    private ArrayList<Repository> data;
    itemClickListener listener;

    public Adapter(ArrayList<Repository>data, itemClickListener listener){
        this.data = data;
        this.listener = listener;

    }
public interface itemClickListener
    {
        /******************************/
        void onItemClick(int clickedItemIndex);
    }
@Override
    public itemHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context =  parent.getContext();

    LayoutInflater inflat = LayoutInflater.from(context);

    boolean shouldAttachToParentImmediately = false;

    View view = inflat.inflate(R.layout.item, parent, shouldAttachToParentImmediately);

    itemHolder holder = new itemHolder(view);

    return holder;
    }
    @Override
    public void onBindViewHolder(itemHolder holder, int position){
        holder.bind(position);
    }
    @Override
    public int getItemCount(){
        return data.size();
    }
    //inner class
    class itemHolder extends ViewHolder implements View.OnClickListener{
    //Variables
        TextView name;
        TextView url;

        itemHolder(View view){
            super(view);

            name =(TextView) view.findViewById(R.id.name);

            url =(TextView)view.findViewById(R.id.url);

            view.setOnClickListener(this);
        }
        //binds some params to for item xml
        public void bind(int pos){
            Repository repo = data.get(pos);
            name.setText(repo.getName());
            url.setText(repo.getUrl());
        }
        @Override
        public void onClick(View v)
        {

            int pos = getPosition();

            listener.onItemClick(pos);
        }

    }
}
