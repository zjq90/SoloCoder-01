<script>
export default {
  onLaunch: function() {
    console.log('App Launch')
  },
  onShow: function() {
    console.log('App Show')
  },
  onHide: function() {
    console.log('App Hide')
  }
}
</script>

<style lang="scss">
/* 引入全局样式变量 */
@import '@/uni.scss';

/* 全局样式 */
page {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  font-size: 28rpx;
  color: #333333;
  background-color: $bg-color;
  min-height: 100vh;
}

/* 文字截断 */
.text-ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.text-ellipsis-2 {
  display: -webkit-box;
  overflow: hidden;
  text-overflow: ellipsis;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
}

/* 安全区域 */
.safe-area-bottom {
  padding-bottom: constant(safe-area-inset-bottom);
  padding-bottom: env(safe-area-inset-bottom);
}

/* 统一按钮样式 */
.btn-primary {
  background-color: $primary-color;
  color: #FFFFFF;
  border-radius: 8rpx;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30rpx;
  padding: 20rpx 40rpx;
  transition: opacity 0.3s;
}

.btn-primary:active {
  opacity: 0.8;
}

.btn-primary:disabled {
  background-color: #CCCCCC;
  color: #999999;
}

.btn-outline {
  background-color: transparent;
  color: $primary-color;
  border: 2rpx solid $primary-color;
  border-radius: 8rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30rpx;
  padding: 18rpx 38rpx;
  transition: opacity 0.3s;
}

.btn-outline:active {
  opacity: 0.8;
}

/* 卡片样式 */
.card {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  padding: 30rpx;
  margin: 20rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

/* 分割线 */
.divider {
  height: 1rpx;
  background-color: #EEEEEE;
  margin: 20rpx 0;
}

/* 页面容器 */
.page-container {
  padding: 20rpx;
  padding-bottom: calc(20rpx + constant(safe-area-inset-bottom));
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
}

/* 列表项 */
.list-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24rpx 30rpx;
  background-color: #FFFFFF;
  border-bottom: 1rpx solid #F0F0F0;
}

.list-item:last-child {
  border-bottom: none;
}

.list-item:active {
  background-color: #F9F9F9;
}

.list-item .left {
  display: flex;
  align-items: center;
}

.list-item .arrow {
  color: #CCCCCC;
  font-size: 32rpx;
}
</style>
