<template>
  <view class="profit-detail-page">
    <view class="header-section">
      <view class="header-info">
        <text class="header-label">分润总额</text>
        <text class="header-value">¥{{ formatAmount(totalProfit) }}</text>
      </view>
      <view class="header-stats">
        <view class="stat-item">
          <text class="stat-label">分润笔数</text>
          <text class="stat-value">{{ profitList.length }}</text>
        </view>
      </view>
    </view>

    <view class="list-section">
      <view class="section-header">
        <text class="section-title">分润明细</text>
      </view>

      <view class="profit-list">
        <view class="profit-item" v-for="item in profitList" :key="item.id">
          <view class="item-left">
            <view class="merchant-icon">
              <text class="icon-text">💰</text>
            </view>
            <view class="item-info">
              <text class="merchant-name">{{ item.merchantName }}</text>
              <view class="item-meta">
                <text class="meta-tag">分润比例 {{ (item.rate * 100).toFixed(1) }}%</text>
                <text class="meta-time">{{ item.time }}</text>
              </view>
            </view>
          </view>
          <view class="item-right">
            <text class="amount">+¥{{ formatAmount(item.amount) }}</text>
          </view>
        </view>
      </view>

      <view class="empty-state" v-if="profitList.length === 0">
        <text class="empty-icon">📊</text>
        <text class="empty-text">暂无分润记录</text>
      </view>
    </view>
  </view>
</template>

<script>
import { mockData, formatAmount } from '@/utils/mockData.js'

export default {
  data() {
    return {
      profitList: mockData.profitList
    }
  },
  computed: {
    totalProfit() {
      return this.profitList.reduce((sum, item) => sum + item.amount, 0)
    }
  },
  methods: {
    formatAmount
  }
}
</script>

<style lang="scss" scoped>
@import '@/uni.scss';

.profit-detail-page {
  min-height: 100vh;
  background-color: $bg-color;
  padding-bottom: 30rpx;
}

.header-section {
  background: linear-gradient(135deg, $primary-color 0%, $primary-dark);
  padding: 60rpx 30rpx 40rpx;
  margin: -30rpx;
  border-radius: 20rpx;
}

.header-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 30rpx;
}

.header-label {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 16rpx;
}

.header-value {
  font-size: 52rpx;
  font-weight: bold;
  color: #FFFFFF;
}

.header-stats {
  background-color: rgba(255, 255, 255, 0.15);
  border-radius: 16rpx;
  padding: 24rpx;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-label {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.85);
}

.stat-value {
  font-size: 32rpx;
  font-weight: bold;
  color: #FFFFFF;
}

.list-section {
  padding: 0 20rpx;
}

.section-header {
  padding: 20rpx 10rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: $text-color;
}

.profit-list {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  overflow: hidden;
}

.profit-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  border-bottom: 1rpx solid #F5F5F5;
}

.profit-item:last-child {
  border-bottom: none;
}

.item-left {
  display: flex;
  align-items: center;
  flex: 1;
}

.merchant-icon {
  width: 72rpx;
  height: 72rpx;
  background: linear-gradient(135deg, #FFE0E0 0%, $primary-light 100%);
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

.merchant-name {
  font-size: 28rpx;
  font-weight: bold;
  color: $text-color;
  margin-bottom: 12rpx;
}

.item-meta {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.meta-tag {
  font-size: 22rpx;
  color: $primary-color;
  background-color: rgba($primary-color, 0.1);
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}

.meta-time {
  font-size: 22rpx;
  color: $text-light;
}

.item-right {
  display: flex;
  align-items: center;
}

.amount {
  font-size: 30rpx;
  font-weight: bold;
  color: #4CAF50;
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
