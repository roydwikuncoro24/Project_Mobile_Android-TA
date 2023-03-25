package com.example.stocki.ModelResponse;

public class InsertResponse {

    String status_code;
    String status;
    int idtransaksi;

    public InsertResponse(String status_code, String status, int idtransaksi) {
        this.status_code = status_code;
        this.status = status;
        this.idtransaksi = idtransaksi;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIdtransaksi() {
        return idtransaksi;
    }

    public void setIdtransaksi(int idtransaksi) {
        this.idtransaksi = idtransaksi;
    }
}
