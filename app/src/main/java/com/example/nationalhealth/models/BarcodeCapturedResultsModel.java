package com.example.nationalhealth.models;

import java.io.Serializable;

public class BarcodeCapturedResultsModel implements Serializable {
    private String surname;
    private String givenName;
    private String dateOfBirth;
    private String sex;
    private String nin;
    private String documentNumber;
    private String expirationDate;
    private String phoneNumber;


    public BarcodeCapturedResultsModel() {
    }

    public BarcodeCapturedResultsModel(String surname, String givenName,String dateOfBirth, String sex,
                                       String nin, String documentNumber, String expirationDate, String phoneNumber) {
        this.surname = surname;
        this.givenName = givenName;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.nin = nin;
        this.documentNumber = documentNumber;
        this.expirationDate = expirationDate;
        this.phoneNumber = phoneNumber;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @Override
    public String toString() {
        return "BarcodeCapturedResultsModel{" +
                "surname='" + surname + '\'' +
                ", givenName='" + givenName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", sex='" + sex + '\'' +
                ", nin='" + nin + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
