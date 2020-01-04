import { BaseUuidEntity } from "./sys$BaseUuidEntity";
export class EntityStatistics extends BaseUuidEntity {
  static NAME = "sys$EntityStatistics";
  createTs?: any | null;
  createdBy?: string | null;
  updateTs?: any | null;
  updatedBy?: string | null;
  name?: string | null;
  instanceCount?: any | null;
  fetchUI?: number | null;
  maxFetchUI?: number | null;
  lazyCollectionThreshold?: number | null;
  lookupScreenThreshold?: number | null;
}
export type EntityStatisticsViewName = "_minimal" | "_local" | "_base";
export type EntityStatisticsView<
  V extends EntityStatisticsViewName
> = V extends "_local"
  ? Pick<
      EntityStatistics,
      | "id"
      | "name"
      | "instanceCount"
      | "fetchUI"
      | "maxFetchUI"
      | "lazyCollectionThreshold"
      | "lookupScreenThreshold"
    >
  : V extends "_base"
  ? Pick<
      EntityStatistics,
      | "id"
      | "name"
      | "instanceCount"
      | "fetchUI"
      | "maxFetchUI"
      | "lazyCollectionThreshold"
      | "lookupScreenThreshold"
    >
  : never;
