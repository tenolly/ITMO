package com.server;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AreaCheckServlet extends HttpServlet {
    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();

        try {
            float x = Float.parseFloat(req.getParameter("x"));
            float y = Float.parseFloat(req.getParameter("y"));
            float r = Float.parseFloat(req.getParameter("r"));

            if (Validator.validateX(x) && Validator.validateY(y) && Validator.validateR(r)) {
                Row row = new Row(x, y, r, Checker.hit(x, y, r));

                Object rowsData = context.getAttribute("rows");
                ArrayList<Row> rows = new ArrayList<Row>();
                if (rowsData != null) {
                    rows.addAll((ArrayList<Row>) context.getAttribute("rows"));
                }

                rows.add(row);

                context.setAttribute("rows", rows);
                req.setAttribute("new_row", row);
            } else {}
        } catch (Exception e) {
            e.printStackTrace();
        }

        req.getRequestDispatcher("results.jsp").forward(req, resp);
    }

    private static class Validator {
        public static boolean validateX(float x) {
            return x >= -5 && x <= 5;
        }
    
        public static boolean validateY(float y) {
            return y >= -5 && y <= 3;
        }
    
        public static boolean validateR(float r) {
            return r >= 1 && r <= 5 && r % 1 == 0;
        }
    }
    
    private static class Checker {
        public static boolean hit(float x, float y, float r) {
            return inRect(x, y, r) || inTriangle(x, y, r) || inCircle(x, y, r);
        }
    
        private static boolean inRect(float x, float y, float r) {
            return x <= 0 && y >= 0 && x >= -r && y <= r;
        }
    
        private static boolean inTriangle(float x, float y, float r) {
            return x >= 0 && y <= 0 && x <= r && y >= -r/2 && y - x/2 + r/2 >= 0;
        }
    
        private static boolean inCircle(float x, float y, float r) {
            return x >= 0 && y >= 0 && x <= r/2 && y <= r/2 && (Math.pow(x, 2) + Math.pow(y, 2) - Math.pow(r/2, 2) <= 0);
        }
    }
    
}
