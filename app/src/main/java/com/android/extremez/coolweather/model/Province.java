package com.android.extremez.coolweather.model;

/**
 * Created by tingzong on 2016/9/28.
 */
public class Province {
    private int id;
    private String provinceName;
    private String provinceCode;

    public int getId(){
        return  id;
    }
    public String getProvinceName(){
        return provinceName;
    }

    public String getProvinceCode(){
        return provinceCode;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setProvinceName(String provinceName){
        this.provinceName = provinceName;
    }

    public void setProvinceCode(String provinceCode){
        this.provinceCode = provinceCode;
    }
}
