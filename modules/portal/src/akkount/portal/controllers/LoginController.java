package akkount.portal.controllers;

import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.portal.App;
import com.haulmont.cuba.portal.Connection;
import com.haulmont.cuba.portal.command.LoginUserCommand;
import com.haulmont.cuba.portal.security.PortalSession;
import com.haulmont.cuba.portal.service.PortalAuthenticationService;
import com.haulmont.cuba.security.global.LoginException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

@Controller
public class LoginController {

    @Inject
    protected PortalAuthenticationService portalAuthenticationService;

    @Inject
    protected PasswordEncryption passwordEncryption;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(Model model) {
        final LoginUserCommand loginUserCommand = new LoginUserCommand();
        model.addAttribute(loginUserCommand);

        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String loginPost(@ModelAttribute LoginUserCommand loginUserCommand, Errors errors) {
        PortalSession session;

        try {
            App app = App.getInstance();

            Connection connection = app.getConnection();

            connection.login(loginUserCommand.getLogin(),
                    passwordEncryption.getPlainHash(loginUserCommand.getPassword()),
                    app.getLocale(), app.getIpAddress(), app.getClientInfo());

            session = connection.getSession();
        } catch (LoginException e) {
            errors.reject("error.login.User");
            return "login";
        }
        portalAuthenticationService.authenticate(session);

        return "redirect:/";
    }
}