<template>
  <view class="profit-page">
    <view class="header-section">
      <view class="profit-summary">
        <text class="summary-label">累计分润（元）</text>
        <text class="summary-value">¥{{ formatAmount(profitTotal) }}</text>
      </view>
      <view class="monthly-summary">
        <view class="monthly-item">
          <text class="monthly-label">本月分润</text>
          <text class="monthly-value">¥{{ formatAmount(profitMonthTotal) }}</text>
        </view>
      </view>
    </view>

    <view class="stats-section">
      <view class="stats-card">
        <view class="stats-row">
          <view class="stats-item">
            <text class="stats-label">分润笔数</text>
            <text class="stats-value">{{ profitDetailList.length }}</text>
          </view>
          <view class="stats-divider"></view>
          <view class="stats-item">
            <text class="stats-label">平均单笔</text>
            <text class="stats-value">¥{{ formatAmount(averageProfit) }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="detail-section">
      <view class="section-header">
        <text class="section-title">分润明细</text>
      </view>

      <view class="detail-list">
        <view class="detail-item" v-for="item in profitDetailList" :key="item.id">
          <view class="detail-left">
            <view class="merchant-icon">
              <text class="icon-text">💰</text>
            </view>
            <view class="detail-info">
              <text class="merchant-name">{{ item.merchantName }}</text>
              <view class="info-row">
                <text class="info-item">交易金额：¥{{ formatAmount(item.transactionAmount) }}</text>
              </view>
              <view class="info-row">
                <text class="info-item">分润比例：{{ (item.rate * 100).toFixed(1) }}%</text>
                <text class="info-time">{{ item.time }}</text>
              </view>
            </view>
          </view>
          <view class="detail-right">
            <text class="profit-amount">+¥{{ formatAmount(item.amount) }}</text>
          </view>
        </view>
      </view>

      <view class="empty-state" v-if="profitDetailList.length === 0">
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
      profitTotal: mockData.profitTotal,
      profitMonthTotal: mockData.profitMonthTotal,
      profitDetailList: mockData.profitDetailList
    }
  },
  computed: {
    averageProfit() {
      if (this.profitDetailList.length === 0) return 0
      const total = this.profitDetailList.reduce((sum, item) => sum + item.amount, 0)
      return total / this.profitDetailList.length
    }
  },
  methods: {
    formatAmount
  }
}
</script>

<style lang="scss" scoped>
@import '@/uni.scss';

.profit-page {
  min-height: 100vh;
  background-color: $bg-color;
  padding-bottom: 30rpx;
}

.header-section {
  background: linear-gradient(135deg, $primary-color 0%, $primary-dark);
  padding: 60rpx 30rpx 40rpx;
}

.profit-summary {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 30rpx;
}

.summary-label {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 16rpx;
}

.summary-value {
  font-size: 56rpx;
  font-weight: bold;
  color: #FFFFFF;
}

.monthly-summary {
  background-color: rgba(255, 255, 255, 0.15);
  border-radius: 16rpx;
  padding: 24rpx;
}

.monthly-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.monthly-label {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.85);
}

.monthly-value {
  font-size: 32rpx;
  font-weight: bold;
  color: #FFFFFF;
}

.stats-section {
  margin-top: -20rpx;
  padding: 0 20rpx;
}

.stats-card {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.06);
}

.stats-row {
  display: flex;
  align-items: center;
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
  font-size: 32rpx;
  font-weight: bold;
  color: $primary-color;
}

.stats-divider {
  width: 1rpx;
  height: 50rpx;
  background-color: #EEEEEE;
}

.detail-section {
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

.detail-list {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  overflow: hidden;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  border-bottom: 1rpx solid #F5F5F5;
}

.detail-item:last-child {
  border-bottom: none;
}

.detail-left {
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

.detail-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.merchant-name {
  font-size: 28rpx;
  font-weight: bold;
  color: $text-color;
  margin-bottom: 8rpx;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6rpx;
}

.info-row:last-child {
  margin-bottom: 0;
}

.info-item {
  font-size: 24rpx;
  color: $text-light;
}

.info-time {
  font-size: 22rpx;
  color: $text-light;
}

.detail-right {
  display: flex;
  align-items: center;
}

.profit-amount {
  font-size: 32rpx;
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
