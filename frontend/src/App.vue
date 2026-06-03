<template>
  <router-view></router-view>
</template>

<script>
import { useStore } from 'vuex'
import {onMounted, onUnmounted} from "vue";
import store from "@/store/index.js";

export default {
  name: 'App',
  setup() {
    const store = useStore()

    const handleBeforeUnload = () => {
    }

    onMounted(() => {
      window.addEventListener('beforeunload', handleBeforeUnload)
    })

    onUnmounted(() => {
      window.removeEventListener('beforeunload', handleBeforeUnload)
    })
  },
  methods: {
    beforeunloadHandler() {
      this.beforeUnload_time = new Date().getTime();
    },
    unloadHandler() {
      this.gap_time = new Date().getTime() - this.beforeUnload_time;
      if (this.gap_time <= 10) {
        store.dispatch('logout'); 
      } else {
        console.log(document.domain);
        return confirm("Are you sure you want to leave this system?");
      }
    },
  },
  unmounted() {
    window.removeEventListener("beforeunload", () => this.beforeunloadHandler());
    window.removeEventListener("unload", () => this.unloadHandler());
  },
  mounted() {
    window.addEventListener("beforeunload", () => this.beforeunloadHandler());
    window.addEventListener("unload", () => this.unloadHandler());
  },


}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>

