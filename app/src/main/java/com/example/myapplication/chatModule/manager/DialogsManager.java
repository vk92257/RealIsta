package com.example.myapplication.chatModule.manager;

import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.chatModule.ChatHelper;
import com.example.myapplication.chatModule.callback.QbEntityCallbackImpl;
import com.example.myapplication.chatModule.holder.QbDialogHolder;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smackx.muc.DiscussionHistory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class DialogsManager {
    public static final String TAG = "DiialogsManager";
    private static final String PROPERTY_OCCUPANTS_IDS = "current_occupant_ids";
    private static final String PROPERTY_DIALOG_TYPE = "type";
    private static final String PROPERTY_DIALOG_NAME = "room_name";
    private static final String PROPERTY_NEW_OCCUPANTS_IDS = "new_occupants_ids";
    public static final String PROPERTY_NOTIFICATION_TYPE = "notification_type";
    private static final String CREATING_DIALOG = "1";
    private static final String OCCUPANTS_ADDED = "2";
    private static final String OCCUPANT_LEFT = "3";

    private Set<ManagingDialogsCallbacks> managingDialogsCallbackListener = new CopyOnWriteArraySet<>();

    private boolean isMessageCreatedDialog(QBChatMessage message) {
        return CREATING_DIALOG.equals(message.getProperty(PROPERTY_NOTIFICATION_TYPE));
    }

    private boolean isMessageAddedUser(QBChatMessage message) {
        return OCCUPANTS_ADDED.equals(message.getProperty(PROPERTY_NOTIFICATION_TYPE));
    }

    private boolean isMessageLeftUser(QBChatMessage message) {
        return OCCUPANT_LEFT.equals(message.getProperty(PROPERTY_NOTIFICATION_TYPE));
    }

    /**
     * message Receivers
     */
    public void onGlobalMessageReceived(String dialogId, final QBChatMessage chatMessage) {
        Log.e(TAG, "onGlobalMessageReceived: " + chatMessage.getId());
        if (isMessageCreatedDialog(chatMessage) && !QbDialogHolder.getInstance().hasDialogWithId(dialogId)) {
            Log.e(TAG, "onGlobalMessageReceived: 1" );
            loadNewDialogByNotificationMessage(chatMessage);
        }

        if (isMessageAddedUser(chatMessage) || isMessageLeftUser(chatMessage)) {

            Log.e(TAG, "onGlobalMessageReceived: 2" );
            if (QbDialogHolder.getInstance().hasDialogWithId(dialogId)) {
                Log.e(TAG, "onGlobalMessageReceived: 3" );
                notifyListenersDialogUpdated(dialogId);
            } else {
                Log.e(TAG, "onGlobalMessageReceived: else 3" );
                loadNewDialogByNotificationMessage(chatMessage);
            }
        }


        if (chatMessage.isMarkable()) {
            Log.e(TAG, "onGlobalMessageReceived: ismarkable" );
            if (QbDialogHolder.getInstance().hasDialogWithId(dialogId)) {
                QbDialogHolder.getInstance().updateDialog(dialogId, chatMessage);
                notifyListenersDialogUpdated(dialogId);
            }
        } else {

            Log.e(TAG, "onGlobalMessageReceived: is not markable" );
            ChatHelper.getInstance().getDialogById(dialogId, new QBEntityCallback<QBChatDialog>() {
                @Override
                public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                    Log.e(TAG, "onSuccess: Loading Dialog Successful" );
                    loadUsersFromDialog(qbChatDialog);
                    QbDialogHolder.getInstance().addDialog(qbChatDialog);
                    notifyListenersNewDialogLoaded(qbChatDialog);
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.e(TAG, "onError:error->  "+e );
                }
            });
        }
    }


    public void onSystemMessageReceived(final QBChatMessage systemMessage) {
        Log.e(TAG, "onSystemMessageReceived: system message -> " + systemMessage.getBody());

        onGlobalMessageReceived(systemMessage.getDialogId(), systemMessage);
    }

    private void loadNewDialogByNotificationMessage(QBChatMessage chatMessage) {

        QBChatDialog chatDialog = buildChatDialogFromNotificationMessage(chatMessage);
        ChatHelper.getInstance().getDialogById(chatDialog.getDialogId(), new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                Log.e(TAG, "onSuccess: loadiung dialog successful");

                qbChatDialog.initForChat(QBChatService.getInstance());
                DiscussionHistory history = new DiscussionHistory();
                history.setMaxStanzas(0);
                qbChatDialog.join(history, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                        QbDialogHolder.getInstance().addDialog(qbChatDialog);
                        notifyListenersDialogCreated(qbChatDialog);
                    }

                    @Override
                    public void onError(QBResponseException e) {

                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG, "onError: error-> " + e.toString());
            }
        });
    }


    /**
     * only for notification
     */
    private QBChatDialog buildChatDialogFromNotificationMessage(QBChatMessage qbChatMessage) {
        QBChatDialog chatDialog = new QBChatDialog();
        chatDialog.setDialogId(qbChatMessage.getDialogId());
        chatDialog.setUnreadMessageCount(0);
        return chatDialog;
    }


    private void notifyListenersDialogCreated(final QBChatDialog chatDialog) {
        for (ManagingDialogsCallbacks listener : getManagingDialogsCallbackListeners()) {
            listener.onDialogCreated(chatDialog);
        }
    }

    private void notifyListenersDialogUpdated(final String dialogId) {
        Log.e(TAG, "notifyListenersDialogUpdated: " );
        for (ManagingDialogsCallbacks listener : getManagingDialogsCallbackListeners()) {
            listener.onDialogUpdated(dialogId);
        }
    }

    private Collection<ManagingDialogsCallbacks> getManagingDialogsCallbackListeners() {
        return Collections.unmodifiableCollection(managingDialogsCallbackListener);
    }


    public interface ManagingDialogsCallbacks {

        void onDialogCreated(QBChatDialog chatDialog);

        void onDialogUpdated(String chatDialog);

        void onNewDialogLoaded(QBChatDialog chatDialog);
    }

    private void loadUsersFromDialog(QBChatDialog chatDialog) {
        ChatHelper.getInstance().getUsersFromDialog(chatDialog, new QbEntityCallbackImpl<ArrayList<QBUser>>());
    }

    private void notifyListenersNewDialogLoaded(final QBChatDialog chatDialog) {
        for (ManagingDialogsCallbacks listener : getManagingDialogsCallbackListeners()) {
            listener.onNewDialogLoaded(chatDialog);
        }
    }

    public void addManagingDialogsCallbackListener(ManagingDialogsCallbacks listener) {
        if (listener != null) {
            managingDialogsCallbackListener.add(listener);
        }
    }






}
