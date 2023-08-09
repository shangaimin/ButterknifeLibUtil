package com.sgx.kotlin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<JPushMessages> jPushMessages;
    private OnLickListener onLickListener;

    public void setOnLickListener(OnLickListener onLickListener) {
        this.onLickListener = onLickListener;
    }

    public MessageAdapter(List<JPushMessages> jPushMessages) {
        this.jPushMessages = jPushMessages;
    }

    public void setjPushMessages(List<JPushMessages> jPushMessages) {
        this.jPushMessages = jPushMessages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTv.setText(jPushMessages.get(position).getTitle());
        holder.contentTv.setText(jPushMessages.get(position).getContent());
        holder.timeTv.setText(jPushMessages.get(position).getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=onLickListener){
                    onLickListener.onCLick(jPushMessages.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return jPushMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTv;
        private TextView contentTv;
        private TextView timeTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.title);
            contentTv = itemView.findViewById(R.id.content);
            timeTv = itemView.findViewById(R.id.time_tv);

        }
    }
    public interface OnLickListener{
        void onCLick(JPushMessages jPushMessages);
    }
}
