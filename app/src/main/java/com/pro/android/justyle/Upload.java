package com.pro.android.justyle;

public class Upload{
private String mName;
private String mImageUrl;
private String mDescription;


        /**
         * empty constructor needed
         */
public Upload(){

        }


public Upload (String name, String imageUrl, String description) {

        if (name.trim().equals("")){
        name = "No name";
        }

        if (description.trim().equals("")){
                description = "No description";
        }

        mName = name;
        mImageUrl = imageUrl;
        mDescription = description;


        }
public String getName(){
        return mName;
        }

public void setName (String name){
        mName = name;
        }

public String getImageUrl(){
        return mImageUrl;
        }

public void setImageUrl (String imageUrl){
        mImageUrl = imageUrl;
        }

        public String getDescription(){
                return mDescription;
        }

        public void setDescription (String description){
                mDescription = description;
        }
        }