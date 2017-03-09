package com.globe3.tno.g3_lite_mobile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.globe3.tno.g3_lite_mobile.R;
import com.globe3.tno.g3_lite_mobile.models.Project;

import java.util.ArrayList;

/**
 * Created by HuyLV on 10-Feb-17.
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    Context context;
    ArrayList<Project> dataList;

    ProjectItemListener projectItemListener;

    public ProjectAdapter(Context context, ArrayList<Project> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void setProjectItemListener(ProjectItemListener listener) {
        projectItemListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_project, parent, false);
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

    public void add(int position, Project project) {
        dataList.add(position, project);
        notifyItemInserted(position);
    }

    public void remove(Project project) {
        int position = dataList.indexOf(project);
        dataList.remove(position);
        notifyItemRemoved(position);
    }

    public void updateAllData(ArrayList<Project> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_content;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    projectItemListener.onProjectItemListener(getAdapterPosition(), dataList.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface ProjectItemListener {
        public void onProjectItemListener(final int position, final Project project);
    }
}
