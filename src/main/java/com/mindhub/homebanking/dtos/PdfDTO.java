package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDateTime;

public class PdfDTO {
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String account;

    public PdfDTO() {
    }

    public PdfDTO(LocalDateTime fromDate, LocalDateTime toDate, String account) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.account = account;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
