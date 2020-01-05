import React from "react";
import {AccountBalanceVal, BalancePartProps} from "./AccountBalanceValue";
import {Card} from "antd";

class BalanceAccounts extends React.Component<BalancePartProps> {

  render() {
    return (
      <Card size="small" title={this.props.title} style={{marginBottom: "12px"}}>
        <table>
          <tbody>
            {this.props.values.map(value => BalanceAccounts.renderRow(value))}
          </tbody>
        </table>
      </Card>
    )
  }

  private static renderRow(accountBal: AccountBalanceVal) {
    return (
      <tr key={accountBal.name}>
        <td>{accountBal.name}</td>
        <td style={{textAlign: "right", padding: "0 5px 0 10px"}}>{accountBal.amount}</td>
        <td>{accountBal.currency}</td>
      </tr>
    )
  }
}

export default BalanceAccounts