import { BaseUuidEntity } from "./sys$BaseUuidEntity";
export class ScheduledTask extends BaseUuidEntity {
  static NAME = "sys$ScheduledTask";
  createTs?: any | null;
  createdBy?: string | null;
  updateTs?: any | null;
  updatedBy?: string | null;
  deleteTs?: any | null;
  deletedBy?: string | null;
  definedBy?: any | null;
  beanName?: string | null;
  methodName?: string | null;
  className?: string | null;
  scriptName?: string | null;
  userName?: string | null;
  singleton?: boolean | null;
  active?: boolean | null;
  period?: number | null;
  timeout?: number | null;
  startDate?: any | null;
  cron?: string | null;
  schedulingType?: any | null;
  timeFrame?: number | null;
  startDelay?: number | null;
  permittedServers?: string | null;
  logStart?: boolean | null;
  logFinish?: boolean | null;
  lastStartTime?: any | null;
  lastStartServer?: string | null;
  methodParamsXml?: string | null;
  description?: string | null;
  methodParametersString?: string | null;
}
export type ScheduledTaskViewName = "_minimal" | "_local" | "_base";
export type ScheduledTaskView<
  V extends ScheduledTaskViewName
> = V extends "_minimal"
  ? Pick<
      ScheduledTask,
      "id" | "beanName" | "methodName" | "className" | "scriptName"
    >
  : V extends "_local"
  ? Pick<
      ScheduledTask,
      | "id"
      | "definedBy"
      | "beanName"
      | "methodName"
      | "className"
      | "scriptName"
      | "userName"
      | "singleton"
      | "active"
      | "period"
      | "timeout"
      | "startDate"
      | "cron"
      | "schedulingType"
      | "timeFrame"
      | "startDelay"
      | "permittedServers"
      | "logStart"
      | "logFinish"
      | "lastStartTime"
      | "lastStartServer"
      | "methodParamsXml"
      | "description"
      | "methodParametersString"
    >
  : V extends "_base"
  ? Pick<
      ScheduledTask,
      | "id"
      | "beanName"
      | "methodName"
      | "className"
      | "scriptName"
      | "definedBy"
      | "userName"
      | "singleton"
      | "active"
      | "period"
      | "timeout"
      | "startDate"
      | "cron"
      | "schedulingType"
      | "timeFrame"
      | "startDelay"
      | "permittedServers"
      | "logStart"
      | "logFinish"
      | "lastStartTime"
      | "lastStartServer"
      | "methodParamsXml"
      | "description"
      | "methodParametersString"
    >
  : never;
