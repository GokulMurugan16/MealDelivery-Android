package com.example.myapplicationadmin;

public class pojo {

    String mImage;
    String mName;
    String mP1ame;
    String mP6ame;
    String mDesc;
    String mAllergy;
    String id;

    public pojo(String mImage, String mName, String mP1ame, String mP6ame, String mDesc, String mAllergy, String id) {
        this.mImage = mImage;
        this.mName = mName;
        this.mP1ame = mP1ame;
        this.mP6ame = mP6ame;
        this.mDesc = mDesc;
        this.mAllergy = mAllergy;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmP1ame() {
        return mP1ame;
    }

    public void setmP1ame(String mP1ame) {
        this.mP1ame = mP1ame;
    }

    public String getmP6ame() {
        return mP6ame;
    }

    public void setmP6ame(String mP6ame) {
        this.mP6ame = mP6ame;
    }

    public String getmDesc() {
        return mDesc;
    }

    public void setmDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getmAllergy() {
        return mAllergy;
    }

    public void setmAllergy(String mAllergy) {
        this.mAllergy = mAllergy;
    }
}
