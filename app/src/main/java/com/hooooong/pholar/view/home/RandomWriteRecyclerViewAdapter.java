package com.hooooong.pholar.view.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hooooong.pholar.R;

import java.util.List;

/**
 * Created by Heepie on 2017. 11. 7..
 */

public class RandomWriteRecyclerViewAdapter extends RecyclerView.Adapter<RandomWriteRecyclerViewAdapter.Holder> {
    private final String TAG = getClass().getSimpleName();

    List<String> data;

    public void setDataAndRefresh(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.each_write, parent, false);

        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        String item = data.get(position);

        holder.setItem(item);
        holder.setDataToScreen();
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        return data.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        private String item;
        private TextView textId;

        public Holder(View itemView) {
            super(itemView);
            textId = itemView.findViewById(R.id.each_write_id);
        }

        public void setItem(String item) {
            this.item = item;
        }

        public void setDataToScreen() {
            textId.setText(item);
        }
    }
}


