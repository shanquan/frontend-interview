/**
 * @author: wang.weili
 * @since : 2020/11/13
 * @description: 物料主资料表表格数据项
 */
const BOOLLIST = [{ label: "是", value: true }, { label: "否", value: false }];
const PREFIXLIST = [{
    "label": "免检标识",
    "value": "exemptChecked",
    "required": true,
    "type": "radio",
    "options": BOOLLIST
}, {
    "label": "DC管控",
    "value": "dcControl",
    "required": true,
    "type": "radio",
    "options": BOOLLIST
}, {
    "label": "LC管控",
    "value": "lcControl",
    "required": true,
    "type": "radio",
    "options": BOOLLIST
}]
export default {
    boollist: BOOLLIST,
    prefix: PREFIXLIST,
    prefix1: [{
        "label": "序号",
        "value": "id",
        "width": 60
    }, {
        "label": "供应商名称",
        "value": "supplierName"
    }, {
        "label": "制造商名称",
        "value": "manufacturerName"
    }, {
        "label": "制造商代码",
        "value": "manufacturerCode",
        "width": 120
    }].concat(PREFIXLIST),
    suffix: [{
        "label": "创建人",
        "value": "createName"
    }, {
        "label": "创建时间",
        "value": "createTime"
    }, {
        "label": "修改人",
        "value": "updateName"
    }, {
        "label": "修改时间",
        "value": "updateTime"
    }],
    common: [{
        "label": "比亚迪料号",
        "value": "bpn",
        "width": 120,
        "required": true
    }, {
        "label": "物料描述",
        "value": "description",
        "type": "textarea",
        "required": true
    }, {
        "label": "APN",
        "value": "apn",
        "width": 120,
        "required": true
    }, {
        "label": "供应商料号",
        "value": "spn",
        "width": 120,
        "required": true
    }, {
        "label": "单位",
        "value": "unit",
        "required": true
    }, {
        "label": "最小包装数量",
        "value": "pickMin",
        "type": "number",
        "required": true
    }, {
        "label": "最小包装单位",
        "value": "pickMinUnit",
        "required": true
    }, {
        "label": "箱包装数量",
        "value": "boxQty",
        "type": "number",
        "required": true
    }, {
        "label": "栈板包装数量",
        "value": "palletQty",
        "type": "number",
        "required": true
    }, {
        "label": "最小发货包装单位",
        "value": "deliveryMinUnit",
        "class": "lh20",
        "required": true
    }, {
        "label": "批次数量",
        "value": "batchQty",
        "required": true
    }, {
        "label": "水位下限",
        "value": "levelLower",
        "type": "number",
        "required": true
    }, {
        "label": "水位上限",
        "value": "levelHigher",
        "type": "number",
        "required": true
    }, {
        "label": "湿敏等级",
        "value": "moistureSensitiveLevel",
        "required": true
    }, {
        "label": "暴露时间上限",
        "value": "exposureTimeHigher",
        "type": "number",
        "required": true
    }, {
        "label": "有效期（天）",
        "value": "effective_days",
        "type": "number"
    }]
}