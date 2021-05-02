import * as React from 'react';
import {
  Datagrid,
  DateField,
  EditButton,
  Filter,
  ListProps,
  ReferenceField,
  ReferenceInput,
  SelectInput,
  TextField,
  TextInput
} from 'react-admin';
import {UserCreatedTimeFilter} from "peacetrue-user";
import {PeaceList} from 'peacetrue-react-admin';
import {UserCreateFields} from "peacetrue-member";

const Filters = (props: any) => (
  <Filter {...props}>
    <TextInput source="code" allowEmpty alwaysOn resettable/>
    <ReferenceInput source="finalState" reference="enums/finalState" allowEmpty alwaysOn>
      <SelectInput source="code" optionText="name" resettable/>
    </ReferenceInput>
    {UserCreatedTimeFilter}
  </Filter>
);

export const OrderList = (props: ListProps) => {
  console.info('OrderList:', props);
  return (
    <PeaceList filters={<Filters/>} {...props} >
      <Datagrid rowClick="show">
        <TextField source="code"/>
        <ReferenceField source="goodsId" reference="goods">
          <TextField source="name"/>
        </ReferenceField>
        <TextField source="goodsCount"/>
        <TextField source="amount"/>
        <ReferenceField source="shippingAddressId" reference="contact-addresses">
          <TextField source="contactName"/>
        </ReferenceField>
        <TextField source="paymentAmount"/>
        <DateField source="paymentTime" showTime/>
        <ReferenceField source="node" reference="enums/orderNode">
          <TextField source="name"/>
        </ReferenceField>
        <ReferenceField source="finalState" reference="enums/finalState">
          <TextField source="name"/>
        </ReferenceField>
        {UserCreateFields}
        <EditButton/>
      </Datagrid>
    </PeaceList>
  )
};
