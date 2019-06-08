package com.liurui.bookshelf;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetString {
    private String name="";
    private String ISBN="";
    private String Author="";
    private String Publisher="";
    private String PubTime="";
    private String ImgPath="";

    public GetString(String information)
    {
        Pattern PatternName = Pattern.compile("\"title\":(.*)\"price\"");
        Matcher matcher = PatternName.matcher(information);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            buffer.append(matcher.group());
            name=buffer.toString().substring(9,buffer.toString().length()-9);
        }

        Pattern PatternISBN = Pattern.compile("\"isbn\":(.*)\"binding\"");
        matcher = PatternISBN.matcher(information);
        buffer = new StringBuffer();
        while (matcher.find()) {
            buffer.append(matcher.group());
            ISBN=buffer.toString().substring(8,buffer.toString().length()-11);
        }

        Pattern PatternAuthor = Pattern.compile("\"author\":(.*)\"title\"");
        matcher = PatternAuthor.matcher(information);
        buffer = new StringBuffer();
        while (matcher.find()) {
            buffer.append(matcher.group());
            Author=buffer.toString().substring(10,buffer.toString().length()-9);
        }

        Pattern PatternPublisher = Pattern.compile("\"publisher\":(.*)\"author\"");
        matcher = PatternPublisher.matcher(information);
        buffer = new StringBuffer();
        while (matcher.find()) {
            buffer.append(matcher.group());
            Publisher=buffer.toString().substring(13,buffer.toString().length()-10);
        }

        Pattern PatternTime = Pattern.compile("(\\d{4}-\\d{1,2})");
        matcher = PatternTime.matcher(information);
        buffer = new StringBuffer();
        while (matcher.find()) {
            buffer.append(matcher.group());
            PubTime=buffer.toString();
        }

        Pattern PatternImg = Pattern.compile("\"img\":(.*)\"gist\"");
        matcher = PatternImg.matcher(information);
        buffer = new StringBuffer();
        while (matcher.find()) {
            buffer.append(matcher.group());
            ImgPath=buffer.toString().substring(7,buffer.toString().length()-8);
        }
    }

    public String getName()
    {
        return name;
    }

    public String getISBN()
    {
        return ISBN;
    }

    public String getAuthor()
    {
        return Author;
    }

    public String getPublisher() {return Publisher;}

    public String getPubTime() {
        return PubTime;
    }

    public String getImgPath() {
        return ImgPath;
    }
}
