package com.cookieservlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.net.URLEncoder;
import java.net.URLDecoder;

@WebServlet("/CookieServlet")
public class CookieServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String userName = request.getParameter("userName");

        // ✅ Create cookies (name + visit count)
        if (userName != null && !userName.isEmpty()) {

            String encodedName = URLEncoder.encode(userName, "UTF-8");

            Cookie userCookie = new Cookie("user", encodedName);
            Cookie countCookie = new Cookie("count", "1");

            userCookie.setMaxAge(30);   // expiry (30 sec)
            countCookie.setMaxAge(30);

            response.addCookie(userCookie);
            response.addCookie(countCookie);
        }

        // ✅ Read cookies
        Cookie[] cookies = request.getCookies();

        String existingUser = null;
        int count = 0;

        if (cookies != null) {
            for (Cookie c : cookies) {

                if (c.getName().equals("user")) {
                    existingUser = URLDecoder.decode(c.getValue(), "UTF-8");
                }

                if (c.getName().equals("count")) {
                    count = Integer.parseInt(c.getValue());
                    count++;

                    Cookie newCount = new Cookie("count", String.valueOf(count));
                    newCount.setMaxAge(30);
                    response.addCookie(newCount);
                }
            }
        }

        out.println("<html><body>");

        // ✅ If user exists
        if (existingUser != null) {

            out.println("<font color='blue'><h1>Welcome back, " + existingUser + "!</h1></font>");
            out.println("<font color='magenta'><h1>You have visited this page " + count + " times!</h1></font>");

            // ✅ Display ALL cookies
            out.println("<h2>List of Cookies:</h2>");
            out.println("<table border='1'>");
            out.println("<tr><th>Cookie Name</th><th>Value</th></tr>");

            for (Cookie c : cookies) {
                String name = c.getName();
                String value = c.getValue();

                // decode user cookie for display
                if (name.equals("user")) {
                    value = URLDecoder.decode(value, "UTF-8");
                }

                out.println("<tr><td>" + name + "</td><td>" + value + "</td></tr>");
            }

            out.println("</table>");
        }

        // ✅ First time user
        else {
            out.println("<h2 style='color:red;'>Welcome Guest! Kindly login first time</h2>");
            out.println("<form action='CookieServlet' method='get'>");
            out.println("Enter your name: <input type='text' name='userName'>");
            out.println("<input type='submit' value='Submit'>");
            out.println("</form>");
        }

        out.println("</body></html>");
    }
}