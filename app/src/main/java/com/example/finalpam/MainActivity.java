package com.example.finalpam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth mFier;
    FirebaseFirestore DB;
    TextView user;

    Adapter adapt;

    Button logout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mFier = FirebaseAuth.getInstance();
        DB = FirebaseFirestore.getInstance();
        logout =findViewById(R.id.logout);

        RecyclerView re = findViewById(R.id.recy);
        user = findViewById(R.id.Uname);
        user.setText(mFier.getCurrentUser().getEmail());
        load();
        re.setLayoutManager(new LinearLayoutManager(this));
        adapt = new Adapter(isi);
        re.setAdapter(adapt);




        logout.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentU = mFier.getCurrentUser();
        user.setText(currentU.getEmail());

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==logout.getId()){
            mFier.signOut();
            Intent lo = new Intent(MainActivity.this,login.class);
            lo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(lo);
        }
    }
    List<Wisata> isi = new ArrayList<>();
    public void load(){
     DB.collection(Wisata.class.getSimpleName())
             .addSnapshotListener(new EventListener<QuerySnapshot>() {
         @Override
         public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
            if(error != null){
                Log.e("Firestore error",error.getMessage());
                return;
            }

            for(DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                Wisata wii = documentChange.getDocument().toObject(Wisata.class);
                wii.setKey(documentChange.getDocument().getId());
                DocumentSnapshot doc = documentChange.getDocument();
                String lo = (String) doc.get("imgUrl");
                wii.setUrl(lo);
                isi.add(wii);
                }
                adapt.notifyDataSetChanged();
            }
         }
     });

    }
}
