import { AbstractCondition } from "./sec$AbstractCondition";
export class GroupCondition extends AbstractCondition {
  static NAME = "sec$GroupCondition";
}
export type GroupConditionViewName = "_minimal" | "_local" | "_base";
export type GroupConditionView<V extends GroupConditionViewName> = never;
