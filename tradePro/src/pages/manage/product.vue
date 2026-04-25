<template>
  <view class="product-manage-page">
    <view class="stats-card">
      <view class="stats-item">
        <text class="stats-label">产品总数</text>
        <text class="stats-value">{{ productList.length }}</text>
      </view>
      <view class="stats-divider"></view>
      <view class="stats-item">
        <text class="stats-label">机器总数</text>
        <text class="stats-value">{{ totalMachineCount }}</text>
      </view>
    </view>

    <view class="list-section">
      <view class="section-header">
        <text class="section-title">产品列表</text>
      </view>

      <view class="product-list">
        <view class="product-item" v-for="product in productList" :key="product.id" @click="goToDetail(product)">
          <view class="product-info">
            <view class="product-icon">
              <text class="icon-text">📦</text>
            </view>
            <view class="product-detail">
              <view class="product-name-row">
                <text class="product-name">{{ product.name }}</text>
                <view class="product-status" :class="product.status === '在售' ? 'active' : 'inactive'">
                  <text class="status-text">{{ product.status }}</text>
                </view>
              </view>
              <text class="product-code">编码：{{ product.code }}</text>
              <view class="product-meta">
                <text class="product-price">售价：¥{{ formatAmount(product.price) }}</text>
                <text class="product-commission">分润：{{ (product.commission * 100).toFixed(1) }}%</text>
              </view>
            </view>
          </view>
          <view class="product-count">
            <text class="count-label">库存</text>
            <text class="count-value">{{ product.count }}</text>
          </view>
          <text class="arrow">›</text>
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
      productList: mockData.productList
    }
  },
  computed: {
    totalMachineCount() {
      return this.productList.reduce((sum, p) => sum + p.count, 0)
    }
  },
  methods: {
    formatAmount,
    goToDetail(product) {
      uni.navigateTo({
        url: `/pages/product/detail?id=${product.id}`
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/uni.scss';

.product-manage-page {
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
  font-size: 26rpx;
  color: $text-light;
  margin-bottom: 12rpx;
}

.stats-value {
  font-size: 44rpx;
  font-weight: bold;
  color: $primary-color;
}

.stats-divider {
  width: 1rpx;
  height: 80rpx;
  background-color: #EEEEEE;
}

.list-section {
  padding: 0 20rpx;
}

.section-header {
  padding: 20rpx 10rpx;
}

.section-title {
  font-size: 28rpx;
  font-weight: bold;
  color: $text-color;
}

.product-list {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  overflow: hidden;
}

.product-item {
  display: flex;
  align-items: center;
  padding: 24rpx;
  border-bottom: 1rpx solid #F5F5F5;
}

.product-item:last-child {
  border-bottom: none;
}

.product-item:active {
  background-color: #FAFAFA;
}

.product-info {
  flex: 1;
  display: flex;
  align-items: center;
}

.product-icon {
  width: 80rpx;
  height: 80rpx;
  background: linear-gradient(135deg, #FFE0E0 0%, $primary-light 100%);
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.icon-text {
  font-size: 40rpx;
}

.product-detail {
  flex: 1;
}

.product-name-row {
  display: flex;
  align-items: center;
  margin-bottom: 8rpx;
}

.product-name {
  font-size: 30rpx;
  font-weight: bold;
  color: $text-color;
  margin-right: 16rpx;
}

.product-status {
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}

.product-status.active {
  background-color: rgba(76, 175, 80, 0.1);
}

.product-status.inactive {
  background-color: rgba(158, 158, 158, 0.1);
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
  display: block;
  font-size: 24rpx;
  color: $text-light;
  margin-bottom: 8rpx;
}

.product-meta {
  display: flex;
  gap: 24rpx;
}

.product-price {
  font-size: 24rpx;
  color: $text-secondary;
}

.product-commission {
  font-size: 24rpx;
  color: $primary-color;
}

.product-count {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-right: 16rpx;
}

.count-label {
  font-size: 22rpx;
  color: $text-light;
  margin-bottom: 4rpx;
}

.count-value {
  font-size: 32rpx;
  font-weight: bold;
  color: $primary-color;
}

.arrow {
  font-size: 32rpx;
  color: #CCCCCC;
}
</style>
