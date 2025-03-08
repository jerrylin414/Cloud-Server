import request from "@/utils/request";

export function login(data) {
    return request.post("http://localhost:80/user/login", data)
}

export function register(data) {
    return request.post("http://localhost:80/user/addUser", data)
}