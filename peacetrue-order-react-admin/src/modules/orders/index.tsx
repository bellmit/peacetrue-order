import * as React from "react";
import {Resource} from "react-admin";

import {OrderList} from './List';
import {OrderCreate} from './Create';
import {OrderEdit} from './Edit';
import {OrderShow} from './Show';

export const Order = {list: OrderList, create: OrderCreate, edit: OrderEdit, show: OrderShow};
export const OrderResource = <Resource name="orders" {...Order} />;
export default OrderResource;
export * from "./Messages"
