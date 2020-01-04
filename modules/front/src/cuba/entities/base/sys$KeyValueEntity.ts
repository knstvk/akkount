export class KeyValueEntity {
  static NAME = "sys$KeyValueEntity";
}
export type KeyValueEntityViewName = "_minimal" | "_local" | "_base";
export type KeyValueEntityView<V extends KeyValueEntityViewName> = never;
