<template>
  <view class="profile-page">
    <view class="header-section">
      <view class="user-info">
        <image class="avatar" :src="userInfo.avatar" mode="aspectFill" />
        <view class="user-details">
          <text class="user-name">{{ userInfo.name }}</text>
          <view class="user-meta">
            <text class="user-level">{{ userInfo.level }}</text>
            <text class="user-phone">{{ userInfo.phone }}</text>
          </view>
        </view>
        <view class="edit-icon">
          <text class="edit-text">›</text>
        </view>
      </view>
    </view>

    <view class="account-summary">
      <view class="summary-item" @click="goToAccount">
        <view class="summary-icon account-icon">
          <text class="icon-text">💰</text>
        </view>
        <view class="summary-info">
          <text class="summary-label">账户余额</text>
          <text class="summary-value">¥{{ formatAmount(accountInfo.totalAmount) }}</text>
        </view>
        <text class="summary-arrow">›</text>
      </view>
    </view>

    <view class="menu-section">
      <view class="menu-group">
        <view class="menu-item" @click="goToAccount">
          <view class="menu-left">
            <view class="menu-icon-wrapper account-icon-bg">
              <text class="menu-icon-text">💳</text>
            </view>
            <text class="menu-text">账户菜单</text>
          </view>
          <text class="menu-arrow">›</text>
        </view>

        <view class="menu-item" @click="goToProfit">
          <view class="menu-left">
            <view class="menu-icon-wrapper profit-icon-bg">
              <text class="menu-icon-text">📊</text>
            </view>
            <text class="menu-text">分润菜单</text>
          </view>
          <text class="menu-arrow">›</text>
        </view>
      </view>

      <view class="menu-group">
        <view class="menu-item" @click="goToSettings">
          <view class="menu-left">
            <view class="menu-icon-wrapper settings-icon-bg">
              <text class="menu-icon-text">⚙️</text>
            </view>
            <text class="menu-text">设置</text>
          </view>
          <text class="menu-arrow">›</text>
        </view>
      </view>

      <view class="menu-group">
        <view class="menu-item" @click="goToHelp">
          <view class="menu-left">
            <view class="menu-icon-wrapper help-icon-bg">
              <text class="menu-icon-text">❓</text>
            </view>
            <text class="menu-text">帮助中心</text>
          </view>
          <text class="menu-arrow">›</text>
        </view>

        <view class="menu-item" @click="goToContact">
          <view class="menu-left">
            <view class="menu-icon-wrapper contact-icon-bg">
              <text class="menu-icon-text">📞</text>
            </view>
            <text class="menu-text">联系客服</text>
          </view>
          <text class="menu-arrow">›</text>
        </view>

        <view class="menu-item" @click="goToAbout">
          <view class="menu-left">
            <view class="menu-icon-wrapper about-icon-bg">
              <text class="menu-icon-text">ℹ️</text>
            </view>
            <text class="menu-text">关于我们</text>
          </view>
          <text class="menu-arrow">›</text>
        </view>
      </view>
    </view>

    <view class="footer-section">
      <text class="version-text">版本 v1.0.0</text>
    </view>
  </view>
</template>

<script>
import { mockData, formatAmount } from '@/utils/mockData.js'

export default {
  data() {
    return {
      userInfo: mockData.userInfo,
      accountInfo: mockData.accountInfo
    }
  },
  methods: {
    formatAmount,
    goToAccount() {
      uni.navigateTo({
        url: '/pages/account/index'
      })
    },
    goToProfit() {
      uni.navigateTo({
        url: '/pages/profit/index'
      })
    },
    goToSettings() {
      uni.navigateTo({
        url: '/pages/settings/index'
      })
    },
    goToHelp() {
      uni.showToast({
        title: '帮助中心',
        icon: 'none'
      })
    },
    goToContact() {
      uni.showToast({
        title: '联系客服',
        icon: 'none'
      })
    },
    goToAbout() {
      uni.showToast({
        title: '关于我们',
        icon: 'none'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/uni.scss';

.profile-page {
  min-height: 100vh;
  background-color: $bg-color;
  padding-bottom: 40rpx;
}

.header-section {
  background: linear-gradient(135deg, $primary-color 0%, $primary-dark 100%);
  padding: 60rpx 30rpx 100rpx;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  border: 4rpx solid rgba(255, 255, 255, 0.3);
  margin-right: 24rpx;
}

.user-details {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 36rpx;
  font-weight: bold;
  color: #FFFFFF;
  margin-bottom: 12rpx;
}

.user-meta {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.user-level {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.9);
  background-color: rgba(255, 255, 255, 0.2);
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
}

.user-phone {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.7);
}

.edit-icon {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.edit-text {
  font-size: 40rpx;
  color: rgba(255, 255, 255, 0.7);
}

.account-summary {
  margin-top: -60rpx;
  padding: 0 20rpx;
}

.summary-item {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  padding: 30rpx;
  display: flex;
  align-items: center;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.06);
}

.summary-item:active {
  opacity: 0.9;
}

.summary-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}

.account-icon {
  background: linear-gradient(135deg, #FFE0E0 0%, $primary-light 100%);
}

.icon-text {
  font-size: 40rpx;
}

.summary-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.summary-label {
  font-size: 26rpx;
  color: $text-light;
  margin-bottom: 8rpx;
}

.summary-value {
  font-size: 36rpx;
  font-weight: bold;
  color: $primary-color;
}

.summary-arrow {
  font-size: 32rpx;
  color: #CCCCCC;
}

.menu-section {
  padding: 30rpx 20rpx;
}

.menu-group {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
  overflow: hidden;
}

.menu-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 28rpx 30rpx;
  border-bottom: 1rpx solid #F5F5F5;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-item:active {
  background-color: #FAFAFA;
}

.menu-left {
  display: flex;
  align-items: center;
}

.menu-icon-wrapper {
  width: 64rpx;
  height: 64rpx;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.account-icon-bg {
  background: linear-gradient(135deg, #FFE0E0 0%, $primary-light 100%);
}

.profit-icon-bg {
  background: linear-gradient(135deg, #E8F5E9 0%, #C8E6C9 100%);
}

.settings-icon-bg {
  background: linear-gradient(135deg, #F3E5F5 0%, #E1BEE7 100%);
}

.help-icon-bg {
  background: linear-gradient(135deg, #E3F2FD 0%, #BBDEFB 100%);
}

.contact-icon-bg {
  background: linear-gradient(135deg, #FFF3E0 0%, #FFE0B2 100%);
}

.about-icon-bg {
  background: linear-gradient(135deg, #E0F7FA 0%, #B2EBF2 100%);
}

.menu-icon-text {
  font-size: 32rpx;
}

.menu-text {
  font-size: 30rpx;
  color: $text-color;
}

.menu-arrow {
  font-size: 32rpx;
  color: #CCCCCC;
}

.footer-section {
  display: flex;
  justify-content: center;
  padding: 40rpx 0;
}

.version-text {
  font-size: 24rpx;
  color: $text-light;
}
</style>
