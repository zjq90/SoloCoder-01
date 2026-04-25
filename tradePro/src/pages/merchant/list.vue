<template>
  <view class="merchant-list-page">
    <view class="filter-section">
      <view class="filter-tabs">
        <view 
          class="filter-tab" 
          :class="{ active: filterType === 'all' }"
          @click="filterType = 'all'"
        >
          <text class="tab-text">全部</text>
          <view class="tab-badge" v-if="filterType === 'all'">{{ merchantList.length }}</view>
        </view>
        <view 
          class="filter-tab" 
          :class="{ active: filterType === 'direct' }"
          @click="filterType = 'direct'"
        >
          <text class="tab-text">直属</text>
          <view class="tab-badge" v-if="filterType === 'direct'">{{ directCount }}</view>
        </view>
        <view 
          class="filter-tab" 
          :class="{ active: filterType === 'agent' }"
          @click="filterType = 'agent'"
        >
          <text class="tab-text">代理</text>
          <view class="tab-badge" v-if="filterType === 'agent'">{{ agentCount }}</view>
        </view>
      </view>
    </view>

    <view class="list-section">
      <view class="merchant-list">
        <view class="merchant-item" v-for="merchant in filteredList" :key="merchant.id" @click="goToDetail(merchant)">
          <view class="item-left">
            <view class="merchant-icon">
              <text class="icon-text">🏪</text>
            </view>
            <view class="item-info">
              <view class="name-row">
                <text class="merchant-name">{{ merchant.name }}</text>
                <view class="merchant-type" :class="merchant.type === '直属' ? 'direct' : 'agent'">
                  <text class="type-text">{{ merchant.type }}</text>
                </view>
              </view>
              <view class="info-row">
                <text class="info-item">{{ merchant.phone }}</text>
              </view>
              <view class="info-row">
                <text class="info-item">创建时间：{{ merchant.createTime }}</text>
              </view>
            </view>
          </view>
          <view class="item-right">
            <view class="merchant-status" :class="merchant.status === '正常' ? 'normal' : 'abnormal'">
              <text class="status-text">{{ merchant.status }}</text>
            </view>
            <text class="arrow">›</text>
          </view>
        </view>
      </view>

      <view class="empty-state" v-if="filteredList.length === 0">
        <text class="empty-icon">🏪</text>
        <text class="empty-text">暂无商户数据</text>
      </view>
    </view>
  </view>
</template>

<script>
import { mockData } from '@/utils/mockData.js'

export default {
  data() {
    return {
      merchantList: mockData.merchantList,
      filterType: 'all'
    }
  },
  onLoad(options) {
    if (options.type) {
      this.filterType = options.type
    }
  },
  computed: {
    directCount() {
      return this.merchantList.filter(m => m.type === '直属').length
    },
    agentCount() {
      return this.merchantList.filter(m => m.type === '代理').length
    },
    filteredList() {
      if (this.filterType === 'all') return this.merchantList
      const typeText = this.filterType === 'direct' ? '直属' : '代理'
      return this.merchantList.filter(m => m.type === typeText)
    }
  },
  methods: {
    goToDetail(merchant) {
      uni.navigateTo({
        url: `/pages/merchant/detail?id=${merchant.id}`
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/uni.scss';

.merchant-list-page {
  min-height: 100vh;
  background-color: $bg-color;
  padding-bottom: 30rpx;
}

.filter-section {
  background-color: #FFFFFF;
  padding: 20rpx;
  margin-bottom: 20rpx;
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
  position: relative;
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

.tab-badge {
  position: absolute;
  top: 8rpx;
  right: 20rpx;
  background-color: #FFFFFF;
  color: $primary-color;
  font-size: 18rpx;
  padding: 2rpx 8rpx;
  border-radius: 10rpx;
  font-weight: bold;
}

.list-section {
  padding: 0 20rpx;
}

.merchant-list {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  overflow: hidden;
}

.merchant-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  border-bottom: 1rpx solid #F5F5F5;
}

.merchant-item:last-child {
  border-bottom: none;
}

.merchant-item:active {
  background-color: #FAFAFA;
}

.item-left {
  display: flex;
  align-items: center;
  flex: 1;
}

.merchant-icon {
  width: 72rpx;
  height: 72rpx;
  background: linear-gradient(135deg, #FFF3E0 0%, #FFE0B2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.icon-text {
  font-size: 32rpx;
}

.item-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.name-row {
  display: flex;
  align-items: center;
  margin-bottom: 12rpx;
}

.merchant-name {
  font-size: 30rpx;
  font-weight: bold;
  color: $text-color;
  margin-right: 16rpx;
}

.merchant-type {
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}

.merchant-type.direct {
  background-color: rgba($primary-color, 0.1);
}

.merchant-type.agent {
  background-color: rgba(33, 150, 243, 0.1);
}

.type-text {
  font-size: 22rpx;
}

.merchant-type.direct .type-text {
  color: $primary-color;
}

.merchant-type.agent .type-text {
  color: #2196F3;
}

.info-row {
  margin-bottom: 6rpx;
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

.merchant-status {
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  margin-right: 12rpx;
}

.merchant-status.normal {
  background-color: rgba(76, 175, 80, 0.1);
}

.merchant-status.abnormal {
  background-color: rgba(244, 67, 54, 0.1);
}

.status-text {
  font-size: 22rpx;
}

.merchant-status.normal .status-text {
  color: #4CAF50;
}

.merchant-status.abnormal .status-text {
  color: #F44336;
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
