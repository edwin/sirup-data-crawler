package com.edw.model;

/**
 * <pre>
 *  com.edw.model.TblKlpd
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 27 Apr 2026 11:59
 */
public class TblKlpd {

    private String idKlpd;
    private String namaKlpd;
    private String jenisKlpd;

    public TblKlpd() {
    }

    public TblKlpd(String idKlpd, String namaKlpd) {
        this.idKlpd = idKlpd;
        this.namaKlpd = namaKlpd;
    }

    public TblKlpd(String idKlpd, String namaKlpd, String jenisKlpd) {
        this.idKlpd = idKlpd;
        this.namaKlpd = namaKlpd;
        this.jenisKlpd = jenisKlpd;
    }

    public String getIdKlpd() {
        return idKlpd;
    }

    public void setIdKlpd(String idKlpd) {
        this.idKlpd = idKlpd;
    }

    public String getNamaKlpd() {
        return namaKlpd;
    }

    public void setNamaKlpd(String namaKlpd) {
        this.namaKlpd = namaKlpd;
    }

    public String getJenisKlpd() {
        return jenisKlpd;
    }

    public void setJenisKlpd(String jenisKlpd) {
        this.jenisKlpd = jenisKlpd;
    }

    @Override
    public String toString() {
        return "TblKlpd{" +
                "idKlpd='" + idKlpd + '\'' +
                ", namaKlpd='" + namaKlpd + '\'' +
                ", jenisKlpd='" + jenisKlpd + '\'' +
                '}';
    }
}
