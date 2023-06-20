package com.example.finalpam;

public class Wisata {
    private String Nama,deskripsi,key,url;
    public Wisata(String nama,String desc){
        this.deskripsi=desc;
        this.Nama = nama;
    }

    public Wisata() {
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getNama() {
        return Nama;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
