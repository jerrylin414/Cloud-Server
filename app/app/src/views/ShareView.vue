<template >
  <div>
    <div>
      <el-button class="fl ml-5" type="danger" @click="handleBack" v-if="isBack"
        >返回上一级</el-button
      >
    </div>

    <el-table
      :data="tableData"
      stripe
      style="width: 100%"
      @cell-mouse-enter="handleMouseEnter"
      @cell-mouse-leave="handleMouseLeave"
      ref="taskTable"
    >
      <el-table-column
        prop="name"
        label="文件名"
        width="400"
        suffix-icon="el-icon-search"
      >
        <template slot-scope="scope">
          <el-link
            type="primary"
            @click="handleOpen(scope.row)"
            :underline="false"
            class="mr-5"
          >
            <i :class="getIcon(scope.row.type)"></i>
          </el-link>
          {{ scope.row.name }}
        </template>
      </el-table-column>

      <el-table-column prop="date" label width="120">
        <template slot-scope="scope">
          <div
            @mouseenter="
              handleMouseEnter(scope.row, scope.column, scope.$index)
            "
            @mouseleave="
              handleMouseLeave(scope.row, scope.column, scope.$index)
            "
          >
            <div v-if="scope.row.isRevealed">
              <el-link
                type="primary"
                @click="handleDownload(scope.row)"
                :underline="false"
                class="mr-5"
              >
                <i class="el-icon-download"></i>
              </el-link>
            </div>
            <div v-else></div>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="date"
        label="修改日期"
        width="500"
        sortable
      ></el-table-column>

      <el-table-column prop="size" label="大小" sortable></el-table-column>
    </el-table>
  </div>
</template>
  
  <script>
