<!--
@author: wang.weili
@since : 2020/11/17
@description: 自动补全控件（远程搜索），类同自动提示下拉列表控件，区别是可以支持自定义输入内容
@notice: no sample or test
-->
<template>
  <el-autocomplete
    popper-class="m-autocomplete"
    v-model="id"
    :fetch-suggestions="querySearchAsync"
    :placeholder="placeholder"
    @select="handleSelect"
    clearable
    @clear="onClear"
  >
  <template slot-scope="{ item }">
    <div class="name">{{ item.name }}</div>
    <span class="code">{{ item.code }}</span>
  </template>
  </el-autocomplete>
</template>

<script>

export default {
  name: 'querySelector',
  props: ['select','modelTag','placeholder'],
  data(){
    return{
      id: this.select
    }
  },
  methods:{
    querySearchAsync(qs,cb){
      let formParams={};
      if(qs)
      formParams.name = qs;
      this.$http.getList(this.modelTag,formParams).then(response=>{
        cb(response.DATA);
      })
    },
    handleSelect(item){
      this.id = item.id;
      this.$emit('change',this.id);
    },
    onClear(){
      this.$emit('clear');
    }
  }
}
</script>
