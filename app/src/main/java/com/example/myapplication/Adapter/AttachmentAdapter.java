package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.Interface.ClickdeleteAttachment;
import com.example.myapplication.pojo.FilenameAndSize;

import java.util.ArrayList;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder>  {
    private ArrayList<FilenameAndSize> filenameAndSizeArrayList;
    private Context context;
    private ClickdeleteAttachment clickdeleteAttachment;

    public AttachmentAdapter(ArrayList<FilenameAndSize> filenameAndSizeArrayList, Context context) {
        this.filenameAndSizeArrayList = filenameAndSizeArrayList;
        this.context = context;
    }
    public void SetonDelete_Attachment(ClickdeleteAttachment clickdeleteAttachment){
        this.clickdeleteAttachment = clickdeleteAttachment;
    }

    @NonNull
    @Override
    public AttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_jobpostattachments,parent,false);
        return new AttachmentViewHolder(view);

    }
    @Override
    public void onBindViewHolder(@NonNull AttachmentViewHolder holder, int position) {
            holder.attchmentname.setText(filenameAndSizeArrayList.get(position).getFilename());
            holder.attchmentSize.setText(filenameAndSizeArrayList.get(position).getFilesize());
            holder.closeattachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickdeleteAttachment.setOnattachmentdelete(position);
                }
            });
    }

    @Override
    public int getItemCount() {
        return filenameAndSizeArrayList.size();
    }

    static class AttachmentViewHolder extends RecyclerView.ViewHolder {
        TextView attchmentname;
        TextView attchmentSize;
        LinearLayout closeattachment;
        public AttachmentViewHolder(@NonNull View itemView) {
            super(itemView);
            attchmentname = itemView.findViewById(R.id.attachment);
            attchmentSize = itemView.findViewById(R.id.size);
            closeattachment = itemView.findViewById(R.id.close);
        }
    }
}
