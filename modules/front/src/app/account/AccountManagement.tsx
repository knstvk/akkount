import * as React from "react";
import { RouteComponentProps } from "react-router";
import { observer } from "mobx-react";
import AccountEdit from "./AccountEdit";
import AccountList from "./AccountList";

type Props = RouteComponentProps<{ entityId?: string }>;

@observer
export class AccountManagement extends React.Component<Props> {
  static PATH = "/accountManagement";
  static NEW_SUBPATH = "new";

  render() {
    const { entityId } = this.props.match.params;
    return (
      <>{entityId ? <AccountEdit entityId={entityId} /> : <AccountList />}</>
    );
  }
}
