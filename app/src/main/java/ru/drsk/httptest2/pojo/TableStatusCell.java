package ru.drsk.httptest2.pojo;

/**
 * Created by sergei on 11.03.2016.
 */
public class TableStatusCell {

    private String number;
    private String date;
    private String nameObj;
    private String address;
    private String type;
    private String power;
    private String voltage;
    private String comment;
    private String status;
    private boolean hasButton;
    private String flagUserFile;
    private String idZaiv;
    private boolean hideText = true;
    public TableStatusCell(String date, String nameObj, String address, String type,
                           String power, String voltage, String comment, String status,
                           boolean hasButton, String flagUserFile, String idZaiv) {
        //this.number = number;
        this.date = date;
        this.nameObj = nameObj;
        this.address = address;
        this.type = type;
        this.power = power;
        this.voltage = voltage;
        this.comment = comment;
        this.status = status;
        this.hasButton = hasButton;
        this.flagUserFile = flagUserFile;
        this.idZaiv = idZaiv;
    }

    public TableStatusCell(String date, String nameObj, String address, String type, String power,
                           String voltage, String comment, String status, boolean hasButton) {
        this.date = date;
        this.nameObj = nameObj;
        this.address = address;
        this.type = type;
        this.power = power;
        this.voltage = voltage;
        this.comment = comment;
        this.status = status;
        this.hasButton = hasButton;
    }

    public boolean isHideText() {
        return hideText;
    }

    public void setHideText(boolean hideText) {
        this.hideText = hideText;
    }

    public TableStatusCell(String number, String date, String nameObj, String address, String type, String power,
                           String voltage, String comment, String status) {
        this.number = number;
        this.date = date;
        this.nameObj = nameObj;
        this.address = address;
        this.type = type;
        this.power = power;
        this.voltage = voltage;
        this.comment = comment;
        this.status = status;
    }

    public boolean isHasButton() {
        return hasButton;
    }

    public void setHasButton(boolean hasButton) {
        this.hasButton = hasButton;
    }

    public String getFlagUserFile() {
        return flagUserFile;
    }

    public void setFlagUserFile(String flagUserFile) {
        this.flagUserFile = flagUserFile;
    }

    public String getIdZaiv() {
        return idZaiv;
    }

    public void setIdZaiv(String idZaiv) {
        this.idZaiv = idZaiv;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNameObj() {
        return nameObj;
    }

    public void setNameObj(String nameObj) {
        this.nameObj = nameObj;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
