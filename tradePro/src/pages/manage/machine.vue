<template>
  <view class="machine-manage-page">
    <view class="stats-card">
      <view class="stats-item">
        <text class="stats-label">机器总数</text>
        <text class="stats-value">{{ machineList.length }}</text>
      </view>
      <view class="stats-divider"></view>
      <view class="stats-item">
        <text class="stats-label">已激活</text>
        <text class="stats-value active">{{ activatedCount }}</text>
      </view>
      <view class="stats-divider"></view>
      <view class="stats-item">
        <text class="stats-label">未激活</text>
        <text class="stats-value">{{ inactiveCount }}</text>
      </view>
    </view>

    <view class="search-section">
      <view class="search-box">
        <text class="search-icon">🔍</text>
        <input 
          class="search-input" 
          placeholder="输入机器SN码搜索" 
          v-model="searchKeyword"
          @confirm="handleSearch"
        />
      </view>
    </view>

    <view class="list-section">
      <view class="section-header">
        <text class="section-title">机器列表</text>
      </view>

      <view class="machine-list">
        <view class="machine-item" v-for="machine in filteredMachineList" :key="machine.id" @click="goToDetail(machine)">
          <view class="machine-header">
            <view class="sn-info">
              <text class="sn-label">SN码</text>
              <text class="sn-value">{{ machine.sn }}</text>
            </view>
            <view class="machine-status" :class="machine.status === '已激活' ? 'active' : 'inactive'">
              <text class="status-text">{{ machine.status }}</text>
            </view>
          </view>

          <view class="machine-detail">
            <view class="detail-item">
              <text class="detail-label">产品类型</text>
              <text class="detail-value">{{ machine.product }}</text>
            </view>
            <view class="detail-item" v-if="machine.merchant !== '-'">
              <text class="detail-label">绑定商户</text>
              <text class="detail-value">{{ machine.merchant }}</text>
            </view>
            <view class="detail-item" v-if="machine.bindTime !== '-'">
              <text class="detail-label">绑定时间</text>
              <text class="detail-value">{{ machine.bindTime }}</text>
            </view>
          </view>

          <text class="arrow">›</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { mockData } from '@/utils/mockData.js'

export default {
  data() {
    return {
      machineList: mockData.machineList,
      searchKeyword: ''
    }
  },
  computed: {
    activatedCount() {
      return this.machineList.filter(m => m.status === '已激活').length
    },
    inactiveCount() {
      return this.machineList.filter(m => m.status === '未激活').length
    },
    filteredMachineList() {
      if (!this.searchKeyword) return this.machineList
      return this.machineList.filter(m => 
        m.sn.toLowerCase().includes(this.searchKeyword.toLowerCase()) ||
        m.product.toLowerCase().includes(this.searchKeyword.toLowerCase())
      )
    }
  },
  methods: {
    handleSearch() {
      // 搜索已通过 computed 实现
    },
    goToDetail(machine) {
      uni.navigateTo({
        url: `/pages/machine/detail?id=${machine.id}`
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/uni.scss';

.machine-manage-page {
  min-height: 100vh;
  background-color: $bg-color;
  padding-bottom: 30rpx;
}

.stats-card {
  background-color: #FFFFFF;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;
  display: flex;
  align-items: center;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.04);
}

.stats-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stats-label {
  font-size: 24rpx;
  color: $text-light;
  margin-bottom: 12rpx;
}

.stats-value {
  font-size: 36rpx;
  font-weight: bold;
  color: $text-color;
}

.stats-value.active {
  color: #4CAF50;
}

.stats-divider {
  width: 1rpx;
  height: 60rpx;
  background-color: #EEEEEE;
}

.search-section {
  padding: 0 20rpx 20rpx;
}

.search-box {
  background-color: #FFFFFF;
  border-radius: 40rpx;
  padding: 20rpx 30rpx;
  display: flex;
  align-items: center;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.04);
}

.search-icon {
  font-size: 28rpx;
  margin-right: 16rpx;
}

.search-input {
  flex: 1;
  font-size: 28rpx;
  color: $text-color;
}

.search-input::placeholder {
  color: $text-light;
}

.list-section {
  padding: 0 20rpx;
}

.section-header {
  padding: 20rpx 10rpx;
}

.section-title {
  font-size: 28rpx;
  font-weight: bold;
  color: $text-color;
}

.machine-list {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  overflow: hidden;
}

.machine-item {
  display: flex;
  flex-direction: column;
  padding: 24rpx;
  border-bottom: 1rpx solid #F5F5F5;
  position: relative;
}

.machine-item:last-child {
  border-bottom: none;
}

.machine-item:active {
  background-color: #FAFAFA;
}

.machine-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;
}

.sn-info {
  display: flex;
  flex-direction: column;
}

.sn-label {
  font-size: 22rpx;
  color: $text-light;
  margin-bottom: 4rpx;
}

.sn-value {
  font-size: 28rpx;
  font-weight: bold;
  color: $text-color;
  font-family: monospace;
}

.machine-status {
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
}

.machine-status.active {
  background-color: rgba(76, 175, 80, 0.1);
}

.machine-status.inactive {
  background-color: rgba(158, 158, 158, 0.1);
}

.status-text {
  font-size: 22rpx;
}

.machine-status.active .status-text {
  color: #4CAF50;
}

.machine-status.inactive .status-text {
  color: #9E9E9E;
}

.machine-detail {
  display: flex;
  flex-direction: column;
  gap: 12rpx;
}

.detail-item {
  display: flex;
  align-items: center;
}

.detail-label {
  font-size: 24rpx;
  color: $text-light;
  width: 120rpx;
}

.detail-value {
  font-size: 24rpx;
  color: $text-secondary;
  flex: 1;
}

.arrow {
  position: absolute;
  right: 24rpx;
  top: 50%;
  transform: translateY(-50%);
  font-size: 32rpx;
  color: #CCCCCC;
}
</style>
