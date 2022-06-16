package com.example.androidapp.adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidapp.ChatActivity;
import com.example.androidapp.R;
import com.example.androidapp.classes.Chat;
import com.example.androidapp.classes.Message;
import com.example.androidapp.classes.MessageDao;

public class ChatAdapter extends RecyclerView.Adapter {

    class ReceiverViewHolder extends RecyclerView.ViewHolder {
        private final TextView receiverText;
        private final TextView receiverTime;

        private ReceiverViewHolder(View view) {
            super(view);
            this.receiverText = view.findViewById(R.id.receiverText);
            this.receiverTime = view.findViewById(R.id.receiverTime);
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {
        private final TextView senderText;
        private final TextView senderTime;

        private SenderViewHolder(View view) {
            super(view);
            this.senderText = view.findViewById(R.id.senderText);
            this.senderTime = view.findViewById(R.id.senderTime);
        }
    }

    @NonNull
    @Override
    public ChatAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MessageViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
