export class AbstractNotPersistentEntity {
  static NAME = "sys$AbstractNotPersistentEntity";
  id?: any | null;
}
export type AbstractNotPersistentEntityViewName =
  | "_minimal"
  | "_local"
  | "_base";
export type AbstractNotPersistentEntityView<
  V extends AbstractNotPersistentEntityViewName
> = never;
