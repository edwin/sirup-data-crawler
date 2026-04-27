package com.edw.model;

/**
 * <pre>
 *  com.edw.model.TblSatker
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 27 Apr 2026 15:45
 */

public class TblSatker {

    private String idSatker;
    private String namaSatker;
    private String idKlpd;

    public TblSatker() {
    }

    public TblSatker(String idSatker, String namaSatker, String idKlpd) {
        this.idSatker = idSatker;
        this.namaSatker = namaSatker;
        this.idKlpd = idKlpd;
    }

    public String getIdSatker() {
        return idSatker;
    }

    public void setIdSatker(String idSatker) {
        this.idSatker = idSatker;
    }

    public String getNamaSatker() {
        return namaSatker;
    }

    public void setNamaSatker(String namaSatker) {
        this.namaSatker = namaSatker;
    }

    public String getIdKlpd() {
        return idKlpd;
    }

    public void setIdKlpd(String idKlpd) {
        this.idKlpd = idKlpd;
    }

    @Override
    public String toString() {
        return "TblSatker{" +
                "idSatker='" + idSatker + '\'' +
                ", namaSatker='" + namaSatker + '\'' +
                ", idKlpd='" + idKlpd + '\'' +
                '}';
    }
}
