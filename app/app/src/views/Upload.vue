<template>
  <el-card style="width: 80%; margin: 80px auto" header="文件分片上传">
    <el-upload
      class="upload-demo"
      drag
      action="/"
      multiple
      :http-request="handleHttpRequest"
      :on-remove="handleRemoveFile"
    >
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">
        请拖拽文件到此处或 <em>点击此处上传</em>
      </div>
    </el-upload>
  </el-card>
</template>

<script>
import md5 from "@/lib/md5";
import { taskInfo, initTask, preSignUrl, merge } from "../lib/api";
import { Notification } from "element-ui"; // 使用 Element UI 的 Notification
import Queue from "promise-queue-plus";
import axios from "axios";
import { mapGetters, mapState } from "vuex";

export default {
  name: "uploadView",
  data() {
    return {
      fileUploadChunkQueue: [],
      identifier: "",
    };
  },
  computed: {
    ...mapGetters("menu", ["getLastEl", "getStack", "getCurId"]),
    ...mapGetters("user", ["getUser"]),
  },
  methods: {
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
            let user = this.getUser;
            let curId = this.getCurId;
            const initTaskData = {
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
                  console.log("identifier", this.identifier);
                  merge(this.identifier).then(({ code, data, msg }) => {
                    if (code == 200) {
                      //logic: change status to Y
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
  },
};
</script>

<style>
</style>