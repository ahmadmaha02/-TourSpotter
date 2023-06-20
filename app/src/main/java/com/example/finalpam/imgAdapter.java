package com.example.finalpam;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.os.Environment.DIRECTORY_PICTURES;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class imgAdapter extends RecyclerView.Adapter<imgAdapter.ViewHold> implements View.OnClickListener {
    List<String> url;

    FirebaseStorage storage;

    public imgAdapter(List<String> ure) {
        this.url = ure;
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View daftar = inflater.inflate(R.layout.cd,parent,false);
        ViewHold isi = new ViewHold(daftar);
        return isi;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(url.get(position))
                .signature(new ObjectKey(System.currentTimeMillis()))
                .override(1418,1418)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        if (url == null){return 0;}
        return url.size();
    }

    @Override
    public void onClick(View view) {

    }

    public class ViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img;
        Button download;
        public ViewHold(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageView);
            download = itemView.findViewById(R.id.download);
            download.setOnClickListener(this);
            storage = FirebaseStorage.getInstance();
        }

        @Override
        public void onClick(View view) {
        if(download.getId() == view.getId()){
            int a = getAdapterPosition();
            String link = url.get(a);
            try {
                download(itemView.getContext(),link);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        }
        public void download( Context context, String url) throws IOException {
//           DownloadManager downloadManager = (DownloadManager) context
//                   .getSystemService(Context.DOWNLOAD_SERVICE);
//            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//            String localFilePath = "image.jpg";
//            request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, localFilePath);
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//            downloadManager.enqueue(request);


            StorageReference httpsReference = storage.getReferenceFromUrl(url);

            final File localFile = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS),"Download");
            if (!localFile.exists()) {
                localFile.mkdirs();
            }
            String name = httpsReference.getName();
            File destinationFile = new File(localFile, name);

            httpsReference.getFile(destinationFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(context, "FIle downloaded", Toast.LENGTH_SHORT).show();

                }
            });
        }


        }
    }

