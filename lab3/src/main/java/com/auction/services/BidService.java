package com.auction.services;

import com.auction.model.Bid;
import com.auction.model.Lot;
import com.auction.model.User;
import com.auction.util.DataStorage;

import jakarta.ejb.Stateless;
import jakarta.ejb.LocalBean;
import jakarta.ejb.EJB;
import java.math.BigDecimal;
import java.util.List;

@Stateless
@LocalBean
public class BidService {
    
    @EJB
    private LotService lotService;
    
    public boolean placeBid(String userId, String lotId, BigDecimal amount) {
        Lot lot = DataStorage.getLot(lotId);
        if (lot == null || !lot.isActive()) {
            return false;
        }
        if (lot.getOwnerId().equals(userId)) {
            return false; 
        }
        
        Bid bid = new Bid(userId, lotId, amount);
        boolean success = lotService.addBidToLot(lot, bid);
  
        if (success) {
            addLotToUserBidLots(userId, lotId);
        }
        return success;
    }
    
    public BigDecimal getMinimumBidAmount(String lotId) {
        Lot lot = DataStorage.getLot(lotId);
        if (lot != null) {
            return lot.getCurrentPrice().add(BigDecimal.ONE);
        }
        return BigDecimal.ZERO;
    }
    
    public Bid getHighestBid(String lotId) {
        Lot lot = DataStorage.getLot(lotId);
        if (lot != null && !lot.getBids().isEmpty()) {
            List<Bid> bids = lot.getBids();
            return bids.get(bids.size() - 1);
        }
        return null;
    }
    
    public boolean canUserBid(String userId, String lotId) {
        Lot lot = DataStorage.getLot(lotId);
        if (lot == null) {
            return false;
        }
        
        if (lot.getOwnerId().equals(userId)) {
            return false;
        }
        
        if (!lot.isActive()) {
            return false;
        }
        
        return true;
    }
    
    private void addLotToUserBidLots(String userId, String lotId) {
        User user = DataStorage.getUser(userId);
        if (user != null) {
            List<String> bidLots = user.getBidLotIds();
            if (!bidLots.contains(lotId)) {
                bidLots.add(lotId);
            }
        }
    }
}