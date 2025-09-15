package br.com.osa.controller;

import static br.com.osa.controller.handler.MoneyFormatter.format;

import br.com.osa.domain.model.Transaction;
import br.com.osa.service.QueryService;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/queries")
public class QueryController {

  private final QueryService queryService;

  public QueryController(QueryService queryService) {
    this.queryService = queryService;
  }

  @GetMapping("/summary")
  public ResponseEntity<?> summary(@AuthenticationPrincipal Principal principal) {
    UUID userId = UUID.fromString(principal.getName());
    BigDecimal saldo = queryService.getBalance(userId);
    List<Transaction> historico = queryService.getHistory(userId);

    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    List<Map<String, Object>> hist = historico.stream()
        .map(t -> Map.<String, Object>of(
            "type", t.getType().name().toLowerCase(),
            "valor", format(t.getAmount()),
            "data", t.getCreatedAt().format(fmt)
        ))
        .toList();
    return ResponseEntity.ok(Map.of("SaldoTotal", format(saldo), "Historico", hist));
  }
}
