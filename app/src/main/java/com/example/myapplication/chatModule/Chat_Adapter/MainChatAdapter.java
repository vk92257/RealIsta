package com.example.myapplication.chatModule.Chat_Adapter;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myapplication.R;
import com.example.myapplication.WebView_activity;
import com.example.myapplication.chatModule.AttachmentVideoActivity;
import com.example.myapplication.chatModule.ChatHelper;
import com.example.myapplication.chatModule.View_ChatImage_fullScreen;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.TimeUtils;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBProgressCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.CollectionsUtil;
import com.quickblox.core.io.IOUtils;
import com.quickblox.users.model.QBUser;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainChatAdapter extends RecyclerView.Adapter<MainChatAdapter.MessgeHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<QBChatMessage> qbChatMessages;
    public static final int CUSTOM_VIEW_TYPE = -1;
    public static final int TYPE_TEXT_RIGHT = 1;
    public static final int TYPE_TEXT_LEFT = 2;
    public static final int TYPE_ATTACH_RIGHT = 3;
    public static final int TYPE_ATTACH_LEFT = 4;
    public static final int TYPE_NOTIFICATION_CENTER = 5;
    private LayoutInflater inflater;


    public static final String PREFIX = "stream2file";
    public static final String SUFFIX = ".tmp";
    private String name;

    public static int positiondata = 0;

    private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private static String TAG = "ChatMessageAdapter";
    //    private QBChatService  qbChatService = QBChatService.getInstance();
    protected QBUser currentUser;

    private final QBChatDialog chatDialog;
    public MainChatAdapter(Context context, ArrayList<QBChatMessage> qbChatMessages, QBChatDialog chatDialog) {
        this.context = context;
        this.qbChatMessages = qbChatMessages;
        this.chatDialog = chatDialog;
        currentUser = ChatHelper.getInstance().getclient().getUser();
    }

    public void getrecivername(String name) {
        this.name = name;
    }

    private SparseIntArray containerLayoutRes = new SparseIntArray() {
        {
            put(TYPE_TEXT_RIGHT, R.layout.list_send_message);
            put(TYPE_TEXT_LEFT, R.layout.list_recv_message);
            put(TYPE_ATTACH_RIGHT, R.layout.list_send_message);
            put(TYPE_ATTACH_LEFT, R.layout.list_recv_message);
        }
    };


    @NonNull
    @Override
    public MessgeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        setHasStableIds(true);

        Log.e(TAG, "onCreateViewHolder: viewtype-> " + viewType + "  " + containerLayoutRes.get(viewType));
//        holder.setIsRecyclable(true);
        View view = inflater.from(context).inflate(containerLayoutRes.get(viewType), parent, false);

        return new MessgeHolder(view);

    }
    /**messageing array populate function*/
    public void addMessages(List<QBChatMessage> items){
        qbChatMessages.addAll(0,items);
        notifyDataSetChanged();


    }

    public void setMessages(List<QBChatMessage> items){
        qbChatMessages.clear();
        qbChatMessages.addAll(items);
        notifyDataSetChanged();
    }

    public void addMessage(QBChatMessage item){
        qbChatMessages.add(item);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyItemInserted(qbChatMessages.size()-1);

            }
        }, 1);


