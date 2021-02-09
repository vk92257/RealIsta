package com.example.myapplication.chatModule.Chat_Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;
import com.example.myapplication.chatModule.chatInterface.SendAttachmentCallback;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBProgressCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.ContentType;
import com.quickblox.core.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

public class Attachment_Adapter extends RecyclerView.Adapter<Attachment_Adapter.Attachment_viewholder> {
    private ArrayList<File> show_attachment;
//    private ArrayList<File> temp_attachment;
    private Context mcontext;
    private static final String TAG = "Attachment_Adapter";
    private static final long MAX_FILE_SIZE_100MB = 104857600;
    private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private int size=0;

//    private Map<File, QBAttachment> fileQBAttachmentMap;
    private Map<File, Integer> fileUploadProgressMap;
    private SendAttachmentCallback sendAttachmentCallback;


    public Attachment_Adapter(ArrayList<File> show_attachment, Context mcontext) {
        this.show_attachment = show_attachment;
        this.mcontext = mcontext;
//        fileQBAttachmentMap =new HashMap<File, QBAttachment>();
//        this.temp_attachment = new ArrayList<>();
//        temp_attachment.containsAll(show_attachment);
        Log.e(TAG, "Attachment_Adapter: showAttachment-------> "+show_attachment );
//        Log.e("nofound-->", "constraction-> " + temp_attachment.size());

    }


    public void onsendAttachment(SendAttachmentCallback sendAttachmentCallback)
    {
          this.sendAttachmentCallback  = sendAttachmentCallback;
    }

    @NonNull
    @Override
    public Attachment_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_attachment_layout, parent, false);
        return new Attachment_viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Attachment_viewholder holder, int position) {

        final File attachmentFile = show_attachment.get(position);
        holder.button_attachment_preview_delete.setVisibility(View.GONE);
        holder.grayoverlay.setVisibility(View.VISIBLE);
        holder.textper.setVisibility(View.VISIBLE);
        holder.textper.setText( "0%");

//        loadimagetochat(attachmentFile);
//        holder.image.setImageURI(Uri.fromFile(attachmentFile));


        int raw = R.drawable.ic_broken_image;
      if(attachmentFile.getName().contains("pdf")){
          Log.e("pdfcon", "onBindViewHolder: contain pdf" );
          raw = R.drawable.ic_file_placeholder;
      }

        Glide.with(mcontext).load(Uri.fromFile(attachmentFile))
                .thumbnail(0.5f)
                .error(raw)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);

        getAtachementLink(attachmentFile, holder.textper, holder.grayoverlay, holder.button_attachment_preview_delete,position);

        holder.button_attachment_preview_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(attachmentFile);
//                removeuploadedAttach(attachmentFile);


            }
        });

//        if(isfileuploaded(attachmentFile)){
//            holder.button_attachment_preview_delete.setVisibility(View.VISIBLE);
//        }

    }


    public Map<File, QBAttachment> getuploadedattachment() {
        return null;
    }

    @Override
    public int getItemCount() {
        return show_attachment.size();
    }

    static class Attachment_viewholder extends RecyclerView.ViewHolder {
        ImageView image;
        View grayoverlay;
        ImageButton button_attachment_preview_delete;
        TextView textper;

        public Attachment_viewholder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_attachment_preview);
            grayoverlay = itemView.findViewById(R.id.grayoverlay);
            button_attachment_preview_delete = itemView.findViewById(R.id.button_attachment_preview_delete);
            textper = itemView.findViewById(R.id.textper);

        }
    }


