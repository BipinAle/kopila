package com.mobile.kopila.pojo;

import org.parceler.Parcel;

@Parcel
public class PhoneDiary {
    public PhoneDiary() {
    }

    private String phoneNumber, personName, wardName, wardNumber, photoUrl;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPersonName() {
        return personName;
    }

    public String getWardName() {
        return wardName;
    }

    public String getWardNumber() {
        return wardNumber;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public PhoneDiary(String personName, String phoneNumber, String wardNumber, String wardName, String photoUrl) {
        this.photoUrl = photoUrl;
        this.phoneNumber = phoneNumber;
        this.personName = personName;
        this.wardName = wardName;
        this.wardNumber = wardNumber;
    }

}
