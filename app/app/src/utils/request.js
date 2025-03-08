import axios from "axios";
import { Message } from 'element-ui'
import router from '@/router'
import store from "@/store";

const request = axios.create({
  baseURL: '/api',
  // timeout: 5000,
  // headers: { 'X-Custom-Header': 'foobar' }
});

request.interceptors.request.use((config) => {
  config.headers['token'] = store.state.user.token
  return config;
}, (error) => {
  // 失败执行promise
  return Promise.reject(error)
})

request.interceptors.response.use((response) => {
  let res = response.data;
  let {code,message} = res

  if (response.config.responseType == 'blob') {
    return response
  }
  if (typeof res == 'string') {
    res = res ? JSON.parse(res) : res
  }
  if(code == 200){
    return res;
  }else{
    Message({ type: 'error', message })
    return Promise.reject(new Error(message))
  }
}, async(error) => {

  return Promise.reject(error)
});

export default request

