package com.example.nationalhealth.models;

public class MRZCapturedResultsModel {

    private String surname;
    private String givenName;
    private String nationality;
    private String sex;
    private String dateOfBirth;
    private String documentNumber;
    private String expirationDate;

    public MRZCapturedResultsModel() {
    }

    public MRZCapturedResultsModel(String surname, String givenName, String nationality, String sex,
                                   String dateOfBirth, String documentNumber, String expirationDate) {
        this.surname = surname;
        this.givenName = givenName;
        this.nationality = nationality;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.documentNumber = documentNumber;
        this.expirationDate = expirationDate;
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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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
}
