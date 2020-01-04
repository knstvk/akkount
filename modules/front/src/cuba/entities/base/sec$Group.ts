import { StandardEntity } from "./sys$StandardEntity";
import { GroupHierarchy } from "./sec$GroupHierarchy";
import { Constraint } from "./sec$Constraint";
import { SessionAttribute } from "./sec$SessionAttribute";
export class Group extends StandardEntity {
  static NAME = "sec$Group";
  name?: string | null;
  parent?: Group | null;
  hierarchyList?: GroupHierarchy[] | null;
  constraints?: Constraint[] | null;
  sessionAttributes?: SessionAttribute[] | null;
}
export type GroupViewName =
  | "_minimal"
  | "_local"
  | "_base"
  | "group.lookup"
  | "group.browse"
  | "group.edit"
  | "group.copy"
  | "group.export";
export type GroupView<V extends GroupViewName> = V extends "_minimal"
  ? Pick<Group, "id" | "name">
  : V extends "_local"
  ? Pick<Group, "id" | "name">
  : V extends "_base"
  ? Pick<Group, "id" | "name">
  : V extends "group.lookup"
  ? Pick<Group, "id" | "name">
  : V extends "group.browse"
  ? Pick<Group, "id" | "name" | "parent">
  : V extends "group.edit"
  ? Pick<Group, "id" | "name" | "parent" | "constraints" | "sessionAttributes">
  : V extends "group.copy"
  ? Pick<Group, "id" | "name" | "parent" | "constraints" | "sessionAttributes">
  : V extends "group.export"
  ? Pick<Group, "id" | "name" | "parent" | "constraints" | "sessionAttributes">
  : never;
