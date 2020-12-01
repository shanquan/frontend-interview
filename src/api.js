/**
 * @author: wang.weili
 * @since : 2020/11/12
 */
import axios from 'axios'
import { Message } from 'element-ui'

axios.defaults.baseURL = 'http://10.12.5.188:20002/'

axios.defaults.headers.post['Content-Type'] = 'application/json';

const MODELS = ['bydBdFactory', 'bydBdManufacturer', 'bydBdMaraType', 'bydBdMara', 'bydBdSupplier', 'bydBdWarehouseArea']
const PAGESIZE = 10;
const mock = true;

var token = "";

function getMethod(url) {
    let pathIdx = url.lastIndexOf("?");
    pathIdx = pathIdx > 0 ? pathIdx : url.length
    let arr = url.substring(0, pathIdx).split('/');
    return arr[arr.length - 2] + "/" + arr[arr.length - 1]
}
if (mock) {
    axios.defaults.baseURL = process.env.BASE_URL + 'mock/';
    axios.interceptors.request.use(function(config) {
        let qIdx = config.url.indexOf('?');
        let mockUrl = qIdx == -1 ? config.url.replace(/\//g, "_") : config.url.substring(0, qIdx).replace(/\//g, "_");
        let method = mockUrl.split('_')[1];
        if (method && ['save', 'updateById', 'removeById'].includes(method))
            mockUrl = 'success'
        config.url = mockUrl + '.json';
        config.method = 'get';
        return config;
    })
}

axios.interceptors.response.use(function(response) {
        if (response.data.RESULT != "PASS") {
            if (response.data.CODE == -1 && response.data.MESSAGE.indexOf('登录') > -1) {
                window.location.href = "/login";
                return;
            }
            Window.messageBox = Message({
                message: getMethod(response.config.url) + '接口报错，code:' + response.data.CODE + ',message:' + response.data.MESSAGE,
                type: "error",
                duration: 0,
                showClose: true
            });
            return Promise.reject(response.data);
        } else {
            if (Window.messageBox)
                Window.messageBox.close();
            return response.data;
        }
    },
    function(error) {
        Window.messageBox = Message({
            message: `code:${error.response.status},message:${error.toString()}`,
            type: "error",
            duration: 0,
            showClose: true
        });
        return Promise.reject({
            code: -1,
            message: error
        });
    });

export default {
    baseUrl: axios.defaults.baseURL,
    mock,
    token,
    login(data) {
        let promise = axios.post("user/login", data);
        return promise;
    },
    setConfig() {
        if (!mock) {
            axios.defaults.baseURL = 'http://10.12.5.188:20002/api/gb/'
            this.baseUrl = axios.defaults.baseURL;
        }
        axios.defaults.headers.post['token'] = this.token;
    },
    resetConfig() {
        if (!mock) {
            axios.defaults.baseURL = 'http://10.12.5.188:20002/'
            this.baseUrl = axios.defaults.baseURL;
        }
    },
    getList(model, data) {
        data = data || {}
        if (model && MODELS.includes(model)) {
            let promise = axios.post(`${model}/getList`, data);
            return promise;
        } else {
            Message.error("invalide model!")
        }
    },
    getPage(model, data, currentPageNo, pageSize) {
        data = data || {}
        if (model && MODELS.includes(model)) {
            Object.assign(data, {
                "currentPageNo": currentPageNo,
                "pageSize": pageSize || PAGESIZE
            })
            let promise = axios.post(`${model}/getPage`, data);
            return promise;
        } else {
            Message.error("invalide model!")
        }
    },
    getById(model, id) {
        if (model && MODELS.includes(model)) {
            let promise = axios.get(`${model}/getById?token=${this.token}&id=${id}`, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });
            return promise;
        } else {
            Message.error("invalide model!")
        }
    },
    save(model, data) {
        if (model && MODELS.includes(model)) {
            let promise = axios.post(`${model}/save`, data);
            return promise;
        } else {
            Message.error("invalide model!")
        }
    },
    updateById(model, data) {
        if (model && MODELS.includes(model)) {
            let promise = axios.post(`${model}/updateById`, data);
            return promise;
        } else {
            Message.error("invalide model!")
        }
    },
    removeById(model, id) {
        if (model && MODELS.includes(model)) {
            let promise = axios.get(`${model}/removeById?token=${this.token}&id=${id}`, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });
            return promise;
        } else {
            Message.error("invalide model!")
        }
    },
    /**
     * @api bydGbWarehouseStock_getStockReportL1ByPage
     * @description 分页获取库存报表 参数以换行符分割
     * @param {JSON} data 
     * @example {
                    "startRow":0,
                    "pageSize":6,
                    "projectName":"LOKI",  
                    "productPn":"12993819-00",
                    "storehouseCode":"",
                    "palletNo":"70112201121010714\n70112201123005985",
                    "boxNo":"BYDSM2020112110437",
                    "sn":"H97DQCRVQ1GC\nH97DQYEUQ1GC",
                    "productOrderGb":"4310550288\n4310550124"
                }
     */
    getStockReportL1ByPage(data, currentPageNo, pageSize) {
        let formParams = {
            "startRow": currentPageNo - 1,
            "pageSize": pageSize || PAGESIZE
        };
        for (let key in data) {
            formParams[key] = data[key]
        }
        let promise = axios.post('bydGbWarehouseStock/getStockReportL1ByPage', formParams);
        return promise;
    },
    /**
     * @api bydGbWarehouseStock_getStockReportL1Count
     * @description 获取库存报表数据数数量 参数以换行符分割
     * @param {JSON} data 
     */
    getStockReportL1Count(data) {
        let promise = axios.post('bydGbWarehouseStock/getStockReportL1Count', data);
        return promise;
    },
    /**
     * @api bydGbWarehouseStock_getStockReportL2
     * @description 根据仓储号和托盘号号获取物料数据
     * @param {JSON} data 
     * @example {
                    "storehouseCode":"123",
                    "palletNo":"70112201123005985"
                }
     */
    getStockReportL2(data) {
        let promise = axios.post('bydGbWarehouseStock/getStockReportL2', data);
        return promise;
    }
}