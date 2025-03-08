export function successMes() {
    this.$message({
        message: "添加成功",
        type: "success",
        duration: 2000
    });
}


export function errorMes() {
    this.$message.error("服務器出錯");
}
