<template>
  <view class="agent-detail-page">
    <view class="header-section">
      <view class="agent-avatar">
        <text class="avatar-text">{{ agent.name ? agent.name.charAt(0) : '代' }}</text>
      </view>
      <view class="agent-info">
        <view class="name-row">
          <text class="agent-name">{{ agent.name || '代理名称' }}</text>
          <view class="agent-status" :class="agent.status === '正常' ? 'normal' : 'abnormal'">
            <text class="status-text">{{ agent.status || '正常' }}</text>
          </view>
        </view>
        <view class="level-row">
          <view class="agent-level">
            <text class="level-text">{{ agent.level || '一级代理' }}</text>
          </view>
          <text class="agent-phone">{{ agent.phone || '-' }}</text>
        </view>
        <view class="parent-row" v-if="agent.parent">
          <text class="parent-label">上级代理：</text>
          <text class="parent-name">{{ agent.parent }}</text>
        </view>
      </view>
    </view>

    <view class="action-section" v-if="isDirect">
      <view class="action-card">
        <view class="action-item" @click="setProducts">
          <view class="action-icon product-icon">
            <text class="icon-text">📦</text>
          </view>
          <text class="action-text">设置可代理产品</text>
          <text class="arrow">›</text>
        </view>
        <view class="divider"></view>
        <view class="action-item" @click="setRate">
          <view class="action-icon rate-icon">
            <text class="icon-text">📊</text>
          </view>
          <text class="action-text">设置分润点</text>
          <text class="arrow">›</text>
        </view>
      </view>
    </view>

    <view class="stats-section">
      <view class="section-title">业务统计</view>
      <view class="stats-card">
        <view class="stats-row">
          <view class="stats-item">
            <text class="stats-label">商户数量</text>
            <text class="stats-value">{{ agent.merchants || 0 }}</text>
          </view>
          <view class="stats-divider"></view>
          <view class="stats-item">
            <text class="stats-label">机器数量</text>
            <text class="stats-value">{{ agent.machines || 0 }}</text>
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

    <view class="product-section" v-if="isDirect">
      <view class="section-header">
        <text class="section-title">可代理产品</text>
      </view>
      <view class="product-list">
        <view class="product-item" v-for="product in agentProducts" :key="product.id">
          <view class="product-info">
            <text class="product-name">{{ product.name }}</text>
            <text class="product-rate">分润：{{ (product.commission * 100).toFixed(1) }}%</text>
          </view>
          <view class="product-count">
            <text class="count-label">库存</text>
            <text class="count-value">{{ product.count }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="popup-mask" v-if="showProductPopup" @click="closePopup">
      <view class="popup-content" @click.stop>
        <view class="popup-header">
          <text class="popup-title">设置可代理产品</text>
          <view class="popup-close" @click="closePopup">
            <text class="close-text">✕</text>
          </view>
        </view>
        <view class="popup-body">
          <view class="product-option" v-for="product in productList" :key="product.id">
            <checkbox :checked="selectedProducts.includes(product.id)" @click="toggleProduct(product.id)" />
            <view class="option-info">
              <text class="option-name">{{ product.name }}</text>
              <text class="option-desc">分润：{{ (product.commission * 100).toFixed(1) }}%</text>
            </view>
          </view>
        </view>
        <view class="popup-footer">
          <view class="cancel-btn" @click="closePopup">
            <text class="btn-text">取消</text>
          </view>
          <view class="confirm-btn" @click="saveProducts">
            <text class="btn-text">确定</text>
          </view>
        </view>
      </view>
    </view>

    <view class="popup-mask" v-if="showRatePopup" @click="closePopup">
      <view class="popup-content" @click.stop>
        <view class="popup-header">
          <text class="popup-title">设置分润点</text>
          <view class="popup-close" @click="closePopup">
            <text class="close-text">✕</text>
          </view>
        </view>
        <view class="popup-body">
          <view class="rate-item" v-for="product in selectedProductsForRate" :key="product.id">
            <view class="rate-header">
              <text class="rate-product-name">{{ product.name }}</text>
              <text class="rate-product-default">默认：{{ (product.commission * 100).toFixed(1) }}%</text>
            </view>
            <view class="rate-input-row">
              <text class="rate-label">分润比例：</text>
              <input 
                class="rate-input" 
                type="digit"
                :value="getRateValue(product.id)"
                @input="setRateValue(product.id, $event)"
                placeholder="请输入分润比例"
              />
              <text class="rate-unit">%</text>
            </view>
          </view>
        </view>
        <view class="popup-footer">
          <view class="cancel-btn" @click="closePopup">
            <text class="btn-text">取消</text>
          </view>
          <view class="confirm-btn" @click="saveRates">
            <text class="btn-text">确定</text>
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
      agentId: null,
      agentType: 'direct',
      agent: {},
      productList: mockData.productList,
      showProductPopup: false,
      showRatePopup: false,
      selectedProducts: [1, 2],
      productRates: {
        1: '1.0',
        2: '1.2'
      }
    }
  },
  computed: {
    isDirect() {
      return this.agentType === 'direct'
    },
    agentProducts() {
      return this.productList.filter(p => this.selectedProducts.includes(p.id))
    },
    selectedProductsForRate() {
      return this.productList.filter(p => this.selectedProducts.includes(p.id))
    }
  },
  onLoad(options) {
    if (options.id) {
      this.agentId = options.id
    }
    if (options.type) {
      this.agentType = options.type
    }
    this.loadAgentDetail()
  },
  methods: {
    formatAmount,
    loadAgentDetail() {
      const agentList = this.agentType === 'direct' ? mockData.agentList.direct : mockData.agentList.indirect
      const agent = agentList.find(a => a.id === parseInt(this.agentId))
      if (agent) {
        this.agent = { ...agent }
      }
    },
    setProducts() {
      this.showProductPopup = true
    },
    setRate() {
      this.showRatePopup = true
    },
    closePopup() {
      this.showProductPopup = false
      this.showRatePopup = false
    },
    toggleProduct(productId) {
      const index = this.selectedProducts.indexOf(productId)
      if (index > -1) {
        this.selectedProducts.splice(index, 1)
      } else {
        this.selectedProducts.push(productId)
      }
    },
    getRateValue(productId) {
      return this.productRates[productId] || ''
    },
    setRateValue(productId, event) {
      const value = event.detail.value
      this.productRates[productId] = value
    },
    saveProducts() {
      uni.showToast({
        title: '设置成功',
        icon: 'success'
      })
      this.closePopup()
    },
    saveRates() {
      uni.showToast({
        title: '保存成功',
        icon: 'success'
      })
      this.closePopup()
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/uni.scss';

.agent-detail-page {
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

.agent-avatar {
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

.agent-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.name-row {
  display: flex;
  align-items: center;
  margin-bottom: 16rpx;
}

.agent-name {
  font-size: 36rpx;
  font-weight: bold;
  color: #FFFFFF;
  margin-right: 16rpx;
}

.agent-status {
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
}

.agent-status.normal {
  background-color: rgba(76, 175, 80, 0.2);
}

.agent-status.abnormal {
  background-color: rgba(244, 67, 54, 0.2);
}

.status-text {
  font-size: 22rpx;
}

.agent-status.normal .status-text {
  color: #4CAF50;
}

.agent-status.abnormal .status-text {
  color: #F44336;
}

.level-row {
  display: flex;
  align-items: center;
  gap: 16rpx;
  margin-bottom: 8rpx;
}

.agent-level {
  background-color: rgba(255, 255, 255, 0.2);
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}

.level-text {
  font-size: 24rpx;
  color: #FFFFFF;
}

.agent-phone {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
}

.parent-row {
  display: flex;
  align-items: center;
  margin-top: 8rpx;
}

.parent-label {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.7);
}

.parent-name {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.9);
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

.action-icon {
  width: 64rpx;
  height: 64rpx;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.product-icon {
  background: linear-gradient(135deg, #FFE0E0 0%, $primary-light 100%);
}

.rate-icon {
  background: linear-gradient(135deg, #E8F5E9 0%, #C8E6C9 100%);
}

.icon-text {
  font-size: 32rpx;
}

.action-text {
  flex: 1;
  font-size: 30rpx;
  color: $text-color;
}

.arrow {
  font-size: 32rpx;
  color: #CCCCCC;
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

.product-section {
  padding: 0 20rpx;
}

.section-header {
  padding: 20rpx 10rpx;
}

.product-list {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  overflow: hidden;
}

.product-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  border-bottom: 1rpx solid #F5F5F5;
}

.product-item:last-child {
  border-bottom: none;
}

.product-info {
  display: flex;
  flex-direction: column;
}

.product-name {
  font-size: 28rpx;
  font-weight: bold;
  color: $text-color;
  margin-bottom: 4rpx;
}

.product-rate {
  font-size: 24rpx;
  color: $primary-color;
}

.product-count {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.count-label {
  font-size: 22rpx;
  color: $text-light;
}

.count-value {
  font-size: 32rpx;
  font-weight: bold;
  color: $primary-color;
}

.popup-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.popup-content {
  width: 85%;
  background-color: #FFFFFF;
  border-radius: 20rpx;
  overflow: hidden;
}

.popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #F5F5F5;
}

.popup-title {
  font-size: 32rpx;
  font-weight: bold;
  color: $text-color;
}

.popup-close {
  width: 48rpx;
  height: 48rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-text {
  font-size: 32rpx;
  color: $text-light;
}

.popup-body {
  padding: 20rpx;
  max-height: 50vh;
  overflow-y: auto;
}

.product-option {
  display: flex;
  align-items: center;
  padding: 24rpx;
  border-bottom: 1rpx solid #F5F5F5;
}

.product-option:last-child {
  border-bottom: none;
}

.option-info {
  flex: 1;
  margin-left: 20rpx;
  display: flex;
  flex-direction: column;
}

.option-name {
  font-size: 28rpx;
  color: $text-color;
  margin-bottom: 4rpx;
}

.option-desc {
  font-size: 24rpx;
  color: $text-light;
}

.popup-footer {
  display: flex;
  border-top: 1rpx solid #F5F5F5;
}

.cancel-btn, .confirm-btn {
  flex: 1;
  padding: 30rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.cancel-btn {
  border-right: 1rpx solid #F5F5F5;
}

.cancel-btn .btn-text {
  color: $text-secondary;
}

.confirm-btn {
  background-color: $primary-color;
}

.confirm-btn .btn-text {
  color: #FFFFFF;
}

.btn-text {
  font-size: 30rpx;
}

.rate-item {
  padding: 24rpx;
  border-bottom: 1rpx solid #F5F5F5;
}

.rate-item:last-child {
  border-bottom: none;
}

.rate-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;
}

.rate-product-name {
  font-size: 28rpx;
  font-weight: bold;
  color: $text-color;
}

.rate-product-default {
  font-size: 24rpx;
  color: $text-light;
}

.rate-input-row {
  display: flex;
  align-items: center;
}

.rate-label {
  font-size: 26rpx;
  color: $text-secondary;
}

.rate-input {
  flex: 1;
  height: 72rpx;
  border: 1rpx solid #DDDDDD;
  border-radius: 8rpx;
  padding: 0 20rpx;
  font-size: 28rpx;
}

.rate-unit {
  font-size: 26rpx;
  color: $text-secondary;
  margin-left: 12rpx;
}
</style>
