import { StandardEntity } from "./sys$StandardEntity";
import { Group } from "./sec$Group";
export class Constraint extends StandardEntity {
  static NAME = "sec$Constraint";
  checkType?: any | null;
  operationType?: any | null;
  code?: string | null;
  entityName?: string | null;
  joinClause?: string | null;
  whereClause?: string | null;
  groovyScript?: string | null;
  filterXml?: string | null;
  isActive?: boolean | null;
  group?: Group | null;
}
export type ConstraintViewName =
  | "_minimal"
  | "_local"
  | "_base"
  | "group.browse"
  | "edit";
export type ConstraintView<V extends ConstraintViewName> = V extends "_local"
  ? Pick<
      Constraint,
      | "id"
      | "checkType"
      | "operationType"
      | "code"
      | "entityName"
      | "joinClause"
      | "whereClause"
      | "groovyScript"
      | "filterXml"
      | "isActive"
    >
  : V extends "_base"
  ? Pick<
      Constraint,
      | "id"
      | "checkType"
      | "operationType"
      | "code"
      | "entityName"
      | "joinClause"
      | "whereClause"
      | "groovyScript"
      | "filterXml"
      | "isActive"
    >
  : V extends "group.browse"
  ? Pick<
      Constraint,
      | "id"
      | "group"
      | "entityName"
      | "isActive"
      | "operationType"
      | "joinClause"
      | "whereClause"
      | "groovyScript"
    >
  : V extends "edit"
  ? Pick<
      Constraint,
      | "id"
      | "entityName"
      | "isActive"
      | "code"
      | "checkType"
      | "operationType"
      | "joinClause"
      | "whereClause"
      | "groovyScript"
      | "filterXml"
      | "group"
    >
  : never;
