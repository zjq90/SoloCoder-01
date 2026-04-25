<template>
  <view class="settings-page">
    <view class="section">
      <view class="section-title">账户设置</view>
      <view class="section-content">
        <view class="setting-item" @click="goToPersonalInfo">
          <view class="item-left">
            <view class="icon-wrapper info-icon">
              <text class="icon-text">👤</text>
            </view>
            <text class="item-label">个人信息</text>
          </view>
          <view class="item-right">
            <text class="item-value">{{ userInfo.name }}</text>
            <text class="arrow">›</text>
          </view>
        </view>
        <view class="setting-item" @click="goToBindPhone">
          <view class="item-left">
            <view class="icon-wrapper phone-icon">
              <text class="icon-text">📱</text>
            </view>
            <text class="item-label">绑定手机</text>
          </view>
          <view class="item-right">
            <text class="item-value">{{ userInfo.phone }}</text>
            <text class="arrow">›</text>
          </view>
        </view>
        <view class="setting-item" @click="goToChangePassword">
          <view class="item-left">
            <view class="icon-wrapper password-icon">
              <text class="icon-text">🔒</text>
            </view>
            <text class="item-label">修改密码</text>
          </view>
          <view class="item-right">
            <text class="arrow">›</text>
          </view>
        </view>
      </view>
    </view>

    <view class="section">
      <view class="section-title">支付设置</view>
      <view class="section-content">
        <view class="setting-item" @click="goToBankCards">
          <view class="item-left">
            <view class="icon-wrapper bank-icon">
              <text class="icon-text">🏦</text>
            </view>
            <text class="item-label">银行卡管理</text>
          </view>
          <view class="item-right">
            <text class="item-value">1张</text>
            <text class="arrow">›</text>
          </view>
        </view>
        <view class="setting-item" @click="goToWithdrawPassword">
          <view class="item-left">
            <view class="icon-wrapper withdraw-icon">
              <text class="icon-text">💳</text>
            </view>
            <text class="item-label">提现密码</text>
          </view>
          <view class="item-right">
            <text class="item-value">已设置</text>
            <text class="arrow">›</text>
          </view>
        </view>
      </view>
    </view>

    <view class="section">
      <view class="section-title">通知设置</view>
      <view class="section-content">
        <view class="setting-item">
          <view class="item-left">
            <view class="icon-wrapper notify-icon">
              <text class="icon-text">🔔</text>
            </view>
            <text class="item-label">交易通知</text>
          </view>
          <view class="item-right">
            <switch :checked="isTransactionNotify" @change="toggleTransactionNotify" color="#E53935" />
          </view>
        </view>
        <view class="setting-item">
          <view class="item-left">
            <view class="icon-wrapper profit-icon">
              <text class="icon-text">💰</text>
            </view>
            <text class="item-label">分润通知</text>
          </view>
          <view class="item-right">
            <switch :checked="isProfitNotify" @change="toggleProfitNotify" color="#E53935" />
          </view>
        </view>
      </view>
    </view>

    <view class="section">
      <view class="section-title">关于</view>
      <view class="section-content">
        <view class="setting-item" @click="goToAbout">
          <view class="item-left">
            <view class="icon-wrapper about-icon">
              <text class="icon-text">ℹ️</text>
            </view>
            <text class="item-label">关于我们</text>
          </view>
          <view class="item-right">
            <text class="arrow">›</text>
          </view>
        </view>
        <view class="setting-item" @click="goToFeedback">
          <view class="item-left">
            <view class="icon-wrapper feedback-icon">
              <text class="icon-text">📝</text>
            </view>
            <text class="item-label">意见反馈</text>
          </view>
          <view class="item-right">
            <text class="arrow">›</text>
          </view>
        </view>
        <view class="setting-item">
          <view class="item-left">
            <view class="icon-wrapper version-icon">
              <text class="icon-text">📋</text>
            </view>
            <text class="item-label">当前版本</text>
          </view>
          <view class="item-right">
            <text class="item-value">v1.0.0</text>
          </view>
        </view>
      </view>
    </view>

    <view class="logout-section">
      <view class="logout-btn" @click="handleLogout">
        <text class="logout-text">退出登录</text>
      </view>
    </view>
  </view>
