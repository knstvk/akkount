-- begin AKK_ACCOUNT
create table AKK_ACCOUNT (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(20) not null,
    DESCRIPTION varchar(100),
    CURRENCY_ID varchar(36) not null,
    CURRENCY_CODE varchar(3) not null,
    ACTIVE boolean,
    INCLUDE_IN_TOTAL boolean,
    --
    primary key (ID)
)^
-- end AKK_ACCOUNT
-- begin AKK_CURRENCY
create table AKK_CURRENCY (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CODE varchar(3) not null,
    NAME varchar(50),
    --
    primary key (ID)
)^
-- end AKK_CURRENCY
-- begin AKK_BALANCE
create table AKK_BALANCE (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    BALANCE_DATE date not null,
    ACCOUNT_ID varchar(36) not null,
    AMOUNT decimal(19, 2),
    --
    primary key (ID)
)^
-- end AKK_BALANCE
-- begin AKK_CATEGORY
create table AKK_CATEGORY (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(50) not null,
    DESCRIPTION varchar(100),
    CAT_TYPE varchar(50) not null,
    --
    primary key (ID)
)^
-- end AKK_CATEGORY
-- begin AKK_OPERATION
create table AKK_OPERATION (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    OP_TYPE varchar(50) not null,
    OP_DATE date not null,
    ACC1_ID varchar(36),
    ACC2_ID varchar(36),
    AMOUNT1 decimal(19, 2),
    AMOUNT2 decimal(19, 2),
    CATEGORY_ID varchar(36),
    COMMENTS varchar(200),
    --
    primary key (ID)
)^
-- end AKK_OPERATION
-- begin AKK_USER_DATA
create table AKK_USER_DATA (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    --
    USER_ID varchar(36) not null,
    KEY_ varchar(50) not null,
    VALUE_ varchar(500),
    --
    primary key (ID)
)^
-- end AKK_USER_DATA
