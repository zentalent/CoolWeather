package com.android.extremez.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.android.extremez.coolweather.model.City;
import com.android.extremez.coolweather.model.CoolWeatherDB;
import com.android.extremez.coolweather.model.County;
import com.android.extremez.coolweather.model.Province;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tingzong on 2016/9/28.
 */
public class Utility {

    public synchronized static boolean handleProvinceResponse(CoolWeatherDB coolWeatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length > 0) {
                for (String p : allProvinces) {
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

    public synchronized static boolean handleProvinceResponsByXML(CoolWeatherDB coolWeatherDB, String response) {
        Province province = new Province();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(response));
            Log.d("rp", "response " + response);
            Log.d("rp", "response to string " + response.toString());
            int eventType = xmlPullParser.getEventType();
            Log.d("XML", "EventType is " + eventType);
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                String attrValueOfName = xmlPullParser.getAttributeValue(null, "quName");
                String attrValueOfCode = xmlPullParser.getAttributeValue(null, "pyName");
                Log.d("ST", "attr0 is " + attrValueOfName + " arrt1 is " + attrValueOfCode);
                switch (eventType) {

                    case XmlPullParser.START_TAG: {
                        Log.d("XML", "EventType is in Start_TAG " + eventType + "node name in ST: " + nodeName);
                        if ("city".equals(nodeName)) {
                            Log.d("ST", "attr0 is " + attrValueOfName + " arrt1 is " + attrValueOfCode);
                            province.setProvinceName(attrValueOfName);
                            province.setProvinceCode(attrValueOfCode);
                        }
                        break;
                    }
                    // 完成解析某个结点
                    case XmlPullParser.END_TAG:
                        if ("city".equals(nodeName)) {
                            Log.d("ET", "Province db was saved" + " " + province.getProvinceName() + " " + province.getProvinceCode());
                            coolWeatherDB.saveProvince(province);
                            break;
                        } else break;
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

    public synchronized static boolean handleCitiesResponseByXML(CoolWeatherDB coolWeatherDB, String response, int provinceId) {
        City city = new City();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(response));
            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                String attrValueOfName = xmlPullParser.getAttributeValue(null, "cityname");
                String attrValueOfCode = xmlPullParser.getAttributeValue(null, "pyName");
                switch (eventType) {
// 开始解析某个结点
                    case XmlPullParser.START_TAG: {
                        if ("city".equals(nodeName)) {
                            city.setCityName(attrValueOfName);
                            city.setCityCode(attrValueOfCode);
                            city.setProvinceId(provinceId);
                        }
                        break;
                    }
                    // 完成解析某个结点
                    case XmlPullParser.END_TAG:
                        if ("city".equals(nodeName)) {
                            Log.d("ET_city", "cities were saved " + city.getCityName() + " " + city.getCityCode());
                            coolWeatherDB.saveCity(city);
                            break;
                        } else break;

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

    public synchronized static boolean handleCountiesResponseByXML(CoolWeatherDB coolWeatherDB, String response, int cityId) {
        County county = new County();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(response));
            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                String attrValueOfName = xmlPullParser.getAttributeValue(null, "cityname");
                String attrValueOfCode = xmlPullParser.getAttributeValue(null, "pyName");
                switch (eventType) {
// 开始解析某个结点
                    case XmlPullParser.START_TAG: {
                        if ("city".equals(nodeName)) {
                            county.setCountyName(attrValueOfName);
                            county.setCountyCode(attrValueOfCode);
                            county.setCityId(cityId);
                        }
                        break;
                    }
                    // 完成解析某个结点
                    case XmlPullParser.END_TAG:
                        if ("city".equals(nodeName)) {
                            coolWeatherDB.saveCounty(county);
                            break;
                        } else break;
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


    public  static void handleWeatherResponse(Context context, String response) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(response));
            int eventType = xmlPullParser.getEventType();
            while("city".equals(XmlPullParser.END_TAG)){
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if("city".equals(xmlPullParser.getName())){
                            String temp1 = xmlPullParser.getAttributeValue(null, "tem1");
                            String temp2 = xmlPullParser.getAttributeValue(null, "tem2");
                            String updateTime = xmlPullParser.getAttributeValue(null, "time");
                            String weather = xmlPullParser.getAttributeValue(null, "stateDetailed");
                            String cityName = xmlPullParser.getAttributeValue(null, "cityname");
                            String cityCode = xmlPullParser.getAttributeValue(null, "pyName");
                            Log.d("Utility_weather", "temp1 temp2 weather " + temp1 + " " + temp2 + " " + weather);
                        }break;




            saveWeatherInfo(context, cityName, temp1, temp2, updateTime, weather,cityCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveWeatherInfo (Context context,String cityName,String temp1,String temp2 ,String updateTime,String weather ,String cityCode){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", cityCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weather);
        editor.putString("publish_time",updateTime);
        editor.putString("current_time",simpleDateFormat.format(new Date()));
        editor.commit();
    }

}

