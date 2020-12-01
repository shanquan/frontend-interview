<!--
@author: wang.weili
@since : 2020/11/25
@description: 供应商查询
-->
<template>
  <div class="Supplier">
    <el-form :inline="true" label-position="right" :model="form" class="form-bar">
      <el-form-item label="供应商代码">
        <el-input :clearable="true" v-model="form.code" placeholder="供应商代码"></el-input>
      </el-form-item>
      <el-form-item label="供应商名称">
        <el-input :clearable="true" v-model="form.name" placeholder="供应商名称"></el-input>
      </el-form-item>  
      <el-form-item>
        <el-button type="primary" @click="search()">查询</el-button>
        <el-button @click="fExport()">导出</el-button>
      </el-form-item>
    </el-form>
    <!--表格 -->
    <el-table
      stripe
      border
      :data="list"
      :loading="true"
      style="width: 100%">
      <el-table-column
        prop="id"
        label="序号"
        width="60">
      </el-table-column>
      <el-table-column
        prop="code"
        label="供应商代码"
        width="100">
      </el-table-column>
      <el-table-column
        prop="name"
        :show-overflow-tooltip="true"
        class-name="nowrap"
        label="供应商名称">
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="100">
        <template slot-scope="scope">
          <el-button @click="del(scope.row)" type="text" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="text-align: center;" @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage"
    :page-sizes="[10, 20, 30, 40, 50]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
    :total="total">
    </el-pagination>
  </div>
  
</template>

<script>
export default {
  name: 'Supplier',
  created(){
    this.getPage();
  },
  data(){
    return{
      list:[],
      pageSize: 10,
      currentPage: 1,
      total: 0,
      form:{
        code:"",
        name:""
      }
    }
  },
  methods:{
    search(){
      this.currentPage=1;
      this.getPage();
    },
    getPage(){
      var formParams={};
      if(this.form.code)
      formParams.code=this.form.code;
      if(this.form.name)
      formParams.name=this.form.name;
      this.$http.getPage('bydBdSupplier',formParams,this.currentPage,this.pageSize).then(response=>{
        this.list = response.DATA.page;
        this.total = response.DATA.total;
      })
    },
    del(item){
      this.$confirm(`将永久删除此供应商, 是否继续?`, '提示', {
          confirmButtonText: '删除',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http.removeById('bydBdSupplier',item.id).then(response=>{
            this.currentPage=1;
            this.getPage();
          })
        })
    },
    fexport(){

    },
    handleSizeChange(val){
      this.pageSize = val;
      this.getPage();
    },
    handleCurrentChange(val){
      this.currentPage = val;
      this.getPage();
    }
  }
}
  </script>