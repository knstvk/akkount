package akkount.web.slack;

import akkount.config.AkkConfig;
import akkount.service.BotService;
import akkount.slack.Slack;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.TrustedClientService;
import com.haulmont.cuba.security.auth.AuthenticationService;
import com.haulmont.cuba.security.auth.TrustedClientCredentials;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/slack")
public class SlackController {

    private static final Logger log = LoggerFactory.getLogger(SlackController.class);

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Inject
    private AkkConfig akkConfig;

    @Inject
    private WebAuthConfig authConfig;

    @Inject
    private AuthenticationService authenticationService;

    @Inject
    private TrustedClientService trustedClientService;

    @Inject
    private Slack slack;

    @Inject
    private BotService botService;

    private UserSession slackUserSession;

    @PreDestroy
    void shutdownExecutor() {
        executor.shutdownNow();
    }

    @PostMapping("/event")
    public String processEvent(@RequestBody String json) {
        log.debug("Received event:\n" + json);

        UserSession systemSession;
        try {
            systemSession = trustedClientService.getSystemSession(authConfig.getTrustedClientPassword());
        } catch (LoginException e) {
            log.error("Unable to get system session");
            return "server error";
        }

        return AppContext.withSecurityContext(new SecurityContext(systemSession), () -> {
            JSONObject jsonObj = new JSONObject(json);
            String token = jsonObj.getString("token");
            if (!token.equals(akkConfig.getSlackVerificationToken())) {
                return "invalid token";
            }
            String type = jsonObj.getString("type");
            switch (type) {
                case "url_verification": return processUrlVerification(jsonObj);
                case "event_callback": return processCallback(jsonObj);
            }
            return "unknown event";
        });
    }

    private String processUrlVerification(JSONObject jsonObj) {
        return jsonObj.getString("challenge");
    }

    private String processCallback(JSONObject jsonObj) {
        JSONObject event = jsonObj.getJSONObject("event");
        if ("message".equals(event.getString("type")) && !"bot_message".equals(event.optString("subtype"))) {
            executor.submit(() -> onMessage(event.getString("text"), event.getString("channel")));
        }
        return "ok";
    }

    private void onMessage(String message, String channel) {
        try {
            slackUserSession = prolongOrCreateNewSession(slackUserSession);
            AppContext.withSecurityContext(new SecurityContext(slackUserSession), () -> {
                String result = botService.processMessage(message);
                slack.sendMessage(channel, result);
            });
        } catch (Exception e) {
            log.debug("Error processing event", e);
            slack.sendMessage(channel, "Error: " + ExceptionUtils.getRootCauseMessage(e));
        }
    }

    private UserSession prolongOrCreateNewSession(@Nullable UserSession session) {
        UserSession resultSession = null;
        try {
            if (session != null) {
                resultSession = trustedClientService.findSession(authConfig.getTrustedClientPassword(), session.getId());
            }
            if (resultSession == null) {
                TrustedClientCredentials credentials = new TrustedClientCredentials(
                        akkConfig.getSlackUserLogin(), authConfig.getTrustedClientPassword(), Locale.ENGLISH);
                resultSession = authenticationService.login(credentials).getSession();
            }
            return resultSession;
        } catch (LoginException e) {
            throw new RuntimeException("Unable to get user session", e);
        }
    }
}
