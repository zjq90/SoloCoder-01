package com.jiaoyi.trade.service;

import com.jiaoyi.trade.common.ResponseCode;
import com.jiaoyi.trade.entity.Channel;
import com.jiaoyi.trade.exception.BusinessException;
import com.jiaoyi.trade.mapper.ChannelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ChannelService {

    @Autowired
    private ChannelMapper channelMapper;

    private final AtomicInteger currentIndex = new AtomicInteger(0);

    public List<Channel> getAllAvailableChannels() {
        return channelMapper.selectAllAvailable();
    }

    public Channel selectNextChannel() {
        List<Channel> availableChannels = channelMapper.selectAllAvailable();
        
        if (availableChannels == null || availableChannels.isEmpty()) {
            throw new BusinessException(ResponseCode.CHANNEL_NOT_AVAILABLE, "暂无可用交易通道");
        }

        List<Channel> validChannels = availableChannels.stream()
                .filter(Channel::isAvailable)
                .collect(Collectors.toList());

        if (validChannels.isEmpty()) {
            throw new BusinessException(ResponseCode.CHANNEL_NOT_AVAILABLE, "所有通道交易量已达上限");
        }

        int index = Math.abs(currentIndex.getAndIncrement()) % validChannels.size();
        Channel selectedChannel = validChannels.get(index);

        channelMapper.incrementTransactionCount(selectedChannel.getId());

        return selectedChannel;
    }

    public Channel getByCode(String channelCode) {
        return channelMapper.selectByCode(channelCode);
    }

    public Channel getById(Long id) {
        return channelMapper.selectById(id);
    }
}
