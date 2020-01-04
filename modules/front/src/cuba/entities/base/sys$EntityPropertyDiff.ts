import { BaseUuidEntity } from "./sys$BaseUuidEntity";
export class EntityPropertyDiff extends BaseUuidEntity {
  static NAME = "sys$EntityPropertyDiff";
  name?: string | null;
  label?: string | null;
  beforeString?: string | null;
  afterString?: string | null;
  beforeCaption?: string | null;
  afterCaption?: string | null;
  itemState?: any | null;
}
export type EntityPropertyDiffViewName = "_minimal" | "_local" | "_base";
export type EntityPropertyDiffView<
  V extends EntityPropertyDiffViewName
> = never;
