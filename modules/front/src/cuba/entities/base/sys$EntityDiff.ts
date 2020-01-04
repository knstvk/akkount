import { BaseUuidEntity } from "./sys$BaseUuidEntity";
export class EntityDiff extends BaseUuidEntity {
  static NAME = "sys$EntityDiff";
  label?: string | null;
}
export type EntityDiffViewName = "_minimal" | "_local" | "_base";
export type EntityDiffView<V extends EntityDiffViewName> = never;
