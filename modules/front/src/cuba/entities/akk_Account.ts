import { StandardEntity } from "./base/sys$StandardEntity";
import { Currency } from "./akk_Currency";
export class Account extends StandardEntity {
  static NAME = "akk_Account";
  name?: string | null;
  description?: string | null;
  currency?: Currency | null;
  currencyCode?: string | null;
  active?: boolean | null;
  includeInTotal?: boolean | null;
  group?: number | null;
}
export type AccountViewName =
  | "_minimal"
  | "_local"
  | "_base"
  | "account-with-currency";
export type AccountView<V extends AccountViewName> = V extends "_minimal"
  ? Pick<Account, "id" | "name">
  : V extends "_local"
  ? Pick<
      Account,
      | "id"
      | "name"
      | "description"
      | "currencyCode"
      | "active"
      | "includeInTotal"
      | "group"
    >
  : V extends "_base"
  ? Pick<
      Account,
      | "id"
      | "name"
      | "description"
      | "currencyCode"
      | "active"
      | "includeInTotal"
      | "group"
    >
  : V extends "account-with-currency"
  ? Pick<
      Account,
      | "id"
      | "name"
      | "description"
      | "currencyCode"
      | "active"
      | "includeInTotal"
      | "group"
      | "currency"
    >
  : never;
