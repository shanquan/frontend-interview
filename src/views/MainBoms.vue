<!--
@author: wang.weili
@since : 2020/11/12
@description: 物料主资料
@todo: 导入、导出接口定义
-->
<template>
  <div class="mainboms">
    <el-form :inline="true" label-position="right" class="form-bar">
      <el-form-item>
        <QuerySelector
          :select="form.supplierId"
          placeholder="选择供应商"
          modelTag="bydBdSupplier"
          @change="onChangeId($event, 'supplierId')"
          @clear="onClear"
        ></QuerySelector>
      </el-form-item>
      <el-form-item>
        <el-select
          v-model="form.manufacturerId"
          :clearable="true"
          placeholder="选择制造商"
        >
          <el-option
            v-for="item in manufacturerList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          >
            <span class="float-left">{{ item.name }}</span>
            <span class="select-notes">{{ item.code }}</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <QuerySelector
          :select="form.maraTypeId"
          placeholder="选择物料类型"
          modelTag="bydBdMaraType"
          class="w100"
          @change="onChangeId($event, 'maraTypeId')"
        ></QuerySelector>
      </el-form-item>
      <el-form-item>
        <QuerySelector
          :select="form.warehouseAreaId"
          placeholder="选择库区"
          modelTag="bydBdWarehouseArea"
          class="w100"
          @change="onChangeId($event, 'warehouseAreaId')"
        ></QuerySelector>
      </el-form-item>
      <el-form-item>
        <MultiSelector
          :select="form.select1"
          :content="form.contentInput1"
          :selectList="selectList"
          @change="onChange($event, '1')"
        ></MultiSelector>
      </el-form-item>
      <el-form-item>
        <MultiSelector
          :select="form.select2"
          :content="form.contentInput2"
          :selectList="selectList"
          @change="onChange($event, '2')"
        ></MultiSelector>
      </el-form-item>
      <el-form-item>
        <MultiSelector
          :select="form.select3"
          :content="form.contentInput3"
          :selectList="selectList"
          @change="onChange($event, '3')"
        ></MultiSelector>
      </el-form-item>
      <el-form-item label="免检标识">
        <el-select class="w60" v-model="form.exemptChecked" :clearable="true">
          <el-option
            v-for="item in boolList"
            :key="item.$index"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="DC管控">
        <el-select class="w60" v-model="form.dcControl" :clearable="true">
          <el-option
            v-for="item in boolList"
            :key="item.$index"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="LC管控">
        <el-select class="w60" v-model="form.lcControl" :clearable="true">
          <el-option
            v-for="item in boolList"
            :key="item.$index"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <!-- <DateRange :type="form.dateType" start="" end="" @change="onDateChange($event)"></DateRange> -->
        <el-row type="flex">
          <div class="el-input-group__prepend" style="width: 60px">
            <el-select v-model="form.dateType">
              <el-option label="创建时间" value="create"></el-option>
              <el-option label="修改时间" value="update"></el-option>
            </el-select>
          </div>
          <el-col>
            <el-date-picker
              v-model="form.dateVal"
              type="daterange"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd HH:mm:ss"
              :default-time="['00:00:00', '23:59:59']"
            >
            </el-date-picker>
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search()">查询</el-button>
        <el-button type="primary" @click="goto(null, 'mainBom/add')"
          >新增</el-button
        >
        <el-button type="danger" @click="dels()">删除</el-button>
      </el-form-item>
      <el-form-item>
        <el-button @click="fExport()">导出</el-button>
        <el-upload
          class="import-btn"
          :action="uploadUrl"
          name="files"
          accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel"
          :show-file-list="false"
          :on-success="handleUploadSuccess"
          :before-upload="beforeUpload"
        >
          <el-button slot="trigger">导入</el-button>
        </el-upload>
        <el-link class="ml10" type="primary" href="./mock/mara_Template.xlsx"
          >导入模板</el-link
        >
      </el-form-item>
    </el-form>
    <!--表格 -->
    <el-table
      stripe
      :data="list"
      :loading="true"
      @selection-change="handleSelectionChange"
      style="width: 100%"
    >
      <el-table-column fixed="left" type="selection" width="50">
      </el-table-column>
      <el-table-column
        v-for="item in columnList"
        :key="item.$index"
        :prop="item.value"
        :label="item.label"
        :show-overflow-tooltip="true"
        :class-name="item.width ? '' : 'nowrap'"
        :width="item.width ? item.width : ''"
        align="center"
      >
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="120">
        <template slot-scope="scope">
          <!-- el-icon-tickets -->
          <!-- <el-button @click="goto(scope.row,'mainBom/detail')" icon="el-icon-thumb" size="small" circle></el-button>
          <el-button type="primary" @click="goto(scope.row,'mainBom/edit')" icon="el-icon-edit" size="small" circle></el-button>
          <el-button type="danger" @click="del(scope.row)" icon="el-icon-delete" size="small" circle></el-button>
          <el-button @click="del(scope.row)" icon="el-icon-printer" size="small" circle></el-button> -->
          <el-button
            @click="goto(scope.row, 'mainBom/detail')"
            type="text"
            size="small"
            >详情</el-button
          >
          <el-button
            @click="goto(scope.row, 'mainBom/edit')"
            type="text"
            size="small"
            >修改</el-button
          >
          <el-button @click="del(scope.row)" type="text" size="small"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      style="text-align: center"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[10, 20, 30, 40, 50]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>
  </div>
</template>

<script>
import MultiSelector from "@/components/MultiSelector.vue";
import QuerySelector from "@/components/QuerySelector.vue";
// import DateRange from "@/components/DateRange.vue";
import Items from "./MainBom";

