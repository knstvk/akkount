import { CubaApp, FetchOptions } from "@cuba-platform/rest";

export type akk_PortalService_getLastAccount_params = {
  opType: any;
};

export var restServices = {
  akk_PortalService: {
    getBalance: (cubaApp: CubaApp, fetchOpts?: FetchOptions) => () => {
      return cubaApp.invokeService(
        "akk_PortalService",
        "getBalance",
        {},
        fetchOpts
      );
    },
    getLastAccount: (cubaApp: CubaApp, fetchOpts?: FetchOptions) => (
      params: akk_PortalService_getLastAccount_params
    ) => {
      return cubaApp.invokeService(
        "akk_PortalService",
        "getLastAccount",
        params,
        fetchOpts
      );
    }
  }
};
