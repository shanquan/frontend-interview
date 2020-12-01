<!--
@author: wang.weili
@since : 2020/11/12
@description: 物料主增改查
-->
<template>
  <div class="pda-box mainBom">
    <el-form ref="form" :model="form" :rules="rules" :class="$route.params.type=='detail'?'detail':''" label-width="110px">
      <el-form-item label="供应商" prop="supplierId">
        <el-input  v-if="$route.params.type=='detail'" v-model="form.supplierName" disabled></el-input>
        <QuerySelector v-else :select="form.supplierId" placeholder="选择供应商" modelTag="bydBdSupplier" @change="onChangeId($event,'supplierId')" @clear="onClear"></QuerySelector>
      </el-form-item>
      <el-form-item label="制造商" prop="manufacturerId">
        <el-input  v-if="$route.params.type=='detail'" v-model="form.manufacturerName" disabled></el-input>
        <el-select v-else
          v-model="form.manufacturerId"
          :clearable="true"
          placeholder="选择制造商">
          <el-option
            v-for="item in manufacturerList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
            <span class="float-left">{{ item.name }}</span>
            <span class="select-notes">{{ item.code }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="物料类型" prop="maraTypeId">
        <el-input  v-if="$route.params.type=='detail'" v-model="form.maraTypeName" disabled></el-input>
        <QuerySelector v-else :select="form.maraTypeId" placeholder="选择物料类型" modelTag="bydBdMaraType" @change="onChangeId($event,'maraTypeId')"></QuerySelector>
      </el-form-item>
      <el-form-item label="库区" prop="warehouseAreaId">
        <el-input  v-if="$route.params.type=='detail'" v-model="form.warehouseAreaName" disabled></el-input>
        <QuerySelector v-else :select="form.warehouseAreaId" placeholder="选择库区" modelTag="bydBdWarehouseArea" @change="onChangeId($event,'warehouseAreaId')"></QuerySelector>
      </el-form-item>
      <el-form-item v-for="item in rowList" :key="item.$index" :label="item.label" :prop="item.value" :class="item.class">
        <el-input v-if="item.type==undefined||item.type=='number'||item.type=='textarea'" :type="item.type" v-model="form[item.value]" :disabled="$route.params.type=='detail'"></el-input>
        <el-radio-group v-if="item.type=='radio'" v-model="form[item.value]" :disabled="$route.params.type=='detail'">
          <el-radio v-for="op in item.options" :key="op.$index" :label="op.value">{{op.label}}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" v-if="$route.params.type=='add'||$route.params.type=='edit'" @click="onSubmit()">保存</el-button>
        <el-button type="primary" v-if="$route.params.type=='add'" @click="onReset()">重置</el-button>
        <el-button @click="goBack">{{$route.params.type=='edit'?"取消":"返回"}}</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import QuerySelector from "@/components/QuerySelector.vue";
import Items from "./MainBom";

export default {
  name: 'mainBom',
  created(){
    this.rowList = Items.prefix.concat(Items.common);
    this.setRules();
    switch(this.$route.params.type){
      case 'add': break;
      case 'edit':
        this.getData();break;
      case 'detail':
        this.rowList = this.rowList.concat(Items.suffix);
        this.getData();break;
    }
  },
  components: {
    QuerySelector
  },
  data(){
    return{
      rules:{
        "supplierId":  [this.$root.requireRule],
        "manufacturerId":  [this.$root.requireRule],
        "maraTypeId":  [this.$root.requireRule],
        "warehouseAreaId":  [this.$root.requireRule],
        "levelLower": [{ validator: (rule, value, callback) => {
          if (this.form.levelHigher && value > this.form.levelHigher) {
            callback(new Error('水位下限不能高于水位上限'));
          } else {
            callback();
          }
        }, trigger: 'blur' }],
        "levelHigher": [{ validator: (rule, value, callback) => {
          if (this.form.levelLower && value < this.form.levelLower) {
            callback(new Error('水位上限不能低于水位下限'));
          } else {
            callback();
          }
        }, trigger: 'blur' }]
      },
      form:{
        "supplierId": "",
        "manufacturerId": "",
        "maraTypeId": "",
        "warehouseAreaId": "",
        exemptChecked: "",
        dcControl:"",
        lcControl:"",
        "bpn": "",
        "description": "",
        "apn": "",
        "spn": "",
        "unit": "",
        "pickMin": "",
        "pickMinUnit": "",
        "boxQty": "",
        "palletQty": "",
        "deliveryMinUnit": "",
        "batchQty": "",
        "levelLower": "",
        "levelHigher": "",
        "moistureSensitiveLevel": "",
        "exposureTimeHigher": "",
        "effective_days": "",
        "createName": "",
        "createTime": "",
        "updateName": "",
        "updateTime": "",
        "supplierName": "",
        "manufacturerName": "",
        "maraTypeName": "",
        "wareHouseAreaName": ""
      },
      manufacturerList:[],
      rowList:[]
    }
  },
  methods:{
    getData(){
      this.$http.getById('bydBdMara',this.$route.params.id).then(response=>{
        Object.assign(this.form,response.DATA);
        if(this.$route.params.type=='edit'){
          this.form.manufacturerId = this.form.manufacturerId.toString();
          this.getManufacturerList();
        }
      })
    },
    getManufacturerList(){
      let formParams={};
      if(this.form.supplierId===0||this.form.supplierId){
        formParams.supplierId = this.form.supplierId;
        this.$http.getList('bydBdManufacturer',formParams).then(response=>{
          this.manufacturerList = response.DATA;
        })
      }else{
        this.manufacturerList = [];
      }
    },
    onClear(){
      this.form.manufacturerId = "";
      this.manufacturerList = [];
    },
    onChangeId(e,tag){
      this.form[tag] = parseInt(e);
      if(tag=="supplierId"){
        this.getManufacturerList();
      }
    },
    setRules(){
      this.rowList.filter(el=>el.required).forEach(el=>{
        if(this.rules[el.value]&&this.rules[el.value].length){
          this.rules[el.value].push(this.$root.requireRule);
        }else{
          this.rules[el.value] = [this.$root.requireRule];
        }
      })
    },
    onSubmit(){
      // 字符串转数字,Id
      // this.rowList.filter(el=>{return el.type=='number'}).forEach(el=>{
      //   this.form[el.value] = Number(this.form[el.value]);
      // })
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.form.manufacturerId = parseInt(this.form.manufacturerId)
          if(this.$route.params.type=='add'){
            this.$http.save('bydBdMara',this.form).then(() => {
              this.$message({
                message: '新增成功！',
                type: 'success'
              });
              this.onReset();
            })
          }else if(this.$route.params.type=='edit'){
            this.$http.updateById('bydBdMara',this.form)
            .then(() => {
              this.$message({
                message: '保存成功！',
                type: 'success'
              });
            })
          }
        } else {
          return false;
        }
      });
    },
    onReset(){
      this.$refs.form.resetFields();
    },
    goBack(){
      this.$router.push({'path':"/mainBoms"})
    }
  }
}
</script>
