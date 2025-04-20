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


@WebServlet(name="Register", value="/signup")
public class SignUpServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        /* Set cors headers to avoid CORS error */
        setCorsHeaders(res);
        /* Set content type */
        res.setContentType("application/json");

        /* Check if User is present */
        HttpSession session = req.getSession();
        User u = (User) session.getAttribute("user");
        if (u != null) {
            res.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }

        try {
            /* Transform json */
            JSONObject json = JsonReader.readFromRequest(req);

            /* Divide into parts */
            String username = json.getString("username");
            String password = json.getString("password");
            String email = json.getString("email");

            /* Create new user to make new session */
            u = new User(username, email, password);

            /* Save user and set response-code as "Created" */
            res.setStatus(HttpServletResponse.SC_CREATED);

            /* Set session to current user */
            session.setAttribute("user", u);

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
    protected void doOptions(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        setCorsHeaders(res);
        res.setStatus(HttpServletResponse.SC_OK);
    }

    private void setCorsHeaders(HttpServletResponse res) {
        res.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type");
        res.setHeader("Access-Control-Allow-Credentials", "true");
    }
}
