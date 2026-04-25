<template>
  <view class="account-page">
    <view class="header-section">
      <view class="balance-info">
        <text class="balance-label">账户总金额（元）</text>
        <text class="balance-value">¥{{ formatAmount(accountInfo.totalAmount) }}</text>
      </view>
    </view>

    <view class="stats-section">
      <view class="stats-card">
        <view class="stats-row">
          <view class="stats-item">
            <text class="stats-label">可提现金额</text>
            <text class="stats-value available">¥{{ formatAmount(accountInfo.availableAmount) }}</text>
          </view>
          <view class="stats-divider"></view>
          <view class="stats-item">
            <text class="stats-label">未提现金额</text>
            <text class="stats-value">¥{{ formatAmount(accountInfo.pendingAmount) }}</text>
          </view>
          <view class="stats-divider"></view>
          <view class="stats-item">
            <text class="stats-label">已提现金额</text>
            <text class="stats-value">¥{{ formatAmount(accountInfo.withdrawnAmount) }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="action-section">
      <view 
        class="withdraw-btn" 
        :class="{ disabled: !canWithdraw }"
        @click="handleWithdraw"
      >
        <text class="btn-text">提现</text>
      </view>
      <view class="tip-text" v-if="!canWithdraw">
        <text class="tip-icon">💡</text>
        <text class="tip-content">{{ withdrawTip }}</text>
      </view>
    </view>

    <view class="detail-section">
      <view class="detail-tabs">
        <view 
          class="tab-item" 
          :class="{ active: currentTab === 'account' }"
          @click="currentTab = 'account'"
        >
          <text class="tab-text">账户明细</text>
          <view class="tab-indicator" v-if="currentTab === 'account'"></view>
        </view>
        <view 
          class="tab-item" 
          :class="{ active: currentTab === 'withdraw' }"
          @click="currentTab = 'withdraw'"
        >
          <text class="tab-text">提现明细</text>
          <view class="tab-indicator" v-if="currentTab === 'withdraw'"></view>
        </view>
      </view>

      <view class="detail-list" v-if="currentTab === 'account'">
        <view class="detail-item" v-for="item in accountDetailList" :key="item.id">
          <view class="detail-left">
            <view class="type-icon" :class="item.amount > 0 ? 'income' : 'expense'">
              <text class="icon-text">{{ item.amount > 0 ? '+' : '-' }}</text>
            </view>
            <view class="detail-info">
              <text class="detail-type">{{ item.type }}</text>
              <text class="detail-remark">{{ item.remark }}</text>
              <text class="detail-time">{{ item.time }}</text>
            </view>
          </view>
          <view class="detail-right">
            <text class="detail-amount" :class="item.amount > 0 ? 'income' : 'expense'">
              {{ item.amount > 0 ? '+' : '' }}¥{{ formatAmount(Math.abs(item.amount)) }}
            </text>
            <text class="detail-balance">余额：¥{{ formatAmount(item.balance) }}</text>
          </view>
        </view>

        <view class="empty-state" v-if="accountDetailList.length === 0">
          <text class="empty-icon">📋</text>
          <text class="empty-text">暂无账户明细</text>
        </view>
      </view>

      <view class="detail-list" v-if="currentTab === 'withdraw'">
        <view class="withdraw-item" v-for="item in withdrawList" :key="item.id">
          <view class="withdraw-header">
            <text class="withdraw-amount">¥{{ formatAmount(item.amount) }}</text>
            <view class="withdraw-status" :class="getStatusClass(item.status)">
              <text class="status-text">{{ item.status }}</text>
            </view>
          </view>
          <view class="withdraw-info">
            <view class="info-row">
              <text class="info-label">提现方式</text>
              <text class="info-value">{{ item.bank }}</text>
            </view>
            <view class="info-row">
              <text class="info-label">收款账户</text>
              <text class="info-value">{{ item.account }}</text>
            </view>
            <view class="info-row">
              <text class="info-label">申请时间</text>
              <text class="info-value">{{ item.time }}</text>
            </view>
          </view>
        </view>

        <view class="empty-state" v-if="withdrawList.length === 0">
          <text class="empty-icon">💳</text>
          <text class="empty-text">暂无提现记录</text>
        </view>
      </view>
    </view>

    <view class="popup-mask" v-if="showWithdrawPopup" @click="closeWithdrawPopup">
      <view class="popup-content" @click.stop>
        <view class="popup-header">
          <text class="popup-title">申请提现</text>
          <view class="popup-close" @click="closeWithdrawPopup">
            <text class="close-text">✕</text>
          </view>
        </view>
        <view class="popup-body">
          <view class="withdraw-info-row">
            <text class="withdraw-label">可提现金额</text>
            <text class="withdraw-amount-highlight">¥{{ formatAmount(accountInfo.availableAmount) }}</text>
          </view>
          <view class="input-section">
            <text class="input-label">提现金额</text>
            <view class="input-row">
              <text class="input-prefix">¥</text>
              <input 
                class="withdraw-input" 
                type="digit"
                v-model="withdrawAmount"
                placeholder="请输入提现金额"
              />
            </view>
            <view class="input-tips">
              <text class="tip-item" @click="withdrawAll">全部提现</text>
              <text class="tip-desc">最低提现10元</text>
            </view>
          </view>
          <view class="bank-section">
            <text class="bank-label">提现到</text>
            <view class="bank-info" @click="selectBank">
              <view class="bank-left">
                <view class="bank-icon">
                  <text class="icon-text">🏦</text>
                </view>
                <view class="bank-detail">
                  <text class="bank-name">招商银行</text>
                  <text class="bank-account">尾号 8888</text>
                </view>
              </view>
              <text class="arrow">›</text>
            </view>
          </view>
        </view>
        <view class="popup-footer">
          <view class="confirm-btn" :class="{ disabled: !isValidWithdrawAmount }" @click="submitWithdraw">
            <text class="btn-text">确认提现</text>
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
      accountInfo: mockData.accountInfo,
      accountDetailList: mockData.accountDetailList,
      withdrawList: mockData.withdrawList,
      currentTab: 'account',
      showWithdrawPopup: false,
      withdrawAmount: ''
    }
  },
  computed: {
    canWithdraw() {
      return this.accountInfo.availableAmount >= 10
    },
    withdrawTip() {
      if (this.accountInfo.availableAmount <= 0) {
        return '可提现金额为0'
      }
      return '可提现金额需大于10元才能提现'
    },
    isValidWithdrawAmount() {
      const amount = parseFloat(this.withdrawAmount) || 0
      return amount >= 10 && amount <= this.accountInfo.availableAmount
    }
  },
  methods: {
    formatAmount,
    handleWithdraw() {
      if (!this.canWithdraw) {
        uni.showToast({
          title: this.withdrawTip,
          icon: 'none'
        })
        return
      }
      this.showWithdrawPopup = true
    },
    closeWithdrawPopup() {
      this.showWithdrawPopup = false
      this.withdrawAmount = ''
    },
    withdrawAll() {
      this.withdrawAmount = this.accountInfo.availableAmount.toString()
    },
    selectBank() {
      uni.showToast({
        title: '选择银行卡',
        icon: 'none'
      })
    },
    submitWithdraw() {
      if (!this.isValidWithdrawAmount) {
        uni.showToast({
          title: '请输入正确的提现金额',
          icon: 'none'
        })
        return
      }
      
      uni.showModal({
        title: '确认提现',
        content: `确认提现 ¥${this.withdrawAmount} 元到招商银行尾号8888？`,
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({
              title: '提交中...'
            })
            setTimeout(() => {
              uni.hideLoading()
              uni.showToast({
                title: '提现申请已提交',
                icon: 'success'
              })
              this.closeWithdrawPopup()
            }, 1000)
          }
        }
      })
    },
    getStatusClass(status) {
      if (status === '已完成') return 'success'
      if (status === '处理中') return 'pending'
      if (status === '失败') return 'failed'
      return ''
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/uni.scss';

.account-page {
  min-height: 100vh;
  background-color: $bg-color;
  padding-bottom: 30rpx;
}

.header-section {
  background: linear-gradient(135deg, $primary-color 0%, $primary-dark);
  padding: 60rpx 30rpx 80rpx;
}

.balance-info {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.balance-label {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 16rpx;
}

.balance-value {
  font-size: 56rpx;
  font-weight: bold;
  color: #FFFFFF;
}

.stats-section {
  margin-top: -50rpx;
  padding: 0 20rpx;
}

.stats-card {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  padding: 30rpx 20rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.06);
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

.stats-value.available {
  color: $primary-color;
}

.stats-divider {
  width: 1rpx;
  height: 50rpx;
  background-color: #EEEEEE;
}

.action-section {
  padding: 30rpx 20rpx;
}

.withdraw-btn {
  background: linear-gradient(135deg, $primary-color 0%, $primary-dark 100%);
  border-radius: 40rpx;
  padding: 28rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 16rpx rgba($primary-color, 0.3);
}

.withdraw-btn:active {
  opacity: 0.9;
}

.withdraw-btn.disabled {
  background-color: #CCCCCC;
  background: none;
  box-shadow: none;
}

.btn-text {
  font-size: 32rpx;
  font-weight: bold;
  color: #FFFFFF;
}

.withdraw-btn.disabled .btn-text {
  color: #999999;
}

.tip-text {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 20rpx;
}

.tip-icon {
  font-size: 24rpx;
  margin-right: 8rpx;
}

.tip-content {
  font-size: 24rpx;
  color: $text-light;
}

.detail-section {
  padding: 0 20rpx;
}

.detail-tabs {
  background-color: #FFFFFF;
  border-radius: 16rpx 16rpx 0 0;
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
  font-size: 28rpx;
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

.detail-list {
  background-color: #FFFFFF;
  border-radius: 0 0 16rpx 16rpx;
  overflow: hidden;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  border-bottom: 1rpx solid #F5F5F5;
}

.detail-item:last-child {
  border-bottom: none;
}

.detail-left {
  display: flex;
  align-items: center;
  flex: 1;
}

.type-icon {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.type-icon.income {
  background-color: rgba(76, 175, 80, 0.1);
}

.type-icon.expense {
  background-color: rgba(244, 67, 54, 0.1);
}

.icon-text {
  font-size: 32rpx;
  font-weight: bold;
}

.type-icon.income .icon-text {
  color: #4CAF50;
}

.type-icon.expense .icon-text {
  color: #F44336;
}

.detail-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.detail-type {
  font-size: 28rpx;
  color: $text-color;
  margin-bottom: 4rpx;
}

.detail-remark {
  font-size: 24rpx;
  color: $text-secondary;
  margin-bottom: 4rpx;
}

.detail-time {
  font-size: 22rpx;
  color: $text-light;
}

.detail-right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.detail-amount {
  font-size: 30rpx;
  font-weight: bold;
  margin-bottom: 8rpx;
}

.detail-amount.income {
  color: #4CAF50;
}

.detail-amount.expense {
  color: #F44336;
}

.detail-balance {
  font-size: 22rpx;
  color: $text-light;
}

.withdraw-item {
  padding: 24rpx;
  border-bottom: 1rpx solid #F5F5F5;
}

.withdraw-item:last-child {
  border-bottom: none;
}

.withdraw-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;
}

.withdraw-amount {
  font-size: 36rpx;
  font-weight: bold;
  color: $text-color;
}

.withdraw-status {
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
}

.withdraw-status.success {
  background-color: rgba(76, 175, 80, 0.1);
}

.withdraw-status.pending {
  background-color: rgba(255, 152, 0, 0.1);
}

.withdraw-status.failed {
  background-color: rgba(244, 67, 54, 0.1);
}

.status-text {
  font-size: 22rpx;
}

.withdraw-status.success .status-text {
  color: #4CAF50;
}

.withdraw-status.pending .status-text {
  color: #FF9800;
}

.withdraw-status.failed .status-text {
  color: #F44336;
}

.withdraw-info {
  background-color: #FAFAFA;
  border-radius: 12rpx;
  padding: 20rpx;
}

.info-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12rpx;
}

.info-row:last-child {
  margin-bottom: 0;
}

.info-label {
  font-size: 24rpx;
  color: $text-light;
}

.info-value {
  font-size: 24rpx;
  color: $text-secondary;
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
  padding: 30rpx;
}

.withdraw-info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx;
  background-color: rgba($primary-color, 0.05);
  border-radius: 12rpx;
  margin-bottom: 30rpx;
}

.withdraw-label {
  font-size: 26rpx;
  color: $text-secondary;
}

.withdraw-amount-highlight {
  font-size: 36rpx;
  font-weight: bold;
  color: $primary-color;
}

.input-section {
  margin-bottom: 30rpx;
}

.input-label {
  display: block;
  font-size: 28rpx;
  color: $text-color;
  margin-bottom: 16rpx;
}

.input-row {
  display: flex;
  align-items: center;
  border: 2rpx solid #DDDDDD;
  border-radius: 12rpx;
  padding: 24rpx 30rpx;
}

.input-prefix {
  font-size: 36rpx;
  color: $text-color;
  margin-right: 12rpx;
}

.withdraw-input {
  flex: 1;
  font-size: 36rpx;
  color: $text-color;
}

.input-tips {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16rpx;
}

.tip-item {
  font-size: 24rpx;
  color: $primary-color;
  padding: 8rpx 16rpx;
  background-color: rgba($primary-color, 0.1);
  border-radius: 8rpx;
}

.tip-desc {
  font-size: 24rpx;
  color: $text-light;
}

.bank-section {
}

.bank-label {
  display: block;
  font-size: 28rpx;
  color: $text-color;
  margin-bottom: 16rpx;
}

.bank-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  background-color: #FAFAFA;
  border-radius: 12rpx;
}

.bank-left {
  display: flex;
  align-items: center;
}

.bank-icon {
  width: 64rpx;
  height: 64rpx;
  background-color: $primary-color;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.bank-detail {
  display: flex;
  flex-direction: column;
}

.bank-name {
  font-size: 28rpx;
  color: $text-color;
  margin-bottom: 4rpx;
}

.bank-account {
  font-size: 24rpx;
  color: $text-light;
}

.arrow {
  font-size: 32rpx;
  color: #CCCCCC;
}

.popup-footer {
  padding: 30rpx;
  border-top: 1rpx solid #F5F5F5;
}

.confirm-btn {
  background: linear-gradient(135deg, $primary-color 0%, $primary-dark 100%);
  border-radius: 40rpx;
  padding: 28rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.confirm-btn.disabled {
  background-color: #CCCCCC;
  background: none;
}

.confirm-btn.disabled .btn-text {
  color: #999999;
}
</style>
