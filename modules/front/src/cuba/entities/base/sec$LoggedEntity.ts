import { BaseUuidEntity } from "./sys$BaseUuidEntity";
import { LoggedAttribute } from "./sec$LoggedAttribute";
export class LoggedEntity extends BaseUuidEntity {
  static NAME = "sec$LoggedEntity";
  createTs?: any | null;
  createdBy?: string | null;
  name?: string | null;
  auto?: boolean | null;
  manual?: boolean | null;
  attributes?: LoggedAttribute[] | null;
}
export type LoggedEntityViewName =
  | "_minimal"
  | "_local"
  | "_base"
  | "loggedAttrs";
export type LoggedEntityView<
  V extends LoggedEntityViewName
> = V extends "_local"
  ? Pick<LoggedEntity, "id" | "name" | "auto" | "manual">
  : V extends "_base"
  ? Pick<LoggedEntity, "id" | "name" | "auto" | "manual">
  : V extends "loggedAttrs"
  ? Pick<LoggedEntity, "id" | "attributes" | "auto" | "manual" | "name">
  : never;
