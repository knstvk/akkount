export interface AccountBalanceVal {
    amount: string;
    name: string;
    currency: string;
}

export interface BalancePartProps {
  values: AccountBalanceVal[]
}
