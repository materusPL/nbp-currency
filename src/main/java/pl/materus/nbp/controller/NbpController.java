package pl.materus.nbp.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import pl.materus.nbp.entity.Log;
import pl.materus.nbp.exceptions.CurrencyException;
import pl.materus.nbp.model.ErrorModel;
import pl.materus.nbp.model.RequestModel;
import pl.materus.nbp.model.ResponseModel;
import pl.materus.nbp.services.NbpService;
import reactor.core.publisher.Mono;

@RestController
public class NbpController {

    private final NbpService nbpService;

    @Autowired
    public NbpController(NbpService nbpService) {
        this.nbpService = nbpService;
    }

    @PostMapping("/currencies/get-current-currency-value-command")
    public ResponseEntity<Mono<ResponseModel>> getRate(
            @RequestHeader(name = HttpHeaders.ACCEPT, defaultValue = MediaType.APPLICATION_JSON_VALUE) String acceptHeader,
            @RequestBody RequestModel request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (request == null ||
                request.name() == null ||
                request.currency() == null ||
                request.name().isEmpty() ||
                request.currency().isEmpty()) {
            throw new CurrencyException("There is error in your request", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().headers(httpHeaders).body(nbpService.getExchangeRate(request.currency())
                .doOnNext(res -> nbpService.saveLog(res, request)));
    }

    @GetMapping("/currencies/requests")
    public ResponseEntity<List<Log>> getLog(@RequestHeader(name = HttpHeaders.ACCEPT) String acceptHeader) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.ok().headers(httpHeaders).body(nbpService.getLog());
    }

    @ExceptionHandler(CurrencyException.class)
    public ResponseEntity<ErrorModel> handleResponseException(CurrencyException ex) {

        ErrorModel model = nbpService.handleCurrencyException(ex);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.status(model.status()).headers(httpHeaders).body(model);

    }

}
