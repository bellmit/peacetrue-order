import * as React from 'react';
import {PeaceShow} from 'peacetrue-react-admin';
import {DateField, NumberField, ReferenceField, ShowProps, SimpleShowLayout, TextField} from 'react-admin';
import {UserCreateModifyFields} from "peacetrue-member";

export const OrderShow = (props: ShowProps) => {
  console.info('OrderShow:', props);
  return (
    <PeaceShow {...props}>
      <SimpleShowLayout>
        <TextField source="code"/>
        <ReferenceField source="goodsId" reference="goods" link={'show'}>
          <TextField source="name"/>
        </ReferenceField>
        <TextField source="goodsCount"/>
        <ReferenceField source="shippingAddressId" reference="contact-addresses" link={'show'}>
          <TextField source="contactName"/>
        </ReferenceField>
        <NumberField source="amount"/>
        <NumberField source="paymentAmount"/>
        <DateField source="paymentTime" showTime/>
        <ReferenceField source="node" reference="enums/orderNode" link={false}>
          <TextField source="name"/>
        </ReferenceField>
        <ReferenceField source="tenseState" reference="enums/tense" link={false}>
          <TextField source="name"/>
        </ReferenceField>
        <ReferenceField source="finalState" reference="enums/finalState" link={false}>
          <TextField source="name"/>
        </ReferenceField>
        <TextField source="remark"/>
        {UserCreateModifyFields}
      </SimpleShowLayout>
    </PeaceShow>
  );
};
