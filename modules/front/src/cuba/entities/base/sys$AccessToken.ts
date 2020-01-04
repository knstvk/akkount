import { BaseUuidEntity } from "./sys$BaseUuidEntity";
export class AccessToken extends BaseUuidEntity {
  static NAME = "sys$AccessToken";
  createTs?: any | null;
  tokenValue?: string | null;
  tokenBytes?: any | null;
  authenticationKey?: string | null;
  authenticationBytes?: any | null;
  expiry?: any | null;
  userLogin?: string | null;
  locale?: string | null;
  refreshTokenValue?: string | null;
}
export type AccessTokenViewName = "_minimal" | "_local" | "_base";
export type AccessTokenView<V extends AccessTokenViewName> = V extends "_local"
  ? Pick<
      AccessToken,
      | "id"
      | "createTs"
      | "tokenValue"
      | "tokenBytes"
      | "authenticationKey"
      | "authenticationBytes"
      | "expiry"
      | "userLogin"
      | "locale"
      | "refreshTokenValue"
    >
  : V extends "_base"
  ? Pick<
      AccessToken,
      | "id"
      | "createTs"
      | "tokenValue"
      | "tokenBytes"
      | "authenticationKey"
      | "authenticationBytes"
      | "expiry"
      | "userLogin"
      | "locale"
      | "refreshTokenValue"
    >
  : never;
