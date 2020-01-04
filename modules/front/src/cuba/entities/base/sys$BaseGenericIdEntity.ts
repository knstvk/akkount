export class BaseGenericIdEntity {
  static NAME = "sys$BaseGenericIdEntity";
}
export type BaseGenericIdEntityViewName = "_minimal" | "_local" | "_base";
export type BaseGenericIdEntityView<
  V extends BaseGenericIdEntityViewName
> = never;
