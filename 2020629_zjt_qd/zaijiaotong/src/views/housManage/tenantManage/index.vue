<template>
  <div class="housInfo table-container">
    <!-- <div class="treeBox">
      <Tree v-model="pathName" />
    </div>-->

    <div class="tableBox">
      <Table v-model="pathName" />
    </div>
  </div>
</template>

<script>
import Table from './table'
// import Tree from '@/components/Tree'
export default {
  name: 'TenantManage',
  components: {
    // Tree,
    Table
  },
  data() {
    return {
      pathName: this.$store.getters.userPath
    }
  },
  methods: {
    getPathName(item) {
      this.pathName = item
    }
  }
}
</script>

<style lang="scss" scoped>
.housInfo {
  display: flex;
  align-items: flex-start;
}
</style>
