import { StandardEntity } from "./sys$StandardEntity";
import { Role } from "./sec$Role";
export class Permission extends StandardEntity {
  static NAME = "sec$Permission";
  type?: any | null;
  target?: string | null;
  value?: number | null;
  role?: Role | null;
}
export type PermissionViewName = "_minimal" | "_local" | "_base" | "role.edit";
export type PermissionView<V extends PermissionViewName> = V extends "_local"
  ? Pick<Permission, "id" | "type" | "target" | "value">
  : V extends "_base"
  ? Pick<Permission, "id" | "type" | "target" | "value">
  : V extends "role.edit"
  ? Pick<Permission, "id" | "type" | "target" | "value">
  : never;
