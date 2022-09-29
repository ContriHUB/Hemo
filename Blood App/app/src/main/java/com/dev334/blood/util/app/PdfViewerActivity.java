package com.dev334.blood.util.app;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.dev334.blood.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PdfViewerActivity extends AppCompatActivity {

    String pdfFile;
    private PDFView pdfView;
    String TAG="PdfViewerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        pdfFile=getIntent().getStringExtra("pdfFile");
        pdfView=findViewById(R.id.pdfView);
        Log.i(TAG, "onCreate: "+pdfFile);
        pdfView.fromFile(new File(pdfFile)).load();
    }
}