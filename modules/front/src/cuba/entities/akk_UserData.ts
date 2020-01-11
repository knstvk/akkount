import { BaseUuidEntity } from "./base/sys$BaseUuidEntity";
import { User } from "./base/sec$User";
export class UserData extends BaseUuidEntity {
  static NAME = "akk_UserData";
  user?: User | null;
  key?: string | null;
  value?: string | null;
  createTs?: any | null;
  createdBy?: string | null;
}
export type UserDataViewName = "_minimal" | "_local" | "_base";
export type UserDataView<V extends UserDataViewName> = V extends "_local"
  ? Pick<UserData, "id" | "key" | "value">
  : V extends "_base"
  ? Pick<UserData, "id" | "key" | "value">
  : never;
