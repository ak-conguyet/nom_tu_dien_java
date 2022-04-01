package com.kiettc.nomtudien;

public class HanNom {
    private String hanNom;
    private String amViet;
    private String mean;
    private String uniCode;
    private String amVKhac;
    private String cachVietK;

    public HanNom(String hanNom, String amViet, String mean,String uniCode,String amVKhac, String cachVietK){
        this.hanNom=hanNom;
        this.amViet=amViet;
        this.mean=mean;
        this.uniCode=uniCode;
        this.amVKhac=amVKhac;
        this.cachVietK=cachVietK;
    }

    public HanNom(String hanNom, String amViet, String mean,String uniCode,String amVKhac){
        this.hanNom=hanNom;
        this.amViet=amViet;
        this.mean=mean;
        this.uniCode=uniCode;
        this.amVKhac=amVKhac;
    }

    public String getHanNom(){return hanNom;}

    public String getAmViet(){return amViet;}

    public String getMean(){return mean;}

    public String getUniCode(){return uniCode;}

    public String getAmVKhac(){return amVKhac;}

    public String getCachVietK(){return cachVietK;}

}
