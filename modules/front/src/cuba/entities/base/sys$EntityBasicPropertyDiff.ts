import { EntityPropertyDiff } from "./sys$EntityPropertyDiff";
export class EntityBasicPropertyDiff extends EntityPropertyDiff {
  static NAME = "sys$EntityBasicPropertyDiff";
}
export type EntityBasicPropertyDiffViewName = "_minimal" | "_local" | "_base";
export type EntityBasicPropertyDiffView<
  V extends EntityBasicPropertyDiffViewName
> = never;
