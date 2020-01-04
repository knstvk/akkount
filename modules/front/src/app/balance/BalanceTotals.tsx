import React from "react";
import {AccountBalanceVal, BalancePartProps} from "./AccountBalanceValue";

class BalanceTotals extends React.Component<BalancePartProps> {

  render() {
    return (
      <ul>
        {this.props.values.map(value => this.renderAccount(value))}
      </ul>
    )
  }

  renderAccount(accountBal: AccountBalanceVal) {
    return (
      <li key={accountBal.currency}><span>{accountBal.amount}</span> <span>{accountBal.currency}</span></li>
    )
  }
}

export default BalanceTotals