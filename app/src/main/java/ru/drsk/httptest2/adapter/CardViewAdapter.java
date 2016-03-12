package ru.drsk.httptest2.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ru.drsk.httptest2.R;
import ru.drsk.httptest2.activity.ActivityStatusFiles;
import ru.drsk.httptest2.pojo.TableStatusCell;

/**
 * Created by sergei on 11.03.2016.
 */
public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {

    private List<TableStatusCell> list;
    private Context context;
    public CardViewAdapter(List<TableStatusCell> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.nameObj.setText(list.get(position).getNameObj());
        holder.address.setText(Html.fromHtml(list.get(position).getAddress()));
        holder.date.setText(list.get(position).getDate());
        holder.status.setText(Html.fromHtml(list.get(position).getStatus()));
        holder.comment.setText(list.get(position).getComment());
        holder.power.setText(list.get(position).getPower());
        holder.voltage.setText(list.get(position).getVoltage());
        holder.type.setText(list.get(position).getType());
        if(list.get(position).isHasButton()){
            holder.addBtnFile.setVisibility(View.VISIBLE);
            holder.addBtnFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), ActivityStatusFiles.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(ActivityStatusFiles.FLAG_ID_ZAYAV, list.get(position).getIdZaiv());
                    intent.putExtra(ActivityStatusFiles.FLAG_USER_FILE, list.get(position).getFlagUserFile());
                    context.startActivity(intent);
                }
            });
        }else{
            holder.addBtnFile.setVisibility(View.GONE);
        }
        if(list.get(position).isHideText()){
            holder.hideLayout.setVisibility(View.GONE);
            holder.expandButton.setImageResource(R.mipmap.ic_expand_more_white_24dp);
        }else{
            holder.expandButton.setImageResource(R.mipmap.ic_expand_less_white_24dp);
            holder.hideLayout.setVisibility(View.VISIBLE);

        }
        holder.expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isHide(holder.hideLayout)){
                    holder.expandButton.setImageResource(R.mipmap.ic_expand_less_white_24dp);
                    list.get(position).setHideText(false);
                    holder.hideLayout.setVisibility(View.VISIBLE);
                }else{
                    holder.expandButton.setImageResource(R.mipmap.ic_expand_more_white_24dp);
                    list.get(position).setHideText(true);
                    holder.hideLayout.setVisibility(View.GONE);
                }
            }
        });
    }
    private boolean isHide(LinearLayout layout){
        return layout.getVisibility() != View.VISIBLE;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout hideLayout;
        private TextView nameObj;
        private TextView date;
        private TextView address;
        private TextView status;
        private TextView power;
        private TextView voltage;
        private TextView comment;
        private TextView type;
        private ImageButton expandButton;
        private Button addBtnFile;
        public ViewHolder(View itemView) {
            super(itemView);
            hideLayout = (LinearLayout) itemView.findViewById(R.id.toggle_linear);
            nameObj = (TextView) itemView.findViewById(R.id.name_obj);
            date = (TextView) itemView.findViewById(R.id.date);
            address = (TextView) itemView.findViewById(R.id.address);
            status = (TextView) itemView.findViewById(R.id.status);
            status.setMovementMethod(LinkMovementMethod.getInstance());
            power = (TextView) itemView.findViewById(R.id.power);
            voltage = (TextView) itemView.findViewById(R.id.voltage);
            comment = (TextView) itemView.findViewById(R.id.comment);
            type = (TextView) itemView.findViewById(R.id.type);
            expandButton = (ImageButton) itemView.findViewById(R.id.expand_btn);
            addBtnFile = (Button) itemView.findViewById(R.id.add_files_btn);
        }
    }
}
