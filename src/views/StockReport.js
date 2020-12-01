/**
 * @author: wang.weili
 * @since : 2020/12/1
 * @description: 库存查询数据项
 */
const sn_status = [{ state: 0, desc: '收货' }, { state: 1, desc: '收货过账' }, { state: 2, desc: '上架' }, { state: 3, desc: '下架' }, { state: 4, desc: '转换' }, { state: 5, desc: 'OQC验证' }, { state: 6, desc: 'OQC完成' }, { state: 7, desc: '称重' }, { state: 8, desc: '发货确认' }, { state: 9, desc: '发货过账' }, { state: 10, desc: '工单领料' }, { state: 100, desc: '发货完成' }];
// columns
const common = [{
        "label": "储位号",
        "value": "storehouseCode"
    }, {
        "label": "托盘号",
        "value": "palletNo"
    }, {
        "label": "成品料号",
        "value": "productPn"
    }, {
        "label": "项目名称",
        "value": "projectName"
    }, {
        "label": "物料描述",
        "value": "pnDescripe"
    }, {
        "label": "状态",
        "value": "statusName"
    }, {
        "label": "数量",
        "value": "qty"
    }, {
        "label": "储位数量",
        "value": "storehouseQty"
    }, {
        "label": "进仓时间",
        "value": "createTime"
    },{
        "label": "箱号",
        "value": "boxNo"
    }, {
        "label": "SN",
        "value": "sn"
    }, {
        "label": "工单号",
        "value": "productOrderGb"
    }
]
export default {}