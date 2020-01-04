import { StandardEntity } from "./sys$StandardEntity";
import { FileDescriptor } from "./sys$FileDescriptor";
import { SendingAttachment } from "./sys$SendingAttachment";
export class SendingMessage extends StandardEntity {
  static NAME = "sys$SendingMessage";
  address?: string | null;
  from?: string | null;
  cc?: string | null;
  bcc?: string | null;
  caption?: string | null;
  contentText?: string | null;
  contentTextFile?: FileDescriptor | null;
  status?: any | null;
  dateSent?: any | null;
  attachmentsName?: string | null;
  deadline?: any | null;
  attemptsCount?: number | null;
  attemptsMade?: number | null;
  attachments?: SendingAttachment[] | null;
  headers?: string | null;
  bodyContentType?: string | null;
}
export type SendingMessageViewName =
  | "_minimal"
  | "_local"
  | "_base"
  | "sendingMessage.browse"
  | "sendingMessage.loadFromQueue"
  | "sendingMessage.loadContentText";
export type SendingMessageView<
  V extends SendingMessageViewName
> = V extends "_local"
  ? Pick<
      SendingMessage,
      | "id"
      | "address"
      | "from"
      | "cc"
      | "bcc"
      | "caption"
      | "contentText"
      | "status"
      | "dateSent"
      | "attachmentsName"
      | "deadline"
      | "attemptsCount"
      | "attemptsMade"
      | "headers"
      | "bodyContentType"
    >
  : V extends "_base"
  ? Pick<
      SendingMessage,
      | "id"
      | "address"
      | "from"
      | "cc"
      | "bcc"
      | "caption"
      | "contentText"
      | "status"
      | "dateSent"
      | "attachmentsName"
      | "deadline"
      | "attemptsCount"
      | "attemptsMade"
      | "headers"
      | "bodyContentType"
    >
  : V extends "sendingMessage.browse"
  ? Pick<
      SendingMessage,
      | "id"
      | "address"
      | "cc"
      | "bcc"
      | "attachmentsName"
      | "attemptsCount"
      | "attemptsMade"
      | "caption"
      | "dateSent"
      | "deadline"
      | "from"
      | "status"
      | "updateTs"
      | "bodyContentType"
    >
  : V extends "sendingMessage.loadFromQueue"
  ? Pick<
      SendingMessage,
      | "id"
      | "version"
      | "createTs"
      | "createdBy"
      | "updateTs"
      | "updatedBy"
      | "deleteTs"
      | "deletedBy"
      | "address"
      | "from"
      | "cc"
      | "bcc"
      | "caption"
      | "contentText"
      | "status"
      | "dateSent"
      | "attachmentsName"
      | "deadline"
      | "attemptsCount"
      | "attemptsMade"
      | "headers"
      | "bodyContentType"
      | "attachments"
      | "contentTextFile"
    >
  : V extends "sendingMessage.loadContentText"
  ? Pick<SendingMessage, "id" | "contentTextFile" | "contentText">
  : never;
