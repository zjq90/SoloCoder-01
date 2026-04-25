<template>
  <view class="transaction-detail-page">
    <view class="header-section">
      <view class="header-info">
        <text class="header-label">交易总额</text>
        <text class="header-value">¥{{ formatAmount(totalTransaction) }}</text>
      </view>
      <view class="header-stats">
        <view class="stat-item">
          <text class="stat-label">交易笔数</text>
          <text class="stat-value">{{ transactionList.length }}</text>
        </view>
      </view>
    </view>

    <view class="list-section">
      <view class="section-header">
        <text class="section-title">交易明细</text>
      </view>

      <view class="transaction-list">
        <view class="transaction-item" v-for="item in transactionList" :key="item.id">
          <view class="item-left">
            <view class="merchant-icon">
              <text class="icon-text">💳</text>
            </view>
            <view class="item-info">
              <text class="merchant-name">{{ item.merchantName }}</text>
              <view class="item-meta">
                <text class="meta-tag">{{ item.type }}</text>
                <text class="meta-time">{{ item.time }}</text>
              </view>
            </view>
          </view>
          <view class="item-right">
            <text class="amount">¥{{ formatAmount(item.amount) }}</text>
          </view>
        </view>
      </view>

      <view class="empty-state" v-if="transactionList.length === 0">
        <text class="empty-icon">📋</text>
        <text class="empty-text">暂无交易记录</text>
      </view>
    </view>
  </view>
</template>

<script>
import { mockData, formatAmount } from '@/utils/mockData.js'

export default {
  data() {
    return {
      transactionList: mockData.transactionList
    }
  },
  computed: {
    totalTransaction() {
      return this.transactionList.reduce((sum, item) => sum + item.amount, 0)
    }
  },
  methods: {
    formatAmount
  }
}
</script>

<style lang="scss" scoped>
@import '@/uni.scss';

.transaction-detail-page {
  min-height: 100vh;
  background-color: $bg-color;
  padding-bottom: 30rpx;
}

.header-section {
  background: linear-gradient(135deg, $primary-color 0%, $primary-dark);
  padding: 60rpx 30rpx 40rpx;
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
  padding: 20rpx;
}

.section-header {
  padding: 20rpx 10rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: $text-color;
}

.transaction-list {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  overflow: hidden;
}

.transaction-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  border-bottom: 1rpx solid #F5F5F5;
}

.transaction-item:last-child {
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
  background: linear-gradient(135deg, #E3F2FD 0%, #BBDEFB 100%);
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
  color: $text-color;
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
