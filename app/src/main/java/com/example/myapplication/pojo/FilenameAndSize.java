package com.example.myapplication.pojo;

import android.net.Uri;

public class FilenameAndSize {
    String filename;
    String filesize;
    Uri fileuri;

    public FilenameAndSize(String filename, String filesize,Uri fileuri) {
        this.filename = filename;
        this.filesize = filesize;
        this.fileuri = fileuri;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public Uri getFileuri() {
        return fileuri;
    }

    public void setFileuri(Uri fileuri) {
        this.fileuri = fileuri;
    }
}
