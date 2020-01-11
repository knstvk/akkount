import { StandardEntity } from "./base/sys$StandardEntity";
import { Account } from "./akk_Account";
export class Balance extends StandardEntity {
  static NAME = "akk_Balance";
  balanceDate?: any | null;
  account?: Account | null;
  amount?: any | null;
}
export type BalanceViewName = "_minimal" | "_local" | "_base";
export type BalanceView<V extends BalanceViewName> = V extends "_local"
  ? Pick<Balance, "id" | "balanceDate" | "amount">
  : V extends "_base"
  ? Pick<Balance, "id" | "balanceDate" | "amount">
  : never;
