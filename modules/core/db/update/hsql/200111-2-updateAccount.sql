alter table AKK_ACCOUNT add column GROUP_ integer ^

update AKK_ACCOUNT set AKK_ACCOUNT.GROUP_ = case when INCLUDE_IN_TOTAL then 1 else 2 end ^