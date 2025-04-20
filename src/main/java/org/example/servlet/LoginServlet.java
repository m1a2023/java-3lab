package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.discover.JsonReader;
import org.example.discover.NonDatabase;
import org.example.discover.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;


@WebServlet(name="Login", value="/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        /* Set cors headers to avoid CORS error */
        setCorsHeaders(res);
        /* Set content type */
        res.setContentType("application/json");

        try {
            /* Transform json */
            JSONObject json = JsonReader.readFromRequest(req);

            /* Divide into parts and save in NonDatabase */
            String username = json.getString("username");
            String password = json.getString("password");

            HttpSession session = req.getSession();
            User u = (User) session.getAttribute("user");

            /* Check if User is not signed up */
            if (u == null) {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            /* Check if name and pswd are correct */
            if (! (u.getPassword().equals(password) && u.getUsername().equals(username))) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            /* Send response-code as "Ok" */
            res.setStatus(HttpServletResponse.SC_OK);

            /* build userId to json format
             *  example: { "success": BOOLEAN } **/
            JSONObject result = new JSONObject();
            result.put("success", true);

            /* return json */
            PrintWriter out = res.getWriter();
            out.print(result);
            out.flush();
        } catch (JSONException e) {
            System.out.println(e);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse res) {
        setCorsHeaders(res);
        res.setStatus(200);
    }

    private void setCorsHeaders(HttpServletResponse res) {
        res.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type");
        res.setHeader("Access-Control-Allow-Credentials", "true");
    }
}
