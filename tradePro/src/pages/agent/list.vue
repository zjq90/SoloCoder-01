<template>
  <view class="agent-list-page">
    <view class="filter-section">
      <view class="filter-tabs">
        <view 
          class="filter-tab" 
          :class="{ active: currentTab === 'direct' }"
          @click="currentTab = 'direct'"
        >
          <text class="tab-text">直属代理</text>
          <view class="tab-badge" v-if="currentTab === 'direct'">{{ agentList.direct.length }}</view>
        </view>
        <view 
          class="filter-tab" 
          :class="{ active: currentTab === 'indirect' }"
          @click="currentTab = 'indirect'"
        >
          <text class="tab-text">间接代理</text>
          <view class="tab-badge" v-if="currentTab === 'indirect'">{{ agentList.indirect.length }}</view>
        </view>
      </view>
    </view>

    <view class="list-section">
      <view class="agent-list">
        <view class="agent-item" v-for="agent in currentAgentList" :key="agent.id" @click="goToDetail(agent)">
          <view class="item-left">
            <view class="agent-avatar">
              <text class="avatar-text">{{ agent.name.charAt(0) }}</text>
            </view>
            <view class="item-info">
              <view class="name-row">
                <text class="agent-name">{{ agent.name }}</text>
                <view class="agent-level">
                  <text class="level-text">{{ agent.level }}</text>
                </view>
              </view>
              <text class="agent-phone">{{ agent.phone }}</text>
              <view class="stats-row">
                <text class="stat-item">商户：{{ agent.merchants }}</text>
                <text class="stat-item">机器：{{ agent.machines }}</text>
              </view>
              <view class="parent-row" v-if="agent.parent">
                <text class="parent-label">上级：</text>
                <text class="parent-name">{{ agent.parent }}</text>
              </view>
            </view>
          </view>
          <view class="item-right">
            <view class="agent-status" :class="agent.status === '正常' ? 'normal' : 'abnormal'">
              <text class="status-text">{{ agent.status }}</text>
            </view>
            <text class="arrow">›</text>
          </view>
        </view>
      </view>

      <view class="empty-state" v-if="currentAgentList.length === 0">
        <text class="empty-icon">👥</text>
        <text class="empty-text">暂无代理数据</text>
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
      currentTab: 'direct'
    }
  },
  computed: {
    currentAgentList() {
      return this.currentTab === 'direct' ? this.agentList.direct : this.agentList.indirect
    }
  },
  methods: {
    goToDetail(agent) {
      uni.navigateTo({
        url: `/pages/agent/detail?id=${agent.id}&type=${this.currentTab}`
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/uni.scss';

.agent-list-page {
  min-height: 100vh;
  background-color: $bg-color;
  padding-bottom: 30rpx;
}

.filter-section {
  background-color: #FFFFFF;
  padding: 20rpx;
  margin-bottom: 20rpx;
}

.filter-tabs {
  display: flex;
  gap: 20rpx;
}

.filter-tab {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16rpx 0;
  border-radius: 8rpx;
  background-color: #F5F5F5;
  position: relative;
}

.filter-tab.active {
  background-color: $primary-color;
}

.tab-text {
  font-size: 26rpx;
  color: $text-secondary;
}

.filter-tab.active .tab-text {
  color: #FFFFFF;
  font-weight: bold;
}

.tab-badge {
  position: absolute;
  top: 8rpx;
  right: 30rpx;
  background-color: #FFFFFF;
  color: $primary-color;
  font-size: 18rpx;
  padding: 2rpx 8rpx;
  border-radius: 10rpx;
  font-weight: bold;
}

.list-section {
  padding: 0 20rpx;
}

.agent-list {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  overflow: hidden;
}

.agent-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  border-bottom: 1rpx solid #F5F5F5;
}

.agent-item:last-child {
  border-bottom: none;
}

.agent-item:active {
  background-color: #FAFAFA;
}

.item-left {
  display: flex;
  align-items: center;
  flex: 1;
}

.agent-avatar {
  width: 72rpx;
  height: 72rpx;
  background: linear-gradient(135deg, $primary-color 0%, $primary-dark);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.avatar-text {
  font-size: 32rpx;
  font-weight: bold;
  color: #FFFFFF;
}

.item-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.name-row {
  display: flex;
  align-items: center;
  margin-bottom: 12rpx;
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
  font-size: 24rpx;
  color: $text-light;
  margin-bottom: 8rpx;
}

.stats-row {
  display: flex;
  gap: 24rpx;
  margin-bottom: 8rpx;
}

.stat-item {
  font-size: 24rpx;
  color: $text-secondary;
}

.parent-row {
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

.item-right {
  display: flex;
  align-items: center;
}

.agent-status {
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  margin-right: 12rpx;
}

.agent-status.normal {
  background-color: rgba(76, 175, 80, 0.1);
}

.agent-status.abnormal {
  background-color: rgba(244, 67, 54, 0.1);
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

.arrow {
  font-size: 32rpx;
  color: #CCCCCC;
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
</style>
