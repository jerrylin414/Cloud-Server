<template >
  <div>
    <div class="el-box">
      <el-input
        style="width: 200px"
        placeholder="Please input your file name"
        suffix-icon="el-icon-search"
        v-model="fileName"
      ></el-input>
      <el-button @click="handleSearch">Search</el-button>
      <el-button type="primary" @click="handleAdd">Create Folder</el-button>
      <!-- <el-upload
        ref="uploadRef"
        class="upload-demo"
        action=""
        :http-request="handleFileUpload"
        :limit="1"
      >
        <el-button type="success">上传文件</el-button>
      </el-upload>-->

      <el-button @click="drawer = true" type="success">Upload File</el-button>
    </div>

    <div>
      <el-button
        class="fl ml-5 mt-10"
        type="danger"
        @click="handleBack"
        v-if="isBack"
        >Back</el-button
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
        label="file name"
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
                @click="handleShare(scope.row)"
                :underline="false"
                class="mr-5"
              >
                <i class="el-icon-share"></i>
              </el-link>
              <el-link
                type="primary"
                @click="downloadFile(scope.row)"
                :underline="false"
                class="mr-5"
              >
                <i class="el-icon-download"></i>
              </el-link>
              <el-link
                type="primary"
                @click="handleDelete(scope.row)"
                :underline="false"
                class="mr-5"
              >
                <i class="el-icon-delete"></i>
              </el-link>
              <el-link
                type="primary"
                @click="handleEdit(scope.row)"
                :underline="false"
                class="mr-5"
              >
                <i class="el-icon-edit"></i>
              </el-link>
              <el-link
                type="primary"
                @click="handleMove(scope.row)"
                :underline="false"
                class="mr-5"
              >
                <i class="el-icon-right"></i>
              </el-link>
            </div>
            <div v-else></div>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="date"
        label="Modification date"
        width="500"
        sortable
      ></el-table-column>

      <el-table-column prop="size" label="size" sortable></el-table-column>
      <el-table-column>
        <template slot-scope="scope">
          <el-progress
            :percentage="scope.row.downloadProgress"
            v-if="scope.row.isDownloading"
            type="line"
          ></el-progress>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      title="create new folder"
      :visible.sync="dialogFormVisible"
      width="20%"
    >
      <el-form :model="form" label-width="80px" size="large">
        <el-form-item label="File Name">
          <el-input v-model="form.folderName" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submitForm">Confirm</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="Edit Folder"
      :visible.sync="dialogEditFormVisible"
      width="20%"
    >
      <el-form :model="editForm" label-width="80px" size="large">
        <el-form-item label="File Name">
          <el-input v-model="editForm.folderName" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogEditFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="editSubmit">Confirm</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="Move to"
      :visible.sync="dialogFolderFormVisible"
      width="20%"
    >
      <el-tree
        :data="folderData"
        :props="defaultProps"
        accordion
        @node-click="handleNodeClick"
      ></el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFolderFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="folderSubmit">Confirm</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="valid days"
      :visible.sync="dialogShareFormVisible"
      width="40%"
      @close="afterClose"
    >
      <div>
        <el-radio-group v-model="radio">
          <el-radio label="1">1day</el-radio>
          <el-radio label="10">10days</el-radio>
          <el-radio label="30">30days</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-top: 10px" v-if="isUrl">
        Link:
        <el-input
          v-model="shareUrl"
          ref="copyInput"
          style="width: 300px"
        ></el-input>
        <el-link
          type="primary"
          :underline="false"
          class="ml-5"
          @click="copyText"
          >Copy</el-link
        >
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogShareFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="createShare">create link</el-button>
      </div>
    </el-dialog>

    <el-drawer title="upload-title" :visible.sync="drawer" :with-header="false">
      <el-card style="width: 80%; margin: 80px auto" header="Upload File">
        <el-upload
          class="upload-demo"
          drag
          action="/"
          multiple
          :http-request="handleHttpRequest"
          :on-remove="handleRemoveFile"
        >
          <el-icon class="el-icon--upload">
            <upload-filled />
          </el-icon>
          <div class="el-upload__text">
            Please drag the file here or
            <em>click here to upload it</em>
          </div>
        </el-upload>
      </el-card>
    </el-drawer>
  </div>
</template>

