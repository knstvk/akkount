import { AbstractCondition } from "./sec$AbstractCondition";
export class CustomCondition extends AbstractCondition {
  static NAME = "sec$CustomCondition";
}
export type CustomConditionViewName = "_minimal" | "_local" | "_base";
export type CustomConditionView<V extends CustomConditionViewName> = never;
