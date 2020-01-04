import React from "react";
import {AccountBalanceVal, BalancePartProps} from "./AccountBalanceValue";

class BalanceAccounts extends React.Component<BalancePartProps> {

  render() {
    return (
      <ul>
        {this.props.values.map(value => this.renderAccount(value))}
      </ul>
    )
  }

  renderAccount(accountBal: AccountBalanceVal) {
    return (
      <li key={accountBal.name}><span>{accountBal.name}</span> <span>{accountBal.amount}</span> <span>{accountBal.currency}</span></li>
    )
  }
}

export default BalanceAccounts