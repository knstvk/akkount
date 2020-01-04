import { StandardEntity } from "./sys$StandardEntity";
import { Group } from "./sec$Group";
import { UserRole } from "./sec$UserRole";
import { UserSubstitution } from "./sec$UserSubstitution";
export class User extends StandardEntity {
  static NAME = "sec$User";
  login?: string | null;
  loginLowerCase?: string | null;
  password?: string | null;
  passwordEncryption?: string | null;
  name?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  middleName?: string | null;
  position?: string | null;
  email?: string | null;
  language?: string | null;
  timeZone?: string | null;
  timeZoneAuto?: boolean | null;
  active?: boolean | null;
  changePasswordAtNextLogon?: boolean | null;
  group?: Group | null;
  userRoles?: UserRole[] | null;
  substitutions?: UserSubstitution[] | null;
  ipMask?: string | null;
}
export type UserViewName =
  | "_minimal"
  | "_local"
  | "_base"
  | "app"
  | "user.edit"
  | "user.browse"
  | "user.copySettings"
  | "user.changepassw"
  | "user.resetPassword"
  | "user.locale"
  | "user.check"
  | "user.timeZone"
  | "user.changePassword"
  | "scheduling"
  | "group.browse"
  | "user.moveToGroup";
export type UserView<V extends UserViewName> = V extends "_minimal"
  ? Pick<User, "id" | "login" | "name">
  : V extends "_local"
  ? Pick<
      User,
      | "id"
      | "login"
      | "loginLowerCase"
      | "password"
      | "passwordEncryption"
      | "name"
      | "firstName"
      | "lastName"
      | "middleName"
      | "position"
      | "email"
      | "language"
      | "timeZone"
      | "timeZoneAuto"
      | "active"
      | "changePasswordAtNextLogon"
      | "ipMask"
    >
  : V extends "_base"
  ? Pick<
      User,
      | "id"
      | "login"
      | "name"
      | "loginLowerCase"
      | "password"
      | "passwordEncryption"
      | "firstName"
      | "lastName"
      | "middleName"
      | "position"
      | "email"
      | "language"
      | "timeZone"
      | "timeZoneAuto"
      | "active"
      | "changePasswordAtNextLogon"
      | "ipMask"
    >
  : V extends "app"
  ? Pick<User, "id" | "login" | "name">
  : V extends "user.edit"
  ? Pick<
      User,
      | "id"
      | "login"
      | "loginLowerCase"
      | "password"
      | "passwordEncryption"
      | "name"
      | "firstName"
      | "lastName"
      | "middleName"
      | "position"
      | "email"
      | "language"
      | "timeZone"
      | "timeZoneAuto"
      | "active"
      | "changePasswordAtNextLogon"
      | "ipMask"
      | "group"
      | "userRoles"
      | "substitutions"
    >
  : V extends "user.browse"
  ? Pick<
      User,
      | "id"
      | "version"
      | "createTs"
      | "createdBy"
      | "updateTs"
      | "updatedBy"
      | "deleteTs"
      | "deletedBy"
      | "login"
      | "loginLowerCase"
      | "password"
      | "passwordEncryption"
      | "name"
      | "firstName"
      | "lastName"
      | "middleName"
      | "position"
      | "email"
      | "language"
      | "timeZone"
      | "timeZoneAuto"
      | "active"
      | "changePasswordAtNextLogon"
      | "ipMask"
      | "group"
    >
  : V extends "user.copySettings"
  ? Pick<User, "id" | "login" | "name" | "login" | "name">
  : V extends "user.changepassw"
  ? Pick<User, "id" | "password" | "changePasswordAtNextLogon">
  : V extends "user.resetPassword"
  ? Pick<
      User,
      | "id"
      | "login"
      | "loginLowerCase"
      | "password"
      | "passwordEncryption"
      | "name"
      | "firstName"
      | "lastName"
      | "middleName"
      | "position"
      | "email"
      | "language"
      | "timeZone"
      | "timeZoneAuto"
      | "active"
      | "changePasswordAtNextLogon"
      | "ipMask"
    >
  : V extends "user.locale"
  ? Pick<User, "id" | "login" | "name" | "language">
  : V extends "user.check"
  ? Pick<User, "id" | "password">
  : V extends "user.timeZone"
  ? Pick<User, "id" | "timeZone" | "timeZoneAuto">
  : V extends "user.changePassword"
  ? Pick<User, "id" | "password" | "changePasswordAtNextLogon">
  : V extends "scheduling"
  ? Pick<User, "id" | "login" | "name">
  : V extends "group.browse"
  ? Pick<User, "id" | "name" | "login" | "group">
  : V extends "user.moveToGroup"
  ? Pick<User, "id" | "group">
  : never;
