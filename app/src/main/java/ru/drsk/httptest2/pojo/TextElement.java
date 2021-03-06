package ru.drsk.httptest2.pojo;

/**
 * Created by sergei on 04.02.2016.
 */
public class TextElement {
    private String text;
    private boolean hasButton;
    private String flagUserFile;
    private String idZaiv;

    public TextElement(String text, boolean hasButton) {
        this.text = text;
        this.hasButton = hasButton;
    }

    public TextElement(String text, boolean hasButton, String flagUserFile, String idZaiv) {
        this.text = text;
        this.hasButton = hasButton;
        this.flagUserFile = flagUserFile;
        this.idZaiv = idZaiv;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getIdZaiv() {
        return idZaiv;
    }
}
