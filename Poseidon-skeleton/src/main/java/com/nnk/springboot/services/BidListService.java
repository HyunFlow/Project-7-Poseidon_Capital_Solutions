package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidAddRequest;
import com.nnk.springboot.repositories.BidListRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BidListService {

    final private BidListRepository bidListRepository;

    @Transactional
    public void addBid(BidAddRequest req) {

        String reqAccount = req.getAccount();
        String reqType = req.getType();
        Double reqBidQty = req.getBidQuantity();

        if(reqBidQty == null || reqBidQty < 0) {
            throw new IllegalArgumentException("bid quantity must be positive");
        }

        BidList newBidList = new BidList();
        newBidList.setAccount(reqAccount);
        newBidList.setType(reqType);
        newBidList.setBidQuantity(reqBidQty);

        bidListRepository.save(newBidList);
    }
}
