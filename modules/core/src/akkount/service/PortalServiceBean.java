package akkount.service;

import akkount.entity.Account;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.inject.Inject;

@Service(PortalService.NAME)
public class PortalServiceBean implements PortalService {

    @Inject
    private UserDataService userDataService;

    @Override
    @Nullable
    public Account getLastAccount(String opType) {
        return userDataService.loadEntity(opType, Account.class);
    }
}