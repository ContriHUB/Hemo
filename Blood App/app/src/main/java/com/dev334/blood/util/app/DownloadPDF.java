package com.dev334.blood.util.app;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class DownloadPDF extends  AsyncTask<String,Void,Void> {


    Context context;


    @Override
    protected Void doInBackground(String... strings) {

        String fileurl=strings[0];
        String fileName=strings[1];

         String extStorageDirectory= Environment.getExternalStorageState().toString();
         File folder=new File(extStorageDirectory,"Hemo Requests");
         folder.mkdir();

         File pdffile=new File(folder,fileName);
         try{
             pdffile.createNewFile();
         }catch (IOException e)
         {
             e.printStackTrace();
         }

         FileDownloader.downloadFile(fileurl,pdffile);
         return  null;
    }
}
