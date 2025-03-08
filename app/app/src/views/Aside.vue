 <template>
  <div>
    <el-menu
      :default-openeds="['1', '3']"
      style="height: 100%; overflow-x: hidden"
      background-color=""
      text-color="#666"
      active-text-color="#2a60c9"
      :collapse-transition="false"
      :collapse="isCollapse"
      router
    >
      <div style="height: 60px; line-height: 60px; text-align: center">
        <img
          src="@/assets/logo.png"
          alt
          style="width: 20px; position: relative; top: 5px; margin-right: 5px"
        />
        <b style="color: #666" v-show="isShowText">Mycloud System</b>
      </div>

      <el-menu-item index="/filesView">
        <i class="el-icon-menu"></i>
        <span slot="title">All</span>
      </el-menu-item>
      <!-- <el-menu-item index="/imageView">
        <i class="el-icon-picture-outline"></i>
        <span slot="title">Images</span>
      </el-menu-item>
      <el-menu-item index="/zipFileView">
        <i class="el-icon-document"></i>
        <span slot="title">Zip Files</span>
      </el-menu-item>
      <el-menu-item index="/index">
        <i class="el-icon-video-play"></i>
        <span slot="title">Bin</span>
      </el-menu-item> -->

    </el-menu>
  </div>
</template>
 
 <script>
export default {
  name: "AsideView",
  props: {
    isCollapse: Boolean,
    isShowText: Boolean
  }
};
</script>
 
 <style>
</style>