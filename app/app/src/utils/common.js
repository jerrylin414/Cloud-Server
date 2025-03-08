export function formatteDate(isoDateString) {
  // 创建一个新的Date对象
  const date = new Date(isoDateString);

  // 验证日期对象是否有效
  if (isNaN(date.getTime())) {
    throw new Error("Invalid date string");
  }

  // 使用Date对象的方法获取各个部分，并格式化为所需的字符串
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0"); // 月份从0开始，所以需要+1
  const day = String(date.getDate()).padStart(2, "0");
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const seconds = String(date.getSeconds()).padStart(2, "0");

  // 返回格式化后的日期时间字符串
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

export function downloadFileByBlob(blob, filename) {
  const href = URL.createObjectURL(blob);
  const a = document.createElement("a");
  a.style.display = "none";
  a.href = href;
  a.download = filename;
  a.rel = "noopener noreferrer";
  document.body.append(a);
  a.click();
  URL.revokeObjectURL(href); //释放URL对象
  a.remove();
}
