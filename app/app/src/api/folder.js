import request from "@/utils/request";
// The folder and files API are both here

export function filesLoad(user) {
  return request.get("http://localhost:80/folder/firstShow", {
    params: {
      userId: user.id,
    },
  });
}

export function addFolder(name, userId, parentId) {
  return request.post(
    "http://localhost:80/folder/addFolder?folderName=" +
      name +
      "&userId=" +
      userId +
      "&parentId=" +
      parentId
  );
}

export function moveFolder(form) {
  return request.post("http://localhost:80/folder/moveFolder", form);
}

export function getFolderFiles(userId, curId) {
  return request.get("http://localhost:80/folder/getFolderFiles", {
    params: {
      userId: userId,
      folderId: curId,
    },
  });
}

export function editFolder(id, name) {
  debugger;
  return request.post(
    "http://localhost:80/folder/editFolder?folderId=" + id + "&newName=" + name
  );
}

export function uploadMinio(data) {
  return request.post("http://localhost:80/file/uploadMinio", data);
}

export function showAll(id) {
  return request.get("http://localhost:80/share/showAll?userId=" + id);
}

export function toBin(data) {
  return request.post("http://localhost:80/folder/toBin", data);
}

export function downloadMinio(row) {
  return request.get("http://localhost:80/download/downloadMinio", {
    params: {
      id: row.id,
      originName: row.name,
      filePath: row.path,
    },
    responseType: "blob",
  });
}
