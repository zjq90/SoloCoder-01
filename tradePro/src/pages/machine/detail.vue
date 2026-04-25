<template>
  <view class="machine-detail-page">
    <view class="header-section">
      <view class="machine-icon" :class="machine.status === '已激活' ? 'activated' : 'inactive'">
        <text class="icon-text">💳</text>
      </view>
      <view class="machine-info">
        <view class="sn-row">
          <text class="sn-label">SN码</text>
          <view class="sn-value-wrapper">
            <text class="sn-value">{{ machine.sn || '-' }}</text>
          </view>
        </view>
        <view class="status-row">
          <text class="product-name">{{ machine.product || '产品名称' }}</text>
          <view class="machine-status" :class="machine.status === '已激活' ? 'active' : 'inactive'">
            <text class="status-text">{{ machine.status || '未激活' }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="info-section">
      <view class="info-card">
        <view class="info-item">
          <text class="info-label">产品类型</text>
          <text class="info-value">{{ machine.product || '-' }}</text>
        </view>
        <view class="divider"></view>
        <view class="info-item">
          <text class="info-label">绑定商户</text>
          <text class="info-value">{{ machine.merchant !== '-' ? machine.merchant : '未绑定' }}</text>
        </view>
        <view class="divider" v-if="machine.merchant !== '-'"></view>
        <view class="info-item" v-if="machine.merchant !== '-'">
          <text class="info-label">绑定时间</text>
          <text class="info-value">{{ machine.bindTime || '-' }}</text>
        </view>
        <view class="divider"></view>
        <view class="info-item">
          <text class="info-label">所属代理</text>
          <text class="info-value">{{ agentName }}</text>
        </view>
      </view>
    </view>

    <view class="stats-section" v-if="machine.status === '已激活'">
      <view class="section-title">交易统计</view>
      <view class="stats-card">
        <view class="stats-row">
          <view class="stats-item">
            <text class="stats-label">累计交易</text>
            <text class="stats-value">¥{{ formatAmount(125680.50) }}</text>
          </view>
          <view class="stats-divider"></view>
          <view class="stats-item">
            <text class="stats-label">交易笔数</text>
            <text class="stats-value">128</text>
          </view>
        </view>
      </view>
    </view>

    <view class="stats-section" v-if="machine.status === '已激活'">
      <view class="section-title">分润统计</view>
      <view class="stats-card">
        <view class="stats-row">
          <view class="stats-item">
            <text class="stats-label">累计分润</text>
            <text class="stats-value highlight">¥{{ formatAmount(2568.80) }}</text>
          </view>
          <view class="stats-divider"></view>
          <view class="stats-item">
            <text class="stats-label">分润比例</text>
            <text class="stats-value">1.0%</text>
          </view>
        </view>
      </view>
    </view>

    <view class="action-section" v-if="machine.status === '未激活'">
      <view class="action-card">
        <view class="action-item" @click="activateMachine">
          <view class="action-icon">
            <text class="icon-text">🔄</text>
          </view>
          <text class="action-text">激活机器</text>
        </view>
        <view class="divider"></view>
        <view class="action-item" @click="bindMerchant">
          <view class="action-icon">
            <text class="icon-text">🔗</text>
          </view>
          <text class="action-text">绑定商户</text>
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
      machineId: null,
      machine: {},
      agentName: '一级代理A'
    }
  },
  onLoad(options) {
    if (options.id) {
      this.machineId = options.id
      this.loadMachineDetail()
    }
  },
  methods: {
    formatAmount,
    loadMachineDetail() {
      const machine = mockData.machineList.find(m => m.id === parseInt(this.machineId))
      if (machine) {
        this.machine = { ...machine }
      }
    },
    activateMachine() {
      uni.showToast({
        title: '激活机器',
        icon: 'none'
      })
    },
    bindMerchant() {
      uni.showToast({
        title: '绑定商户',
        icon: 'none'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/uni.scss';

.machine-detail-page {
  min-height: 100vh;
  background-color: $bg-color;
  padding-bottom: 30rpx;
}

.header-section {
  background: linear-gradient(135deg, $primary-color 0%, $primary-dark);
  padding: 60rpx 30rpx;
  display: flex;
  align-items: center;
}

.machine-icon {
  width: 120rpx;
  height: 120rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}

.machine-icon.activated {
  background-color: rgba(76, 175, 80, 0.3);
}

.machine-icon.inactive {
  background-color: rgba(255, 255, 255, 0.2);
}

.icon-text {
  font-size: 56rpx;
}

.machine-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.sn-row {
  display: flex;
  align-items: center;
  margin-bottom: 16rpx;
}

.sn-label {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.7);
  margin-right: 12rpx;
}

.sn-value-wrapper {
  background-color: rgba(255, 255, 255, 0.15);
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
}

.sn-value {
  font-size: 28rpx;
  color: #FFFFFF;
  font-family: monospace;
}

.status-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.product-name {
  font-size: 32rpx;
  color: rgba(255, 255, 255, 0.9);
}

.machine-status {
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
}

.machine-status.active {
  background-color: rgba(76, 175, 80, 0.2);
}

.machine-status.inactive {
  background-color: rgba(158, 158, 158, 0.2);
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

.info-section {
  padding: 20rpx;
}

.info-card {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  padding: 0 30rpx;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 28rpx 0;
}

.info-label {
  font-size: 28rpx;
  color: $text-secondary;
}

.info-value {
  font-size: 28rpx;
  color: $text-color;
}

.divider {
  height: 1rpx;
  background-color: #F5F5F5;
}

.stats-section {
  padding: 0 20rpx;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: $text-color;
  padding: 20rpx 10rpx;
}

.stats-card {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  padding: 30rpx;
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
  color: $text-color;
}

.stats-value.highlight {
  color: $primary-color;
}

.stats-divider {
  width: 1rpx;
  height: 50rpx;
  background-color: #EEEEEE;
}

.action-section {
  padding: 20rpx;
}

.action-card {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  padding: 0 30rpx;
}

.action-item {
  display: flex;
  align-items: center;
  padding: 28rpx 0;
}

.action-item:active {
  opacity: 0.8;
}

.action-icon {
  width: 64rpx;
  height: 64rpx;
  background: linear-gradient(135deg, #FFE0E0 0%, $primary-light 100%);
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.action-text {
  font-size: 30rpx;
  color: $text-color;
}
</style>
