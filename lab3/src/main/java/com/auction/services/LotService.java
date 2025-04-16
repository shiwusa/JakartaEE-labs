package com.auction.services;

import com.auction.model.Bid;
import com.auction.model.Lot;
import com.auction.model.User;
import com.auction.util.DataStorage;

import jakarta.ejb.Stateless;
import java.math.BigDecimal;
import jakarta.ejb.LocalBean;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
@LocalBean
public class LotService {
    
    public Lot createLot(String title, String description, BigDecimal startPrice, String userId) {
        Lot newLot = new Lot(title, description, startPrice, userId);
        DataStorage.addLot(newLot);
        addLotToUserOwnedLots(userId, newLot.getId());
        return newLot;
    }
    
    public Lot getLot(String id) {
        return DataStorage.getLot(id);
    }
    
    public List<Lot> getAllLots() {
        return DataStorage.getAllLots();
    }
    
    public List<Lot> getActiveLots() {
        return DataStorage.getActiveLots();
    }
    
    public List<Lot> searchLotsByKeyword(String keyword) {
        return DataStorage.searchLotsByKeyword(keyword);
    }
    
    public List<Lot> getUserLots(String userId) {
        return DataStorage.getUserLots(userId);
    }
    
    public boolean startAuction(String lotId, String userId) {
        Lot lot = DataStorage.getLot(lotId);
        if (lot != null && lot.getOwnerId().equals(userId)) {
            lot.setActive(true);
            lot.setStartedAt(LocalDateTime.now());
            return true;
        }
        return false;
    }
    
    public boolean stopAuction(String lotId, String userId) {
        Lot lot = DataStorage.getLot(lotId);
        if (lot != null && lot.getOwnerId().equals(userId)) {
            lot.setActive(false);
            lot.setEndedAt(LocalDateTime.now());
            return true;
        }
        return false;
    }
    
    public boolean deleteLot(String lotId, String userId) {
        Lot lot = DataStorage.getLot(lotId);
        if (lot != null && lot.getOwnerId().equals(userId)) {
            DataStorage.removeLot(lotId);
            return true;
        }
        return false;
    }
    
    public String generateShareUrl(String lotId) {
        return "/lot?id=" + lotId;
    }
    
    public boolean addBidToLot(Lot lot, Bid bid) {
        if (!lot.isActive()) {
            return false;
        }
        if (bid.getAmount().compareTo(lot.getCurrentPrice()) <= 0) {
            return false;
        }
        lot.getBids().add(bid);
        lot.setCurrentPrice(bid.getAmount());
        return true;
    }
    
    private void addLotToUserOwnedLots(String userId, String lotId) {
        User user = DataStorage.getUser(userId);
        if (user != null) {
            List<String> ownedLots = user.getOwnedLotIds();
            if (!ownedLots.contains(lotId)) {
                ownedLots.add(lotId);
            }
        }
    }
}