<!--
@author: wang.weili
@since : 2020/11/18
@description: 日期区段选择控件
@notice: no sample or test
-->
<template>
  <el-row type="flex">
    <div class="el-input-group__prepend" style="width:60px">
        <el-select v-model="dateType" @change="onChange">
          <el-option label="创建时间" value="create"></el-option>
          <el-option label="修改时间" value="update"></el-option>
        </el-select>
    </div>
    <el-col :span="9">
      <el-date-picker type="date" placeholder="开始日期" 
        v-model="startDate" 
        value-format="yyyy-MM-dd" 
        @change="onChange"
        :picker-options="{disabledDate:disabledStartDate}" style="width: 100%;"></el-date-picker>
    </el-col>
    <el-col class="line center" :span="2">-</el-col>
    <el-col :span="9">
      <el-date-picker type="date" placeholder="结束日期" 
        v-model="endDate" 
        value-format="yyyy-MM-dd"
        @change="onChange"
        :picker-options="{disabledDate:disabledEndDate}" style="width: 100%;"></el-date-picker>
    </el-col>
  </el-row>
</template>

<script>

export default {
  name: 'dateRange',
  props: ['type','start','end'],
  data(){
    return{
      dateType: this.type,
      startDate: this.start,
      endDate: this.end,
    }
  },
  methods:{
    disabledStartDate(dm){
      if(this.endDate){
        return dm > new Date(this.endDate)
      }else{
        return dm > new Date()
      }
    },
    disabledEndDate(dm){
      if(this.startDate){
        let sd = new Date(this.startDate);
        sd.setDate(sd.getDate()-1);
        return dm < sd || dm > new Date()
      }else{
        return dm > new Date()
      }
    },
    onChange(){
      let formParams = {};
      if(this.startDate)
        formParams[`${this.type}TimeBegin`]=this.startDate+" 00:00:00";
      if(this.endDate)
        formParams[`${this.type}TimeEnd`] = this.endDate+" 23:59:59";
      // console.log(formParams)
      this.$emit('change',formParams);
    }
  }
}
</script>
