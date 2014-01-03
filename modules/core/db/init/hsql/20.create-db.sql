-- begin AKK_ACCOUNT
alter table AKK_ACCOUNT add constraint FK_AKK_ACCOUNT_CURRENCY_ID foreign key (CURRENCY_ID) references AKK_CURRENCY(ID)^
create index IDX_AKK_ACCOUNT_CURRENCY on AKK_ACCOUNT (CURRENCY_ID)^

-- end AKK_ACCOUNT

-- begin AKK_BALANCE
alter table AKK_BALANCE add constraint FK_AKK_BALANCE_ACCOUNT_ID foreign key (ACCOUNT_ID) references AKK_ACCOUNT(ID)^
create index IDX_AKK_BALANCE_ACCOUNT on AKK_BALANCE (ACCOUNT_ID)^
-- end AKK_BALANCE

-- begin AKK_OPERATION
alter table AKK_OPERATION add constraint FK_AKK_OPERATION_ACC1_ID foreign key (ACC1_ID) references AKK_ACCOUNT(ID)^
alter table AKK_OPERATION add constraint FK_AKK_OPERATION_ACC2_ID foreign key (ACC2_ID) references AKK_ACCOUNT(ID)^
alter table AKK_OPERATION add constraint FK_AKK_OPERATION_CATEGORY_ID foreign key (CATEGORY_ID) references AKK_CATEGORY(ID)^
create index IDX_AKK_OPERATION_ACC2 on AKK_OPERATION (ACC2_ID)^
create index IDX_AKK_OPERATION_ACC1 on AKK_OPERATION (ACC1_ID)^
create index IDX_AKK_OPERATION_CATEGORY on AKK_OPERATION (CATEGORY_ID)^

-- end AKK_OPERATION
