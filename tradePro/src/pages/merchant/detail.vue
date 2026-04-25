<template>
  <view class="merchant-detail-page">
    <view class="header-section">
      <view class="merchant-avatar">
        <text class="avatar-text">{{ merchant.name ? merchant.name.charAt(0) : '商' }}</text>
      </view>
      <view class="merchant-info">
        <view class="name-row">
          <text class="merchant-name">{{ merchant.name || '商户名称' }}</text>
          <view class="merchant-status" :class="merchant.status === '正常' ? 'normal' : 'abnormal'">
            <text class="status-text">{{ merchant.status || '正常' }}</text>
          </view>
        </view>
        <view class="type-row">
          <view class="merchant-type" :class="merchant.type === '直属' ? 'direct' : 'agent'">
            <text class="type-text">{{ merchant.type || '直属' }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="info-section">
      <view class="info-card">
        <view class="info-item">
          <text class="info-label">联系电话</text>
          <text class="info-value">{{ merchant.phone || '-' }}</text>
        </view>
        <view class="divider"></view>
        <view class="info-item">
          <text class="info-label">创建时间</text>
          <text class="info-value">{{ merchant.createTime || '-' }}</text>
        </view>
        <view class="divider" v-if="merchant.type === '代理'"></view>
        <view class="info-item" v-if="merchant.type === '代理'">
          <text class="info-label">所属代理</text>
          <text class="info-value">{{ merchant.agentName || '一级代理A' }}</text>
        </view>
      </view>
    </view>

    <view class="stats-section">
      <view class="section-title">交易统计</view>
      <view class="stats-card">
        <view class="stats-row">
          <view class="stats-item">
            <text class="stats-label">累计交易</text>
            <text class="stats-value">¥{{ formatAmount(1256800.50) }}</text>
          </view>
          <view class="stats-divider"></view>
          <view class="stats-item">
            <text class="stats-label">交易笔数</text>
            <text class="stats-value">128</text>
          </view>
        </view>
      </view>
    </view>

    <view class="stats-section">
      <view class="section-title">分润统计</view>
      <view class="stats-card">
        <view class="stats-row">
          <view class="stats-item">
            <text class="stats-label">累计分润</text>
            <text class="stats-value highlight">¥{{ formatAmount(25680.80) }}</text>
          </view>
          <view class="stats-divider"></view>
          <view class="stats-item">
            <text class="stats-label">分润比例</text>
            <text class="stats-value">1.0%</text>
          </view>
        </view>
      </view>
    </view>

    <view class="machine-section">
      <view class="section-header">
        <text class="section-title">绑定机器</text>
        <text class="section-count">共 {{ merchantMachineCount }} 台</text>
      </view>
      <view class="machine-list">
        <view class="machine-item" v-for="machine in merchantMachines" :key="machine.id">
          <view class="machine-info">
            <text class="machine-sn">{{ machine.sn }}</text>
            <text class="machine-product">{{ machine.product }}</text>
          </view>
          <view class="machine-status" :class="machine.status === '已激活' ? 'active' : 'inactive'">
            <text class="status-text">{{ machine.status }}</text>
          </view>
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
      merchantId: null,
      merchant: {}
    }
  },
  computed: {
    merchantMachineCount() {
      return this.merchantMachines.length
    },
    merchantMachines() {
      return mockData.machineList.filter(m => m.merchant === this.merchant.name).slice(0, 3)
    }
  },
  onLoad(options) {
    if (options.id) {
      this.merchantId = options.id
      this.loadMerchantDetail()
    }
  },
  methods: {
    formatAmount,
    loadMerchantDetail() {
      const merchant = mockData.merchantList.find(m => m.id === parseInt(this.merchantId))
      if (merchant) {
        this.merchant = { ...merchant }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/uni.scss';

.merchant-detail-page {
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

.merchant-avatar {
  width: 120rpx;
  height: 120rpx;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}

.avatar-text {
  font-size: 48rpx;
  font-weight: bold;
  color: #FFFFFF;
}

.merchant-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.name-row {
  display: flex;
  align-items: center;
  margin-bottom: 16rpx;
}

.merchant-name {
  font-size: 36rpx;
  font-weight: bold;
  color: #FFFFFF;
  margin-right: 16rpx;
}

.merchant-status {
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
}

.merchant-status.normal {
  background-color: rgba(76, 175, 80, 0.2);
}

.merchant-status.abnormal {
  background-color: rgba(244, 67, 54, 0.2);
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

.type-row {
  display: flex;
}

.merchant-type {
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
}

.merchant-type.direct {
  background-color: rgba(255, 255, 255, 0.2);
}

.merchant-type.agent {
  background-color: rgba(33, 150, 243, 0.3);
}

.type-text {
  font-size: 24rpx;
  color: #FFFFFF;
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

.machine-section {
  padding: 0 20rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 10rpx;
}

.section-count {
  font-size: 24rpx;
  color: $text-light;
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

.machine-info {
  display: flex;
  flex-direction: column;
}

.machine-sn {
  font-size: 28rpx;
  font-weight: bold;
  color: $text-color;
  margin-bottom: 4rpx;
  font-family: monospace;
}

.machine-product {
  font-size: 24rpx;
  color: $text-light;
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

.machine-status.active .status-text {
  color: #4CAF50;
}

.machine-status.inactive .status-text {
  color: #9E9E9E;
}
</style>
