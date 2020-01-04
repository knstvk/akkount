import { BaseDbGeneratedIdEntity } from "./sys$BaseDbGeneratedIdEntity";
export class BaseIntIdentityIdEntity extends BaseDbGeneratedIdEntity {
  static NAME = "sys$BaseIntIdentityIdEntity";
  id?: number | null;
}
export type BaseIntIdentityIdEntityViewName = "_minimal" | "_local" | "_base";
export type BaseIntIdentityIdEntityView<
  V extends BaseIntIdentityIdEntityViewName
> = never;
