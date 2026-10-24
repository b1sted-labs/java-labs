package ru.basted.javalabs.lab7.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import ru.basted.javalabs.lab7.dao.WinningTicketDao;
import ru.basted.javalabs.lab7.dao.WinningTicketDaoImpl;
import ru.basted.javalabs.lab7.model.WinningTicket;
import ru.basted.javalabs.lab7.util.ParseTools;

@WebServlet("/lottery")
public class WinningTicketServlet extends HttpServlet {
    private WinningTicketDao winningTicketDao;

    public void init() {
        winningTicketDao = new WinningTicketDaoImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String errorMessage = (String) session.getAttribute("errorMessage");
        if (errorMessage != null) {
            request.setAttribute("error", errorMessage);
            session.removeAttribute("errorMessage");
        }

        try {
            if ("specificTicket".equals(request.getParameter("action"))) {
                int ticketId = ParseTools.parseInteger(request.getParameter("ticketId"));
                request.setAttribute("specificTicket", winningTicketDao.findByDrawNumber(ticketId));
            } else {
                request.setAttribute("ticketsArray", winningTicketDao.findAll());
            }
        } catch (IllegalArgumentException ex) {
            request.setAttribute("error", ex.getMessage());
        } catch (SQLException ex) {
            request.setAttribute("error", "При операциях с БД произошла ошибка: " + ex.getMessage());
        }

        request.getRequestDispatcher("/WEB-INF/lottery.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding("UTF-8");

        String rawTicketNumber = request.getParameter("ticketNumber");
        String rawPrizeAmount = request.getParameter("prizeAmount");
        String rawDrawDate = request.getParameter("drawDate");

        try {
            WinningTicket winningTicket = new WinningTicket(
                    ParseTools.parseInteger(rawTicketNumber),
                    ParseTools.parseInteger(rawPrizeAmount)
            );

            winningTicket.setDateTime(rawDrawDate.isBlank() ? LocalDateTime.now() : ParseTools.parseDateTime(rawDrawDate));

            winningTicketDao.insertWinningTicket(winningTicket);
        } catch (IllegalArgumentException ex) {
            session.setAttribute("errorMessage", ex.getMessage());
        } catch (SQLException ex) {
            session.setAttribute("errorMessage", "При операциях с БД произошла ошибка: " + ex.getMessage());
        }

        response.sendRedirect("lottery");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding("UTF-8");

        String rawDrawNumber = request.getParameter("drawNumber");
        String rawTicketNumber = request.getParameter("ticketNumber");
        String rawPrizeAmount = request.getParameter("prizeAmount");
        String rawDrawDate = request.getParameter("drawDate");

        try {
            int drawNumber = ParseTools.parseInteger(rawDrawNumber);
            WinningTicket winningTicket = winningTicketDao.findByDrawNumber(drawNumber);

            if (!rawTicketNumber.isBlank()) {
                winningTicket.setTicketNumber(ParseTools.parseInteger(rawTicketNumber));
            }

            if (!rawPrizeAmount.isBlank()) {
                winningTicket.setPrizeAmount(ParseTools.parseInteger(rawPrizeAmount));
            }

            if (!rawDrawDate.isBlank()) {
                winningTicket.setDateTime(ParseTools.parseDateTime(rawDrawDate));
            }

            winningTicketDao.updateWinningTicket(drawNumber, winningTicket);
        } catch (IllegalArgumentException ex) {
            session.setAttribute("errorMessage", ex.getMessage());
        } catch (SQLException ex) {
            session.setAttribute("errorMessage", "При операциях с БД произошла ошибка: " + ex.getMessage());
        }

        response.sendRedirect("lottery");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding("UTF-8");

        String rawDrawNumber = request.getParameter("drawNumber");

        try {
            int drawNumber = ParseTools.parseInteger(rawDrawNumber);
            winningTicketDao.removeWinningTicket(drawNumber);
        } catch (IllegalArgumentException ex) {
            session.setAttribute("errorMessage", ex.getMessage());
        } catch (SQLException ex) {
            session.setAttribute("errorMessage", "При операциях с БД произошла ошибка: " + ex.getMessage());
        }

        response.sendRedirect("lottery");
    }
}
