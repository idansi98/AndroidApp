package com.example.androidapp.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.ChatActivity;
import com.example.androidapp.R;
import com.example.androidapp.classes.Chat;
import com.example.androidapp.classes.Message;
import com.example.androidapp.classes.MessageDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatsListAdapter extends RecyclerView.Adapter<ChatsListAdapter.ChatViewHolder> {

    class ChatViewHolder extends RecyclerView.ViewHolder {
        private final TextView userName;
        private final TextView lastMessage;
        private final TextView dateTime;
        private final LinearLayout container;
        private final ImageView profilePic;

        private ChatViewHolder(View view) {
            super(view);
            this.userName = view.findViewById(R.id.username);
            this.lastMessage = view.findViewById(R.id.lastmessage);
            this.dateTime = view.findViewById(R.id.datetime);
            this.container = view.findViewById(R.id.linearLayout2);
            this.profilePic = view.findViewById(R.id.profile_image);
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
        View viewItem = mInflater.inflate(R.layout.chatitem, parent, false);
        return new ChatViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        if (chats != null) {
            final Chat current = chats.get(position);
            String userName = current.getUserName();
            holder.userName.setText(current.getDisplayName());

            List<Message> messages = messageDao.get(userName);
            if (messages.size() > 0) {
                Message lastMessage = messages.get(messages.size()-1);
                holder.lastMessage.setText(lastMessage.getText());
                Date date = new Date(lastMessage.getTimeInMS());


                String pattern = "dd-MM hh:mm";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                String dateStr = simpleDateFormat.format(date);
                holder.dateTime.setText(dateStr);
            } else {
                holder.lastMessage.setText("");
                holder.dateTime.setText("");
            }
            // set random pic
            char firstLetter = userName.toLowerCase(Locale.ROOT).charAt(0);
            if (firstLetter < 'c') {
                holder.profilePic.setImageResource(R.drawable.pfp1);
            } else            if (firstLetter < 'f') {
                holder.profilePic.setImageResource(R.drawable.pfp2);
            } else            if (firstLetter < 'i') {
                holder.profilePic.setImageResource(R.drawable.pfp3);
            } else            if (firstLetter < 'l') {
                holder.profilePic.setImageResource(R.drawable.pfp4);
            } else            if (firstLetter < 'o') {
                holder.profilePic.setImageResource(R.drawable.pfp5);
            } else            if (firstLetter < 'r') {
                holder.profilePic.setImageResource(R.drawable.pfp6);
            } else            if (firstLetter < 't') {
                holder.profilePic.setImageResource(R.drawable.pfp7);
            } else            if (firstLetter < 'w') {
                holder.profilePic.setImageResource(R.drawable.pfp8);
            } else            if (firstLetter < 'y') {
                holder.profilePic.setImageResource(R.drawable.pfp9);
            } else {
                holder.profilePic.setImageResource(R.drawable.pfp10);
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







