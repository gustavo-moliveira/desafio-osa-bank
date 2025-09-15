package br.com.osa.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import br.com.osa.domain.exception.ResourceNotFoundException;
import br.com.osa.domain.model.Transaction;
import br.com.osa.domain.model.TransactionType;
import br.com.osa.service.QueryService;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class QueryControllerTest {

  @Mock
  private QueryService queryService;

  @InjectMocks
  private QueryController controller;

  @Test
  void whenSummary_withValidUser_thenReturnSaldoAndHistory() {
    UUID userId = UUID.randomUUID();
    UUID accountId = UUID.randomUUID();

    when(queryService.getBalance(any())).thenReturn(new BigDecimal("123.45"));

    Transaction tx = new Transaction(UUID.randomUUID(), accountId, TransactionType.DEPOSIT,
        new BigDecimal("50.00"), LocalDateTime.now());

    when(queryService.getHistory(any())).thenReturn(List.of(tx));

    Principal principal = () -> userId.toString();
    ResponseEntity<?> resp = controller.summary(principal);

    assertEquals(200, resp.getStatusCodeValue());
    Object body = resp.getBody();
    Map<String, Object> map = (Map<String, Object>) body;
    assertTrue(map.containsKey("SaldoTotal"));
    assertTrue(map.containsKey("Historico"));
  }

  @Test
  void whenSummary_accountNotFound_thenThrowsResourceNotFoundException() {
    when(queryService.getBalance(any())).thenThrow(
        new ResourceNotFoundException("Conta não encontrada"));

    Principal principal = () -> UUID.randomUUID().toString();

    try {
      controller.summary(principal);
    } catch (Exception ex) {
      assertInstanceOf(ResourceNotFoundException.class, ex);
      assertEquals("Conta não encontrada", ex.getMessage());
    }
  }
}

