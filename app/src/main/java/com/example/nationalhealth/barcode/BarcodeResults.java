package com.example.nationalhealth.barcode;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "misho_barcode_userDetails",indices = {@Index(value = {"nin_number"},unique = true)})
public class BarcodeResults {
    @PrimaryKey(autoGenerate = true)
    private int barcodeUserID;

    @ColumnInfo(name = "surname")
    @NonNull
    private String surname;

    @ColumnInfo(name = "given_name")
    @NonNull
    private String givenName;

    @ColumnInfo(name = "date_of_birth")
    @NonNull
    private String dateOfBirth;

    @ColumnInfo(name = "sex")
    @NonNull
    private String sex;

    @ColumnInfo(name = "nin_number")
    @NonNull
    private String nin;

    @ColumnInfo(name = "card_number")
    @NonNull
    private String cardNumber;

    @ColumnInfo(name = "expiration_date")
    @NonNull
    private String expirationDate;

    @ColumnInfo(name = "phone_number")
    @NonNull
    private String phoneNumber;

    public BarcodeResults(@NonNull String surname, @NonNull String givenName, @NonNull String dateOfBirth,
                          @NonNull String sex, @NonNull String nin, @NonNull String cardNumber,
                          @NonNull String expirationDate, @NonNull String phoneNumber) {
        this.surname = surname;
        this.givenName = givenName;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.nin = nin;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.phoneNumber = phoneNumber;
    }

    public int getBarcodeUserID() {
        return barcodeUserID;
    }

    public void setBarcodeUserID(int barcodeUserID) {
        this.barcodeUserID = barcodeUserID;
    }

    @NonNull
    public String getSurname() {
        return surname;
    }

    public void setSurname(@NonNull String surname) {
        this.surname = surname;
    }

    @NonNull
    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(@NonNull String givenName) {
        this.givenName = givenName;
    }

    @NonNull
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(@NonNull String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @NonNull
    public String getSex() {
        return sex;
    }

    public void setSex(@NonNull String sex) {
        this.sex = sex;
    }

    @NonNull
    public String getNin() {
        return nin;
    }

    public void setNin(@NonNull String nin) {
        this.nin = nin;
    }

    @NonNull
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(@NonNull String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @NonNull
    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(@NonNull String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @NonNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NonNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
