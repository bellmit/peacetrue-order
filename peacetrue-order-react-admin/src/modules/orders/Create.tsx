import * as React from 'react';
import {
  Create,
  CreateProps,
  DateTimeInput,
  maxLength,
  minValue,
  NumberInput,
  ReferenceInput,
  required,
  SelectInput,
  SimpleForm,
  TextInput
} from 'react-admin';

export const OrderCreate = (props: CreateProps) => {
  console.info('OrderCreate:', props);
  return (
    <Create {...props}>
      <SimpleForm>
        <ReferenceInput source="goodsId" reference="goods" validate={[required(),]}>
          <SelectInput optionText="name" resettable/>
        </ReferenceInput>
        <NumberInput source="goodsCount" validate={[required(), minValue(1)]} min={1}/>
        <ReferenceInput source="shippingAddressId" reference="contact-addresses" validate={[required(),]}>
          <SelectInput optionText="contactName" resettable/>
        </ReferenceInput>
        <NumberInput source="amount" validate={[required()]} min={0} step={0.01}/>
        <NumberInput source="paymentAmount" validate={[required()]} min={0} step={0.01}/>
        <DateTimeInput source="paymentTime" validate={[required()]}/>
        <TextInput source="remark" validate={[maxLength(255)]} resettable fullWidth/>
      </SimpleForm>
    </Create>
  );
};
