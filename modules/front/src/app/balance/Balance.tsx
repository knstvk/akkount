import React from "react";
import {restServices} from "../../cuba/services"
import {cubaREST} from "../../index"
import {AccountBalanceVal} from "./AccountBalanceValue";
import BalanceTotals from "./BalanceTotals";
import BalanceAccounts from "./BalanceAccounts";

interface BalanceVal {
  excludedAccounts: AccountBalanceVal[];
  includedAccounts: AccountBalanceVal[];
  totals: AccountBalanceVal[];
}

interface BalState {
  balance: BalanceVal
}

class Balance extends React.Component<any, BalState> {

  constructor(props: any) {
    super(props);
    this.state = {balance: {excludedAccounts: [], includedAccounts: [], totals: []}}
  }

  render() {
    return (
      <div>
        <BalanceTotals values={this.state.balance.totals}/>
        <BalanceAccounts title="Included in Total" values={this.state.balance.includedAccounts}/>
        <BalanceAccounts title="Not Included in Total" values={this.state.balance.excludedAccounts}/>
      </div>
    )
  }

  componentDidMount(): void {
    restServices.akk_PortalService.getBalance(cubaREST)().then((value: string) => {
      this.setState({balance: JSON.parse(value)});
    });
  }
}

export default Balance