//       new  runOnUiThread(new Runnable() {
//            public void run() {
//                innerAdapter.notifyItemInserted(position /* position of newly added item */);
//            }
//        });

    }


    public int getsizemessage(){
        return qbChatMessages.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MessgeHolder holder, int position) {
//        holder.message_content.setText(qbChatMessages.get(position).getBody());
//        QBChatMessage chatMessage = getItem(position);
//        holder.setIsRecyclable(false);
        QBChatMessage chatMessage = getItem(position);
        chatMessage.setMarkable(true);
        File videoFile = null;


        if (chatMessage != null) {
            if (isIncoming(chatMessage) && !isReadByCurrentUser(chatMessage)) {
                readMessage(chatMessage);
            }

            QBAttachment attachment = getAttachment(position);

//          String imageUrl = QBFile.getPrivateUrlForUID(attachment.getId());

            if(attachment != null){
                String filename =attachment.getName();
                 videoFile = new File(context.getFilesDir(), filename);
            }



            switch (getItemViewType(position)) {


                case TYPE_TEXT_RIGHT:
                    Log.e("listdata", "onBindViewHolder: prinltn-> rightmessage");
                    holder.Attachment_layout.setVisibility(View.GONE);
//                    holder.message_content.setVisibility(View.VISIBLE);
                    holder.ll_message_body_container.setVisibility(View.VISIBLE);
                    holder.message_content.setText(chatMessage.getBody());
                    holder.txt_time.setText(TimeUtils.getTimewitham_pm(chatMessage.getDateSent()));
                    holder.doc_layout.setVisibility(View.GONE);
                    textmessateStatus(holder,chatMessage);
                    break;
                case TYPE_TEXT_LEFT:
                    Log.e("listdata", "onBindViewHolder: prinltn-> leftmessage");
                    holder.Attachment_layout.setVisibility(View.GONE);
                    holder.ll_message_body_container.setVisibility(View.VISIBLE);
                    holder.nametxt.setText(name);
//                    holder.message_content.setVisibility(View.VISIBLE);
                    holder.bubbleTextView.setText(chatMessage.getBody());
                    holder.txt_time.setText(TimeUtils.getTimewitham_pm(chatMessage.getDateSent()));
                    holder.doc_layout.setVisibility(View.GONE);
                    textmessateStatus(holder,chatMessage);
                    break;
                case TYPE_ATTACH_RIGHT:

                    Log.e("filefetch", "onBindViewHolder: right side" );
                    if (videoFile.exists()) {

                        setattachement(attachment, videoFile, holder,true,chatMessage);

                    } else {

                        loadFileFromQB(attachment, videoFile, holder,chatMessage);
                    }

                    break;
                case TYPE_ATTACH_LEFT:

                    if (videoFile.exists()) {
                        setattachement(attachment, videoFile, holder,false,chatMessage);
                    } else {
                        loadFileFromQB(attachment, videoFile, holder, chatMessage);
                    }

                    break;
                default:
                    Log.e("listdata", "onBindViewHolder: prinltn-> default value");
                    Log.d(TAG, "onBindViewHolder TYPE_ATTACHMENT_CUSTOM");
                    break;
            }
        }


        Log.e("iop ", "onBindViewHolder: " + positiondata);

    }


    @Override
    public long getItemId(int position) {
        Object listItem = qbChatMessages.get(position);
        return listItem.hashCode();
    }




    /**message status
 * @param holder
 * @param chatMessage*/
    private void textmessateStatus(MessgeHolder holder, QBChatMessage chatMessage) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            boolean read = isRead(chatMessage);
          boolean delivered = isDelivered(chatMessage);
          if(read){
              holder.iv_message_status.setImageDrawable(context.getDrawable(R.drawable.ic_status_read));
          }else if(delivered){
              holder.iv_message_status.setImageDrawable(context.getDrawable(R.drawable.ic_status_delivered));
          }
          else{
              holder.iv_message_status.setImageDrawable(context.getDrawable(R.drawable.ic_status_sent));
          }
        }

    }

    private void attachmentmessageStatus(MessgeHolder holder, QBChatMessage chatMessage) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            boolean read = isRead(chatMessage);
            boolean delivered = isDelivered(chatMessage);
            if(read){
                holder.iv_messageattch_status.setImageDrawable(context.getDrawable(R.drawable.ic_status_read));
            }else if(delivered){
                holder.iv_messageattch_status.setImageDrawable(context.getDrawable(R.drawable.ic_status_delivered));
            }
            else{
                holder.iv_messageattch_status.setImageDrawable(context.getDrawable(R.drawable.ic_status_sent));
            }
        }

    }



    private void attachmentFileStatus(MessgeHolder holder, QBChatMessage chatMessage){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            boolean read = isRead(chatMessage);
            boolean delivered = isDelivered(chatMessage);
            if(read){
                holder.iv_messagedoc_status.setImageDrawable(context.getDrawable(R.drawable.ic_status_read));
            }else if(delivered){
                holder.iv_messagedoc_status.setImageDrawable(context.getDrawable(R.drawable.ic_status_delivered));
            }
            else{
                holder.iv_messagedoc_status.setImageDrawable(context.getDrawable(R.drawable.ic_status_sent));
            }
        }
    }



    /**read and delivere check*/
    private boolean isRead(QBChatMessage chatMessage){
        boolean read = false;
        Integer recipientId = chatMessage.getRecipientId();

        Integer currentUserId = ChatHelper.getCurrentUser(context).getId();

        Collection<Integer> readIds= chatMessage.getReadIds();
        if(readIds == null){
            return false;
        }
        if (recipientId != null && !recipientId.equals(currentUserId) && readIds.contains(recipientId)) {
            read = true;
        } else if (readIds.size() == 1 && readIds.contains(currentUserId)) {
            read = false;
        } else if (readIds.size() > 0) {
            read = true;
        }
        return read;
    }


    private boolean isDelivered(QBChatMessage chatMessage){
        boolean delivered = false;
        Integer recipientId = chatMessage.getRecipientId();
        Integer currentUserId = ChatHelper.getCurrentUser(context).getId();

        Collection<Integer> deliveredIds = chatMessage.getDeliveredIds();
        if (deliveredIds == null) {
            return false;
        }
        if (recipientId != null && !recipientId.equals(currentUserId) && deliveredIds.contains(recipientId)) {
            delivered = true;
        } else if (deliveredIds.size() == 1 && deliveredIds.contains(currentUserId)) {
            delivered = false;
        } else if (deliveredIds.size() > 0) {
            delivered = true;
        }
        return delivered;
    }


    private void setattachement(QBAttachment attachment, File imageUrl, MessgeHolder holder,boolean sender,QBChatMessage chatMessage) {

        Log.e("filefetch", "setattachement: start" );
        boolean image = QBAttachment.IMAGE_TYPE.equalsIgnoreCase(attachment.getType());
        boolean video = QBAttachment.VIDEO_TYPE.equalsIgnoreCase(attachment.getType());
        boolean audio = QBAttachment.AUDIO_TYPE.equalsIgnoreCase(attachment.getType());
        boolean file = attachment.getType().equals("file") || attachment.getType().contains("file") || attachment.getType().equals("");

//        String filename = imageUrl.getAbsolutePath();
//        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(imageUrl.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
//        Log.e("bitmap", "setattachement:bitmap->  "+bitmap );
//
//        Log.e("filefetch", "setattachement: filepath -> " + filename);
//
//        Log.e("filefetch", "setattachement: isimage-> "+image+" isvideo-> "+video+" isfile-> "+file );


//        holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_placeholder));



        if(image){
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_file_placeholder));
            setdataforimage(attachment,imageUrl,holder,sender,chatMessage);
        }
        else if(video){
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.attachment));
            setdatforvideo(attachment,imageUrl,holder,sender,chatMessage);
        }
        else if(file){
            setdataforfile(attachment,imageUrl,holder,sender,chatMessage);
        }


        Log.e(TAG, "*******************onResourceReady:\n\n\n\n\n\n\n\n " + attachment.getUrl());

    }


    private void setdataforimage(QBAttachment attachment, File imageUrl, MessgeHolder holder,boolean sender,QBChatMessage chatMessage) {
        holder.progress_image.setVisibility(View.VISIBLE);
        holder.Attachment_layout.setVisibility(View.VISIBLE);
            holder.ll_message_body_container.setVisibility(View.GONE);
        holder.att_time.setText(TimeUtils.getTimewitham_pm(chatMessage.getDateSent()));
        holder.progress_image.setVisibility(View.VISIBLE);
        Log.e("view2", "setdataforimage: holder-> "+holder.doc_layout );

        holder.doc_layout.setVisibility(View.GONE);
        Bitmap myBitmap = BitmapFactory.decodeFile(imageUrl.getAbsolutePath());
        attachmentmessageStatus(holder,chatMessage);

        holder.imageView.setImageBitmap(myBitmap);

//        Glide.with(context)
//                .load(imageUrl)
//                .placeholder(R.drawable.ic_image_placeholder)
//                .addListener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        Log.e("loading", "onLoadFailed:loading--> " );
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//
//                        Log.e("loading", "onResourceReady: ---> "+imageUrl.getAbsolutePath() +" attachment-> "+attachment.getUrl());
//                        return true;
//                    }
//                }).into(holder.imageView);

//        holder.imageView.setImageURI(Uri.parse(imageUrl.toString()));

////
//        Glide.with(context)
//                .load(myBitmap)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .centerCrop()
//                .placeholder(R.drawable.attachment_image_placeholder_right)
//                .addListener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        Log.e("setAttchment", "onLoadFailed: failed to load data");
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
////
////                        notifyDataSetChanged();
//                        Log.e("setAttchment", "onResourceReady: ");
////                            holder.progress_image.setVisibility(View.GONE);
//                        holder.progress_image.setVisibility(View.GONE);
//                        Log.e("setAttchment", "onResourceReady: image loading");
//                        holder.play_button.setVisibility(View.GONE);
//                        holder.Attachment_layout.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                holder.progress_image.setVisibility(View.GONE);
//                                Intent intent = new Intent(context, View_ChatImage_fullScreen.class);
//                                intent.putExtra(ConstantString.CHAT_IMAGE_VIEW, imageUrl.getAbsolutePath());
//                                intent.putExtra(ConstantString.ATTACHMENT_TYPE, attachment.getType());
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                context.startActivity(intent);
//                            }
//                        });
//
//
//                        Log.e("setAttchment", "onResourceReady: complete data ");
//
//                        return false;
//                    }
//                }).into(holder.imageView);


        holder.progress_image.setVisibility(View.GONE);
        Log.e("setAttchment", "onResourceReady: image loading");
        holder.play_button.setVisibility(View.GONE);
        holder.Attachment_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.progress_image.setVisibility(View.GONE);
                Intent intent = new Intent(context, View_ChatImage_fullScreen.class);
                intent.putExtra(ConstantString.CHAT_IMAGE_VIEW, imageUrl.getAbsolutePath());
                intent.putExtra(ConstantString.ATTACHMENT_TYPE, attachment.getType());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

//        notifyDataSetChanged();
    }



    @SuppressLint("CheckResult")
    private void setdatforvideo(QBAttachment attachment, File imageUrl, MessgeHolder holder,boolean sender,QBChatMessage chatMessage) {

        holder.ll_message_body_container.setVisibility(View.GONE);
        holder.att_time.setText(TimeUtils.getTimewitham_pm(chatMessage.getDateSent()));
        holder.progress_image.setVisibility(View.VISIBLE);
        holder.doc_layout.setVisibility(View.GONE);
        holder.Attachment_layout.setVisibility(View.VISIBLE);
        attachmentmessageStatus(holder,chatMessage);

        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(imageUrl.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);

        holder.progress_image.setVisibility(View.GONE);
        holder.play_button.setVisibility(View.VISIBLE);
        Log.e("bitmap", "setdatforvideo:bitmap--> "+bitmap );


        holder.imageView.setImageBitmap(bitmap);
//        holder.imageView.setImageBitmap(bitmap);
//        Glide.with(context)
//                .load(bitmap)
//                .thumbnail(0.1f)
//                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .addListener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        holder.progress_image.setVisibility(View.GONE);
//                        holder.play_button.setVisibility(View.VISIBLE);
//                        return false;
//                    }
//                })
//                .into(holder.imageView);





        holder.Attachment_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AttachmentVideoActivity.class);
                String url = QBFile.getPrivateUrlForUID(attachment.getId());
                intent.putExtra(ConstantString.CHAT_IMAGE_VIEW, url);
                intent.putExtra(ConstantString.ATTACHMENT_TYPE, attachment.getName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });



    }

    @SuppressLint("SetTextI18n")
    private void setdataforfile(QBAttachment attachment, File imageUrl, MessgeHolder holder, boolean sender, QBChatMessage chatMessage) {
        attachmentFileStatus(holder,chatMessage);
        Log.e("filefetch", "setdataforfile: file attachment send" );
        holder.ll_message_body_container.setVisibility(View.GONE);
        holder.Attachment_layout.setVisibility(View.GONE);
        holder.doc_att_time.setText(TimeUtils.getTimewitham_pm(chatMessage.getDateSent()));
        holder.progress_doc.setVisibility(View.VISIBLE);
        holder.docsize.setText(android.text.format.Formatter.formatShortFileSize(context, (long) attachment.getSize()));
        holder.doc_layout.setVisibility(View.VISIBLE);
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(imageUrl.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
        Log.e("bitmap", "setdatforvideo:bitmap--> "+bitmap );
        holder.doc_content.setImageResource(R.drawable.ic_file_placeholder);

        holder.progress_doc.setVisibility(View.GONE);
        holder.docname.setText(attachment.getName());
//        holder.doc_content.setImageBitmap(bitmap);
//        holder.imageView.setImageBitmap(bitmap);
//        Glide.with(context)
//                .load(bitmap)
//                .thumbnail(0.1f)
//                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .addListener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        holder.progress_doc.setVisibility(View.GONE);
//                        return false;
//                    }
//                })
//                .into(holder.doc_content);





        holder.doc_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("pdf", "onClick: pdf file--> "+imageUrl );


                Intent intent = new Intent(context, WebView_activity.class);
                intent.putExtra("webview",imageUrl);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }




    public void copy(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }


    @Override
    public int getItemCount() {
        return qbChatMessages.size();
    }


    @Override
    public int getItemViewType(int position) {

        QBChatMessage chatMessage = getItem(position);
        int p = CUSTOM_VIEW_TYPE;


        Log.e(TAG, "getItemViewType: qbcatservice-> " + ChatHelper.getInstance().getclient().getUser() +
                " \n\n\n\n\n getchathelper+ " + qbChatMessages.get(position) + " qbchatservice-> " + ChatHelper.getInstance().getclient() +
                "\n\n\n\n\n\n wbchatlkjd-> " + ChatHelper.getInstance().getclient().getUser() +
                " \n\n\n\n\n " + currentUser.getId()
        );


        Log.e(TAG, "getItemViewType:is Incoming -> " + isIncoming(chatMessage));
        if (currentUser.getId() != null) {
//            if (qbChatMessages.get(position).getSenderId().equals(currentUser.getId())) {
//                p = 1;
//            } else {
//                p = 0;
//            }


            assert chatMessage != null;
            if (chatMessage.getAttachments() != null && chatMessage.getAttachments().size() > 0) {
                QBAttachment attachment = getAttachment(position);
                boolean photo = QBAttachment.PHOTO_TYPE.equalsIgnoreCase(attachment.getType());
                boolean image = QBAttachment.IMAGE_TYPE.equalsIgnoreCase(attachment.getType());
                boolean video = QBAttachment.VIDEO_TYPE.equalsIgnoreCase(attachment.getType());
                boolean audio = QBAttachment.AUDIO_TYPE.equalsIgnoreCase(attachment.getType());
                boolean file = attachment.getType().equals("file") || attachment.getType().contains("file") || attachment.getType().equals("");

                if (photo || image || video || audio || file) {
                    if (isIncoming(chatMessage)) {
                        p = TYPE_ATTACH_LEFT;

                    } else {

                        p = TYPE_ATTACH_RIGHT;
                    }
                }
            } else if (isIncoming(chatMessage)) {
                p = TYPE_TEXT_LEFT;


            } else {
                p = TYPE_TEXT_RIGHT;
            }

        }


        return p;
    }


//
//    /**set image/file/video thumnail using glide*/
//
//    private void set


//    private void  loadSendimage(Context context,String imageurl,ImageView imageView){
//        mainThreadHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Glide.with(context)
//                        .load(imageurl)
//                        .into(imageView);
//            }
//        });
//    }

    private QBChatMessage getItem(int position) {
        if (position <= getItemCount() - 1) {
            return qbChatMessages.get(position);
        } else {
            return null;
        }
    }

    private QBAttachment getAttachment(int position) {
        QBChatMessage chatMessage = getItem(position);
        if (chatMessage != null && chatMessage.getAttachments() != null && chatMessage.getAttachments().iterator().hasNext()) {
            return chatMessage.getAttachments().iterator().next();
        } else {
            return null;
        }
    }


    /**
     * download attachment
     **/
    private void downloadAttachment(Collection<QBAttachment> attachment, ImageView imageView, ProgressBar progressBar) {
        Log.e("outputStream", "downloadAttachment: islogin---------> " + ChatHelper.getInstance().isLogged());
        Log.e("outputStream", "\n\n\n\n\n\n\n-1-----------downloadAttachment: " + attachment + "  \n\n\n\n\n");
        for (QBAttachment attachment1 : attachment) {
            Log.e("outputStream", "downloadAttachment: url==> " + attachment1.getUrl());
            String fileID = attachment1.getUrl();
//            new BackgroundOperation(fileID,imageView,progressBar).execute();
//            downloadimage(fileID, imageView, progressBar);
//            loadFileFromQB(attachment1);
        }

    }


    private boolean isIncoming(QBChatMessage chatMessage) {

        return chatMessage.getSenderId() != null && !chatMessage.getSenderId().equals(currentUser.getId());
    }


    public static class MessgeHolder extends RecyclerView.ViewHolder {
        TextView message_content,docname,docsize;
        TextView bubbleTextView;
        TextView nametxt;
        RelativeLayout Attachment_layout,doc_layout;
        ImageView imageView,doc_content;
        LinearLayout ll_message_body_container;
        ProgressBar progressBar;
        ProgressBar progress_image,progress_doc;
        ImageView play_button;
        TextView att_time, txt_time,doc_att_time;
        ImageView iv_messageattch_status,iv_message_status,download_button,iv_messagedoc_status;

        @SuppressLint("CutPasteId")
        public MessgeHolder(@NonNull View itemView) {
            super(itemView);
            message_content = itemView.findViewById(R.id.sendmessage_content);
            bubbleTextView = (TextView) itemView.findViewById(R.id.message_content);
            nametxt = (TextView) itemView.findViewById(R.id.nametxt);
            Attachment_layout = (RelativeLayout) itemView.findViewById(R.id.ll_attachment_body_container);
            imageView = (ImageView) itemView.findViewById(R.id.attachment_content);
            ll_message_body_container = (LinearLayout) itemView.findViewById(R.id.ll_message_body_container);
//            progressBar = (ProgressBar) itemView.findViewById(R.id.download_process_id);
            progress_image = (ProgressBar) itemView.findViewById(R.id.progress_image);
            play_button = (ImageView) itemView.findViewById(R.id.play_button);
            att_time = (TextView) itemView.findViewById(R.id.att_time);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            iv_messageattch_status = itemView.findViewById(R.id.iv_messageattch_status);
            iv_message_status = itemView.findViewById(R.id.iv_message_status);

            doc_layout = itemView.findViewById(R.id.ll_doc_body_container);
            doc_content = itemView.findViewById(R.id.doc_content);
            progress_doc = itemView.findViewById(R.id.progress_doc);
            download_button = itemView.findViewById(R.id.download_button);
            docname = itemView.findViewById(R.id.docname);
            docsize = itemView.findViewById(R.id.docsize);
            doc_att_time = itemView.findViewById(R.id.doc_att_time);
            iv_messagedoc_status = itemView.findViewById(R.id.iv_messagedoc_status);
        }


    }


    /**
     * download attachment do in background
     */
    class BackgroundOperation extends AsyncTask<InputStream, Void, InputStream> {


        String imageid;
        InputStream inputStream;
        ImageView imageView;
        ProgressBar progressBar;

        BackgroundOperation(String imageid, ImageView imageView, ProgressBar progressBar) {
            this.imageid = imageid;
            this.imageView = imageView;
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected InputStream doInBackground(InputStream... params) {

            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.post(new Runnable() {
                public void run() {
//                    downloadAttachment(null);
                    Log.e("Timedownload", "run: imageid-> " + imageid + " " + System.currentTimeMillis());


                    QBContent.downloadFile(imageid).performAsync(new QBEntityCallback<InputStream>() {
                        @Override
                        public void onSuccess(InputStream inputStream, Bundle bundle) {
                            // process file
//                            QBAttachment attachment2 = new QBAttachment(inputStream.toString());
//                            QBChatMessage chatMessage = new QBChatMessage();
//                            chatMessage.addAttachment(attachment2);


                            Log.e("Timedownload", "onSuccess:---->>>>>> " + inputStream);


                        }

                        @Override
                        public void onError(QBResponseException e) {

                        }
                    });


                }
            });

            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream s) {
            super.onPostExecute(s);
            Log.e("Timedownload", "onSuccess:---->>>>>> " + s);
            progressBar.setVisibility(View.GONE);
            if (s != null) {
                Log.d("InputStream Value :", "******" + s.toString() + "******************");
                Bitmap bmp = BitmapFactory.decodeStream(s);
                Drawable d = new BitmapDrawable(context.getResources(), bmp);

            }
        }
    }


    /**
     * handle for download image quickblox
     */
    private void downloadimage(String imageid, ImageView imageView, ProgressBar progressBar) {

        Glide.with(context)
                .load(imageid).thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade()).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                Log.e("glidedata", "onLoadFailed: failed to load data");

                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                Log.e("glidedata", "onResourceReady: ");
                progressBar.setVisibility(View.GONE);


                return false;
            }
        }).into(imageView);



    }


    private void loadFileFromQB(QBAttachment attachment, File videoFile, MessgeHolder holder, QBChatMessage chatMessage) {
        String attachmentID = attachment.getId();

                Log.e("fileloading", "runnning loop for : "+attachmentID );
                QBContent.downloadFile(attachmentID, new QBProgressCallback() {
                    @Override
                    public void onProgressUpdate(int i) {
                        Log.e("fileloading", "onProgressUpdate: video download-> " + i);
                    }
                }, null).performAsync(new QBEntityCallback<InputStream>() {
                    @Override
                    public void onSuccess(InputStream inputStream, Bundle bundle) {
                        Log.e("fileloading", "onSuccess: file download successfully input stream-> "+inputStream);
                        if (inputStream != null) {
                            new LoaderAsyncTask(videoFile, inputStream, holder, attachment,chatMessage).execute();
                        }
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("fileloading", "onError: "+e.toString() );
                    }
                });



    }


    private class LoaderAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private File file;
        private InputStream inputStream;
        //        private NewMessageViewHolder holder;
        private MessgeHolder holder;
        private QBAttachment attachment;
        private QBChatMessage chatMessage;
        private int position;

        LoaderAsyncTask(File file, InputStream inputStream, MessgeHolder holder, QBAttachment attachment, QBChatMessage chatMessage) {
            this.file = file;
            this.inputStream = inputStream;
            this.holder = holder;
            this.attachment = attachment;
//            this.position = position;
            this.chatMessage = chatMessage;

        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            String root = Environment.getExternalStorageDirectory().toString();
            Log.d("fileloading", "Downloading File as InputStream file root -> " + root + " file -> " + file);
            try {
                FileOutputStream output = new FileOutputStream(file);

                if (inputStream != null) {
                    IOUtils.copy(inputStream, output);
                    inputStream.close();
                    output.close();
                    Log.e("fileloading", "doInBackground: output-> " + output);
                } else {
                    Log.e("fileloading", "doInBackground: is null ");
                }
            } catch (IOException e) {
                Log.e("fileloading", e.getMessage());
            }


            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.e("filefetch", "\n\n\n\n\n\n\n\n\n File Downloaded@@@@@@ " + file.getAbsolutePath() + "\n\n\n\n\n\n");
            setattachement(attachment, file, holder,false,chatMessage);


//            try {
//                copy(context.getFilesDir(),file);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            fillVideoFileThumb(file);
        }
    }

    private void fillVideoFileThumb(File file) {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(file.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
    }




    @Override
    public long getHeaderId(int position) {
        QBChatMessage chatMessage = getItem(position);
        if (chatMessage != null) {
//            return TimeUtils.getDateAsHeaderId(chatMessage.getDateSent() * 1000)
            return getDateAsHeaderId(chatMessage.getDateSent() * 1000);
        } else {
            return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_chat_message_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }


    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        View view = viewHolder.itemView;
        TextView dateTextView = view.findViewById(R.id.header_date_textview);

        QBChatMessage chatMessage = getItem(i);

        if (chatMessage != null) {
            long timeInMillis = chatMessage.getDateSent() * 1000;

//            Calendar msgTime = Calendar.getInstance();
//            msgTime.setTimeInMillis(timeInMillis);

            dateTextView.setText(messageDaySend(timeInMillis));

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) dateTextView.getLayoutParams();
            lp.topMargin = 0;
            dateTextView.setLayoutParams(lp);


        }
    }

    public static long getDateAsHeaderId(long milliseconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        return Long.parseLong(dateFormat.format(new Date(milliseconds)));
    }


    /**
     * message send date
     */
    private String messageDaySend(long timeInMillis) {
        String title = "today";
        Calendar msgTime = Calendar.getInstance();
        msgTime.setTimeInMillis(timeInMillis);

        Calendar now = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM", Locale.ENGLISH);
        SimpleDateFormat lastYearFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);

        boolean sameDay = now.get(Calendar.DATE) == msgTime.get(Calendar.DATE);
        boolean lastDay = now.get(Calendar.DAY_OF_YEAR) - msgTime.get(Calendar.DAY_OF_YEAR) == 1;
        boolean sameYear = now.get(Calendar.YEAR) == msgTime.get(Calendar.YEAR);

        if (sameDay && sameYear) {
//            title = context.getString(R.string.today);
            title = "today";
        } else if (lastDay & sameYear) {
//            title = context.getString(R.string.yesterday);
            title = "yesterday";
        } else if (sameYear) {
            title = dateFormat.format(new Date(timeInMillis));
        } else {
            title = lastYearFormat.format(new Date(timeInMillis));
        }
        return title;
    }

    /**update Chat Adapter*/
    public void updateStatusDelivered(String messageID,Integer userId){
        for(int position = 0;position < qbChatMessages.size();position++){
            QBChatMessage message = qbChatMessages.get(position);
            if(message.getId().equals(messageID)){
                ArrayList<Integer> deliveredIds = new ArrayList<>();
                if(message.getDeliveredIds() != null){
                    deliveredIds.addAll(message.getDeliveredIds());
                }
                deliveredIds.add(userId);
                message.setDeliveredIds(deliveredIds);
                notifyItemChanged(position);
            }
        }
    }

    public void updateStatusRead(String messageID, Integer userId) {
        for (int position = 0; position < qbChatMessages.size(); position++) {
            QBChatMessage message = qbChatMessages.get(position);
            if (message.getId().equals(messageID)) {
                ArrayList<Integer> readIds = new ArrayList<>();
                if (message.getReadIds() != null) {
                    readIds.addAll(message.getReadIds());
                }
                readIds.add(userId);
                message.setReadIds(readIds);
                notifyItemChanged(position);
            }
        }
    }


    /**read message notifiy to server*/
    private void readMessage(QBChatMessage chatMessage) {
        try {

            chatDialog.readMessage(chatMessage);
        } catch (XMPPException | SmackException.NotConnectedException e) {
            Log.w(TAG, e);
        }
    }

    private boolean isReadByCurrentUser(QBChatMessage chatMessage) {
        Integer currentUserId = ChatHelper.getCurrentUser(context).getId();
        return !CollectionsUtil.isEmpty(chatMessage.getReadIds()) && chatMessage.getReadIds().contains(currentUserId);
    }
}
