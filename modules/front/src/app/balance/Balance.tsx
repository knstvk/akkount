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

  componentDidMount(): void {
    restServices.akk_PortalService.getBalance(cubaREST)().then((value: string) => {
      console.log(value);
      this.setState({balance: JSON.parse(value)});
    });
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
}

export default Balance