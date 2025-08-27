package com.nnk.springboot.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.repositories.BidListRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BidListServiceTest {
    @Mock
    BidListRepository repository;

    @InjectMocks
    BidListService service;

    @Test
    void createBid_savesEntityWithRequest() {
        // given
        BidListDto request = new BidListDto();
        request.setAccount("test account");
        request.setType("test type");
        request.setBidQuantity(11.6);

        // when
        service.createBid(request);

        // then
        ArgumentCaptor<BidList> captor = ArgumentCaptor.forClass(BidList.class);
        verify(repository).save(captor.capture());
        verifyNoMoreInteractions(repository);

        BidList saved = captor.getValue();
        assertEquals("test account", saved.getAccount());
        assertEquals("test type", saved.getType());
        assertEquals(11.6, saved.getBidQuantity(), 0.0001);
    }

    @Test
    void updateBid_updatesFoundEntityAndSaves() {
        // given
        Integer bidListId = 1;

        BidList existing = new BidList();
        existing.setBidListId(1);
        existing.setAccount("existing account");
        existing.setType("existing type");
        existing.setBidQuantity(44.7);

        when(repository.findById(bidListId)).thenReturn(Optional.of(existing));

        BidListDto request = new BidListDto();
        request.setAccount("new account");
        request.setType("new type");
        request.setBidQuantity(662.3);

        // when
        service.updateBid(bidListId, request);

        // then
        ArgumentCaptor<BidList> captor = ArgumentCaptor.forClass(BidList.class);
        verify(repository).findById(bidListId);
        verify(repository).save(captor.capture());
        verifyNoMoreInteractions(repository);

        BidList saved = captor.getValue();
        assertEquals("new account", saved.getAccount());
        assertEquals("new type", saved.getType());
        assertEquals(662.3, saved.getBidQuantity(), 0.0001);
    }

    @Test
    void updateBid_throws_whenNotFound()  {
        // given
        Integer bidListId = 1;
        when(repository.findById(bidListId)).thenReturn(Optional.empty());

        BidListDto request = new BidListDto();
        request.setAccount("new account");
        request.setType("new type");
        request.setBidQuantity(662.3);

        // when
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.updateBid(bidListId, request));
        assertEquals("bid not found", ex.getMessage());

        // then
        verify(repository).findById(bidListId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteBid_deletes_whenExists() {
        // when
        when(repository.existsById(1)).thenReturn(true);
        service.deleteBid(1);

        // then
        verify(repository).existsById(1);
        verify(repository).deleteById(1);
    }

    @Test
    void deleteBid_throws_whenNotExists() {
        // when
        when(repository.existsById(1)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> service.deleteBid(1));

        // then
        verify(repository).existsById(1);
        verifyNoMoreInteractions(repository);
    }

}
