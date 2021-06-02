package com.basic.chatterbox1.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basic.chatterbox1.MessageModal;
import com.basic.chatterbox1.R;

import java.util.ArrayList;

public class ChattingAdapter extends RecyclerView.Adapter<ChattingAdapter.chattingViewHolder> {

    ArrayList<MessageModal> al_messages;
    public ChattingAdapter(ArrayList<MessageModal> al_messages) {
        this.al_messages=al_messages;
    }

    @NonNull
    @Override
    public chattingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(R.layout.chatting_item_layout,parent,false);
        return new chattingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChattingAdapter.chattingViewHolder holder, int position) {

        holder.tv_chattingItem.setText(al_messages.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return al_messages.size();
    }

    public class chattingViewHolder extends RecyclerView.ViewHolder {

        TextView tv_chattingItem;
        public chattingViewHolder(@NonNull View itemView) {

            super(itemView);
            tv_chattingItem=itemView.findViewById(R.id.tv_chatting_item);

        }
    }
}
