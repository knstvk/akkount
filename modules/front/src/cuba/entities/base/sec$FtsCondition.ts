import { AbstractCondition } from "./sec$AbstractCondition";
export class FtsCondition extends AbstractCondition {
  static NAME = "sec$FtsCondition";
}
export type FtsConditionViewName = "_minimal" | "_local" | "_base";
export type FtsConditionView<V extends FtsConditionViewName> = never;
