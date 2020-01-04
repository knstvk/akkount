import { StandardEntity } from "./sys$StandardEntity";
import { Category } from "./sys$Category";
import { CategoryAttributeConfiguration } from "./sys$CategoryAttributeConfiguration";
export class CategoryAttribute extends StandardEntity {
  static NAME = "sys$CategoryAttribute";
  category?: Category | null;
  categoryEntityType?: string | null;
  name?: string | null;
  code?: string | null;
  description?: string | null;
  enumeration?: string | null;
  dataType?: any | null;
  entityClass?: string | null;
  defaultEntity?: any | null;
  orderNo?: number | null;
  screen?: string | null;
  required?: boolean | null;
  lookup?: boolean | null;
  targetScreens?: string | null;
  defaultString?: string | null;
  defaultInt?: number | null;
  defaultDouble?: any | null;
  defaultDecimal?: any | null;
  defaultBoolean?: boolean | null;
  defaultDate?: any | null;
  defaultDateWithoutTime?: any | null;
  defaultDateIsCurrent?: boolean | null;
  width?: string | null;
  rowsCount?: number | null;
  isCollection?: boolean | null;
  whereClause?: string | null;
  joinClause?: string | null;
  filterXml?: string | null;
  localeNames?: string | null;
  localeDescriptions?: string | null;
  enumerationLocales?: string | null;
  attributeConfigurationJson?: string | null;
  localeName?: string | null;
  localeDescription?: string | null;
  enumerationLocale?: string | null;
  configuration?: CategoryAttributeConfiguration | null;
}
export type CategoryAttributeViewName =
  | "_minimal"
  | "_local"
  | "_base"
  | "categoryAttribute.browse"
  | "category.edit"
  | "for.cache";
export type CategoryAttributeView<
  V extends CategoryAttributeViewName
> = V extends "_minimal"
  ? Pick<CategoryAttribute, "id" | "localeName" | "code">
  : V extends "_local"
  ? Pick<
      CategoryAttribute,
      | "id"
      | "categoryEntityType"
      | "name"
      | "code"
      | "description"
      | "enumeration"
      | "dataType"
      | "entityClass"
      | "orderNo"
      | "screen"
      | "required"
      | "lookup"
      | "targetScreens"
      | "defaultString"
      | "defaultInt"
      | "defaultDouble"
      | "defaultDecimal"
      | "defaultBoolean"
      | "defaultDate"
      | "defaultDateWithoutTime"
      | "defaultDateIsCurrent"
      | "width"
      | "rowsCount"
      | "isCollection"
      | "whereClause"
      | "joinClause"
      | "filterXml"
      | "localeNames"
      | "localeDescriptions"
      | "enumerationLocales"
      | "attributeConfigurationJson"
    >
  : V extends "_base"
  ? Pick<
      CategoryAttribute,
      | "id"
      | "localeName"
      | "code"
      | "categoryEntityType"
      | "name"
      | "description"
      | "enumeration"
      | "dataType"
      | "entityClass"
      | "orderNo"
      | "screen"
      | "required"
      | "lookup"
      | "targetScreens"
      | "defaultString"
      | "defaultInt"
      | "defaultDouble"
      | "defaultDecimal"
      | "defaultBoolean"
      | "defaultDate"
      | "defaultDateWithoutTime"
      | "defaultDateIsCurrent"
      | "width"
      | "rowsCount"
      | "isCollection"
      | "whereClause"
      | "joinClause"
      | "filterXml"
      | "localeNames"
      | "localeDescriptions"
      | "enumerationLocales"
      | "attributeConfigurationJson"
    >
  : V extends "categoryAttribute.browse"
  ? Pick<
      CategoryAttribute,
      | "id"
      | "categoryEntityType"
      | "name"
      | "code"
      | "description"
      | "enumeration"
      | "dataType"
      | "entityClass"
      | "orderNo"
      | "screen"
      | "required"
      | "lookup"
      | "targetScreens"
      | "defaultString"
      | "defaultInt"
      | "defaultDouble"
      | "defaultDecimal"
      | "defaultBoolean"
      | "defaultDate"
      | "defaultDateWithoutTime"
      | "defaultDateIsCurrent"
      | "width"
      | "rowsCount"
      | "isCollection"
      | "whereClause"
      | "joinClause"
      | "filterXml"
      | "localeNames"
      | "localeDescriptions"
      | "enumerationLocales"
      | "attributeConfigurationJson"
      | "defaultEntity"
    >
  : V extends "category.edit"
  ? Pick<
      CategoryAttribute,
      | "id"
      | "categoryEntityType"
      | "name"
      | "code"
      | "description"
      | "enumeration"
      | "dataType"
      | "entityClass"
      | "orderNo"
      | "screen"
      | "required"
      | "lookup"
      | "targetScreens"
      | "defaultString"
      | "defaultInt"
      | "defaultDouble"
      | "defaultDecimal"
      | "defaultBoolean"
      | "defaultDate"
      | "defaultDateWithoutTime"
      | "defaultDateIsCurrent"
      | "width"
      | "rowsCount"
      | "isCollection"
      | "whereClause"
      | "joinClause"
      | "filterXml"
      | "localeNames"
      | "localeDescriptions"
      | "enumerationLocales"
      | "attributeConfigurationJson"
      | "category"
      | "defaultEntity"
    >
  : V extends "for.cache"
  ? Pick<
      CategoryAttribute,
      | "id"
      | "categoryEntityType"
      | "name"
      | "code"
      | "description"
      | "enumeration"
      | "dataType"
      | "entityClass"
      | "orderNo"
      | "screen"
      | "required"
      | "lookup"
      | "targetScreens"
      | "defaultString"
      | "defaultInt"
      | "defaultDouble"
      | "defaultDecimal"
      | "defaultBoolean"
      | "defaultDate"
      | "defaultDateWithoutTime"
      | "defaultDateIsCurrent"
      | "width"
      | "rowsCount"
      | "isCollection"
      | "whereClause"
      | "joinClause"
      | "filterXml"
      | "localeNames"
      | "localeDescriptions"
      | "enumerationLocales"
      | "attributeConfigurationJson"
      | "category"
      | "defaultEntity"
    >
  : never;
