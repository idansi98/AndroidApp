package com.example.androidapp.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.ChatActivity;
import com.example.androidapp.R;
import com.example.androidapp.classes.Chat;

import java.util.List;

public class ChatsListAdapter extends RecyclerView.Adapter<ChatsListAdapter.ChatViewHolder> {

    class ChatViewHolder extends RecyclerView.ViewHolder {
        private final TextView userName;
        private final TextView lastMessage;
        private final TextView dateTime;
        private final LinearLayout container;

        private ChatViewHolder(View view) {
            super(view);
            this.userName = view.findViewById(R.id.username);
            this.lastMessage = view.findViewById(R.id.lastmessage);
            this.dateTime = view.findViewById(R.id.datetime);
            this.container = view.findViewById(R.id.linearLayout2);
        }
    }
    private final LayoutInflater mInflater;
    private List<Chat> chats;
    private final Context context;

    public ChatsListAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.chatitem, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        if (chats != null) {
            final Chat current = chats.get(position);
            String userName = current.getUserName();
            holder.userName.setText(userName);
            holder.lastMessage.setText("TEMPORARY");
            holder.dateTime.setText("TEMPORARY");
            holder.container.setOnClickListener(v ->  {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("username",userName);
                context.startActivity(intent);
            });
        }
    }

    public void setChats(List<Chat> s) {
        chats = s;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(chats != null) {
            return chats.size();
        }
        else return 1;
    }

    public List<Chat> getChats() {
        return chats;
    }
}







