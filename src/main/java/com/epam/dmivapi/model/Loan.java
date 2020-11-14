package com.epam.dmivapi.model;

import java.time.LocalDate;

public class Loan extends Entity {
    private Integer userId;
    private Integer bookCopyId;
    private LocalDate dateOut;
    private LocalDate dueDate;
    private LocalDate dateIn;
    private boolean readingRoom;

    private String libCode; // code for in-library referencing
    private String bookTitle;
    private String bookAuthors;
    private String bookGenre;
    private String bookPublisher;
    private int bookPublicationYear;
    private int price;

    private String email; // login
    private String firstName;
    private String lastName;
    private boolean blocked;

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

    public boolean isReadingRoom() {
        return readingRoom;
    }

    public void setReadingRoom(boolean readingRoom) {
        this.readingRoom = readingRoom;
    }

    public String getLibCode() {
        return libCode;
    }

    public void setLibCode(String libCode) {
        this.libCode = libCode;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthors() {
        return bookAuthors;
    }

    public void setBookAuthors(String bookAuthors) {
        this.bookAuthors = bookAuthors;
    }

    public String getBookGenre() {
        return bookGenre;
    }

    public void setBookGenre(String bookGenre) {
        this.bookGenre = bookGenre;
    }

    public String getBookPublisher() {
        return bookPublisher;
    }

    public void setBookPublisher(String bookPublisher) {
        this.bookPublisher = bookPublisher;
    }

    public int getBookPublicationYear() {
        return bookPublicationYear;
    }

    public void setBookPublicationYear(int bookPublicationYear) {
        this.bookPublicationYear = bookPublicationYear;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
