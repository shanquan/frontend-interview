/**
 * @author: wang.weili
 * @since : 2020/11/24
 * @description: 菜单配置
 */
const PACKAGENAME = "com.byd.mes3";
export default {
    packageName: PACKAGENAME,
    mobileList: [{
        "title": "工单发货",
        "icon": "el-icon-s-unfold", //el-icon-ship
        "index": "/productOrderDelivery"
    }, {
        "title": "库存管理",
        "icon": "el-icon-box",
        "index": "stockManage",
        "subs": [{
            "title": "库存状态调整",
            "index": "/stockAdjust"
        }]
    }],
    menuList: [{
            "title": "信息配置",
            "icon": "el-icon-tickets",
            "index": "base",
            "subs": [{
                "title": "物料信息",
                "index": "/mainBoms"
            }, {
                "title": "供应商",
                "index": "/supplier"
            }]
        }, {
            "title": "报表",
            "icon": "el-icon-data-analysis",
            "index": "report",
            "subs": [{
                "title": "库存查询",
                "index": "/stockReport"
            }]
        }, {
            "title": "PDA操作",
            "icon": "el-icon-mobile",
            "index": "pda",
            "subs": []
        }
        // ,{
        //   "title":"",
        //   "icon": "el-icon-star-on",
        //   "subs":[{
        //     "title":"",
        //     "index":""
        //   }]
        // }
    ]
}