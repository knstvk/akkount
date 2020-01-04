import { StandardEntity } from "./sys$StandardEntity";
import { User } from "./sec$User";
import { Role } from "./sec$Role";
export class UserRole extends StandardEntity {
  static NAME = "sec$UserRole";
  user?: User | null;
  role?: Role | null;
}
export type UserRoleViewName =
  | "_minimal"
  | "_local"
  | "_base"
  | "user.edit"
  | "tmp.user.edit";
export type UserRoleView<V extends UserRoleViewName> = V extends "user.edit"
  ? Pick<UserRole, "id" | "role">
  : V extends "tmp.user.edit"
  ? Pick<UserRole, "id" | "user" | "role">
  : never;
