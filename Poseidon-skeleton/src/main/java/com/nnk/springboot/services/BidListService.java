package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidAddRequest;
import com.nnk.springboot.dto.BidListReadDto;
import com.nnk.springboot.dto.BidUpdateRequest;
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
    public void createBid(BidAddRequest req) {

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
    public void updateBid(Integer bidListId, BidUpdateRequest req) {
        BidList currentBid = bidListRepository.findById(bidListId)
            .orElseThrow(() -> new IllegalArgumentException("bid not found"));

        String reqAccount = req.getAccount();
        String reqType = req.getType();
        Double reqBidQty = req.getBidQuantity();

        currentBid.setAccount(reqAccount);
        currentBid.setType(reqType);
        currentBid.setBidQuantity(reqBidQty);

        bidListRepository.save(currentBid);
    }

    public BidUpdateRequest loadForUpdate(Integer bidListId) {
        BidList currentBid = bidListRepository.findById(bidListId)
            .orElseThrow(() -> new IllegalArgumentException("bid not found"));

        BidUpdateRequest dto = new BidUpdateRequest();
        dto.setBidListId(currentBid.getBidListId());
        dto.setAccount(currentBid.getAccount());
        dto.setType(currentBid.getType());
        dto.setBidQuantity(currentBid.getBidQuantity());

        return dto;
    }

    public List<BidListReadDto> loadAllBidList() {
        return bidListRepository.findAll().stream()
            .map(bid -> {
                BidListReadDto dto = new BidListReadDto();
                dto.setBidListId(bid.getBidListId());
                dto.setAccount(bid.getAccount());
                dto.setType(bid.getType());
                dto.setBidQuantity(bid.getBidQuantity());
                return dto;
            })
            .toList();
    }

    public void deleteBid(Integer bidListId) {
        if (!bidListRepository.existsById(bidListId)) {
            throw new IllegalArgumentException("bid not found");
        }
            bidListRepository.deleteById(bidListId);
    }

}
