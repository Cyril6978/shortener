package com.shortener.shortener.entity;

public class LogErrMessage {

    public String method;
    public String pathHttp;
    public String adressIp;
    public String typeOfError;
    public String messageError;
    public String fileSrc;
    public Integer line;

    public LogErrMessage() {
    }

    public LogErrMessage(String method, String pathHttp, String adressIp, String typeOfError, String messageError, String fileSrc, Integer line) {
        this.method = method;
        this.pathHttp = pathHttp;
        this.adressIp = adressIp;
        this.typeOfError = typeOfError;
        this.messageError = messageError;
        this.fileSrc = fileSrc;
        this.line = line;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPathHttp() {
        return pathHttp;
    }

    public void setPathHttp(String pathHttp) {
        this.pathHttp = pathHttp;
    }

    public String getAdressIp() {
        return adressIp;
    }

    public void setAdressIp(String adressIp) {
        this.adressIp = adressIp;
    }

    public String getTypeOfError() {
        return typeOfError;
    }

    public void setTypeOfError(String typeOfError) {
        this.typeOfError = typeOfError;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public String getFileSrc() {
        return fileSrc;
    }

    public void setFileSrc(String fileSrc) {
        this.fileSrc = fileSrc;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return this.getMethod() + " " + this.getPathHttp() + " from " + this.getAdressIp() + ", "
                + this.getTypeOfError() + ":" + this.getMessageError() + " (" + this.getFileSrc() + " => " + this.getLine() + ")";

    }
}
