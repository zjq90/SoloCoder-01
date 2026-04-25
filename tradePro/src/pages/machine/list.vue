<template>
  <view class="machine-list-page">
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

    <view class="filter-section">
      <view class="filter-tabs">
        <view 
          class="filter-tab" 
          :class="{ active: filterType === 'all' }"
          @click="filterType = 'all'"
        >
          <text class="tab-text">全部</text>
        </view>
        <view 
          class="filter-tab" 
          :class="{ active: filterType === 'activated' }"
          @click="filterType = 'activated'"
        >
          <text class="tab-text">已激活</text>
        </view>
        <view 
          class="filter-tab" 
          :class="{ active: filterType === 'inactive' }"
          @click="filterType = 'inactive'"
        >
          <text class="tab-text">未激活</text>
        </view>
      </view>
    </view>

    <view class="list-section">
      <view class="machine-list">
        <view class="machine-item" v-for="machine in filteredList" :key="machine.id" @click="goToDetail(machine)">
          <view class="item-left">
            <view class="machine-icon" :class="machine.status === '已激活' ? 'activated' : 'inactive'">
              <text class="icon-text">💳</text>
            </view>
            <view class="item-info">
              <view class="sn-row">
                <text class="sn-label">SN码</text>
                <text class="sn-value">{{ machine.sn }}</text>
              </view>
              <text class="product-name">{{ machine.product }}</text>
              <view class="info-row">
                <text class="info-item" v-if="machine.merchant !== '-'">绑定商户：{{ machine.merchant }}</text>
                <text class="info-item" v-else>未绑定商户</text>
              </view>
              <view class="info-row" v-if="machine.bindTime !== '-'">
                <text class="info-item">绑定时间：{{ machine.bindTime }}</text>
              </view>
            </view>
          </view>
          <view class="item-right">
            <view class="machine-status" :class="machine.status === '已激活' ? 'active' : 'inactive'">
              <text class="status-text">{{ machine.status }}</text>
            </view>
            <text class="arrow">›</text>
          </view>
        </view>
      </view>

      <view class="empty-state" v-if="filteredList.length === 0">
        <text class="empty-icon">💳</text>
        <text class="empty-text">暂无机器数据</text>
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
      filterType: 'all'
    }
  },
  computed: {
    activatedCount() {
      return this.machineList.filter(m => m.status === '已激活').length
    },
    inactiveCount() {
      return this.machineList.filter(m => m.status === '未激活').length
    },
    filteredList() {
      if (this.filterType === 'all') return this.machineList
      if (this.filterType === 'activated') {
        return this.machineList.filter(m => m.status === '已激活')
      }
      return this.machineList.filter(m => m.status === '未激活')
    }
  },
  methods: {
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

.machine-list-page {
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

.filter-section {
  background-color: #FFFFFF;
  margin: 0 20rpx 20rpx;
  padding: 20rpx;
  border-radius: 16rpx;
}

.filter-tabs {
  display: flex;
  gap: 20rpx;
}

.filter-tab {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16rpx 0;
  border-radius: 8rpx;
  background-color: #F5F5F5;
}

.filter-tab.active {
  background-color: $primary-color;
}

.tab-text {
  font-size: 26rpx;
  color: $text-secondary;
}

.filter-tab.active .tab-text {
  color: #FFFFFF;
  font-weight: bold;
}

.list-section {
  padding: 0 20rpx;
}

.machine-list {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  overflow: hidden;
}

.machine-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  border-bottom: 1rpx solid #F5F5F5;
}

.machine-item:last-child {
  border-bottom: none;
}

.machine-item:active {
  background-color: #FAFAFA;
}

.item-left {
  display: flex;
  align-items: center;
  flex: 1;
}

.machine-icon {
  width: 72rpx;
  height: 72rpx;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.machine-icon.activated {
  background: linear-gradient(135deg, #E8F5E9 0%, #C8E6C9 100%);
}

.machine-icon.inactive {
  background: linear-gradient(135deg, #FAFAFA 0%, #F5F5F5 100%);
}

.icon-text {
  font-size: 32rpx;
}

.item-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.sn-row {
  display: flex;
  align-items: center;
  margin-bottom: 8rpx;
}

.sn-label {
  font-size: 24rpx;
  color: $text-light;
  margin-right: 8rpx;
}

.sn-value {
  font-size: 28rpx;
  font-weight: bold;
  color: $text-color;
  font-family: monospace;
}

.product-name {
  font-size: 26rpx;
  color: $text-secondary;
  margin-bottom: 8rpx;
}

.info-row {
  margin-bottom: 4rpx;
}

.info-row:last-child {
  margin-bottom: 0;
}

.info-item {
  font-size: 24rpx;
  color: $text-light;
}

.item-right {
  display: flex;
  align-items: center;
}

.machine-status {
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  margin-right: 12rpx;
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

.arrow {
  font-size: 32rpx;
  color: #CCCCCC;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 80rpx 0;
}

.empty-icon {
  font-size: 80rpx;
  margin-bottom: 20rpx;
}

.empty-text {
  font-size: 28rpx;
  color: $text-light;
}
</style>
