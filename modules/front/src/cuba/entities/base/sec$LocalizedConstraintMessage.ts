import { StandardEntity } from "./sys$StandardEntity";
export class LocalizedConstraintMessage extends StandardEntity {
  static NAME = "sec$LocalizedConstraintMessage";
  entityName?: string | null;
  operationType?: any | null;
  values?: string | null;
}
export type LocalizedConstraintMessageViewName =
  | "_minimal"
  | "_local"
  | "_base";
export type LocalizedConstraintMessageView<
  V extends LocalizedConstraintMessageViewName
> = V extends "_local"
  ? Pick<
      LocalizedConstraintMessage,
      "id" | "entityName" | "operationType" | "values"
    >
  : V extends "_base"
  ? Pick<
      LocalizedConstraintMessage,
      "id" | "entityName" | "operationType" | "values"
    >
  : never;
