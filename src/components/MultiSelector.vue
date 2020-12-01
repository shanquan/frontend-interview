<!--
@author: wang.weili
@since : 2020/11/13
@description: 多字段查询控件
-->
<template>
  <el-input :type="isNumber?'number':'text'" :clearable="true" placeholder="请输入内容" v-model="contentInput" @change="onIptChange" class="input-with-select">
    <el-select v-model="type" @change="onTypeChange" slot="prepend" placeholder="请选择">
      <el-option v-for="item in selectList" :key="item.$index" :label="item.label" :value="item.value"></el-option>
    </el-select>
  </el-input>
</template>

<script>
export default {
  name: 'multiSelector',
  props: ['select','content','selectList'],
  data(){
    return{
      isNumber: false,
      type: this.select,
      contentInput: this.content
    }
  },
  methods:{
    onTypeChange(){
      this.contentInput = "";
      this.isNumber = this.selectList.find(el=>el.value==this.type).type=='number';
      this.$emit('change',{
          select: this.type,
          content: this.isNumber?Number(this.contentInput):this.contentInput
      });
    },
    onIptChange(){
      this.$emit('change',{
          select: this.type,
          content: this.isNumber?Number(this.contentInput):this.contentInput
      });
    }
  }
}
</script>
