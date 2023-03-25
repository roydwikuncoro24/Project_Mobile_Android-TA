package com.example.stocki.ModelData;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BarangModelData {
//    @SerializedName("id")
//    @Expose
    String id, idtoko, namatoko, idadmin, kode, namabarang, stok, hargadasar, hargajual,
           hargajualtoko, keterangan, image, idtipe, tipe, aset;

    public BarangModelData(String id, String idadmin, String hargadasar, String stok,
                           String hargajual, String hargajualtoko, String idtipe){
        this.id = id;
        this.idadmin = idadmin;
        this.hargadasar = hargadasar;
        this.hargajual = hargajual;
        this.hargajualtoko = hargajualtoko;
        this.stok = stok;
        this.idtipe = idtipe;
    }

    //GET
    public String getId() {
        return id;
    }

    public String getIdtoko() {
        return idtoko;
    }

    public String getNamatoko() {
        return namatoko;
    }

    public String getIdadmin() {
        return idadmin;
    }

    public String getKode() {
        return kode;
    }

    public String getNamabarang() {
        return namabarang;
    }

    public String getStok() {
        return stok;
    }

    public String getHargadasar() {
        return hargadasar;
    }

    public String getHargajual() {
        return hargajual;
    }

    public String getHargajualtoko() {
        return hargajualtoko;
    }

    public String getImage() {
        return image;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getidTipe() {
        return idtipe;
    }

    public String getTipe() {
        return tipe;
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

    public void setNamatoko(String namatoko) {
        this.namatoko = namatoko;
    }

    public void setIdadmin(String idadmin) {
        this.idadmin = idadmin;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public void setNamabarang(String namabarang) {
        this.namabarang = namabarang;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public void setHargadasar(String hargadasar) {
        this.hargadasar = hargadasar;
    }

    public void setHargajual(String hargajual) {
        this.hargajual = hargajual;
    }

    public void setHargajualtoko(String hargajualtoko) {
        this.hargajualtoko = hargajualtoko;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public void setidTipe(String idtipe) {
        this.idtipe = idtipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public void setAset(String aset) {
        this.aset = aset;
    }


    public static Parcelable.Creator<BarangModelData> getCREATOR() {
        return CREATOR;
    }

    protected BarangModelData(Parcel in) {
        id = in.readString();
        idtoko = in.readString();
        idadmin = in.readString();
        kode = in.readString();
        image = in.readString();
        namabarang = in.readString();
        hargadasar = in.readString();
        stok = in.readString();
        hargajual = in.readString();
        hargajualtoko = in.readString();
        keterangan = in.readString();
        idtipe = in.readString();
        aset = in.readString();
    }

    public static final Parcelable.Creator<BarangModelData> CREATOR = new Parcelable.Creator<BarangModelData>() {
        @Override
        public BarangModelData createFromParcel(Parcel in) {
            return new BarangModelData(in);
        }

        @Override
        public BarangModelData[] newArray(int size) {
            return new BarangModelData[size];
        }
    };

    //@Override
    public int describeContents() {
        return 0;
    }

    //@Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(idtoko);
        parcel.writeString(idadmin);
        parcel.writeString(kode);
        parcel.writeString(namabarang);
        parcel.writeString(hargadasar);
        parcel.writeString(stok);
        parcel.writeString(hargajual);
        parcel.writeString(hargajualtoko);
        parcel.writeString(image);
        parcel.writeString(keterangan);
        parcel.writeString(idtipe);
        parcel.writeString(aset);
    }
}
