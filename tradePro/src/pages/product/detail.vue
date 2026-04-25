<template>
  <view class="product-detail-page">
    <view class="header-section">
      <view class="product-icon">
        <text class="icon-text">📦</text>
      </view>
      <view class="product-info">
        <view class="name-row">
          <text class="product-name">{{ product.name || '产品名称' }}</text>
          <view class="product-status" :class="product.status === '在售' ? 'active' : 'inactive'">
            <text class="status-text">{{ product.status || '在售' }}</text>
          </view>
        </view>
        <text class="product-code">编码：{{ product.code || '-' }}</text>
      </view>
    </view>

    <view class="info-section">
      <view class="info-card">
        <view class="info-item">
          <text class="info-label">产品价格</text>
          <text class="info-value">¥{{ formatAmount(product.price || 0) }}</text>
        </view>
        <view class="divider"></view>
        <view class="info-item">
          <text class="info-label">分润比例</text>
          <text class="info-value highlight">{{ (product.commission * 100).toFixed(1) }}%</text>
        </view>
        <view class="divider"></view>
        <view class="info-item">
          <text class="info-label">库存数量</text>
          <text class="info-value">{{ product.count || 0 }} 台</text>
        </view>
      </view>
    </view>

    <view class="stats-section">
      <view class="section-title">使用统计</view>
      <view class="stats-card">
        <view class="stats-row">
          <view class="stats-item">
            <text class="stats-label">已发放</text>
            <text class="stats-value">156</text>
          </view>
          <view class="stats-divider"></view>
          <view class="stats-item">
            <text class="stats-label">已激活</text>
            <text class="stats-value">128</text>
          </view>
          <view class="stats-divider"></view>
          <view class="stats-item">
            <text class="stats-label">剩余库存</text>
            <text class="stats-value highlight">{{ product.count || 0 }}</text>
          </view>
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
            <text class="stats-label">累计分润</text>
            <text class="stats-value highlight">¥{{ formatAmount(25680.80) }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="agent-section">
      <view class="section-header">
        <text class="section-title">可代理此产品的代理</text>
      </view>
      <view class="agent-list">
        <view class="agent-item" v-for="agent in agentList" :key="agent.id">
          <view class="agent-avatar">
            <text class="avatar-text">{{ agent.name.charAt(0) }}</text>
          </view>
          <view class="agent-info">
            <view class="name-row">
              <text class="agent-name">{{ agent.name }}</text>
              <view class="agent-level">
                <text class="level-text">{{ agent.level }}</text>
              </view>
            </view>
            <view class="stats-row">
              <text class="stat-item">商户：{{ agent.merchants }}</text>
              <text class="stat-item">机器：{{ agent.machines }}</text>
            </view>
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
      productId: null,
      product: {},
      agentList: []
    }
  },
  onLoad(options) {
    if (options.id) {
      this.productId = options.id
      this.loadProductDetail()
    }
  },
  methods: {
    formatAmount,
    loadProductDetail() {
      const product = mockData.productList.find(p => p.id === parseInt(this.productId))
      if (product) {
        this.product = { ...product }
      }
      this.agentList = mockData.agentList.direct.slice(0, 3)
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/uni.scss';

.product-detail-page {
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

.product-icon {
  width: 120rpx;
  height: 120rpx;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}

.icon-text {
  font-size: 56rpx;
}

.product-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.name-row {
  display: flex;
  align-items: center;
  margin-bottom: 12rpx;
}

.product-name {
  font-size: 36rpx;
  font-weight: bold;
  color: #FFFFFF;
  margin-right: 16rpx;
}

.product-status {
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
}

.product-status.active {
  background-color: rgba(76, 175, 80, 0.2);
}

.product-status.inactive {
  background-color: rgba(158, 158, 158, 0.2);
}

.status-text {
  font-size: 22rpx;
}

.product-status.active .status-text {
  color: #4CAF50;
}

.product-status.inactive .status-text {
  color: #9E9E9E;
}

.product-code {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
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
  font-size: 30rpx;
  font-weight: bold;
  color: $text-color;
}

.info-value.highlight {
  color: $primary-color;
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

.agent-section {
  padding: 0 20rpx;
}

.section-header {
  padding: 20rpx 10rpx;
}

.agent-list {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  overflow: hidden;
}

.agent-item {
  display: flex;
  align-items: center;
  padding: 24rpx;
  border-bottom: 1rpx solid #F5F5F5;
}

.agent-item:last-child {
  border-bottom: none;
}

.agent-avatar {
  width: 64rpx;
  height: 64rpx;
  background: linear-gradient(135deg, $primary-color 0%, $primary-dark);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.avatar-text {
  font-size: 28rpx;
  font-weight: bold;
  color: #FFFFFF;
}

.agent-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.name-row {
  display: flex;
  align-items: center;
  margin-bottom: 8rpx;
}

.agent-name {
  font-size: 28rpx;
  font-weight: bold;
  color: $text-color;
  margin-right: 16rpx;
}

.agent-level {
  background-color: rgba($primary-color, 0.1);
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}

.level-text {
  font-size: 22rpx;
  color: $primary-color;
}

.stats-row {
  display: flex;
  gap: 24rpx;
}

.stat-item {
  font-size: 24rpx;
  color: $text-light;
}
</style>
