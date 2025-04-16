package com.auction.util;

import com.auction.model.Bid;
import com.auction.model.Lot;
import com.auction.model.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

public class DataStorage {
    private static final Map<String, User> users = new HashMap<>();
    private static final Map<String, Lot> lots = new HashMap<>();

    static {
        // Create test users with passwords
        User user1 = new User("qweqweqwe", "qwe@example.com", "qweqweqwe");
        User user2 = new User("peter_parker", "peter@example.com", "spidey123");
        users.put(user1.getId(), user1);
        users.put(user2.getId(), user2);

        // Create test lots
        Lot lot1 = new Lot("Vintage Watch", "Rare vintage watch from 1960s",
                new BigDecimal("500.00"), user1.getId());
        Lot lot2 = new Lot("Antique Table", "Beautiful wooden table from 19th century",
                new BigDecimal("1200.00"), user1.getId());
        Lot lot3 = new Lot("Painting", "Original oil painting of landscape",
                new BigDecimal("800.00"), user2.getId());

        // Activate lots
        lot1.setActive(true);
        lot1.setStartedAt(LocalDateTime.now());
        lot3.setActive(true);
        lot3.setStartedAt(LocalDateTime.now());

        // Add bids
        Bid bid1 = new Bid(user2.getId(), lot1.getId(), new BigDecimal("550.00"));
        lot1.getBids().add(bid1);
        lot1.setCurrentPrice(new BigDecimal("550.00"));

        lots.put(lot1.getId(), lot1);
        lots.put(lot2.getId(), lot2);
        lots.put(lot3.getId(), lot3);

        // Update user's owned lots
        user1.getOwnedLotIds().add(lot1.getId());
        user1.getOwnedLotIds().add(lot2.getId());
        user2.getOwnedLotIds().add(lot3.getId());

        // Update user's bid lots
        user2.getBidLotIds().add(lot1.getId());
    }

    public static User getUser(String id) {
        return users.get(id);
    }

    public static void addUser(User user) {
        users.put(user.getId(), user);
    }

    public static List<Lot> getAllLots() {
        return new ArrayList<>(lots.values());
    }

    public static List<Lot> getActiveLots() {
        return lots.values().stream()
                .filter(Lot::isActive)
                .collect(Collectors.toList());
    }

    public static Lot getLot(String id) {
        return lots.get(id);
    }

    public static void addLot(Lot lot) {
        lots.put(lot.getId(), lot);
    }

    public static void removeLot(String id) {
        lots.remove(id);
    }

    public static List<Lot> searchLotsByKeyword(String keyword) {
        String searchTerm = keyword.toLowerCase();
        return lots.values().stream()
                .filter(lot -> lot.getTitle().toLowerCase().contains(searchTerm) ||
                        lot.getDescription().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    public static List<Lot> getUserLots(String userId) {
        return lots.values().stream()
                .filter(lot -> lot.getOwnerId().equals(userId))
                .collect(Collectors.toList());
    }

    public static String getFirstUserId() {
        if (!users.isEmpty()) {
            return users.values().iterator().next().getId();
        }
        return null;
    }

    public static String getFirstLotId() {
        if (!lots.isEmpty()) {
            return lots.values().iterator().next().getId();
        }
        return null;
    }

    public static Map<String, User> getAllUsers() {
        return users;
    }
}