<template>
  <view class="manage-page">
    <view class="header-section">
      <text class="header-title">管理中心</text>
      <text class="header-subtitle">高效管理您的业务</text>
    </view>

    <view class="menu-section">
      <view class="menu-grid">
        <view class="menu-item" @click="goToProductManage">
          <view class="menu-icon product-icon">
            <text class="icon-text">📦</text>
          </view>
          <text class="menu-label">产品管理</text>
          <view class="menu-badge" v-if="productCount">
            <text class="badge-text">{{ productCount }}</text>
          </view>
        </view>

        <view class="menu-item" @click="goToMachineManage">
          <view class="menu-icon machine-icon">
            <text class="icon-text">💳</text>
          </view>
          <text class="menu-label">机器管理</text>
          <view class="menu-badge" v-if="machineCount">
            <text class="badge-text">{{ machineCount }}</text>
          </view>
        </view>

        <view class="menu-item" @click="goToAgentManage">
          <view class="menu-icon agent-icon">
            <text class="icon-text">👥</text>
          </view>
          <text class="menu-label">代理管理</text>
          <view class="menu-badge" v-if="agentCount">
            <text class="badge-text">{{ agentCount }}</text>
          </view>
        </view>

        <view class="menu-item" @click="goToMerchantManage">
          <view class="menu-icon merchant-icon">
            <text class="icon-text">🏪</text>
          </view>
          <text class="menu-label">商户管理</text>
          <view class="menu-badge" v-if="merchantCount">
            <text class="badge-text">{{ merchantCount }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="quick-stats-section">
      <view class="section-title">快速统计</view>
      <view class="stats-cards">
        <view class="quick-stat-card">
          <view class="stat-header">
            <text class="stat-name">今日交易</text>
            <text class="stat-unit">笔</text>
          </view>
          <text class="stat-number">128</text>
          <view class="stat-trend up">
            <text class="trend-text">↑ 12.5%</text>
          </view>
        </view>
        <view class="quick-stat-card">
          <view class="stat-header">
            <text class="stat-name">本月分润</text>
            <text class="stat-unit">元</text>
          </view>
          <text class="stat-number">8,560</text>
          <view class="stat-trend up">
            <text class="trend-text">↑ 8.3%</text>
          </view>
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
      productCount: mockData.productList.length,
      machineCount: mockData.machineList.length,
      agentCount: mockData.agentList.direct.length + mockData.agentList.indirect.length,
      merchantCount: mockData.merchantList.length
    }
  },
  methods: {
    goToProductManage() {
      uni.navigateTo({
        url: '/pages/manage/product'
      })
    },
    goToMachineManage() {
      uni.navigateTo({
        url: '/pages/manage/machine'
      })
    },
    goToAgentManage() {
      uni.navigateTo({
        url: '/pages/manage/agent'
      })
    },
    goToMerchantManage() {
      uni.navigateTo({
        url: '/pages/manage/merchant'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/uni.scss';

.manage-page {
  min-height: 100vh;
  background-color: $bg-color;
  padding-bottom: 30rpx;
}

.header-section {
  background: linear-gradient(135deg, $primary-color 0%, $primary-dark 100%);
  padding: 60rpx 30rpx 80rpx;
}

.header-title {
  display: block;
  font-size: 40rpx;
  font-weight: bold;
  color: #FFFFFF;
  margin-bottom: 12rpx;
}

.header-subtitle {
  display: block;
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
}

.menu-section {
  margin-top: -50rpx;
  padding: 0 20rpx;
}

.menu-grid {
  background-color: #FFFFFF;
  border-radius: 20rpx;
  padding: 40rpx 20rpx;
  display: flex;
  flex-wrap: wrap;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.06);
}

.menu-item {
  width: 25%;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}

.menu-item:active {
  opacity: 0.8;
}

.menu-icon {
  width: 96rpx;
  height: 96rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16rpx;
}

.product-icon {
  background: linear-gradient(135deg, #FFE0E0 0%, $primary-light 100%);
}

.machine-icon {
  background: linear-gradient(135deg, #E3F2FD 0%, #BBDEFB 100%);
}

.agent-icon {
  background: linear-gradient(135deg, #FFF3E0 0%, #FFE0B2 100%);
}

.merchant-icon {
  background: linear-gradient(135deg, #E8F5E9 0%, #C8E6C9 100%);
}

.icon-text {
  font-size: 44rpx;
}

.menu-label {
  font-size: 26rpx;
  color: $text-color;
  font-weight: 500;
}

.menu-badge {
  position: absolute;
  top: 0;
  right: 20rpx;
  background-color: $primary-color;
  border-radius: 20rpx;
  min-width: 36rpx;
  height: 36rpx;
  padding: 0 10rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.badge-text {
  font-size: 20rpx;
  color: #FFFFFF;
}

.quick-stats-section {
  padding: 40rpx 20rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: $text-color;
  margin-bottom: 24rpx;
  padding-left: 10rpx;
}

.stats-cards {
  display: flex;
  gap: 20rpx;
}

.quick-stat-card {
  flex: 1;
  background-color: #FFFFFF;
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.04);
}

.stat-header {
  display: flex;
  align-items: baseline;
  margin-bottom: 16rpx;
}

.stat-name {
  font-size: 26rpx;
  color: $text-light;
  margin-right: 8rpx;
}

.stat-unit {
  font-size: 22rpx;
  color: $text-light;
}

.stat-number {
  display: block;
  font-size: 44rpx;
  font-weight: bold;
  color: $text-color;
  margin-bottom: 12rpx;
}

.stat-trend {
  display: inline-flex;
  align-items: center;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}

.stat-trend.up {
  background-color: rgba(76, 175, 80, 0.1);
}

.trend-text {
  font-size: 22rpx;
  color: #4CAF50;
}
</style>
