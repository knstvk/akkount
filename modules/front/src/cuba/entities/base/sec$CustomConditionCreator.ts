import { AbstractConditionDescriptor } from "./sec$AbstractConditionDescriptor";
export class CustomConditionCreator extends AbstractConditionDescriptor {
  static NAME = "sec$CustomConditionCreator";
}
export type CustomConditionCreatorViewName = "_minimal" | "_local" | "_base";
export type CustomConditionCreatorView<
  V extends CustomConditionCreatorViewName
> = never;
