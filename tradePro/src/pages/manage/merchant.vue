<template>
  <view class="merchant-manage-page">
    <view class="stats-card">
      <view class="stats-item">
        <text class="stats-label">直属商户</text>
        <text class="stats-value">{{ directMerchantCount }}</text>
      </view>
      <view class="stats-divider"></view>
      <view class="stats-item">
        <text class="stats-label">代理商户</text>
        <text class="stats-value">{{ agentMerchantCount }}</text>
      </view>
      <view class="stats-divider"></view>
      <view class="stats-item">
        <text class="stats-label">商户总数</text>
        <text class="stats-value highlight">{{ merchantList.length }}</text>
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
          :class="{ active: filterType === 'direct' }"
          @click="filterType = 'direct'"
        >
          <text class="tab-text">直属</text>
        </view>
        <view 
          class="filter-tab" 
          :class="{ active: filterType === 'agent' }"
          @click="filterType = 'agent'"
        >
          <text class="tab-text">代理</text>
        </view>
      </view>
    </view>

    <view class="list-section">
      <view class="merchant-list">
        <view class="merchant-item" v-for="merchant in filteredMerchantList" :key="merchant.id" @click="goToDetail(merchant)">
          <view class="merchant-left">
            <view class="merchant-icon">
              <text class="icon-text">🏪</text>
            </view>
            <view class="merchant-info">
              <view class="name-row">
                <text class="merchant-name">{{ merchant.name }}</text>
                <view class="merchant-type" :class="merchant.type === '直属' ? 'direct' : 'agent'">
                  <text class="type-text">{{ merchant.type }}</text>
                </view>
              </view>
              <text class="merchant-phone">{{ merchant.phone }}</text>
              <view class="merchant-meta">
                <text class="meta-item">创建时间：{{ merchant.createTime }}</text>
              </view>
            </view>
          </view>
          <view class="merchant-right">
            <view class="merchant-status" :class="merchant.status === '正常' ? 'normal' : 'abnormal'">
              <text class="status-text">{{ merchant.status }}</text>
            </view>
            <text class="arrow">›</text>
          </view>
        </view>
      </view>

      <view class="empty-state" v-if="filteredMerchantList.length === 0">
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
  computed: {
    directMerchantCount() {
      return this.merchantList.filter(m => m.type === '直属').length
    },
    agentMerchantCount() {
      return this.merchantList.filter(m => m.type === '代理').length
    },
    filteredMerchantList() {
      if (this.filterType === 'all') return this.merchantList
      return this.merchantList.filter(m => m.type === (this.filterType === 'direct' ? '直属' : '代理'))
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

.merchant-manage-page {
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

.stats-value.highlight {
  color: $primary-color;
}

.stats-divider {
  width: 1rpx;
  height: 60rpx;
  background-color: #EEEEEE;
}

.filter-section {
  background-color: #FFFFFF;
  margin: 0 20rpx;
  border-radius: 16rpx;
  padding: 20rpx;
}

.filter-tabs {
  display: flex;
  gap: 20rpx;
}

.filter-tab {
  flex: 1;
  padding: 16rpx 0;
  border-radius: 8rpx;
  display: flex;
  align-items: center;
  justify-content: center;
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
  padding: 20rpx;
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

.merchant-left {
  display: flex;
  align-items: center;
  flex: 1;
}

.merchant-icon {
  width: 80rpx;
  height: 80rpx;
  background: linear-gradient(135deg, #FFF3E0 0%, #FFE0B2 100%);
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.icon-text {
  font-size: 40rpx;
}

.merchant-info {
  flex: 1;
}

.name-row {
  display: flex;
  align-items: center;
  margin-bottom: 8rpx;
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

.merchant-phone {
  display: block;
  font-size: 24rpx;
  color: $text-light;
  margin-bottom: 8rpx;
}

.merchant-meta {
  display: flex;
}

.meta-item {
  font-size: 22rpx;
  color: $text-light;
}

.merchant-right {
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
