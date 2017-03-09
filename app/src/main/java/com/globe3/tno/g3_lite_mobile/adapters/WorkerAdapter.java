package com.globe3.tno.g3_lite_mobile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.globe3.tno.g3_lite_mobile.R;
import com.globe3.tno.g3_lite_mobile.models.Worker;

import java.util.ArrayList;

/**
 * Created by HuyLV on 10-Feb-17.
 */

public class WorkerAdapter extends RecyclerView.Adapter<WorkerAdapter.ViewHolder> {

    Context context;
    ArrayList<Worker> dataList;
    WorkerItemListener workerItemListener;

    public void setDataList(ArrayList<Worker> workerList) {
        this.dataList = workerList;
        notifyDataSetChanged();
    }

    public void setWorkerItemListener(WorkerItemListener listener) {
        workerItemListener = listener;
    }

    public WorkerAdapter(Context context, ArrayList<Worker> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_worker, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_content.setText(dataList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_content;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    workerItemListener.onWorkerItemListener(getAdapterPosition(), dataList.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface WorkerItemListener {
        public void onWorkerItemListener(final int position, final Worker worker);
    }
}