<script>
import request from "@/utils/request";
import { formatteDate, downloadFileByBlob } from "@/utils/common";
import {
  addFolder,
  downloadMinio,
  editFolder,
  filesLoad,
  getFolderFiles,
  moveFolder,
  showAll,
  toBin,
  uploadMinio,
} from "@/api/folder";
import { mapGetters, mapState } from "vuex";
import { createShare } from "@/api/share";
import md5 from "@/lib/md5";
import {
  taskInfo,
  initTask,
  preSignUrl,
  merge,
  chunkDownloadFile,
} from "../lib/api";
import { Notification } from "element-ui"; // 使用 Element UI 的 Notification
import Queue from "promise-queue-plus";
import axios from "axios";
import { CHUNK_SIZE } from "@/constants";

export default {
  name: "FilesView",
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
      dialogFolderFormVisible: false,
      dialogShareFormVisible: false,
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
      folderData: [],
      defaultProps: {
        children: "childrenFolders",
        label: "name",
      },
      moveItem: "",
      moveItemFolder: "",
      radio: "1",
      shareUrl: "",
      isUrl: false,
      shareRow: "",
      //嵌入upload.vue ----- 分片上传minio
      fileUploadChunkQueue: [],
      identifier: "",
      drawer: false,
      state: {
        dataMap: new Map(),
        blobRef: new Map(),
      },
    };
  },
  computed: {
    ...mapState("user", ["userInfo"]),
    ...mapGetters("menu", ["getLastEl", "getStack", "getCurId"]),
    ...mapGetters("user", ["getUser"]),
  },
  created() {
    this.load();
  },
  methods: {
    copyText() {
      const input = this.$refs.copyInput;
      input.value = this.shareUrl; // 设置要复制的内容
      input.select(); // 选中内容
      document.execCommand("copy"); // 执行复制
      this.$message({
        type: "success",
        message: "复制成功",
        duration: 2000,
      });
    },
    afterClose() {
      this.isUrl = false;
      this.shareUrl = "";
    },
    async createShare() {
      let row = this.shareRow;
      let user = this.getUser;
      let fileType = "";
      if (row.type !== "folder") {
        fileType = "2";
      } else {
        fileType = "1";
      }
      let vo = {
        userId: user.id,
        fileId: row.id,
        name: row.name,
        shareTime: this.radio,
        fileType: fileType,
      };
      try {
        const res = await createShare(vo);
        if (res.code == 200) {
          let code = res.data.code;
          let url = "http://localhost:80/share/code=" + code;
          this.shareUrl = url;
          this.isUrl = true;
        } else {
          this.$message.error("服務器出錯");
        }
      } catch (error) {
        return error;
      }
    },
    handleNodeClick(data) {
      this.moveItemFolder = data;
    },
    async folderSubmit() {
      let current = this.moveItem;
      let target = this.moveItemFolder;
      let id = current.id;
      let targetId = target.id;

      let form = { id: id, targetId: targetId, type: current.type };
      let user = this.getUser;
      let curId = this.getCurId;

      try {
        const res = await moveFolder(form);
        debugger;
        if (res.code == 200) {
          this.dialogFolderFormVisible = false;
          this.$message({
            type: "success",
            message: "操作成功",
            duration: 2000,
          });
          this.getCurrentFiles(user.id, curId);
        }
      } catch (error) {
        return error;
      }
    },
    getIcon(fileType) {
      if (fileType.includes("image")) {
        return "el-icon-notebook-2";
      } else {
        return "el-icon-folder-opened";
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
      this.dialogShareFormVisible = true;
      this.shareRow = row;
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
        duration: 2000,
      });
    },
    errorMes() {
      this.$message.error("服務器出錯");
    },
    async load() {
      this.checkFolderStack();
      this.initStack();
      let user = this.getUser;
      let list = [];
      try {
        const res = await filesLoad(user);
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
        this.total = res.data.total;
      } catch (error) {
        return error;
      }
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
        fileSize: "",
        isDownloading: false,
        downloadProgress: 0,
      };
      obj.id = file.id;
      obj.name = file.fileName;
      obj.size = file.fileData;
      obj.path = file.filePath;
      obj.type = file.fileType;
      obj.date = formatteDate(file.createTime);
      obj.folderId = file.folderId;
      obj.fileSize = file.fileSize;
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
      obj.size = "500KB";
      obj.path = "";
      obj.type = "folder";
      obj.date = formatteDate(folder.createTime);
      obj.parentId = folder.parentId;
      return obj;
    },
    handleAdd() {
      this.dialogFormVisible = true;
    },
    async submitForm() {
      let user = this.getUser;
      let parentId = this.getCurId;
      try {
        const res = await addFolder(this.form.folderName, user.id, parentId);
        if (res.code == 200) {
          this.successMes();
          this.dialogFormVisible = false;
        } else {
          this.errorMes();
          this.dialogFormVisible = false;
        }
        this.dialogFormVisible = false;
        // this.form.folderName = '';
        this.form = this.resetForm;
        this.getCurrentFiles(user.id, parentId);
      } catch (error) {
        return error;
      }
    },
    async editSubmit() {
      let user = this.getUser;
      let curId = this.getCurId;
      let originName = this.editName;

      if (originName == this.editForm.folderName) {
        this.$message.error("Please input new folder name");
        return;
      } else {
        let formData = this.editNew;
        let id = formData.id;
        let newName = this.editForm.folderName;
        try {
          const res = await editFolder(id, newName);
          if (res.code == 200) {
            this.$message({
              message: "modify success",
              type: "success",
              duration: 2000,
            });
            this.dialogEditFormVisible = false;
            this.getCurrentFiles(user.id, curId);
          }
        } catch (error) {
          this.$message.error(error);
        }
      }
    },
    handleSearch() {
      this.pageNum = 1;
      this.findByName();
    },
    async handleBack() {
      this.$store.commit("menu/remove");
      try {
        const res = await getFolderFiles(this.userInfo.id, this.getCurId);
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
    async handleOpen(row) {
      if (row.type == "folder") {
        this.$store.commit("menu/setCurId", row.id);
        this.$store.commit("menu/insert", row.id);
        this.isBack = true;
        let list = [];
        try {
          const res = await getFolderFiles(this.userInfo.id, row.id);
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
        this.$message.error("This is not folder");
      }
    },
    // 覆盖默认的http行为
    async handleFileUpload(options) {
      const { file } = options;
      let user = this.getUser;
      let curId = this.getCurId;

      const formData = new FormData();
      formData.append("file", file);
      formData.append("folderId", curId);
      formData.append("userId", user.id);
      try {
        const res = await uploadMinio(formData);
        if (res.code == 200) {
          this.$message({
            message: "上传成功",
            type: "success",
            duration: 2000,
          });
        } else {
          this.$message.error("upload failed");
        }
        const res2 = await getFolderFiles(user.id, curId);
        let list = [];
        if (res2.code == 200) {
          let fileList = res2.data.fileList;
          let folderList = res2.data.folderList;
          if (fileList != null && fileList.length !== 0) {
            for (let i = 0; i < fileList.length; i++) {
              let file = fileList[i];
              let obj = this.transferFile(file);
              list.push(obj);
            }
          }
          if (folderList != null && folderList.length !== 0) {
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

      this.$refs.uploadRef.clearFiles();
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
    async handleMove(row) {
      this.moveItem = row;
      let user = this.getUser;
      try {
        const res = await showAll(user.id);
        if (res.code == 200) {
          this.folderData = res.data;
        } else {
          this.$message.error("server error");
        }
        this.dialogFolderFormVisible = true;
      } catch (error) {
        this.$message.error(error);
      }
    },
    handleEdit(row) {
      debugger;
      if (row.type !== "folder") {
        this.$message.error("Can not modify file name!");
      } else {
        this.dialogEditFormVisible = true;
        this.editName = row.name; //初始名字
        this.editForm.folderName = row.name;
        this.editNew = row; //整个对象
      }
    },
    handleDelete(row) {
      let user = this.getUser;
      let curId = this.getCurId;
      let data = {
        userId: user.id,
        fileOrFolderId: row.id,
        fileType: row.type,
      };
      this.$confirm("Put it in the recycle bin?", "Prompt", {
        confirmButtonText: "Confirm",
        cancelButtonText: "Cancel",
        type: "warning",
      })
        .then(() => {
          request.post("http://localhost:80/folder/toBin", data).then((res) => {
            if (res.code == 200) {
              this.$message({
                type: "success",
                message: "删除成功!",
                duration: 2000,
              });
              this.getCurrentFiles(user.id, curId);
            }
          });
        })
        .catch(() => {});
    },
    async getCurrentFiles(userId, curId) {
      try {
        const res = await getFolderFiles(userId, curId);
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
    //========== upload START
    //first step
    getTaskInfo(file) {
      return new Promise(async (resolve, reject) => {
        debugger;
        let task;
        const identifier = await md5(file); //second step
        this.identifier = identifier;
        const { code, data, msg } = await taskInfo(identifier); //third step
        if (code == 200) {
          debugger;
          task = data;
          if (!task) {
            debugger;
            let userId = this.userInfo.id;
            let folderId = this.getCurId;
            const initTaskData = {
              userId,
              folderId,
              identifier,
              fileName: file.name,
              totalSize: file.size,
              chunkSize: 5 * 1024 * 1024,
            };
            const {
              code: initCode,
              data: initData,
              msg: initMsg,
            } = await initTask(initTaskData);
            if (initCode == 200) {
              task = initData;
            } else {
              Notification.error({
                title: "文件上传错误",
                message: initMsg,
              });
              reject(initMsg);
            }
          }
        } else {
          Notification.error({
            title: "文件上传错误",
            message: msg,
          });
          reject(msg);
        }
        resolve(task);
      });
    },
    handleUpload(file, taskRecord, options) {
      //fourth step
      let lastUploadedSize = 0; // 上次断点续传时上传的总大小
      let uploadedSize = 0; // 已上传的大小
      const totalSize = file.size || 0; // 文件总大小
      let startMs = new Date().getTime(); // 开始上传的时间
      const { exitPartList, chunkSize, chunkNum, fileIdentifier } = taskRecord;

      // 获取从开始上传到现在的平均速度（byte/s）
      const getSpeed = () => {
        // 已上传的总大小 - 上次上传的总大小（断点续传）= 本次上传的总大小（byte）
        const intervalSize = uploadedSize - lastUploadedSize;
        const nowMs = new Date().getTime();
        // 时间间隔（s）
        const intervalTime = (nowMs - startMs) / 1000;
        return intervalSize / intervalTime;
      };

      const uploadNext = async (partNumber) => {
        //fifth step
        const start = new Number(chunkSize) * (partNumber - 1);
        const end = start + new Number(chunkSize);
        const blob = file.slice(start, end);
        const { code, data, msg } = await preSignUrl({
          identifier: fileIdentifier,
          partNumber: partNumber,
        });
        if (code == 200 && data) {
          await axios.request({
            url: data,
            method: "PUT",
            data: blob,
            headers: { "Content-Type": "application/octet-stream" },
          });
          return Promise.resolve({
            partNumber: partNumber,
            uploadedSize: blob.size,
          });
        }
        return Promise.reject(`分片${partNumber}， 获取上传地址失败`);
      };

      /**
       * 更新上传进度
       * @param increment 为已上传的进度增加的字节量
       */
      const updateProcess = (increment) => {
        increment = new Number(increment);
        const { onProgress } = options;
        let factor = 1000; // 每次增加1000 byte
        let from = 0;
        // 通过循环一点一点的增加进度
        while (from <= increment) {
          from += factor;
          uploadedSize += factor;
          const percent = Math.round((uploadedSize / totalSize) * 100).toFixed(
            2
          );
          onProgress({ percent: percent });
        }

        const speed = getSpeed();
        const remainingTime =
          speed != 0
            ? Math.ceil((totalSize - uploadedSize) / speed) + "s"
            : "未知";
        // console.log(
        //   "剩余大小：",
        //   (totalSize - uploadedSize) / 1024 / 1024,
        //   "mb"
        // );
        // console.log("当前速度：", (speed / 1024 / 1024).toFixed(2), "mbps");
        // console.log("预计完成：", remainingTime);
      };

      return new Promise((resolve) => {
        const failArr = [];
        const queue = Queue(5, {
          retry: 3, //Number of retries
          retryIsJump: false, //retry now?
          workReject: function (reason, queue) {
            failArr.push(reason);
          },
          queueEnd: function (queue) {
            resolve(failArr);
          },
        });
        this.fileUploadChunkQueue[file.uid] = queue;
        for (let partNumber = 1; partNumber <= chunkNum; partNumber++) {
          const exitPart = (exitPartList || []).find(
            (exitPart) => exitPart.partNumber == partNumber
          );
          if (exitPart) {
            // 分片已上传完成，累计到上传完成的总额中,同时记录一下上次断点上传的大小，用于计算上传速度
            lastUploadedSize += new Number(exitPart.size);
            updateProcess(exitPart.size);
          } else {
            queue.push(() =>
              uploadNext(partNumber).then((res) => {
                // 单片文件上传完成再更新上传进度
                updateProcess(res.uploadedSize);
              })
            );
          }
        }
        if (queue.getLength() == 0) {
          // 所有分片都上传完，但未合并，直接return出去，进行合并操作
          resolve(failArr);
          return;
        }
        queue.start();
      });
    },
    handleHttpRequest(options) {
      const file = options.file;
      this.getTaskInfo(file)
        .then((task) => {
          if (task) {
            const { finished, path, taskRecord } = task;
            const { fileIdentifier: identifier } = taskRecord;
            if (finished) {
              return path;
            } else {
              this.handleUpload(file, taskRecord, options).then((errorList) => {
                if (errorList.length > 0) {
                  Notification.error({
                    title: "文件上传错误",
                    message: "部分分片上次失败，请尝试重新上传文件",
                  });
                } else {
                  merge(this.identifier).then(({ code, data, msg }) => {
                    if (code == 200) {
                      // const res = this.getFolderFiles(
                      //   this.userInfo.id,
                      //   this.getCurId
                      // );
                      // let list = [];
                      // if (res.code == 200) {
                      //   let fileList = res.data.fileList;
                      //   let folderList = res.data.folderList;
                      //   if (fileList.length !== 0) {
                      //     for (let i = 0; i < fileList.length; i++) {
                      //       let file = fileList[i];
                      //       let obj = this.transferFile(file);
                      //       list.push(obj);
                      //     }
                      //   }
                      //   if (folderList.length !== 0) {
                      //     for (let i = 0; i < folderList.length; i++) {
                      //       let folder = folderList[i];
                      //       let obj = this.transferFolder(folder);
                      //       list.push(obj);
                      //     }
                      //   }
                      // }
                      // this.tableData = list;

                      return path;
                    } else {
                      Notification.error({
                        title: "文件上传错误",
                        message: msg,
                      });
                    }
                  });
                }
              });
            }
          } else {
            Notification.error({
              title: "文件上传错误",
              message: "获取上传任务失败",
            });
          }
        })
        .catch((err) => {
          Notification.error({
            title: "文件上传错误",
            message: err,
          });
        });
    },
    handleRemoveFile(uploadFile, uploadFiles) {
      const queueObject = this.fileUploadChunkQueue[uploadFile.uid];
      if (queueObject) {
        queueObject.stop();
        // delete fileUploadChunkQueue[uploadFile.uid];
      }
    },
    //========== upload END

    //========large file download logic START============
    async downloadFile(record) {
      const state = this.state;
      const blobRef = state.blobRef;
      const size = record.fileSize;
      const dataMap = state.dataMap;
      record.isDownloading = true;

      const totalChunks = Math.ceil(size / CHUNK_SIZE);
      const offset = blobRef.get(record.id)?.length || 0;

      for (let i = offset + 1; i <= totalChunks; i++) {
        // 暂停/错误 终止后续请求
        // if (state.dataSource[index].status !== 'downloading') return

        const start = CHUNK_SIZE * (i - 1);
        let end = CHUNK_SIZE * i - 1;
        if (end > size) end = size; // 虽然超出不会影响内容读取，但是会影响进度条的展示
        let range = "bytes=" + start + "-" + end;
        try {
          const res = await chunkDownloadFile(record.id, range);
          const currentDataBlob = blobRef.get(record.id) || [];

          debugger;
          blobRef.set(record.id, [...currentDataBlob, res]); //set multy blob
          record.downloadProgress = Math.floor((end / size) * 100);
          dataMap.set(record.id, record); //记录当前文件的下载状态和进度
          // state.dataSource[index].progress = Math.floor((end / size) * 100);
        } catch (error) {
          // state.dataSource[index].status = "error";
          return;
        }
      }

      // state.dataSource[index].status = undefined; // 重置状态
      // state.dataSource[index].progress = undefined; // 重置进度条
      const blob = new Blob(blobRef.get(record.id));
      downloadFileByBlob(blob, record.name);
    },
    //========large file download logic END============
  },
};
</script>

<style>
</style>