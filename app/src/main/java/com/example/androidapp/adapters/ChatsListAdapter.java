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
import com.example.androidapp.classes.Message;
import com.example.androidapp.classes.MessageDao;

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
    private MessageDao messageDao;

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

            List<Message> messages = messageDao.get(userName);
            if (messages.size() > 0) {
                Message lastMessage = messages.get(messages.size()-1);
                holder.lastMessage.setText(lastMessage.getText());
                holder.dateTime.setText("Time in ms: " +lastMessage.getTimeInMS());
            } else {
                holder.lastMessage.setText("");
                holder.dateTime.setText("");
            }
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

    public void setMessageDao(MessageDao messageDao) {
        this.messageDao = messageDao;
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