</template>

<script>
import { mockData } from '@/utils/mockData.js'

export default {
  data() {
    return {
      userInfo: mockData.userInfo,
      isTransactionNotify: true,
      isProfitNotify: true
    }
  },
  methods: {
    goToPersonalInfo() {
      uni.showToast({
        title: '个人信息',
        icon: 'none'
      })
    },
    goToBindPhone() {
      uni.showToast({
        title: '绑定手机',
        icon: 'none'
      })
    },
    goToChangePassword() {
      uni.showToast({
        title: '修改密码',
        icon: 'none'
      })
    },
    goToBankCards() {
      uni.showToast({
        title: '银行卡管理',
        icon: 'none'
      })
    },
    goToWithdrawPassword() {
      uni.showToast({
        title: '提现密码',
        icon: 'none'
      })
    },
    toggleTransactionNotify(e) {
      this.isTransactionNotify = e.detail.value
    },
    toggleProfitNotify(e) {
      this.isProfitNotify = e.detail.value
    },
    goToAbout() {
      uni.showToast({
        title: '关于我们',
        icon: 'none'
      })
    },
    goToFeedback() {
      uni.showToast({
        title: '意见反馈',
        icon: 'none'
      })
    },
    handleLogout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: '已退出登录',
              icon: 'success'
            })
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/uni.scss';

.settings-page {
  min-height: 100vh;
  background-color: $bg-color;
  padding-bottom: 40rpx;
}

.section {
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 26rpx;
  color: $text-light;
  padding: 20rpx 30rpx;
}

.section-content {
  background-color: #FFFFFF;
}

.setting-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 28rpx 30rpx;
  border-bottom: 1rpx solid #F5F5F5;
}

.setting-item:last-child {
  border-bottom: none;
}

.setting-item:active {
  background-color: #FAFAFA;
}

.item-left {
  display: flex;
  align-items: center;
}

.icon-wrapper {
  width: 64rpx;
  height: 64rpx;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.info-icon {
  background: linear-gradient(135deg, #FFE0E0 0%, $primary-light 100%);
}

.phone-icon {
  background: linear-gradient(135deg, #E3F2FD 0%, #BBDEFB 100%);
}

.password-icon {
  background: linear-gradient(135deg, #FFF3E0 0%, #FFE0B2 100%);
}

.bank-icon {
  background: linear-gradient(135deg, #E8F5E9 0%, #C8E6C9 100%);
}

.withdraw-icon {
  background: linear-gradient(135deg, #F3E5F5 0%, #E1BEE7 100%);
}

.notify-icon {
  background: linear-gradient(135deg, #FFFDE7 0%, #FFF9C4 100%);
}

.profit-icon {
  background: linear-gradient(135deg, #E0F7FA 0%, #B2EBF2 100%);
}

.about-icon {
  background: linear-gradient(135deg, #FCE4EC 0%, #F8BBD9 100%);
}

.feedback-icon {
  background: linear-gradient(135deg, #EDE7F6 0%, #D1C4E9 100%);
}

.version-icon {
  background: linear-gradient(135deg, #E0F2F1 0%, #B2DFDB 100%);
}

.icon-text {
  font-size: 32rpx;
}

.item-label {
  font-size: 30rpx;
  color: $text-color;
}

.item-right {
  display: flex;
  align-items: center;
}

.item-value {
  font-size: 28rpx;
  color: $text-light;
  margin-right: 8rpx;
}

.arrow {
  font-size: 32rpx;
  color: #CCCCCC;
}

.logout-section {
  padding: 40rpx 30rpx;
}

.logout-btn {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  padding: 28rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2rpx solid $primary-color;
}

.logout-btn:active {
  background-color: rgba($primary-color, 0.05);
}

.logout-text {
  font-size: 32rpx;
  color: $primary-color;
  font-weight: bold;
}
</style>
