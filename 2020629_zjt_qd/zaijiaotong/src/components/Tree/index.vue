<template>
  <div>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item v-for="(item, index) in pathArray" :key="index">
        <el-link @click="changePath(item)">{{ item.label }}</el-link>
      </el-breadcrumb-item>
    </el-breadcrumb>
    <el-tree lazy :props="props" :load="loadNode" @node-click="handleNodeClick" />
  </div>
</template>

<script>
import { getSysDict, getSysChildDict } from '@/api/user'
export default {
  name: 'Tree',
  props: {
    value: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      pathArray: [],
      props: {
        label: 'name',
        isLeaf: 'leaf',
        children: 'childid'
      }
    }
  },
  mounted() {
    this.pathArray.push({
      label: this.$store.getters.userPathName,
      value: this.$store.getters.userPath
    })
  },
  methods: {
    changePath(item) {
      const n = this.pathArray.indexOf(item)
      this.pathArray = this.pathArray.splice(0, n + 1)
      this.$emit('input', item.value)
    },
    handleNodeClick(node, data) {
      console.log(node)
      console.log(data)
      this.pathArray = this.pathArray.slice(0, 0)

      let n = []
      getParent(data)
      n = n.reverse()
      this.pathArray = this.pathArray.concat(n)

      function getParent(params) {
        Object.keys(params).map(item => {
          if (params['parent'] && item === 'parent') {
            const { data } = params
            n.push({
              label: data.name,
              value: data.id
            })
            getParent(params['parent'])
            return
          }
        })
      }
      this.$emit('input', node.id)
    },
    async loadNode(node, resolve) {
      console.log(node, node)

      if (node.level === 0) {
        await this.requestData(resolve)
      }
      if (node.level >= 1) {
        await this.getChildData(node, resolve)
      }
    },
    async requestData(resolve) {
      await getSysDict(this.value)
        .then(result => {
          const { data } = result
          const dataList = []
          for (var i = 0; i < data.length; i++) {
            dataList.push({
              name: data[i].sdName,
              id: data[i].sdCode,
              leaf: data[i].sdLevel === 3
            })
          }
          resolve(dataList)
        })
        .catch(err => {
          console.error(err)
          resolve()
        })
    },
    async getChildData(node, resolve) {
      await getSysChildDict(node.data.id)
        .then(result => {
          const { data } = result
          const dataList = []
          data.map((item, id) => {
            dataList.push({
              name: item.sdName,
              id: item.sdCode,
              leaf: item.sdLevel === 3
            })
          })
          resolve(dataList)
        })
        .catch(err => {
          console.error(err)
          resolve()
        })
    }
  }
}
</script>

<style lang="scss" scoped>
.el-breadcrumb {
  padding: 10px;
  font-weight: bold;
  border-bottom: 3px solid #3c9cff;
}
</style>
