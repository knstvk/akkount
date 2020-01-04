import { BaseUuidEntity } from "./sys$BaseUuidEntity";
export class AbstractCondition extends BaseUuidEntity {
  static NAME = "sec$AbstractCondition";
  locCaption?: string | null;
}
export type AbstractConditionViewName = "_minimal" | "_local" | "_base";
export type AbstractConditionView<V extends AbstractConditionViewName> = never;