//    private void  loadimagetochat(){
//        mainThreadHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
//    }

    public void add(final File item) {
        size = size+1;
        show_attachment.add(item);
        notifyDataSetChanged();
//        temp_attachment.add(item);

    }

    public void cleaArraylist() {
        Log.e("attachmentCount", "cleaArraylist:show_attachmentsize->  " + show_attachment.size() + " tem_attachent-> " + show_attachment.size());
        show_attachment.clear();
//        temp_attachment.clear();
//        fileQBAttachmentMap.clear();
        notifyDataSetChanged();
    }

    public void remove(File item) {
        Log.e("attachmentCount -> ", "remove item cleaArraylist:show_attachmentsize->  " + show_attachment.size() + " tem_attachent-> " + show_attachment.size());

//        if (show_attachment.size() == 1) {
//            cleaArraylist();
//        } else {
            show_attachment.remove(item);
//            notifyItemRemoved(item);

//        }
//        notifyDataSetChanged();
    }

    public void removePosition(int position){

    }

    public int size() {
        return size;
    }


    /**generate file link from quick blox*/
    private void getAtachementLink(File imagefile, TextView textView, View grayoverlay, ImageButton cancle, int position) {

//        mainThreadHandler.post(new Runnable() {
//            @Override
//            public void run() {

//        File filename = new File(imagefile.getPath(),"newname");
//                Log.e("filenameconvert", "getAtachementLink: file--> " + imagefile.getAbsolutePath()+" "+imagefile.getPath()+" new name-> "+filename.getAbsolutePath());

        File newfile = null;

        try {

            Uri file = Uri.fromFile(imagefile);
            FileInputStream newfilestream = new FileInputStream(imagefile.getAbsoluteFile());
            newfile=  File.createTempFile(String.valueOf(Calendar.getInstance().getTimeInMillis()), "."+MimeTypeMap.getFileExtensionFromUrl(file.toString()));
            newfile.deleteOnExit();

            FileOutputStream out = new FileOutputStream(newfile);
            IOUtils.copy(newfilestream, out);


            Log.e("filenameconvert", "getAtachementLink: filename->^^^^^^^ "+newfile.getAbsolutePath()+" imagefilename-> "+imagefile.getAbsolutePath() );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        String[] tags = new String[]{"fun", "notfun"};
                QBContent.uploadFileTask(newfile, false, Arrays.toString(tags), new QBProgressCallback() {
                    @Override
                    public void onProgressUpdate(int i) {

                        Log.e(TAG, "onProgressUpdate: -> " + i);
                        if (i != 100) {
                            textView.setText(i + "%");


                        } else {

                        }
                    }
                }).performAsync(new QBEntityCallback<QBFile>() {
                    @Override
                    public void onSuccess(QBFile qbFile, Bundle bundle) {

                        Log.e("again_call", "onSuccess: again call" );

//                        QBChatMessage chatMessage = new QBChatMessage();
//                        chatMessage.setSaveToHistory(true);
                        Log.e("filenameconvert", "onSuccess: qbfile-> "+qbFile.getPrivateUrl() );

                        String type = "photo";
                        if (qbFile.getContentType().contains(QBAttachment.IMAGE_TYPE)) {
                            type = QBAttachment.IMAGE_TYPE;
                        } else if (qbFile.getContentType().contains(QBAttachment.VIDEO_TYPE)) {
                            type = QBAttachment.VIDEO_TYPE;
                        }
                        else {
                            type = "file";
                        }


                        // attach a photo
                        QBAttachment attachment = new QBAttachment(type);
                        attachment.setId(qbFile.getUid().toString());
                        attachment.setSize(qbFile.getSize());
                        attachment.setName(qbFile.getName());
                        attachment.setUrl(qbFile.getPrivateUrl());
//                        attachment.setContentType(qbFile.getContentType());
//                            new LoaderAsyncTask(qbFile.getName()).execute();
                      File sourcefile = new File(qbFile.getName());
                        File videoFile = new File(mcontext.getFilesDir(), qbFile.getName());
//                      mainThreadHandler.post(new Runnable() {
//                          @Override
//                          public void run() {
//                              Log.e(TAG, "run: sharedata-------------------" );
//                              copyFile(sourcefile,videoFile);
//                          }
//                      });


                        Log.e("entry-> Adapter-> ", "onSuccess: "+attachment +" type "+type+" seturl-> "
                                +qbFile.getPrivateUrl()+" QBfb "+qbFile.getName());

//
//                        textView.setVisibility(View.GONE);
//                        grayoverlay.setVisibility(View.GONE);
                        sendAttachmentCallback.sendattachment(attachment,position);


                        Log.e(TAG, "\n\n\n\n\n\n\n-------------------------onSuccess: attachment size---> "+show_attachment.size()+"\n\n\n\n\n\n\n\n" );


                        int postiondata = show_attachment.indexOf(imagefile);
                        Log.e("postionindex ", "onSuccess: position-> "+position + " imagefile-> position --> "+postiondata);
                        show_attachment.remove(imagefile);
////                        notifyItemChanged(position);
//                        checkforattchmentposition(imagefile);
                        notifyItemRemoved(postiondata);
                        size = size-1;


                            Intent intent = new Intent("countarray");
                            intent.putExtra("size",size);
                            mcontext.sendBroadcast(intent);

                        Log.e("again_call", "onSuccess: notifyDataSetChanged call" );


                        Log.e("n", "onSuccess: fileto delete-> "+imagefile.getName()+" show_attachment-> "+show_attachment +" QBfb "+qbFile.getName());

                        Log.e(TAG, "onSuccess: QbFile uid-> " + qbFile.getUid() + " QBFile id-> " + qbFile.getId() + " QBFile-> " + qbFile);


                    }


                    @Override
                    public void onError(QBResponseException e) {

                        cleaArraylist();
                        Log.e(TAG, "onError: onAttachment--> " + e.toString());
                        Toast.makeText(mcontext,e.getMessage(),Toast.LENGTH_SHORT);

                    }
                });
//            }
//        });

    }



    public boolean isfileuploaded(File attachFile) {
        return false;
    }

    public boolean checkattchmentReady() {
        Log.e("nofound-->", "checkattchmentReady: " + null);
        return false;
    }


    public void removeuploadedAttach(File filename) {

    }


    /**
     * copy contents from source file to destination file
     *
     * @param sourceFilePath  Source file path address
     * @param destinationFilePath Destination file path address
     */
    private void copyFile(File sourceFilePath, File destinationFilePath) {

        Log.e(TAG, "copyFile: loginfile sourcefile-> "+sourceFilePath+
                " destinationfile-> "+destinationFilePath);


        try{

            if (!sourceFilePath.exists()) {
                return;
            }

            FileChannel source = null;
            FileChannel destination = null;
            source = new FileInputStream(sourceFilePath).getChannel();
            destination = new FileOutputStream(destinationFilePath).getChannel();
            if (destination != null && source != null) {
                destination.transferFrom(source, 0, source.size());
            }
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }

    }
}
