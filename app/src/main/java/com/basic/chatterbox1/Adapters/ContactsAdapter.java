package com.basic.chatterbox1.Adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basic.chatterbox1.Activities.ChatsActivity;
import com.basic.chatterbox1.Activities.ContactsActivity;
import com.basic.chatterbox1.Activities.VerificationActivity;
import com.basic.chatterbox1.R;
import com.basic.chatterbox1.RecyclerViewOnClickInterface;
import com.basic.chatterbox1.UserModal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.contactsViewHolder> {

    ArrayList<UserModal> al_userList;
    RecyclerViewOnClickInterface recyclerViewOnClickInterface;

    public ContactsAdapter(ArrayList<UserModal> al_userList , RecyclerViewOnClickInterface recyclerViewOnClickInterface) {
        this.al_userList = al_userList;
        this.recyclerViewOnClickInterface=recyclerViewOnClickInterface;
    }


    public class contactsViewHolder extends RecyclerView.ViewHolder {

        TextView tv_contacts_name,tv_contacts_phno;
        LinearLayout ll_contacts_item;
        public contactsViewHolder(@NonNull View itemView) {

            super(itemView);
            tv_contacts_name=itemView.findViewById(R.id.tv_name);
            tv_contacts_phno=itemView.findViewById(R.id.tv_phno);
            ll_contacts_item=itemView.findViewById(R.id.ll_contacts_item);

            itemView.setOnClickListener(v -> recyclerViewOnClickInterface.Onclick(getAdapterPosition()));

        }
    }

    public ContactsAdapter.contactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(R.layout.contacts_item_layout,parent,false);
        return new ContactsAdapter.contactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.contactsViewHolder holder, int position) {
        holder.tv_contacts_name.setText(al_userList.get(position).getName());
        holder.tv_contacts_phno.setText(al_userList.get(position).getphno());
    }

    @Override
    public int getItemCount() {
        return al_userList.size();
    }
}
