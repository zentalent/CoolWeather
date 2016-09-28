package com.android.extremez.coolweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.android.extremez.coolweather.model.City;
import com.android.extremez.coolweather.model.CoolWeatherDB;
import com.android.extremez.coolweather.model.County;
import com.android.extremez.coolweather.model.Province;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

/**
 * Created by tingzong on 2016/9/28.
 */
public class Utility {

    public synchronized static boolean handleProvinceResponse(CoolWeatherDB coolWeatherDB,String response){
        if(!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if(allProvinces != null && allProvinces.length >0){
                for(String p : allProvinces){
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,
                                               String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0) {
                for (String c : allCities) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
// 将解析出来的数据存储到City表
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,
                                                 String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCounties = response.split(",");
            if (allCounties != null && allCounties.length > 0) {
                for (String c : allCounties) {
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
// 将解析出来的数据存储到County表
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleProvinceResponsByXML(CoolWeatherDB coolWeatherDB,String response){
        Province province = new Province();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(response));
            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
// 开始解析某个结点
                    case XmlPullParser.START_TAG: {
                        if ("quName".equals(nodeName)) {
                            province.setProvinceName(xmlPullParser.nextText());
                            Log.d("XML","getProvinceName");
                        }else if("pyName".equals(nodeName)){
                            province.setProvinceCode(xmlPullParser.nextText());
                            Log.d("XML", "getProvinceCode");
                        }
                    }
                    // 完成解析某个结点
                    case XmlPullParser.END_TAG:
                        Log.d("XML", "savedProvince");
                        coolWeatherDB.saveProvince(province);
                        break;
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized static boolean handleCitiesResponseByXML(CoolWeatherDB coolWeatherDB,String response,int provinceId){
        City city = new City();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(response));
            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
// 开始解析某个结点
                    case XmlPullParser.START_TAG: {
                        if ("cityname".equals(nodeName)) {
                            city.setCityName(xmlPullParser.nextText());
                            Log.d("XML","GetCityName");
                        }else if("pyName".equals(nodeName)){
                            city.setCityCode(xmlPullParser.nextText());
                            Log.d("XML", "getProvinceCode");
                        }
                    }
                    // 完成解析某个结点
                    case XmlPullParser.END_TAG:
                        Log.d("XML", "savedProvince");
                        coolWeatherDB.saveCity(city);
                        break;

                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized static boolean handleCountiesResponseByXML(CoolWeatherDB coolWeatherDB,String response,int provinceId){
        County county = new County();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(response));
            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
// 开始解析某个结点
                    case XmlPullParser.START_TAG: {
                        if ("cityname".equals(nodeName)) {
                            county.setCountyName(xmlPullParser.nextText());
                            Log.d("XML","GetCountyName");
                        }else if("pyName".equals(nodeName)){
                            county.setCountyCode(xmlPullParser.nextText());
                            Log.d("XML", "getCountyCode");
                        }
                    }
                    // 完成解析某个结点
                    case XmlPullParser.END_TAG:
                        Log.d("XML", "savedProvince");
                        coolWeatherDB.saveCounty(county);
                        break;
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

