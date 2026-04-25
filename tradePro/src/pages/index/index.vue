<template>
  <view class="home-page">
    <view class="header-section">
      <view class="welcome-text">
        <text class="greeting">您好，{{ userInfo.name }}</text>
        <text class="level">{{ userInfo.level }}</text>
      </view>
    </view>

    <view class="stats-section">
      <view class="stats-row">
        <view class="stat-card" @click="goToTransactionDetail">
          <view class="stat-icon transaction-icon">
            <text class="icon-text">💰</text>
          </view>
          <view class="stat-info">
            <text class="stat-label">交易量统计</text>
            <text class="stat-value">¥{{ formatAmount(homeStats.transactionAmount) }}</text>
          </view>
          <text class="arrow">›</text>
        </view>
        <view class="stat-card" @click="goToProfitDetail">
          <view class="stat-icon profit-icon">
            <text class="icon-text">📊</text>
          </view>
          <view class="stat-info">
            <text class="stat-label">分润统计</text>
            <text class="stat-value">¥{{ formatAmount(homeStats.profitAmount) }}</text>
          </view>
          <text class="arrow">›</text>
        </view>
      </view>
    </view>

    <view class="grid-section">
      <view class="grid-title">数据概览</view>
      <view class="grid-row">
        <view class="grid-item" @click="goToMerchantList('direct')">
          <view class="grid-icon merchants-icon">
            <text class="icon-text">🏪</text>
          </view>
          <text class="grid-value">{{ homeStats.directMerchants }}</text>
          <text class="grid-label">直属商户</text>
        </view>
        <view class="grid-item" @click="goToMerchantList('agent')">
          <view class="grid-icon agent-merchants-icon">
            <text class="icon-text">🏬</text>
          </view>
          <text class="grid-value">{{ homeStats.agentMerchants }}</text>
          <text class="grid-label">代理商户</text>
        </view>
        <view class="grid-item" @click="goToAgentList">
          <view class="grid-icon agents-icon">
            <text class="icon-text">👥</text>
          </view>
          <text class="grid-value">{{ homeStats.totalAgents }}</text>
          <text class="grid-label">代理总数</text>
        </view>
      </view>
      <view class="grid-row">
        <view class="grid-item" @click="goToProductList">
          <view class="grid-icon products-icon">
            <text class="icon-text">📦</text>
          </view>
          <text class="grid-value">{{ homeStats.products }}</text>
          <text class="grid-label">产品数量</text>
        </view>
        <view class="grid-item" @click="goToMachineList">
          <view class="grid-icon machines-icon">
            <text class="icon-text">💳</text>
          </view>
          <text class="grid-value">{{ homeStats.machines }}</text>
          <text class="grid-label">机器数量</text>
        </view>
        <view class="grid-item placeholder">
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { mockData, formatAmount } from '@/utils/mockData.js'

export default {
  data() {
    return {
      userInfo: mockData.userInfo,
      homeStats: mockData.homeStats
    }
  },
  methods: {
    formatAmount,
    goToTransactionDetail() {
      uni.navigateTo({
        url: '/pages/transaction/detail'
      })
    },
    goToProfitDetail() {
      uni.navigateTo({
        url: '/pages/profit/detail'
      })
    },
    goToMerchantList(type) {
      uni.navigateTo({
        url: `/pages/merchant/list?type=${type}`
      })
    },
    goToAgentList() {
      uni.navigateTo({
        url: '/pages/agent/list'
      })
    },
    goToProductList() {
      uni.navigateTo({
        url: '/pages/product/list'
      })
    },
    goToMachineList() {
      uni.navigateTo({
        url: '/pages/machine/list'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/uni.scss';

.home-page {
  min-height: 100vh;
  background-color: $bg-color;
  padding-bottom: 20rpx;
}

.header-section {
  background: linear-gradient(135deg, $primary-color 0%, $primary-dark 100%);
  padding: 40rpx 30rpx 80rpx;
}

.welcome-text {
  display: flex;
  flex-direction: column;
}

.greeting {
  color: #FFFFFF;
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 8rpx;
}

.level {
  color: rgba(255, 255, 255, 0.8);
  font-size: 26rpx;
}

.stats-section {
  margin-top: -60rpx;
  padding: 0 20rpx;
}

.stats-row {
  display: flex;
  gap: 20rpx;
}

.stat-card {
  flex: 1;
  background-color: #FFFFFF;
  border-radius: 16rpx;
  padding: 24rpx;
  display: flex;
  align-items: center;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.08);
}

.stat-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.transaction-icon {
  background: linear-gradient(135deg, #FFE0E0 0%, #FFCDD2 100%);
}

.profit-icon {
  background: linear-gradient(135deg, #E8F5E9 0%, #C8E6C9 100%);
}

.icon-text {
  font-size: 40rpx;
}

.stat-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.stat-label {
  font-size: 24rpx;
  color: $text-light;
  margin-bottom: 8rpx;
}

.stat-value {
  font-size: 32rpx;
  font-weight: bold;
  color: $text-color;
}

.arrow {
  font-size: 32rpx;
  color: #CCCCCC;
}

.grid-section {
  padding: 30rpx 20rpx;
}

.grid-title {
  font-size: 30rpx;
  font-weight: bold;
  color: $text-color;
  margin-bottom: 20rpx;
  padding-left: 10rpx;
}

.grid-row {
  display: flex;
  gap: 20rpx;
  margin-bottom: 20rpx;
}

.grid-item {
  flex: 1;
  background-color: #FFFFFF;
  border-radius: 16rpx;
  padding: 30rpx 20rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.04);
}

.grid-item:active {
  opacity: 0.9;
  transform: scale(0.98);
}

.grid-icon {
  width: 88rpx;
  height: 88rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16rpx;
}

.merchants-icon {
  background: linear-gradient(135deg, #FFF3E0 0%, #FFE0B2 100%);
}

.agent-merchants-icon {
  background: linear-gradient(135deg, #E3F2FD 0%, #BBDEFB 100%);
}

.agents-icon {
  background: linear-gradient(135deg, #F3E5F5 0%, #E1BEE7 100%);
}

.products-icon {
  background: linear-gradient(135deg, #E0F7FA 0%, #B2EBF2 100%);
}

.machines-icon {
  background: linear-gradient(135deg, #FFF8E1 0%, #FFECB3 100%);
}

.grid-value {
  font-size: 40rpx;
  font-weight: bold;
  color: $text-color;
  margin-bottom: 8rpx;
}

.grid-label {
  font-size: 24rpx;
  color: $text-light;
}

.placeholder {
  background-color: transparent;
  box-shadow: none;
}
</style>
