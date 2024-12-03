package pl.materus.nbp.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import pl.materus.nbp.entity.Log;
import pl.materus.nbp.exceptions.CurrencyException;
import pl.materus.nbp.model.ErrorModel;
import pl.materus.nbp.model.ExchangeTableModel;
import pl.materus.nbp.model.RequestModel;
import pl.materus.nbp.model.ResponseModel;
import pl.materus.nbp.repository.LogRepository;
import reactor.core.publisher.Mono;

@Service
public class NbpService {
    private final WebClient nbpWebClient;
    private final LogRepository logRepository;

    @Autowired
    public NbpService(WebClient nbpWebClient, LogRepository logRepository) {
        this.nbpWebClient = nbpWebClient;
        this.logRepository = logRepository;
    }

    @Cacheable(value = "exchangeRatesCache")
    public Mono<List<ExchangeTableModel>> getExchangeRates() {

        return nbpWebClient.get()
                .uri("/api/exchangerates/tables/A?format=json")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ExchangeTableModel>>() {
                });

    }

    public Mono<ResponseModel> getExchangeRate(String code) {
        return getExchangeRates()
                .flatMap(exchangeTables -> {
                    if (exchangeTables != null && !exchangeTables.isEmpty()) {
                        return Mono.justOrEmpty(
                                exchangeTables.get(0)
                                        .rates()
                                        .stream()
                                        .filter(rate -> code.equalsIgnoreCase(rate.code()))
                                        .map(rate -> new ResponseModel(new BigDecimal(rate.mid())))
                                        .findFirst())
                                .switchIfEmpty(Mono.error(
                                        new CurrencyException("Currency not found: " + code, HttpStatus.NOT_FOUND)));
                    } else {
                        return Mono
                                .error(new CurrencyException("Currency table is null or empty",
                                        HttpStatus.INTERNAL_SERVER_ERROR));
                    }
                });
    }

    public void saveLog(ResponseModel response, RequestModel request) {
        Log log = new Log(request.currency(), request.name(), response.value(), LocalDateTime.now());
        logRepository.save(log);
    }

    public List<Log> getLog() {
        return logRepository.findAll();
    }

    public ErrorModel handleCurrencyException(CurrencyException ex) {
        return new ErrorModel(ex.getMessage(), ex.getStatus().value());
    }

}
