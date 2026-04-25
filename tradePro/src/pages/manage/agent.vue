<template>
  <view class="agent-manage-page">
    <view class="stats-card">
      <view class="stats-item">
        <text class="stats-label">直属代理</text>
        <text class="stats-value">{{ agentList.direct.length }}</text>
      </view>
      <view class="stats-divider"></view>
      <view class="stats-item">
        <text class="stats-label">间接代理</text>
        <text class="stats-value">{{ agentList.indirect.length }}</text>
      </view>
      <view class="stats-divider"></view>
      <view class="stats-item">
        <text class="stats-label">代理总数</text>
        <text class="stats-value highlight">{{ totalAgents }}</text>
      </view>
    </view>

    <view class="tab-section">
      <view class="tab-list">
        <view 
          class="tab-item" 
          :class="{ active: currentTab === 'direct' }"
          @click="switchTab('direct')"
        >
          <text class="tab-text">直属代理</text>
          <view class="tab-indicator" v-if="currentTab === 'direct'"></view>
        </view>
        <view 
          class="tab-item" 
          :class="{ active: currentTab === 'indirect' }"
          @click="switchTab('indirect')"
        >
          <text class="tab-text">间接代理</text>
          <view class="tab-indicator" v-if="currentTab === 'indirect'"></view>
        </view>
      </view>
    </view>

    <view class="list-section">
      <view class="agent-list">
        <view class="agent-item" v-for="agent in currentAgentList" :key="agent.id">
          <view class="agent-header" @click="goToDetail(agent)">
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
              <text class="agent-phone">{{ agent.phone }}</text>
              <view class="agent-stats-row">
                <text class="stat-item">商户：{{ agent.merchants }}</text>
                <text class="stat-item">机器：{{ agent.machines }}</text>
              </view>
              <view class="parent-info" v-if="agent.parent">
                <text class="parent-label">上级：</text>
                <text class="parent-name">{{ agent.parent }}</text>
              </view>
            </view>
            <text class="arrow">›</text>
          </view>

          <view class="agent-actions" v-if="currentTab === 'direct'">
            <view class="action-btn set-product-btn" @click="setAgentProducts(agent)">
              <text class="action-icon">📦</text>
              <text class="action-text">设置产品</text>
            </view>
            <view class="action-btn set-rate-btn" @click="setAgentRate(agent)">
              <text class="action-icon">📊</text>
              <text class="action-text">设置分润</text>
            </view>
          </view>
        </view>
      </view>

      <view class="empty-state" v-if="currentAgentList.length === 0">
        <text class="empty-icon">📋</text>
        <text class="empty-text">暂无代理数据</text>
      </view>
    </view>

    <view class="popup-mask" v-if="showProductPopup" @click="closePopup">
      <view class="popup-content" @click.stop>
        <view class="popup-header">
          <text class="popup-title">设置代理产品</text>
          <view class="popup-close" @click="closePopup">
            <text class="close-text">✕</text>
          </view>
        </view>
        <view class="popup-body">
          <view class="product-option" v-for="product in productList" :key="product.id">
            <view class="option-left">
              <checkbox :checked="selectedProducts.includes(product.id)" @click="toggleProduct(product.id)" />
              <view class="option-info">
                <text class="option-name">{{ product.name }}</text>
                <text class="option-desc">分润：{{ (product.commission * 100).toFixed(1) }}%</text>
              </view>
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
import { mockData } from '@/utils/mockData.js'

