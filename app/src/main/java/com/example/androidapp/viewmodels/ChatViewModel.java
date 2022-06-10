//package com.example.androidapp.viewmodels;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.ViewModel;
//
//import com.example.androidapp.classes.Chat;
//
//import java.util.List;
//
//public class ChatViewModel extends ViewModel {
//    private ChatsRepository mRepository;
//
//    private LiveData<List<Chat>> chats;
//    public ChatViewModel() {
//        mRepository = new ChatsRepository();
//        chats = mRepository.getAll();
//    }
//    public LiveData<List<Chat>> get() {
//        return chats;
//    }
//    public void add(Chat chat) {
//        mRepository.add(chat);
//    }
//    public void delete(Chat chat) {
//        mRepository.delete(chat);
//    }
//    public void reload () {
//        mRepository.reload();
//    }
//}