export default {
  name: "MainBoms",
  created() {
    this.uploadUrl = this.$http.baseUrl + "importExport";
    this.columnList = Items.prefix1.concat(this.selectList, Items.suffix);
    this.getPage();
  },
  components: {
    MultiSelector,
    QuerySelector,
  },
  data() {
    return {
      form: {
        select1: "bpn",
        select2: "spn",
        select3: "apn",
        contentInput1: "",
        contentInput2: "",
        contentInput3: "",
        supplierId: "",
        manufacturerId: "",
        maraTypeId: "",
        warehouseAreaId: "",
        exemptChecked: "",
        dcControl: "",
        lcControl: "",
        dateType: "create",
        dateVal: [],
      },
      manufacturerList: [],
      boolList: Items.boollist,
      // table data
      list: [],
      checkedIds: [],
      pageSize: 10,
      currentPage: 1,
      total: 0,
      columnList: [],
      uploadUrl: "/importExport",
      // 多字段查询组件: 下拉列表项
      selectList: Items.common,
    };
  },
  methods: {
    search() {
      this.currentPage = 1;
      this.getPage();
    },
    getManufacturerList() {
      let formParams = {};
      if (this.form.supplierId === 0 || this.form.supplierId) {
        formParams.supplierId = this.form.supplierId;
        this.$http.getList("bydBdManufacturer", formParams).then((response) => {
          this.manufacturerList = response.DATA;
        });
      } else {
        this.manufacturerList = [];
      }
    },
    onClear() {
      this.form.manufacturerId = "";
      this.manufacturerList = [];
    },
    onChangeId(e, tag) {
      this.form[tag] = parseInt(e);
      if (tag == "supplierId") {
        this.getManufacturerList();
      }
    },
    onChange(e, idx) {
      let sel = `select${idx}`;
      let con = `contentInput${idx}`;
      this.form[sel] = e.select;
      this.form[con] = e.content;
    },
    getPage() {
      var formParams = {};
      try {
        if (this.form.contentInput1)
          formParams[this.form.select1] = this.form.contentInput1;
        if (this.form.contentInput2)
          formParams[this.form.select2] = this.form.contentInput2;
        if (this.form.contentInput3)
          formParams[this.form.select3] = this.form.contentInput3;
        if (this.form.supplierId === 0 || this.form.supplierId)
          formParams.supplierId = this.form.supplierId;
        if (this.form.manufacturerId === 0 || this.form.manufacturerId)
          formParams.manufacturerId = parseInt(this.form.manufacturerId);
        if (this.form.maraTypeId === 0 || this.form.maraTypeId)
          formParams.maraTypeId = this.form.maraTypeId;
        if (this.form.warehouseAreaId === 0 || this.form.warehouseAreaId)
          formParams.warehouseAreaId = this.form.warehouseAreaId;
        if (this.form.exemptChecked !== "") {
          formParams.exemptChecked = this.form.exemptChecked;
        }
        if (this.form.dcControl !== "") {
          formParams.dcControl = this.form.dcControl;
        }
        if (this.form.lcControl !== "") {
          formParams.lcControl = this.form.lcControl;
        }
        // dateRange
        if (this.form.dateVal[0]) {
          formParams[`${this.form.dateType}TimeBegin`] = this.form.dateVal[0];
          formParams[`${this.form.dateType}TimeEnd`] = this.form.dateVal[1];
        }
      } catch (e) {
        //console.error(e)
      }
      this.$http
        .getPage("bydBdMara", formParams, this.currentPage, this.pageSize)
        .then((response) => {
          this.list = response.DATA.page.map((el) => {
            if (el.exemptChecked != undefined) {
              el.exemptChecked = el.exemptChecked ? "是" : "否";
            }
            if (el.dcControl != undefined) {
              el.dcControl = el.dcControl ? "是" : "否";
            }
            if (el.lcControl != undefined) {
              el.lcControl = el.lcControl ? "是" : "否";
            }
            return el;
          });
          this.total = response.DATA.total;
        });
    },
    goto(row, path) {
      if (row && row.id) {
        this.$router.push({ path: `/${path}/${row.id}` });
      } else {
        this.$router.push({ path: `/${path}` });
      }
    },
    del(item) {
      this.$confirm(`将永久删除此物料主数据, 是否继续?`, "提示", {
        confirmButtonText: "删除",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.$http.removeById("bydBdMara", item.id).then(() => {
          this.currentPage = 1;
          this.getPage();
        });
      });
    },
    handleSelectionChange(val) {
      this.checkedIds = val.map((el) => el.id);
    },
    dels() {
      if (this.checkedIds.length) {
        this.$confirm(`将删除所选物料主数据, 是否继续?`, "提示", {
          confirmButtonText: "删除所选",
          cancelButtonText: "取消",
          type: "warning",
        }).then(() => {
          this.$http
            .removeListId("bydBdMara", this.checkedIds.join(","))
            .then(() => {
              this.currentPage = 1;
              this.getPage();
            });
        });
      } else {
        this.$message.error("请选择删除项！");
      }
    },
    fExport() {},
    handleUploadSuccess() {
      this.$message({
        message: "导入成功！",
        type: "success",
      });
      this.currentPage = 1;
      this.getPage();
    },
    beforeUpload(file) {
      const isExcel = [
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        "application/vnd.ms-excel",
      ].includes(file.type);
      const isLt1M = file.size / 1024 / 1024 < 1;
      if (!isExcel) {
        this.$message.error("上传文件只能是 EXCEL 格式!");
      }
      if (!isLt1M) {
        this.$message.error("上传文件大小不能超过 1MB!");
      }
      return isExcel && isLt1M;
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.getPage();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getPage();
    },
  },
};
</script>
