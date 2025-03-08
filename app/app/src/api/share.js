import request from "@/utils/request";

export function createShare(vo) {
    return request.post("http://localhost:80/share/createShare", vo)
}

export function getCodeT(code) {
    return request.get("http://localhost:80/share/getCode", {
        params: {
            code: code,
        },
    })
}

export function downloadMinio(row) {
    return request
        .get("http://localhost:80/download/downloadMinio", {
            params: {
                id: row.id,
                originName: row.name,
                filePath: row.path,
            },
            responseType: "blob",
        })
}

export function getFolderFiles(userId, curId) {
    return request
        .get("http://localhost:80/folder/getFolderFiles", {
            params: {
                userId: userId,
                folderId: curId,
            },
        })
}