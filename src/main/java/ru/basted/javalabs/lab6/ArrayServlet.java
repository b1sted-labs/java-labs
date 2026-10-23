package ru.basted.javalabs.lab6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ArrayServlet", value = "/servlet")
public class ArrayServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String errorMessage = (String) session.getAttribute("errorMessage");
        if (errorMessage != null) {
            request.setAttribute("error", errorMessage);
            session.removeAttribute("errorMessage");
        }

        request.getRequestDispatcher("/WEB-INF/array.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        session.removeAttribute("initialArray");
        session.removeAttribute("sortedArray");

        String input = request.getParameter("array").trim();

        try {
            List<Integer> initialArray = parseString(input);
            session.setAttribute("initialArray", initialArray);

            List<Integer> sortedArray = new ArrayList<>(initialArray);
            Collections.sort(sortedArray);
            session.setAttribute("sortedArray", sortedArray);
        } catch (IllegalArgumentException ex) {
            session.setAttribute("errorMessage", ex.getMessage());
        }

        response.sendRedirect("servlet");
    }

    private List<Integer> parseString(String input) {
        if (input.contains(" ") && !input.contains(", ") ) {
            throw new IllegalArgumentException(
                    "Элементы массива должны быть отделены друг от друга запятой. Было передано: [" + input +
                            "]. Ожидалось (пример корректного ввода): [1, 2, 3]"
            );
        }

        String[] tokens = input.split(", ", -1);

        List<Integer> initialArray = new ArrayList<>();
        for (String token : tokens) {
            if (token.isBlank()) {
                throw new IllegalArgumentException("Два разделителя подряд или пустой элемент!");
            }

            if (token.contains(" ") || token.trim().length() != token.length()) {
                throw new IllegalArgumentException("Токен '" + token + "' содержит пробелы или не разделен запятой!");
            }

            try {
                initialArray.add(Integer.parseInt(token));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException(
                        "В переданном массиве [" + input + "] содержится не целое число: '" + token + "'"
                );
            }
        }

        return initialArray;
    }
}
