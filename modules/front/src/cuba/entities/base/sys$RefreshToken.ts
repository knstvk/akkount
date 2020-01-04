import { BaseUuidEntity } from "./sys$BaseUuidEntity";
export class RefreshToken extends BaseUuidEntity {
  static NAME = "sys$RefreshToken";
  createTs?: any | null;
  tokenValue?: string | null;
  tokenBytes?: any | null;
  authenticationBytes?: any | null;
  expiry?: any | null;
  userLogin?: string | null;
}
export type RefreshTokenViewName = "_minimal" | "_local" | "_base";
export type RefreshTokenView<
  V extends RefreshTokenViewName
> = V extends "_local"
  ? Pick<
      RefreshToken,
      | "id"
      | "createTs"
      | "tokenValue"
      | "tokenBytes"
      | "authenticationBytes"
      | "expiry"
      | "userLogin"
    >
  : V extends "_base"
  ? Pick<
      RefreshToken,
      | "id"
      | "createTs"
      | "tokenValue"
      | "tokenBytes"
      | "authenticationBytes"
      | "expiry"
      | "userLogin"
    >
  : never;
