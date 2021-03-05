<template>
  <div class="dashboard-container">
    <div class="title">
      <b>统计数据</b>
    </div>
    <div class="box">
      <div class="box-body">
        <div class="row">
          <div v-for="item in showData" :key="item.id" class="form-group">
            <div class="main_count_box">
              <div class="icon icon_10" />
              <div class="count_box_right">
                <div class="count_num">{{ item.val }}</div>
                <div class="count_label">&nbsp;{{ item.label }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getStatistics } from '@/api/user'

export default {
  name: 'Dashboard',
  data() {
    return {
      showData: [],
      posData: {
        path: []
      }
    }
  },
  computed: {
    ...mapGetters(['name'])
  },
  async mounted() {
    // const n = this.$store.getters.userPathList[0].children
    // n.map(item => {
    //   this.posData.path.push(item.value)
    // })
    // await this.getStatistics()
  },
  methods: {
    async getStatistics() {
      const map = {
        fg: '房管数量',
        fgrzsl: '房管日增长',
        zk: '租客数量',
        zkrzsl: '租客日增长',
        housesCounts: '楼宇数量',
        housesRzsl: '楼宇日增长',
        wzkly: '无租客楼宇',
        sgynwgxzh: '三个月内无更新租客',
        dfjzh: '多房间租客',
        wfwjg: '无房屋结构楼宇',
        ycz: '已出租',
        kcz: '可出租',
        bkcz: '不可出租',
        doorCounts: '房间总数'
      }
      await getStatistics(this.posData)
        .then(result => {
          const allData = {}
          result.map((item, id) => {
            if (item.data) {
              const { data } = item
              Object.assign(allData, data)
            }
          })
          Object.keys(allData).map(key => {
            const item = allData[key]
            Object.keys(map).map(mapKey => {
              const mapItem = map[mapKey]
              if (key === mapKey) {
                const pushData = { label: mapItem, val: item }
                this.showData.push(pushData)
              }
            })
          })
        })
        .catch(err => {
          console.error(err)
        })
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  width: 96%;
  height: 600px;
  margin: 20px 10px 20px 10px !important;
}
.title {
  width: 100%;
  height: 30px;
  font-size: 16px;
  border-bottom: 1px solid darkgrey;
  text-indent: 10px;
}
.box {
  border-radius: 2px;
  box-shadow: 0 0 3px rgba(0, 0, 0, 0.2);
  clear: both;
  margin-top: 0px;
  margin-bottom: 10px;
  padding: 0px;
  height: 570px;
}
.box-body {
  overflow-x: hidden;
}
.form-group {
  position: relative;
  margin-bottom: 5px;
  padding: 6px 12px;
  width: 25%;
  height: 89px;
  float: left;
}
.main_count_box {
  position: relative;
  width: 170px;
  height: 50px;
  text-align: left;
  padding: 5px 5px;
}
.count_num {
  color: #4087ef;
  padding-left: 5px;
  font-family: Digital-7Mono2;
  font-size: 25px;
  letter-spacing: 1px;
}
</style>
