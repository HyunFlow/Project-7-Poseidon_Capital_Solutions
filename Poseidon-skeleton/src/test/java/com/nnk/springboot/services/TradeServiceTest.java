package com.nnk.springboot.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.repositories.TradeRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeService tradeService;

    @Test
    void loadAllTrades_mapsEntityToDto() {
        // given
        Trade t1 = new Trade();
        t1.setTradeId(1);
        t1.setAccount("ACC1");
        t1.setType("TYPE1");
        t1.setBuyQuantity(10.5);

        Trade t2 = new Trade();
        t2.setTradeId(2);
        t2.setAccount("ACC2");
        t2.setType("TYPE2");
        t2.setBuyQuantity(20.0);

        when(tradeRepository.findAll()).thenReturn(List.of(t1, t2));

        // when
        List<TradeDto> dtos = tradeService.loadAllTrades();

        // then
        assertEquals(2, dtos.size());
        assertEquals(1, dtos.get(0).getTradeId());
        assertEquals("ACC1", dtos.get(0).getAccount());
        assertEquals("TYPE1", dtos.get(0).getType());
        assertEquals(10.5, dtos.get(0).getBuyQuantity());

        assertEquals(2, dtos.get(1).getTradeId());
        assertEquals("ACC2", dtos.get(1).getAccount());
        assertEquals("TYPE2", dtos.get(1).getType());
        assertEquals(20.0, dtos.get(1).getBuyQuantity());

        verify(tradeRepository).findAll();
        verifyNoMoreInteractions(tradeRepository);
    }

    @Test
    void createTrade_savesMappedEntity() {
        // given
        TradeDto request = new TradeDto();
        request.setAccount("ACC");
        request.setType("TYPE");
        request.setBuyQuantity(99.9);

        ArgumentCaptor<Trade> tradeCaptor = ArgumentCaptor.forClass(Trade.class);

        when(tradeRepository.save(tradeCaptor.capture())).thenAnswer(inv -> {
            Trade saved = inv.getArgument(0);
            saved.setTradeId(123);
            return saved;
        });

        // when
        tradeService.createTrade(request);

        // then
        Trade saved = tradeCaptor.getValue();
        assertEquals("ACC", saved.getAccount());
        assertEquals("TYPE", saved.getType());
        assertEquals(99.9, saved.getBuyQuantity());

        verify(tradeRepository).save(saved);
        verifyNoMoreInteractions(tradeRepository);
    }

    @Test
    void updateTrade_updatesExistingEntity() {
        // given
        Integer id = 7;
        Trade existing = new Trade();
        existing.setTradeId(id);
        existing.setAccount("OLD");
        existing.setType("OLDTYPE");
        existing.setBuyQuantity(1.0);

        when(tradeRepository.findById(id)).thenReturn(Optional.of(existing));

        TradeDto update = new TradeDto();
        update.setAccount("NEW");
        update.setType("NEWTYPE");
        update.setBuyQuantity(2.5);

        ArgumentCaptor<Trade> tradeCaptor = ArgumentCaptor.forClass(Trade.class);
        when(tradeRepository.save(tradeCaptor.capture())).thenAnswer(inv -> inv.getArgument(0));

        // when
        tradeService.updateTrade(id, update);

        // then
        Trade toSave = tradeCaptor.getValue();
        assertEquals(id, toSave.getTradeId());
        assertEquals("NEW", toSave.getAccount());
        assertEquals("NEWTYPE", toSave.getType());
        assertEquals(2.5, toSave.getBuyQuantity());

        verify(tradeRepository).findById(id);
        verify(tradeRepository).save(toSave);
        verifyNoMoreInteractions(tradeRepository);
    }

    @Test
    void updateTrade_throws_whenNotFound() {
        // given
        Integer id = 99;
        when(tradeRepository.findById(id)).thenReturn(Optional.empty());

        // when & then
        TradeDto update = new TradeDto();
        assertThrows(IllegalArgumentException.class, () -> tradeService.updateTrade(id, update));

        verify(tradeRepository).findById(id);
        verify(tradeRepository, never()).save(org.mockito.Mockito.any());
        verifyNoMoreInteractions(tradeRepository);
    }

    @Test
    void deleteTrade_deletes_whenExists() {
        // given
        Integer id = 3;
        when(tradeRepository.existsById(id)).thenReturn(true);

        // when
        tradeService.deleteTrade(id);

        // then
        verify(tradeRepository).existsById(id);
        verify(tradeRepository).deleteById(id);
        verifyNoMoreInteractions(tradeRepository);
    }

    @Test
    void deleteTrade_throws_whenNotFound() {
        // given
        Integer id = 3;
        when(tradeRepository.existsById(id)).thenReturn(false);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> tradeService.deleteTrade(id));

        verify(tradeRepository).existsById(id);
        verifyNoMoreInteractions(tradeRepository);
    }
}
