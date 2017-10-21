package com.codepath.cameratesting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity {

    private static final int IMAGE_OR_CAMERA_REQUEST_CODE = 2;

    private Uri outputFileUri;
    File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        ArrayList<Integer> results = new ArrayList<>();
        ArrayList<String> perms = new ArrayList<>();
//        res.addAll(grantResults);
        for (int i = 0; i < grantResults.length; i++) {
            results.add(grantResults[i]);
        }
        for (int i = 0; i < permissions.length; i++) {
            perms.add(permissions[i]);
        }

        Log.d("jenda", "onRequestPermissionsResult requestCode " + requestCode);
        Log.d("jenda", "onRequestPermissionsResult permissions " + perms);
        Log.d("jenda", "onRequestPermissionsResult grantResults " + results);

//        PackageManager.PERMISSION_GRANTED
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.d("jenda", "requestCode " + requestCode);
        Log.d("jenda", "resultCode " + resultCode);
        Log.d("jenda", "data " + data);
        Log.d("jenda", "resultCode OK " + (resultCode == RESULT_OK));


        if (resultCode == RESULT_OK) {}


        if (requestCode == IMAGE_OR_CAMERA_REQUEST_CODE) {
            boolean exists = photoFile.exists();//new File(outputFileUri.getPath()).exists();
            Log.d("jenda", "outputFileUri exists " + exists);
            Toast.makeText(this, "Image exists " + exists, Toast.LENGTH_LONG).show();

        }
    }


    @OnClick(R.id.cameraButton)
    void showedImage() {

        Log.d("jenda", "showedImage ");

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            String[] perms = {  Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE };
            int permsRequestCode = 200;
            requestPermissions(perms, permsRequestCode);

            Log.d("jenda", "permission not granted ");
            return;
        }

        Log.d("jenda", "permission granted ");

        final File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        photoFile = new File(root, "share_image_" + System.currentTimeMillis() + ".jpeg");


        if (!photoFile.getParentFile().exists() && !photoFile.getParentFile().mkdirs()) {
            Toast.makeText(this, "failed to created dirs. ", Toast.LENGTH_LONG).show();
            return;
        }
        outputFileUri = FileProvider.getUriForFile(this, "com.codepath.cameratesting", photoFile);

        Log.d("jenda", " outputFileUri.getPath() " +  outputFileUri.getPath());
        Log.d("jenda", " file.getAbsolutePath() " +  photoFile.getAbsolutePath());

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        // set the image file name

        startActivityForResult(intent, IMAGE_OR_CAMERA_REQUEST_CODE);
    }
}
