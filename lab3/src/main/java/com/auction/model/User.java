package com.auction.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private final String id;
    private String username;
    private String email;
    private String password;
    private List<String> ownedLotIds;
    private List<String> bidLotIds;

    public User(String username, String email, String password) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.password = password;
        this.ownedLotIds = new ArrayList<>();
        this.bidLotIds = new ArrayList<>();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getOwnedLotIds() {
        return ownedLotIds;
    }

    public List<String> getBidLotIds() {
        return bidLotIds;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOwnedLotIds(List<String> ownedLotIds) {
        this.ownedLotIds = ownedLotIds;
    }

    public void setBidLotIds(List<String> bidLotIds) {
        this.bidLotIds = bidLotIds;
    }
}