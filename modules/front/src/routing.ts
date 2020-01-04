import { AccountManagement } from "./app/account/AccountManagement";
import { CurrencyManagement } from "./app/currency/CurrencyManagement";
import { CategoryManagement } from "./app/category/CategoryManagement";
import { getMenuItems } from "@cuba-platform/react";

export const menuItems = getMenuItems();

// Code below demonstrates how we can create SubMenu section
// Remove '/*' '*/' comments and restart app to get this block in menu

/*
// This is RouteItem object that we want to see in User Settings sub menu
const backToHomeRouteItem = {
  pathPattern: "/backToHome",
  menuLink: "/",
  component: null,
  caption: "home"
};
// SubMenu object
const userSettingsSubMenu = {
  caption: 'UserSettings', // add router.UserSettings key to src/i18n/en.json for valid caption
  items: [backToHomeRouteItem]};
// Add sub menu item to menu config
menuItems.push(userSettingsSubMenu);
*/

menuItems.push({
  pathPattern: "/accountManagement/:entityId?",
  menuLink: "/accountManagement",
  component: AccountManagement,
  caption: "AccountManagement"
});

menuItems.push({
  pathPattern: "/categoryManagement/:entityId?",
  menuLink: "/categoryManagement",
  component: CategoryManagement,
  caption: "CategoryManagement"
});

menuItems.push({
  pathPattern: "/currencyManagement/:entityId?",
  menuLink: "/currencyManagement",
  component: CurrencyManagement,
  caption: "CurrencyManagement"
});
