import { BaseGenericIdEntity } from "./sys$BaseGenericIdEntity";
export class BaseLongIdEntity extends BaseGenericIdEntity {
  static NAME = "sys$BaseLongIdEntity";
  id?: any | null;
}
export type BaseLongIdEntityViewName = "_minimal" | "_local" | "_base";
export type BaseLongIdEntityView<V extends BaseLongIdEntityViewName> = never;
