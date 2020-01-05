import * as React from "react";
import { RouteComponentProps } from "react-router";
import { observer } from "mobx-react";
import OperationEdit from "./OperationEdit";
import OperationList from "./OperationList";

type Props = RouteComponentProps<{ entityId?: string }>;

@observer
export class OperationManagement extends React.Component<Props> {
  static PATH = "/operationManagement";
  static NEW_SUBPATH = "new";
  static NEW_EXPENSE = "new_expense";
  static NEW_INCOME = "new_income";
  static NEW_TRANSFER = "new_transfer";

  render() {
    const { entityId } = this.props.match.params;
    return (
      <>
        {entityId ? <OperationEdit entityId={entityId} /> : <OperationList />}
      </>
    );
  }
}
