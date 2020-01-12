import React from "react";
import {restServices} from "../../cuba/services"
import {cubaREST} from "../../index"
import {Card} from "antd";

interface State {
  balance: BalanceData[]
}

export type BalanceData = {
  totals: AccountBalance[];
  accounts: AccountBalance[];
}

export type AccountBalance = {
  name: string;
  currency: string;
  amount: number;
}

class Balance extends React.Component<any, State> {

  private numFormat: Intl.NumberFormat = new Intl.NumberFormat("en-US", {maximumFractionDigits: 0});

  constructor(props: any) {
    super(props);
    this.state = {balance: []};
  }

  render() {
    return (
      <div>
        <Card size="small" style={{marginBottom: "12px"}}>
          <table>
            <tbody>
              {this.state.balance.length === 0 ?
                <div>No data</div> :
                this.state.balance.map((value,idx) => this.renderGroup(value, idx))}
            </tbody>
          </table>
        </Card>
      </div>
    )
  }

  componentDidMount(): void {
    restServices.akk_BalanceService.getBalanceData(cubaREST)({date: new Date()}).then((value: string) => {
      console.log(value);
      this.setState({balance: JSON.parse(value)});
    });
  }

  private renderGroup(balanceData: BalanceData, idx: number) {
    return (
      <React.Fragment key={idx}>
        {balanceData.accounts.map(value => this.renderAccRow(value))}
        {balanceData.totals.map(value => this.renderTotalRow(value))}
        {idx < this.state.balance.length - 1 ? this.renderSeparatorRow() : null}
      </React.Fragment>
    );
  }

  private renderAccRow(bal: AccountBalance) {
    return (
      <tr key={bal.name}>
        <td>{bal.name}</td>
        <td style={{textAlign: "right", padding: "0 5px 0 10px"}}>{this.numFormat.format(bal.amount)}</td>
        <td style={{paddingRight: "4px"}}>{bal.currency}</td>
      </tr>
    )
  }

  private renderTotalRow(bal: AccountBalance) {
    return (
      <tr key={bal.currency} style={{backgroundColor: "rgb(240, 242, 245)", paddingRight: "5px"}}>
        <td/>
        <td style={{textAlign: "right", padding: "0 5px 0 10px", fontWeight: "bold"}}>{this.numFormat.format(bal.amount)}</td>
        <td style={{paddingRight: "4px", fontWeight: "bold"}}>{bal.currency}</td>
      </tr>
    )
  }

  private renderSeparatorRow() {
    return (
      <tr>
        <td colSpan={3}><span>&nbsp;</span></td>
      </tr>
    );
  }
}

export default Balance