package akkount.service;

import akkount.entity.Account;

import javax.annotation.Nullable;

public interface PortalService {
    String NAME = "akk_PortalService";

    @Nullable
    Account getLastAccount(String opType);
}