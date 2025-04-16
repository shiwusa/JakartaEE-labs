package com.auction.services;

import jakarta.ejb.Stateless;
import jakarta.ejb.LocalBean;
import java.util.Map;
import java.util.List;
import com.auction.util.DataStorage;
import com.auction.model.User;

@Stateless
@LocalBean
public class UserService {
    public User authenticate(String username, String password) {
        return DataStorage.getAllUsers().values().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public User getUser(String id) {
        return DataStorage.getUser(id);
    }
    
    public Map<String, User> getAllUsers() {
        return DataStorage.getAllUsers();
    }
    
    public String getFirstUserId() {
        return DataStorage.getFirstUserId();
    }
    
    public void addUser(User user) {
        DataStorage.addUser(user);
    }
    
    public void addOwnedLot(String userId, String lotId) {
        User user = DataStorage.getUser(userId);
        if (user != null) {
            List<String> ownedLots = user.getOwnedLotIds();
            if (!ownedLots.contains(lotId)) {
                ownedLots.add(lotId);
            }
        }
    }
    
    public void addBidLot(String userId, String lotId) {
        User user = DataStorage.getUser(userId);
        if (user != null) {
            List<String> bidLots = user.getBidLotIds();
            if (!bidLots.contains(lotId)) {
                bidLots.add(lotId);
            }
        }
    }
    
    public boolean isLotOwner(String userId, String lotId) {
        User user = DataStorage.getUser(userId);
        return user != null && user.getOwnedLotIds().contains(lotId);
    }
    
    public boolean hasBidOnLot(String userId, String lotId) {
        User user = DataStorage.getUser(userId);
        return user != null && user.getBidLotIds().contains(lotId);
    }
}