import request from "@/utils/request";
import { formatteDate } from "@/utils/common";
import { mapGetters, mapState } from "vuex";
import { downloadMinio, getCodeT, getFolderFiles } from "@/api/share";
export default {
  name: "ShareView",
  data() {
    return {
      fileName: "",
      tableData: [],
      collapseBtn: "el-icon-s-fold",
      isCollapse: false,
      sideWidth: 200,
      isShowText: true,
      total: 0,
      pageNum: 1,
      pageSize: 5,
      dialogFormVisible: false,
      dialogEditFormVisible: false,
      formLabelWidth: "120px",
      form: {
        folderName: "",
      },
      resetForm: {
        folderName: "",
      },
      editForm: {
        folderName: "",
      },
      showAction: false,
      isRevealed: true,
      hoveredRowId: "",
      iconClass: "",
      isBack: false,
      editName: "",
      editNew: {},
    };
  },
  computed: {
    ...mapState("user", ["userInfo"]),
    ...mapGetters("menu", ["getLastEl", "getStack", "getCurId"]),
    ...mapGetters("user", ["getUser"]),
  },
  created() {
    this.getCode();
  },
  methods: {
    getIcon(fileType) {
      debugger;
      if (fileType.includes("image")) {
        return "el-icon-notebook-2";
      } else {
        return "el-icon-folder-opened";
      }
    },
    async getCode() {
      let code = this.$route.params.code || "";
      if (code.length > 5) {
        code = code.substring(5);
      }
      try {
        const res = await getCodeT(code);
        let list = [];
        if (res.code == 200) {
          let fileList = res.data.fileList;
          let folderList = res.data.folderList;
          if (fileList.length !== 0) {
            for (let i = 0; i < fileList.length; i++) {
              let file = fileList[i];
              let obj = this.transferFile(file);
              list.push(obj);
            }
          }
          if (folderList.length !== 0) {
            for (let i = 0; i < folderList.length; i++) {
              let folder = folderList[i];
              let obj = this.transferFolder(folder);
              list.push(obj);
            }
          }
        }
        this.tableData = list;
        this.checkFolderStack();
      } catch (error) {
        this.$message.error(error);
      }
    },
    revealRow(rowId, type) {
      const obj = this.tableData.find(
        (item) => item.id == rowId && item.type == type
      );
      obj.isRevealed = true;
    },
    hideRow(rowId, type) {
      const obj = this.tableData.find(
        (item) => item.id == rowId && item.type == type
      );
      obj.isRevealed = false;
    },
    handleMouseLeave(row, column, rowIndex) {
      this.hoveredRowId = null; // 清除悬停行的id
      this.hideRow(row.id, row.type); // 隐藏该行数据
    },
    handleMouseEnter(row, column, rowIndex) {
      this.hoveredRowId = row.id; // 设置当前悬停行的id
      this.revealRow(row.id, row.type); // 显示该行的操作图标
    },
    handleShare(row) {
      console.log("row", row);
    },
    callcoll() {
      debugger;
      this.isCollapse = !this.isCollapse;
      if (this.isCollapse) {
        this.sideWidth = 64;
        this.collapseBtn = "el-icon-s-unfold";
        this.isShowText = false;
      } else {
        this.sideWidth = 200;
        this.collapseBtn = "el-icon-s-fold";
        this.isShowText = true;
      }
    },
    successMes() {
      this.$message({
        message: "添加成功",
        type: "success",
        duration: 2000
      });
    },
    errorMes() {
      this.$message.error("服務器出錯");
    },
    transferFile(file) {
      let obj = {
        id: "",
        name: "",
        size: "",
        date: "",
        path: "",
        isRevealed: "",
        folderId: "",
      };
      obj.id = file.id;
      obj.name = file.fileName;
      obj.size = file.fileData;
      obj.path = file.filePath;
      obj.type = file.fileType;
      obj.date = formatteDate(file.createTime);
      obj.folderId = file.folderId;
      return obj;
    },
    transferFolder(folder) {
      let obj = {
        id: "",
        name: "",
        size: "",
        date: "",
        path: "",
        isRevealed: "",
        parentId: "",
      };
      obj.id = folder.id;
      obj.name = folder.name;
      obj.size = "40KB--";
      obj.path = "";
      obj.type = "folder";
      obj.date = formatteDate(folder.createTime);
      obj.parentId = folder.parentId;
      return obj;
    },
    async handleBack() {
      this.$store.commit("menu/remove");
      try{
        const res = await getFolderFiles(this.userInfo.id,this.getCurId)
        let list = [];
          if (res.code == 200) {
            debugger;
            let fileList = res.data.fileList;
            let folderList = res.data.folderList;
            if (fileList.length !== 0) {
              for (let i = 0; i < fileList.length; i++) {
                let file = fileList[i];
                let obj = this.transferFile(file);
                list.push(obj);
              }
            }
            if (folderList.length !== 0) {
              for (let i = 0; i < folderList.length; i++) {
                let folder = folderList[i];
                let obj = this.transferFolder(folder);
                list.push(obj);
              }
            }
          }
          this.tableData = list;
          this.checkFolderStack();
      }catch(error){
        this.$message.error(error)
      }
    },
    async handleOpen(row) {
      if (row.type == "folder") {
        let user = this.getUser;
        this.$store.commit("menu/setCurId", row.id);
        this.$store.commit("menu/insert", row.id);
        this.isBack = true;
        let list = [];
        try {
          const res = await getFolderFiles(user.id, row.id);
          if (res.code == 200) {
            let fileList = res.data.fileList;
            let folderList = res.data.folderList;
            if (fileList.length !== 0) {
              for (let i = 0; i < fileList.length; i++) {
                let file = fileList[i];
                let obj = this.transferFile(file);
                list.push(obj);
              }
            }
            if (folderList.length !== 0) {
              for (let i = 0; i < folderList.length; i++) {
                let folder = folderList[i];
                let obj = this.transferFolder(folder);
                list.push(obj);
              }
            }
          }
          this.tableData = list;
        } catch (error) {
          this.$message.error(error);
        }
      } else {
        this.$message.error("此文件不是文件夹");
      }
    },
    checkFolderStack() {
      const stack = this.getStack;
      if (stack.length > 1) {
        this.isBack = true;
      } else {
        this.isBack = false;
      }
    },
    initStack() {
      this.$store.commit("menu/initStack");
    },
    async handleDownload(row) {
      if (row.type == "folder") {
        this.$message.error("不可下载文件夹！");
        return;
      }
      try {
        const res = await downloadMinio(row);
        if (res.status == 200) {
          this.$message({
            type: "success",
            message: "正在下载中",
            duration: 2000
          });
          // 创建一个 Blob 对象
          const blob = new Blob([res.data], {
            type: res.headers["content-type"],
          });

          // 创建一个下载链接
          const downloadUrl = window.URL.createObjectURL(blob);
          const link = document.createElement("a");
          link.href = downloadUrl;

          // 假设后端返回的文件名已经正确编码在响应头中，我们可以直接获取并使用
          const contentDisposition = res.headers["content-disposition"];
          console.log("contentDisposition", contentDisposition);

          const filenameMatch =
            contentDisposition &&
            contentDisposition.match(
              /filename[^;=\n]*=((['"]).*?(['"])|[^;\n]*)/
            );
          console.log("filenameMatch", filenameMatch);
          const fileName =
            (filenameMatch && filenameMatch[1]) || "downloadedFile"; // 默认文件名
          console.log("fileName", fileName);
          link.setAttribute("download", fileName.trim().replace(/["']/g, "")); // 移除引号

          document.body.appendChild(link);
          link.click(); // 触发下载
          document.body.removeChild(link); // 清理

          // 释放 URL 对象
          window.URL.revokeObjectURL(downloadUrl);

          console.log("文件下載成功");
        } else {
          this.$message.error("服務器出錯！");
        }
      } catch (error) {
        this.$message.error(error);
      }
    },
    async getCurrentFiles(userId, curId) {
      try {
        const res = await getFolderFiles(userId, curId);
        let list = [];
        if (res.code == 200) {
          debugger;
          let fileList = res.data.fileList;
          let folderList = res.data.folderList;
          if (fileList.length !== 0) {
            for (let i = 0; i < fileList.length; i++) {
              let file = fileList[i];
              let obj = this.transferFile(file);
              list.push(obj);
            }
          }
          if (folderList.length !== 0) {
            for (let i = 0; i < folderList.length; i++) {
              let folder = folderList[i];
              let obj = this.transferFolder(folder);
              list.push(obj);
            }
          }
        }
        this.tableData = list;

        this.checkFolderStack();
      } catch (error) {
        this.$message.error(error);
      }
    },
  },
};
</script>
  
  <style>
</style>