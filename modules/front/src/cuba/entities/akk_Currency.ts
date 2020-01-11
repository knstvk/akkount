import { StandardEntity } from "./base/sys$StandardEntity";
export class Currency extends StandardEntity {
  static NAME = "akk_Currency";
  code?: string | null;
  name?: string | null;
}
export type CurrencyViewName = "_minimal" | "_local" | "_base";
export type CurrencyView<V extends CurrencyViewName> = V extends "_minimal"
  ? Pick<Currency, "id" | "code">
  : V extends "_local"
  ? Pick<Currency, "id" | "code" | "name">
  : V extends "_base"
  ? Pick<Currency, "id" | "code" | "name">
  : never;
