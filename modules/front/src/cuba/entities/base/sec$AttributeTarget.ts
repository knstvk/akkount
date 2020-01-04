export class AttributeTarget {
  static NAME = "sec$AttributeTarget";
  id?: string | null;
  permissionVariant?: any | null;
}
export type AttributeTargetViewName = "_minimal" | "_local" | "_base";
export type AttributeTargetView<V extends AttributeTargetViewName> = never;
