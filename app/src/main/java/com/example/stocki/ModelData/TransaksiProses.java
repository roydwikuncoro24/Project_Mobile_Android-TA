package com.example.stocki.ModelData;

import android.os.Parcel;
import android.os.Parcelable;

public class TransaksiProses implements Parcelable {
    String id, idtoko, namatoko, idadmin, kode,
           namabarang, jumlah,
           modal, stok, jual, jualtoko,
           idtipe, tipe, image, aset;

    public TransaksiProses (String id, String idadmin, String namabarang, String kode,
                     String jumlah, String modal, String stok,
                     String jual, String jualtoko, String tipe,
                     String image, String idtipe) {
        this.id = id;
        this.idadmin = idadmin;
        this.namabarang = namabarang;
        this.kode = kode;
        this.jumlah = jumlah;
        this.modal = modal;
        this.stok = stok;
        this.jual = jual;
        this.jualtoko = jualtoko;
        this.tipe = tipe;
        this.image = image;
        this.idtipe = idtipe;
    }

    protected TransaksiProses (Parcel in) {
        id = in.readString();
        idtoko = in.readString();
        namatoko = in.readString();
        idadmin = in.readString();
        kode = in.readString();
        namabarang = in.readString();
        jumlah = in.readString();
        modal = in.readString();
        stok = in.readString();
        jual = in.readString();
        jualtoko = in.readString();
        idtipe = in.readString();
        tipe = in.readString();
        image = in.readString();
        aset = in.readString();
    }

    public static final Creator<TransaksiProses> CREATOR = new Creator<TransaksiProses>() {
        @Override
        public TransaksiProses createFromParcel(Parcel in) {
            return new TransaksiProses(in);
        }

        @Override
        public TransaksiProses[] newArray(int size) {
            return new TransaksiProses[size];
        }
    };

    //GET
    public String getId() {
        return id;
    }

    public String getIdtoko() {
        return idtoko;
    }

    public String getIdadmin() {
        return idadmin;
    }

    public String getNamatoko() {
        return namatoko;
    }

    public String getNamabarang() {
        return namabarang;
    }

    public String getKode() {
        return kode;
    }

    public String getJumlah() {
        return jumlah;
    }

    public String getModal() {
        return modal.replace(",","");
    }

    public String getJual() {
        return jual.replace(",","");
    }

    public String getJualtoko() {
        return jualtoko.replace(",","");
    }

    public String getStok() {
        return stok;
    }

    public String getIdtipe() {
        return idtipe;
    }

    public String getTipe() {
        return tipe;
    }

    public String getImage() {
        return image;
    }

    public String getAset() {
        return aset;
    }

    //SET
    public void setId(String id) {
        this.id = id;
    }

    public void setIdtoko(String idtoko) {
        this.idtoko = idtoko;
    }

    public void setIdadmin(String idadmin) {
        this.idadmin = idadmin;
    }

    public void setNamatoko(String namatoko) {
        this.namatoko = namatoko;
    }

    public void setNamabarang(String namabarang) {
        this.namabarang = namabarang;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public void setModal(String modal) {
        this.modal = modal;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public void setJual(String jual) {
        this.jual = jual;
    }

    public void setJualtoko(String jualtoko) {
        this.jualtoko = jualtoko;
    }

    public void setIdtipe(String idtipe) {
        this.idtipe = idtipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAset(String aset) {
        this.aset = aset;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(idtoko);
        parcel.writeString(namatoko);
        parcel.writeString(idadmin);
        parcel.writeString(kode);
        parcel.writeString(namabarang);
        parcel.writeString(jumlah);
        parcel.writeString(modal);
        parcel.writeString(stok);
        parcel.writeString(jual);
        parcel.writeString(jualtoko);
        parcel.writeString(idtipe);
        parcel.writeString(tipe);
        parcel.writeString(image);
        parcel.writeString(aset);
    }
}
