<template>
  <div id="app">
    <!-- <nav>
      <router-link to="/">Home</router-link> |
      <router-link to="/about">About</router-link>
    </nav> -->
    <router-view />
  </div>
</template>

<script>
export default {
  created() {
    // 监听beforeunload事件
    window.addEventListener("beforeunload", this.handleBeforeUnload)
  },
  destroyed() {
    window.removeEventListener("beforeunload", this.handleBeforeUnload)
  },
  methods: {
    handleBeforeUnload(event) {
      // event.returnValue = "你Confirm要离开吗？" //默认浏览器提示
      this.removeStorage()
    },
    removeStorage(){
      this.$store.dispatch('menu/clearStack')
    }
  },
};
</script>


<style lang="less">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  // color: #2c3e50;
  height: 100%;
}

nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}
</style>
