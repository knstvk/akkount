import { BaseUuidEntity } from "./sys$BaseUuidEntity";
import { User } from "./sec$User";
export class UserSetting extends BaseUuidEntity {
  static NAME = "sec$UserSetting";
  createTs?: any | null;
  createdBy?: string | null;
  user?: User | null;
  clientType?: any | null;
  name?: string | null;
  value?: string | null;
}
export type UserSettingViewName =
  | "_minimal"
  | "_local"
  | "_base"
  | "userSetting.value";
export type UserSettingView<V extends UserSettingViewName> = V extends "_local"
  ? Pick<UserSetting, "id" | "clientType" | "name" | "value">
  : V extends "_base"
  ? Pick<UserSetting, "id" | "clientType" | "name" | "value">
  : V extends "userSetting.value"
  ? Pick<UserSetting, "id" | "value">
  : never;
