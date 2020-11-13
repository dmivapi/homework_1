package com.epam.dmivapi.entity;

import java.time.LocalDate;

public class Loan extends Entity {
    private Integer userId;
    private Integer bookCopyId;
    private LocalDate dateOut;
    private LocalDate dueDate;
    private LocalDate dateIn;
    private boolean readingRoom;

    public enum LoanStatus {NEW, OUT, OVERDUE, RETURNED}

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBookCopyId() {
        return bookCopyId;
    }

    public void setBookCopyId(Integer bookCopyId) {
        this.bookCopyId = bookCopyId;
    }

    public LocalDate getDateOut() {
        return dateOut;
    }

    public void setDateOut(LocalDate dateOut) {
        this.dateOut = dateOut;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getDateIn() {
        return dateIn;
    }

    public void setDateIn(LocalDate dateIn) {
        this.dateIn = dateIn;
    }

    public boolean isReadingRoom() {  return readingRoom; }

    public void setReadingRoom(boolean readingRoom) { this.readingRoom = readingRoom;  }

    public LoanStatus getStatus() {
        if (dateOut == null)
            return LoanStatus.NEW;

        if (dateIn != null)
            return LoanStatus.RETURNED;

        if (LocalDate.now().isAfter(dueDate))
            return LoanStatus.OVERDUE;

        return LoanStatus.OUT;
    }
}
