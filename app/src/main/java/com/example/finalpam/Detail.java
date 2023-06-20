package com.example.finalpam;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Detail extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rex;
    TextView titel, detil;
    imgAdapter imgAdapter;
    List<String> ure = new ArrayList<>();
    FirebaseFirestore DB;
    StorageReference storageReference;
    FirebaseStorage storage;
    String docId;
    ActivityResultLauncher<Intent> filePickerLauncher;
    Uri fileUri;
    Button upload;
    String Linked;
    Map<Object, String> leh = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        titel = findViewById(R.id.Title);
        detil = findViewById(R.id.detil);
        rex = findViewById(R.id.papew);
        imgAdapter = new imgAdapter(ure);
        docId = getIntent().getStringExtra("docid");
        storage = FirebaseStorage.getInstance();
        DB = FirebaseFirestore.getInstance();
        storageReference = storage.getReference();

        upload = findViewById(R.id.upload);
        upload.setOnClickListener(this);
        rex.setLayoutManager(new LinearLayoutManager(this));
        rex.setAdapter(imgAdapter);


        titel.setText(getIntent().getStringExtra("titelG"));
        detil.setText(getIntent().getStringExtra("crypt"));

        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();
                            fileUri = data.getData();
                            if (fileUri != null) {
                                uploadFile(fileUri);
                            } else {
                                Log.e("Detail", "Failed to retrieve file URI");
                            }
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        load();
    }

    public void load(){

        DB.collection(Wisata.class.getSimpleName()).document(docId).collection("Gambare")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }
                        ure.clear();
                        for(DocumentChange documentChange : value.getDocumentChanges()){
                            if(documentChange.getType() == DocumentChange.Type.ADDED){
                                DocumentSnapshot doc = documentChange.getDocument();
                                String lo = (String) doc.get("urI");

                                ure.add(lo);
                                Log.d(TAG, "imgAdapter" + dc.getDocument().getData());
                            }
                            imgAdapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    private void uploadFile(Uri fileUri) {
        File file = new File(fileUri.getPath());
        StorageReference fileRef = storageReference.child("img/" +docId+"/"+ file.getName());
        // Upload file to Firebase Storage
        UploadTask uploadTask = fileRef.putFile(fileUri);
        // Register observers to listen for the upload progress or errors
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // File uploaded successfully
                Log.d("MainActivity", "File uploaded successfully");
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Linked = uri.toString();
                        // Use the imageUrl as needed
                        addLink(Linked);
                    }

                });
//                leh.put("urI",Linked);
//                DB.collection(Wisata.class.getSimpleName()).document(docId)
//                        .collection("Gambare").add(leh);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle unsuccessful uploads
                Log.e("MainActivity", "Failed to upload file: " + e.getMessage());
            }
        });
    }
    public void addLink(String link){
        leh.put("urI",link);
        DB.collection(Wisata.class.getSimpleName()).document(docId)
                .collection("Gambare").add(leh);
    }

    @Override
    public void onClick(View view) {
        if(upload.getId()==view.getId()){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            filePickerLauncher.launch(intent);
        }
    }
}