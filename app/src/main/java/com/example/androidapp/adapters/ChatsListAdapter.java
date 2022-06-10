package com.example.androidapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.classes.Chat;

import java.util.List;

public class ChatsListAdapter extends RecyclerView.Adapter<ChatsListAdapter.ViewHolder> {
    private List<Chat> chats;
    private LayoutInflater lif;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView userName;
        private final TextView lastMessage;
        private final TextView dateTime;
        private final ImageView profileImage;

        public ViewHolder(View view) {
            super(view);
            this.userName = view.findViewById(R.id.username);
            this.lastMessage = view.findViewById(R.id.lastmessage);
            this.dateTime = view.findViewById(R.id.datetime);
            this.profileImage = view.findViewById(R.id.profile_image);
        }

        public TextView getUserName() {
            return userName;
        }

        public TextView getLastMessage() {
            return lastMessage;
        }

        public TextView getDateTime() {
            return dateTime;
        }

        public ImageView getProfileImage() {
            return profileImage;
        }

    }


    public ChatsListAdapter(Context context) {
        this.lif = LayoutInflater.from(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = lif.inflate(R.layout.chatitem, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (chats != null) {
            final Chat current = chats.get(position);
            viewHolder.userName.setText(current.getUserName());
            viewHolder.profileImage.setImageResource(R.drawable.defaultp);
            viewHolder.dateTime.setText(current.getLastMessage().getText());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (chats != null) {
            return chats.size();
        }
        return 0;
    }
    public void setChats(List <Chat> s) {
        this.chats = s;
        notifyDataSetChanged();
    }
}

