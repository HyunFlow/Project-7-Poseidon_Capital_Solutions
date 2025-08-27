package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.repositories.BidListRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BidListService {

    final private BidListRepository bidListRepository;

    @Transactional
    public void createBid(BidListDto request) {
        BidList newBid = new BidList();
        newBid.setAccount(request.getAccount());
        newBid.setType(request.getType());
        newBid.setBidQuantity(request.getBidQuantity());

        bidListRepository.save(newBid);
    }

    @Transactional
    public void updateBid(Integer bidListId, BidListDto request) {
        BidList currentBid = bidListRepository.findById(bidListId)
            .orElseThrow(() -> new IllegalArgumentException("bid not found"));

        currentBid.setAccount(request.getAccount());
        currentBid.setType(request.getType());
        currentBid.setBidQuantity(request.getBidQuantity());

        bidListRepository.save(currentBid);
    }

    @Transactional
    public void deleteBid(Integer bidListId) {
        if (!bidListRepository.existsById(bidListId)) {
            throw new IllegalArgumentException("bid not found");
        }
        bidListRepository.deleteById(bidListId);
    }

    public BidListDto loadBidById(Integer bidListId) {
        BidList currentBid = bidListRepository.findById(bidListId)
            .orElseThrow(() -> new IllegalArgumentException("bid not found"));

        return getBidListDto(currentBid);
    }

    public List<BidListDto> loadAllBidList() {
        return bidListRepository.findAll().stream()
            .map(this::getBidListDto).toList();
    }

    private BidListDto getBidListDto(BidList currentBidList) {
        BidListDto dto = new BidListDto();
        dto.setBidListId(currentBidList.getBidListId());
        dto.setAccount(currentBidList.getAccount());
        dto.setType(currentBidList.getType());
        dto.setBidQuantity(currentBidList.getBidQuantity());
        return dto;
    }

}
