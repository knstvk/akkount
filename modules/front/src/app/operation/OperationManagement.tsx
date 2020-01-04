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

  render() {
    const { entityId } = this.props.match.params;
    return (
      <>
        {entityId ? <OperationEdit entityId={entityId} /> : <OperationList />}
      </>
    );
  }
}
