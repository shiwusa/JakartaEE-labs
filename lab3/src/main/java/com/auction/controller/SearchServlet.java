package com.auction.controller;

import com.auction.model.Lot;
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
import java.util.List;

@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {

    @EJB
    private LotService lotService;
    
    @EJB
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String keyword = HtmlUtils.getParameter(request, "keyword");

        if (keyword != null && !keyword.trim().isEmpty()) {
            List<Lot> searchResults = lotService.searchLotsByKeyword(keyword);
            request.setAttribute("lots", searchResults);
            request.setAttribute("keyword", keyword);
            request.setAttribute("title", "Search Results for: " + keyword);
        } else {
            request.setAttribute("title", "Search");
        }

        request.setAttribute("userId", userService.getFirstUserId());
        request.getRequestDispatcher("/jsp/search.jsp").forward(request, response);
    }
}