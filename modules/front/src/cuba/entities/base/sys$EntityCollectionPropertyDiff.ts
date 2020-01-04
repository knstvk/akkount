import { EntityPropertyDiff } from "./sys$EntityPropertyDiff";
export class EntityCollectionPropertyDiff extends EntityPropertyDiff {
  static NAME = "sys$EntityCollectionPropertyDiff";
}
export type EntityCollectionPropertyDiffViewName =
  | "_minimal"
  | "_local"
  | "_base";
export type EntityCollectionPropertyDiffView<
  V extends EntityCollectionPropertyDiffViewName
> = never;
