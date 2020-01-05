import React from "react";
import {AccountBalanceVal, BalancePartProps} from "./AccountBalanceValue";
import {Card} from "antd";

class BalanceTotals extends React.Component<BalancePartProps> {

  render() {
    return (
      <Card size="small" title="Total" style={{marginBottom: "12px"}}>
        <table>
          <tbody>
            {this.props.values ? this.props.values.map(value => BalanceTotals.renderRow(value)) : null}
          </tbody>
        </table>
      </Card>
    )
  }

  private static renderRow(accountBal: AccountBalanceVal) {
    return (
      <tr key={accountBal.currency}>
        <td style={{textAlign: "right", padding: "0 5px 0 10px"}}>{accountBal.amount}</td>
        <td>{accountBal.currency}</td>
      </tr>
    )
  }
}

export default BalanceTotals