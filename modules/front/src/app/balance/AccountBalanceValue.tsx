export interface AccountBalanceVal {
    amount: string;
    name: string;
    currency: string;
}

export interface BalancePartProps {
  title?: string,
  values: AccountBalanceVal[]
}
