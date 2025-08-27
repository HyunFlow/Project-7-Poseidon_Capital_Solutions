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
    public void createBid(BidListDto req) {

        String reqAccount = req.getAccount();
        String reqType = req.getType();
        Double reqBidQty = req.getBidQuantity();

        if (reqBidQty == null || reqBidQty < 0) {
            throw new IllegalArgumentException("bid quantity must be positive");
        }

        BidList newBid = new BidList();
        newBid.setAccount(reqAccount);
        newBid.setType(reqType);
        newBid.setBidQuantity(reqBidQty);

        bidListRepository.save(newBid);
    }

    @Transactional
    public void updateBid(Integer bidListId, BidListDto req) {
        BidList currentBid = bidListRepository.findById(bidListId)
            .orElseThrow(() -> new IllegalArgumentException("bid not found"));

        currentBid.setAccount(req.getAccount());
        currentBid.setType(req.getType());
        currentBid.setBidQuantity(req.getBidQuantity());

        bidListRepository.save(currentBid);
    }

    @Transactional
    public void deleteBid(Integer bidListId) {
        if (!bidListRepository.existsById(bidListId)) {
            throw new IllegalArgumentException("bid not found");
        }
        bidListRepository.deleteById(bidListId);
    }

    public BidListDto loadForUpdate(Integer bidListId) {
        BidList currentBid = bidListRepository.findById(bidListId)
            .orElseThrow(() -> new IllegalArgumentException("bid not found"));

        BidListDto dto = new BidListDto();
        dto.setBidListId(currentBid.getBidListId());
        dto.setAccount(currentBid.getAccount());
        dto.setType(currentBid.getType());
        dto.setBidQuantity(currentBid.getBidQuantity());

        return dto;
    }

    public List<BidListDto> loadAllBidList() {
        return bidListRepository.findAll().stream()
            .map(bid -> {
                BidListDto dto = new BidListDto();
                dto.setBidListId(bid.getBidListId());
                dto.setAccount(bid.getAccount());
                dto.setType(bid.getType());
                dto.setBidQuantity(bid.getBidQuantity());
                return dto;
            })
            .toList();
    }

}