export default {
  data() {
    return {
      agentList: mockData.agentList,
      productList: mockData.productList,
      currentTab: 'direct',
      showProductPopup: false,
      showRatePopup: false,
      currentAgent: null,
      selectedProducts: [],
      productRates: {}
    }
  },
  computed: {
    totalAgents() {
      return this.agentList.direct.length + this.agentList.indirect.length
    },
    currentAgentList() {
      return this.currentTab === 'direct' ? this.agentList.direct : this.agentList.indirect
    },
    selectedProductsForRate() {
      return this.productList.filter(p => this.selectedProducts.includes(p.id))
    }
  },
  methods: {
    switchTab(tab) {
      this.currentTab = tab
    },
    goToDetail(agent) {
      uni.navigateTo({
        url: `/pages/agent/detail?id=${agent.id}&type=${this.currentTab}`
      })
    },
    setAgentProducts(agent) {
      this.currentAgent = agent
      this.selectedProducts = [1, 2]
      this.showProductPopup = true
    },
    setAgentRate(agent) {
      this.currentAgent = agent
      this.selectedProducts = [1, 2]
      this.productRates = {
        1: '1.0',
        2: '1.2'
      }
      this.showRatePopup = true
    },
    closePopup() {
      this.showProductPopup = false
      this.showRatePopup = false
      this.currentAgent = null
      this.selectedProducts = []
      this.productRates = {}
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

.agent-manage-page {
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
  font-size: 24rpx;
  color: $text-light;
  margin-bottom: 12rpx;
}

.stats-value {
  font-size: 36rpx;
  font-weight: bold;
  color: $text-color;
}

.stats-value.highlight {
  color: $primary-color;
}

.stats-divider {
  width: 1rpx;
  height: 60rpx;
  background-color: #EEEEEE;
}

.tab-section {
  background-color: #FFFFFF;
  margin: 0 20rpx;
  border-radius: 16rpx;
  overflow: hidden;
}

.tab-list {
  display: flex;
}

.tab-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 28rpx 0;
  position: relative;
}

.tab-text {
  font-size: 30rpx;
  color: $text-secondary;
}

.tab-item.active .tab-text {
  color: $primary-color;
  font-weight: bold;
}

.tab-indicator {
  position: absolute;
  bottom: 0;
  width: 60rpx;
  height: 6rpx;
  background-color: $primary-color;
  border-radius: 3rpx;
}

.list-section {
  padding: 20rpx;
}

.agent-list {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  overflow: hidden;
}

.agent-item {
  border-bottom: 1rpx solid #F5F5F5;
}

.agent-item:last-child {
  border-bottom: none;
}

.agent-header {
  display: flex;
  align-items: center;
  padding: 24rpx;
}

.agent-header:active {
  background-color: #FAFAFA;
}

.agent-avatar {
  width: 88rpx;
  height: 88rpx;
  background: linear-gradient(135deg, $primary-color 0%, $primary-dark 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.avatar-text {
  font-size: 36rpx;
  color: #FFFFFF;
  font-weight: bold;
}

.agent-info {
  flex: 1;
}

.name-row {
  display: flex;
  align-items: center;
  margin-bottom: 8rpx;
}

.agent-name {
  font-size: 30rpx;
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

.agent-phone {
  display: block;
  font-size: 24rpx;
  color: $text-light;
  margin-bottom: 8rpx;
}

.agent-stats-row {
  display: flex;
  gap: 24rpx;
}

.stat-item {
  font-size: 24rpx;
  color: $text-secondary;
}

.parent-info {
  margin-top: 8rpx;
  display: flex;
  align-items: center;
}

.parent-label {
  font-size: 22rpx;
  color: $text-light;
}

.parent-name {
  font-size: 22rpx;
  color: $text-secondary;
}

.arrow {
  font-size: 32rpx;
  color: #CCCCCC;
}

.agent-actions {
  display: flex;
  padding: 0 24rpx 24rpx;
  gap: 20rpx;
}

.action-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20rpx;
  border-radius: 12rpx;
  border: 2rpx solid;
}

.action-btn:active {
  opacity: 0.8;
}

.set-product-btn {
  background-color: rgba($primary-color, 0.05);
  border-color: $primary-color;
}

.set-rate-btn {
  background-color: rgba(76, 175, 80, 0.05);
  border-color: #4CAF50;
}

.action-icon {
  font-size: 28rpx;
  margin-right: 8rpx;
}

.action-text {
  font-size: 26rpx;
}

.set-product-btn .action-text {
  color: $primary-color;
}

.set-rate-btn .action-text {
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
  max-height: 70vh;
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
  padding: 24rpx;
  border-bottom: 1rpx solid #F5F5F5;
}

.product-option:last-child {
  border-bottom: none;
}

.option-left {
  display: flex;
  align-items: center;
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
