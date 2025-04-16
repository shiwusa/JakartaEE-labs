package com.auction.controller;

import com.auction.model.Lot;
import com.auction.services.BidService;
import com.auction.services.LotService;
import com.auction.services.UserService;

import com.auction.util.HtmlUtils;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "LotServlet", urlPatterns = {"/lot"})
public class LotServlet extends HttpServlet {
    @EJB
    private LotService lotService;
    
    @EJB
    private UserService userService;
    
    @EJB
    private BidService bidService;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        if (action == null) {
            showLotDetails(request, response);
        } else {
            switch (action) {
                case "create":
                    showCreateLotForm(request, response);
                    break;
                case "delete":
                    deleteLot(request, response);
                    break;
                case "start":
                    startAuction(request, response);
                    break;
                case "stop":
                    stopAuction(request, response);
                    break;
                default:
                    showLotDetails(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            createLot(request, response);
        } else if ("bid".equals(action)) {
            placeBid(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/lots");
        }
    }

    private void showLotDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String currentUserId = session.getAttribute("userId").toString();
        String lotId = request.getParameter("id");
        if (lotId != null) {
            Lot lot = lotService.getLot(lotId);
            if (lot != null) {
                request.setAttribute("lot", lot);
                request.setAttribute("userId", currentUserId);
                request.getRequestDispatcher("/jsp/lot-details.jsp").forward(request, response);
                return;
            }
        }
        response.sendRedirect(request.getContextPath() + "/lots");
    }

    private void showCreateLotForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("userId", userService.getFirstUserId());
        request.getRequestDispatcher("/jsp/create-lot.jsp").forward(request, response);
    }

    private void createLot(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get parameters using utility method to prevent XSS
        String title = HtmlUtils.getParameter(request, "title");
        String description = HtmlUtils.getSanitizedParameter(request, "description");
        String priceStr = request.getParameter("startPrice");
        String userId = request.getParameter("userId");

        try {
            BigDecimal startPrice = new BigDecimal(priceStr);
            Lot newLot = lotService.createLot(title, description, startPrice, userId);
            response.sendRedirect(request.getContextPath() + "/lot?id=" + newLot.getId());
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid price format");
            request.getRequestDispatcher("/jsp/create-lot.jsp").forward(request, response);
        }
    }

    private void deleteLot(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String lotId = request.getParameter("id");
        String userId = request.getParameter("userId");

        lotService.deleteLot(lotId, userId);
        response.sendRedirect(request.getContextPath() + "/lots");
    }

    private void startAuction(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String lotId = request.getParameter("id");
        String userId = request.getParameter("userId");

        lotService.startAuction(lotId, userId);
        response.sendRedirect(request.getContextPath() + "/lot?id=" + lotId);
    }

    private void stopAuction(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String lotId = request.getParameter("id");
        String userId = request.getParameter("userId");

        lotService.stopAuction(lotId, userId);
        response.sendRedirect(request.getContextPath() + "/lot?id=" + lotId);
    }

    private void placeBid(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String lotId = request.getParameter("id");
        String userId = request.getParameter("userId");
        String bidAmountStr = request.getParameter("bidAmount");

        try {
            if (lotId != null && userId != null && bidAmountStr != null) {
                BigDecimal bidAmount = new BigDecimal(bidAmountStr);
                boolean success = bidService.placeBid(userId, lotId, bidAmount);
                if (!success) {
                    request.setAttribute("error", "Bid amount must be higher than current price");
                }
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid bid amount");
        }

        response.sendRedirect(request.getContextPath() + "/lot?id=" + lotId);
    }
}