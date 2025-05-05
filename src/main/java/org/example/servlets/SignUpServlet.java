package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.db.DatabaseService;
import org.example.db.UserTable;
import org.example.models.JsonReader;
import org.example.models.User;
import org.example.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

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

        try {
            /* Transform json */
            JSONObject json = JsonReader.readFromRequest(req);

            /* Divide into parts */
            String username = json.getString("username");
            String password = json.getString("password");
            String email = json.getString("email");

            { // Debug
                System.out.printf("\nun:%s\nps:%s\ne:%s\n", username, password, email);
            }

            UserService userService = new UserService();

            /* Check if user is present */
            User found = userService.found(username);
            if (found != null) {
                res.setStatus(HttpServletResponse.SC_CONFLICT);
                return;
            }
            { // Debug
                System.out.println("f is not null");
            }
            /* Create new user to make new session */
            User u = new User(username, email, password);
            /* Save user to database */
            userService.addUser(u);
            /* Close connection */
//            userService.closeConnection(); // What's the best con lifecycle?

            /* Set status conde to 201*/
            res.setStatus(HttpServletResponse.SC_CREATED);

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
