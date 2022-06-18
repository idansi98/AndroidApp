package com.example.androidapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.classes.Message;
import com.example.androidapp.classes.MessageDao;

import java.util.List;

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

    private final LayoutInflater mInflater;
    private List<Message> messages;
    private final Context context;
    private MessageDao messageDao;
    private int senderViewType = 1;
    private int receiverViewType = 2;
    private String username;

    public ChatAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == senderViewType) {
            View viewItem =  mInflater.inflate(R.layout.samplesender, parent, false);
            return new SenderViewHolder(viewItem);
        }
        else {
            View viewItem =  mInflater.inflate(R.layout.samplereceiver, parent, false);
            return new ReceiverViewHolder(viewItem);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (messages != null) {
            Message message = messages.get(position);
            if (holder.getClass() == SenderViewHolder.class) {
                ((SenderViewHolder)holder).senderText.setText(message.getText());
                ((SenderViewHolder)holder).senderTime.setText("" +  message.getTimeInMS());
            }
            else {
                ((ReceiverViewHolder)holder).receiverText.setText(message.getText());
                ((ReceiverViewHolder)holder).receiverTime.setText("" + message.getTimeInMS());
            }
        }
    }

    public void setMessages(List<Message> m) {
        this.messages = m;
        notifyDataSetChanged();
    }

    public void setMessageDao(MessageDao messageDao) {
        this.messageDao = messageDao;
        notifyDataSetChanged();
    }

    public void setUsername(String username) {
        this.username = username;
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        if (messages != null) {
            return messages.size();
        }
        else return 1;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
