import { EntityPropertyDiff } from "./sys$EntityPropertyDiff";
export class EntityClassPropertyDiff extends EntityPropertyDiff {
  static NAME = "sys$EntityClassPropertyDiff";
}
export type EntityClassPropertyDiffViewName = "_minimal" | "_local" | "_base";
export type EntityClassPropertyDiffView<
  V extends EntityClassPropertyDiffViewName
> = never;
