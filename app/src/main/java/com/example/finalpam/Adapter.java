package com.example.finalpam;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.core.FirestoreClient;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<Wisata> itemList;

    public Adapter(List<Wisata> itemLIst) {
    this.itemList = itemLIst;
    }

    public void setItemList(List<Wisata> itemList) {
        this.itemList = itemList;
    }
    DatabaseReference db;
    FirebaseDatabase data;
    FirebaseFirestore dataB;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View daftar = inflater.inflate(R.layout.lcard,parent,false);
        ViewHolder isi = new ViewHolder(daftar);
        return isi;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder._nama.setText(itemList.get(position).getNama());
        holder._isi.setText(itemList.get(position).getDeskripsi());
        Glide.with(holder.itemView.getContext())
                .load(itemList.get(position).getUrl())
                .override(100,100)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        if(itemList == null){
            return 0;
        }
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView _nama,_isi;
        Button _delete,_update;
        CardView cardView;
        ImageView img;
        FirebaseStorage ref;
        StorageReference ferr;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            data = FirebaseDatabase.getInstance();
            _nama = itemView.findViewById(R.id.Nama);
            _isi = itemView.findViewById(R.id.descript);
            cardView= itemView.findViewById(R.id.card);
            cardView.setOnClickListener(this);
            img = itemView.findViewById(R.id.image);
            ref = FirebaseStorage.getInstance();
            ferr = ref.getReference("Wisata");
//            dataB.collection("Wisata").get();
        }

        @Override
        public void onClick(View view) {
        if(cardView.getId()==view.getId()){
        int a = getAdapterPosition();
        Intent peh = new Intent(view.getContext(),Detail.class);
        peh.putExtra("titelG",itemList.get(a).getNama());
        peh.putExtra("crypt", itemList.get(a).getDeskripsi());
        peh.putExtra("docid",itemList.get(a).getKey());
        view.getContext().startActivity(peh);
    }
        }
    }
}




