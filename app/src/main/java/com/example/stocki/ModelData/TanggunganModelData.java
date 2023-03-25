package com.example.stocki.ModelData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TanggunganModelData {
    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("idadmin")
    @Expose
    String idadmin;
    @SerializedName("idtoko")
    @Expose
    String idtoko;
    @SerializedName("namatoko")
    @Expose
    String namatoko;
    @SerializedName("tanggal")
    @Expose
    String tanggal;
    @SerializedName("tanggungan")
    @Expose
    String tanggungan;
    @SerializedName("keterangan")
    @Expose
    String keterangan;

    // Getter Methods

    public String getId() {
        return id;
    }

    public String getIdadmin() {
        return idadmin;
    }

    public String getIdtoko() {
        return idtoko;
    }

    public String getNamatoko() {
        return namatoko;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getTanggungan() {
        return tanggungan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    // Setter Methods

    public void setId(String id) {
        this.id = id;
    }

    public void setIdadmin(String idadmin) {
        this.idadmin = idadmin;
    }

    public void setIdtoko(String idtoko) {
        this.idtoko = idtoko;
    }

    public void setNamatoko(String namatoko) {
        this.namatoko = namatoko;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setTanggungan(String tanggungan) {
        this.tanggungan = tanggungan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}