export class EmbeddableEntity {
  static NAME = "sys$EmbeddableEntity";
}
export type EmbeddableEntityViewName = "_minimal" | "_local" | "_base";
export type EmbeddableEntityView<V extends EmbeddableEntityViewName> = never;
