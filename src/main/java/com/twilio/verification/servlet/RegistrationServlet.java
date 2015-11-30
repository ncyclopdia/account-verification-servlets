package com.twilio.verification.servlet;

import com.twilio.verification.lib.RequestParametersValidator;
import com.twilio.verification.model.User;
import com.twilio.verification.repository.UsersRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegistrationServlet extends HttpServlet {

    private final UsersRepository usersRepository;

    public RegistrationServlet() {
       this(new UsersRepository());
    }

    public RegistrationServlet(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String countryCode = request.getParameter("countryCode");
        String phoneNumber = request.getParameter("phoneNumber");

        if (validateRequest(request)) {

            User user = usersRepository.create(new User(name, email, password, countryCode, phoneNumber));
            response.getWriter().print("User was created");
        } else {
            preserveStatusRequest(request, name, email, countryCode, phoneNumber);
            request.getRequestDispatcher("/registration.jsp").forward(request, response);
        }
    }

    private boolean validateRequest(HttpServletRequest request) {
        RequestParametersValidator validator = new RequestParametersValidator(request);

        return validator.validatePresence("name", "email", "password", "countryCode", "phoneNumber")
                && validator.validateEmail("email");
    }

    private void preserveStatusRequest(
            HttpServletRequest request,
            String name,
            String email,
            String countryCode,
            String phoneNumber) {
        request.setAttribute("name", name);
        request.setAttribute("email", email);
        request.setAttribute("countryCode", countryCode);
        request.setAttribute("phoneNumber", phoneNumber);
    }
}

