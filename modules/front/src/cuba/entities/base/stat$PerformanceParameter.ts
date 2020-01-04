import { BaseUuidEntity } from "./sys$BaseUuidEntity";
export class PerformanceParameter extends BaseUuidEntity {
  static NAME = "stat$PerformanceParameter";
  parameterName?: string | null;
  displayName?: string | null;
  parameterGroup?: string | null;
  currentStringValue?: string | null;
  recentStringValue?: string | null;
  averageStringValue?: string | null;
}
export type PerformanceParameterViewName = "_minimal" | "_local" | "_base";
export type PerformanceParameterView<
  V extends PerformanceParameterViewName
> = never;
