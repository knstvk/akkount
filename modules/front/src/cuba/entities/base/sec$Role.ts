import { StandardEntity } from "./sys$StandardEntity";
import { Permission } from "./sec$Permission";
export class Role extends StandardEntity {
  static NAME = "sec$Role";
  name?: string | null;
  locName?: string | null;
  description?: string | null;
  type?: any | null;
  defaultRole?: boolean | null;
  permissions?: Permission[] | null;
}
export type RoleViewName =
  | "_minimal"
  | "_local"
  | "_base"
  | "role.lookup"
  | "role.browse"
  | "role.edit"
  | "role.export"
  | "role.copy";
export type RoleView<V extends RoleViewName> = V extends "_minimal"
  ? Pick<Role, "id" | "locName" | "name">
  : V extends "_local"
  ? Pick<
      Role,
      "id" | "name" | "locName" | "description" | "type" | "defaultRole"
    >
  : V extends "_base"
  ? Pick<
      Role,
      "id" | "locName" | "name" | "description" | "type" | "defaultRole"
    >
  : V extends "role.lookup"
  ? Pick<Role, "id" | "name">
  : V extends "role.browse"
  ? Pick<Role, "id" | "name">
  : V extends "role.edit"
  ? Pick<Role, "id" | "name" | "type">
  : V extends "role.export"
  ? Pick<
      Role,
      | "id"
      | "name"
      | "locName"
      | "description"
      | "type"
      | "defaultRole"
      | "permissions"
    >
  : V extends "role.copy"
  ? Pick<
      Role,
      "id" | "name" | "type" | "locName" | "permissions" | "description"
    >
  : never;
