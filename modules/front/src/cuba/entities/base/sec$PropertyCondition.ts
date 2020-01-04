import { AbstractCondition } from "./sec$AbstractCondition";
export class PropertyCondition extends AbstractCondition {
  static NAME = "sec$PropertyCondition";
}
export type PropertyConditionViewName = "_minimal" | "_local" | "_base";
export type PropertyConditionView<V extends PropertyConditionViewName> = never;
