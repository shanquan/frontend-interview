<!--
@author: wang.weili
@since : 2020/11/16
@description: 下拉搜选控件
-->
<template>
  <el-select 
    v-model="id"
    filterable
    :filter-method="filterMethod"
    :clearable="true"
    @focus="onFocus"
    @change="onChange"
    @clear="onClear"
    :placeholder="placeholder">
    <el-option
      v-for="item in filterList"
      :key="item.id"
      :label="item.name"
      :value="item.id">
      <span class="float-left">{{ item.name }}</span>
      <span class="select-notes">{{ item.code }}</span>
    </el-option>
  </el-select>
</template>

<script>

export default {
  name: 'querySelector',
  props: ['select','modelTag','placeholder'],
  data(){
    return{
      id: this.select===0||this.select?this.select.toString():"",
      filterList: [],
      list: []
    }
  },
  watch:{
    select(newVal){
      this.id = newVal===0||newVal?newVal.toString():"";
    }
  },
  mounted(){
    this.$http.getList(this.modelTag).then(response=>{
      this.filterList = response.DATA;
      this.list = response.DATA;
    })
  },
  methods:{
    filterMethod(qs){
      this.filterList = qs ? this.list.filter(el=>{return el.name.indexOf(qs)>-1||el.code.indexOf(qs)>-1}):this.list;
    },
    onFocus(){
      this.filterList = this.list;
    },
    onChange(){
      this.$emit('change',this.id);
    },
    onClear(){
      this.$emit('clear');
    }
  }
}
</script>
