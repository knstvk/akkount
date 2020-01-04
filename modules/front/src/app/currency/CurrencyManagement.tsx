import * as React from "react";
import { RouteComponentProps } from "react-router";
import { observer } from "mobx-react";
import CurrencyEdit from "./CurrencyEdit";
import CurrencyList from "./CurrencyList";

type Props = RouteComponentProps<{ entityId?: string }>;

@observer
export class CurrencyManagement extends React.Component<Props> {
  static PATH = "/currencyManagement";
  static NEW_SUBPATH = "new";

  render() {
    const { entityId } = this.props.match.params;
    return (
      <>{entityId ? <CurrencyEdit entityId={entityId} /> : <CurrencyList />}</>
    );
  }
}
