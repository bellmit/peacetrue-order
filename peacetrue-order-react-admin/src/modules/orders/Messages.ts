import {Messages} from "peacetrue-user";

export const OrderMessages = {
    resources: {
        "orders": {
            name: '订单',
            fields: {
                'id': '主键',
                'code': '编号',
                'goodsId': '商品',
                'goodsCount': '件数',
                'shippingAddressId': '收货地址',
                'amount': '金额(元)',
                'paymentAmount': '付款金额(元)',
                'paymentTime': '付款时间',
                'node': '环节',
                'tenseState': '时态',
                'finalState': '终态',
                'remark': '备注',
                ...Messages
            },
        }
    }
}

//['id','code','goodsId','goodsCount','shippingAddressId','amount','paymentAmount','paymentTime','node','tenseState','finalState','remark','creatorId','createdTime','modifierId','modifiedTime',]
//['主键','编号','商品','商品件数','收货地址','金额(元)','付款金额(元)','付款时间','环节','时态','终态','备注','创建者','创建时间','最近修改者','最近修改时间',]

export default OrderMessages;
