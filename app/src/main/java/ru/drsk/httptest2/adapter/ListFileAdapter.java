package ru.drsk.httptest2.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.drsk.httptest2.R;
import ru.drsk.httptest2.activity.ActivityStatusFiles;
import ru.drsk.httptest2.activity.FileChooseActivity;
import ru.drsk.httptest2.pojo.TextAddFile;

/**
 * Created by sergei on 11.02.2016.
 */
public class ListFileAdapter extends RecyclerView.Adapter<ListFileAdapter.ViewHolder> {

    List<TextAddFile> list = new ArrayList<>();
    Context context;
    public ListFileAdapter(List<TextAddFile> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_status_file_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(list.get(position).getLoadFile());
        holder.notifyFile.setText(list.get(position).getStatusLoadFile());
        holder.addBtnFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FileChooseActivity.class);
               // i.putExtra(ActivityStatusFiles.BUTTON_ADD_ID, v.getId());
                ((ActivityStatusFiles) context).startActivityForResult(i, ActivityStatusFiles.REQUEST_FILE_CHOOSE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private TextView notifyFile;
        private TextView fileName;
        private Button addBtnFile;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.load_text);
            addBtnFile = (Button) itemView.findViewById(R.id.btn_choose_file);
            notifyFile = (TextView) itemView.findViewById(R.id.notify_files);
            fileName = (TextView) itemView.findViewById(R.id.file_name);
        }
    }
}
