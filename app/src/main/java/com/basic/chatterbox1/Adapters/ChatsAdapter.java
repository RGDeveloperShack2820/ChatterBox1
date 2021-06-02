package com.basic.chatterbox1.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basic.chatterbox1.ChatModal;
import com.basic.chatterbox1.R;
import com.basic.chatterbox1.RecyclerViewOnClickInterface;
import com.basic.chatterbox1.UserModal;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.myViewHolder> {

    RecyclerViewOnClickInterface recyclerViewOnClickInterface;
    ArrayList<ChatModal> al_chats;
    String str_message[];
    int[] int_userIds;

    public ChatsAdapter(RecyclerViewOnClickInterface recyclerViewOnClickInterface, ArrayList al_chats, String[] str_message, int[] int_userIds){

      this.recyclerViewOnClickInterface=recyclerViewOnClickInterface;
     this.al_chats=al_chats;
      this.str_message=str_message;
      this.int_userIds=int_userIds;
      
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
       View view= inflater.inflate(R.layout.chats_item_layout,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsAdapter.myViewHolder holder, int position) {

        holder.tv_name.setText(al_chats.get(position).getChatName());
        holder.tv_message.setText(str_message[position]);
        holder.civ_dps.setImageResource(int_userIds[position]);

    }

    @Override
    public int getItemCount() {
        return al_chats.size();
    }


    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name,tv_message,tv_timestamp;
        CircleImageView civ_dps;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name=itemView.findViewById(R.id.tv_name);
            tv_message=itemView.findViewById(R.id.tv_lst_message);
            tv_timestamp=itemView.findViewById(R.id.tv_timestamp);
            civ_dps=itemView.findViewById(R.id.civ_dp);

            itemView.setOnClickListener(v -> recyclerViewOnClickInterface.Onclick(getAdapterPosition()));
        }
    }
}
