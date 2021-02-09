package com.example.myapplication.Chat_module;

import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.users.model.QBUser;

public class ChatHelper  {

    private static ChatHelper instance;
    private QBChatService qbChatService = QBChatService.getInstance();

    public static synchronized ChatHelper getInstance(){
        if(instance == null){
            instance = new ChatHelper();
        }
        return instance;
    }
    public void destroy() {
        qbChatService.destroy();
    }

    public void loginToChat(final QBUser user, final QBEntityCallback<Void> callback) {
        if (!qbChatService.isLoggedIn()) {
            qbChatService.login(user, callback);
        } else {
            callback.onSuccess(null, null);
        }
    }

}
