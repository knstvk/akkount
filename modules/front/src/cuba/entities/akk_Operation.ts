import { StandardEntity } from "./base/sys$StandardEntity";
import { OperationType } from "../enums/enums";
import { Account } from "./akk_Account";
import { Category } from "./akk_Category";
export class Operation extends StandardEntity {
  static NAME = "akk_Operation";
  opType?: OperationType | null;
  opDate?: any | null;
  acc1?: Account | null;
  acc2?: Account | null;
  amount1?: any | null;
  amount2?: any | null;
  category?: Category | null;
  comments?: string | null;
}
export type OperationViewName =
  | "_minimal"
  | "_local"
  | "_base"
  | "operation-browse"
  | "operation-edit"
  | "operation-with-accounts"
  | "operation-recalc-balance";
export type OperationView<V extends OperationViewName> = V extends "_local"
  ? Pick<
      Operation,
      "id" | "opType" | "opDate" | "amount1" | "amount2" | "comments"
    >
  : V extends "_base"
  ? Pick<
      Operation,
      "id" | "opType" | "opDate" | "amount1" | "amount2" | "comments"
    >
  : V extends "operation-browse"
  ? Pick<
      Operation,
      | "id"
      | "version"
      | "createTs"
      | "createdBy"
      | "updateTs"
      | "updatedBy"
      | "deleteTs"
      | "deletedBy"
      | "opType"
      | "opDate"
      | "amount1"
      | "amount2"
      | "comments"
      | "acc1"
      | "acc2"
      | "category"
    >
  : V extends "operation-edit"
  ? Pick<
      Operation,
      | "id"
      | "opType"
      | "opDate"
      | "amount1"
      | "amount2"
      | "comments"
      | "acc1"
      | "acc2"
      | "category"
    >
  : V extends "operation-with-accounts"
  ? Pick<
      Operation,
      | "id"
      | "opType"
      | "opDate"
      | "amount1"
      | "amount2"
      | "comments"
      | "acc1"
      | "acc2"
    >
  : V extends "operation-recalc-balance"
  ? Pick<Operation, "id" | "opDate" | "acc1" | "acc2" | "amount1" | "amount2">
  : never;
