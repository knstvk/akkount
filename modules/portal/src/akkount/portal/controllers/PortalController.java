package akkount.portal.controllers;

import akkount.entity.Account;
import akkount.service.BalanceService;
import akkount.service.UserDataService;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.restapi.Authentication;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class PortalController {

    @Inject
    private DataService dataService;

    @Inject
    private BalanceService balanceService;

    @Inject
    private UserDataService userDataService;

    @Inject
    private TimeSource timeSource;

    @Inject
    protected Authentication authentication;

    private Log log = LogFactory.getLog(getClass());

    @RequestMapping(value = "/api/balance", method = RequestMethod.GET)
    public void getBalance(@RequestParam(value = "s") String sessionId,
                           HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        if (!authentication.begin(sessionId)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        try {
            JSONObject result = new JSONObject();
            LoadContext loadContext = new LoadContext(Account.class);
            loadContext.setQueryString("select a from akk$Account a where a.active = true order by a.name");
            List<Account> accounts = dataService.loadList(loadContext);
            if (accounts.size() > 0) {
                DecimalFormat format = new DecimalFormat("#,###");

                Map<Account, BigDecimal> balances = new LinkedHashMap<>();
                for (Account account : accounts) {
                    BigDecimal balance = balanceService.getBalance(account.getId(), timeSource.currentTimestamp());
                    if (BigDecimal.ZERO.compareTo(balance) != 0)
                        balances.put(account, balance);
                }

                Map<String, BigDecimal> totals = new TreeMap<>();
                for (Map.Entry<Account, BigDecimal> entry : balances.entrySet()) {
                    if (BooleanUtils.isTrue(entry.getKey().getIncludeInTotal())) {
                        BigDecimal total = totals.get(entry.getKey().getCurrencyCode());
                        if (total == null)
                            total = entry.getValue();
                        else
                            total = total.add(entry.getValue());
                        totals.put(entry.getKey().getCurrencyCode(), total);
                    }
                }

                JSONArray totalsJS = new JSONArray();
                for (Map.Entry<String, BigDecimal> entry : totals.entrySet()) {
                    JSONObject totalJS = new JSONObject();
                    totalJS.put("currency", entry.getKey());
                    totalJS.put("amount", format.format(entry.getValue()));
                    totalsJS.put(totalJS);
                }
                result.put("totals", totalsJS);

                JSONArray includedAccounts = new JSONArray();
                JSONArray excludedAccounts = new JSONArray();

                for (Map.Entry<Account, BigDecimal> entry : balances.entrySet()) {
                    JSONObject accJS = new JSONObject();
                    accJS.put("name", entry.getKey().getName());
                    accJS.put("currency", entry.getKey().getCurrencyCode());
                    accJS.put("amount", format.format(entry.getValue()));
                    if (BooleanUtils.isTrue(entry.getKey().getIncludeInTotal())) {
                        includedAccounts.put(accJS);
                    } else {
                        excludedAccounts.put(accJS);
                    }
                }
                result.put("includedAccounts", includedAccounts);
                result.put("excludedAccounts", excludedAccounts);
            }
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(result.toString());
            writer.flush();
        } catch (Throwable e) {
            log.error("Error processing request: " + request.getRequestURI() + "?" + request.getQueryString(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            authentication.end();
        }
    }

    @RequestMapping(value = "/api/last-account", method = RequestMethod.GET)
    public void getLastAccount(@RequestParam(value = "s") String sessionId,
                               @RequestParam(value = "t") String opType,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        if (!authentication.begin(sessionId)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        try {
            Account account = userDataService.loadEntity(opType, Account.class);
            JSONObject result = new JSONObject();
            if (account != null) {
                result.put("id", account.getId());
                result.put("name", account.getName());
            }
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(result.toString());
            writer.flush();
        } catch (Throwable e) {
            log.error("Error processing request: " + request.getRequestURI() + "?" + request.getQueryString(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            authentication.end();
        }
    }
}
