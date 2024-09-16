package com.server;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/check")
public class AreaCheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int x = Integer.parseInt(req.getParameter("x"));
            float y = Float.parseFloat(req.getParameter("y"));
            float r = Float.parseFloat(req.getParameter("r"));

            if (Validator.validateX(x) && Validator.validateY(y) && Validator.validateR(r)) {
                //json.put("result", Checker.hit(x, y, r));
            } else {
                //json.put("error", "invalid data");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //json.put("error", e.toString());
        }

        req.getRequestDispatcher("results.jsp").forward(req, resp);
    }

    private static class Validator {
        public static boolean validateX(int x) {
            return x >= -3 && x <= 5;
        }
    
        public static boolean validateY(float y) {
            return y >= -5 && y <= 3;
        }
    
        public static boolean validateR(float r) {
            return r >= 1 && r <= 3 && r % 0.5 == 0;
        }
    }
    
    private static class Checker {
        public static boolean hit(int x, float y, float r) {
            return inRect(x, y, r) || inTriangle(x, y, r) || inCircle(x, y, r);
        }
    
        private static boolean inRect(int x, float y, float r) {
            return x >= 0 && y >= 0 && x <= r && y <= r/2;
        }
    
        private static boolean inTriangle(int x, float y, float r) {
            return x >= 0 && y <= 0 && x <= r && y >= -r && y - x + r >= 0;
        }
    
        private static boolean inCircle(int x, float y, float r) {
            return x <= 0 && y >= 0 && x >= -r && y <= r && (Math.pow(x, 2) + Math.pow(y, 2) - Math.pow(r, 2) < 0);
        }
    }
    
}
