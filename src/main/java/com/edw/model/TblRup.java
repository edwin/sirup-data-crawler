package com.edw.model;

import java.math.BigDecimal;

/**
 * <pre>
 *  com.edw.model.TblRup
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 27 Apr 2026 18:59
 */
public class TblRup {

    private String idRup;
    private String namaRup;
    private BigDecimal paguRup;
    private String tahunRup;
    private String idSatker;

    public TblRup() {
    }

    public TblRup(String idRup, String namaRup, BigDecimal paguRup, String tahunRup, String idSatker) {
        this.idRup = idRup;
        this.namaRup = namaRup;
        this.paguRup = paguRup;
        this.tahunRup = tahunRup;
        this.idSatker = idSatker;
    }

    public String getIdRup() {
        return idRup;
    }

    public void setIdRup(String idRup) {
        this.idRup = idRup;
    }

    public String getNamaRup() {
        return namaRup;
    }

    public void setNamaRup(String namaRup) {
        this.namaRup = namaRup;
    }

    public BigDecimal getPaguRup() {
        return paguRup;
    }

    public void setPaguRup(BigDecimal paguRup) {
        this.paguRup = paguRup;
    }

    public String getTahunRup() {
        return tahunRup;
    }

    public void setTahunRup(String tahunRup) {
        this.tahunRup = tahunRup;
    }

    public String getIdSatker() {
        return idSatker;
    }

    public void setIdSatker(String idSatker) {
        this.idSatker = idSatker;
    }

    @Override
    public String toString() {
        return "TblRup{" +
                "idRup='" + idRup + '\'' +
                ", namaRup='" + namaRup + '\'' +
                ", paguRup=" + paguRup +
                ", tahunRup='" + tahunRup + '\'' +
                ", idSatker='" + idSatker + '\'' +
                '}';
    }
}
