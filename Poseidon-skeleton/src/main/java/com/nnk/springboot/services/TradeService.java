package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.repositories.TradeRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TradeService {
    private final TradeRepository tradeRepository;

    @Transactional
    public void createTrade(TradeDto request) {
        Trade trade = new Trade();
        trade.setAccount(request.getAccount());
        trade.setType(request.getType());
        trade.setBuyQuantity(request.getBuyQuantity());

        tradeRepository.save(trade);
    }

    @Transactional
    public void updateTrade(Integer tradeId, TradeDto request) {
        Trade currentTrade = tradeRepository.findById(tradeId)
            .orElseThrow(()-> new IllegalArgumentException("Trade not found"));

        currentTrade.setAccount(request.getAccount());
        currentTrade.setType(request.getType());
        currentTrade.setBuyQuantity(request.getBuyQuantity());

        tradeRepository.save(currentTrade);
    }

    @Transactional
    public void deleteTrade(Integer tradeId) {
        if(!tradeRepository.existsById(tradeId)) {
            throw new IllegalArgumentException("Trade not found");
        }
        tradeRepository.deleteById(tradeId);
    }

    public TradeDto loadTradeById(Integer tradeId) {
        Trade currentTrade = tradeRepository.findById(tradeId)
            .orElseThrow(()-> new IllegalArgumentException("Trade not found"));

        return getTradeDto(currentTrade);
    }

    public List<TradeDto> loadAllTrades() {
        return tradeRepository.findAll().stream()
            .map(this::getTradeDto).toList();
    }

    private TradeDto getTradeDto(Trade currentTrade) {
        TradeDto dto = new TradeDto();
        dto.setTradeId(currentTrade.getTradeId());
        dto.setAccount(currentTrade.getAccount());
        dto.setType(currentTrade.getType());
        dto.setBuyQuantity(currentTrade.getBuyQuantity());
        return dto;
    }
}
