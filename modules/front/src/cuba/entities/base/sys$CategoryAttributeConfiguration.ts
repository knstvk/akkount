import { BaseGenericIdEntity } from "./sys$BaseGenericIdEntity";
import { CategoryAttribute } from "./sys$CategoryAttribute";
export class CategoryAttributeConfiguration extends BaseGenericIdEntity {
  static NAME = "sys$CategoryAttributeConfiguration";
  minInt?: number | null;
  minDouble?: any | null;
  minDecimal?: any | null;
  maxInt?: number | null;
  maxDouble?: any | null;
  maxDecimal?: any | null;
  validatorGroovyScript?: string | null;
  columnName?: string | null;
  columnAlignment?: string | null;
  columnWidth?: number | null;
  numberFormatPattern?: string | null;
  optionsLoaderType?: any | null;
  optionsLoaderScript?: string | null;
  recalculationScript?: string | null;
  xCoordinate?: number | null;
  yCoordinate?: number | null;
  dependsOnAttributes?: CategoryAttribute | null;
}
export type CategoryAttributeConfigurationViewName =
  | "_minimal"
  | "_local"
  | "_base";
export type CategoryAttributeConfigurationView<
  V extends CategoryAttributeConfigurationViewName
> = never;
