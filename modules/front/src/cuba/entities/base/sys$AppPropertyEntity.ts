import { BaseUuidEntity } from "./sys$BaseUuidEntity";
export class AppPropertyEntity extends BaseUuidEntity {
  static NAME = "sys$AppPropertyEntity";
  updateTs?: any | null;
  updatedBy?: string | null;
  parent?: AppPropertyEntity | null;
  name?: string | null;
  defaultValue?: string | null;
  currentValue?: string | null;
  category?: boolean | null;
  overridden?: boolean | null;
  dataTypeName?: string | null;
  enumValues?: string | null;
  secret?: boolean | null;
  displayedCurrentValue?: string | null;
  displayedDefaultValue?: string | null;
}
export type AppPropertyEntityViewName = "_minimal" | "_local" | "_base";
export type AppPropertyEntityView<V extends AppPropertyEntityViewName> = never;
