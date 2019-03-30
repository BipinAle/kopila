package com.mobile.kopila.pojo;

public class ToleData {
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

    public ToleData(String personName, String phoneNumber, String wardNumber, String wardName, String photoUrl) {
        this.photoUrl = photoUrl;
        this.phoneNumber = phoneNumber;
        this.personName = personName;
        this.wardName = wardName;
        this.wardNumber = wardNumber;
    }
}
