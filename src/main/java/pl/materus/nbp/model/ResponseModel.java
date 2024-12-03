package pl.materus.nbp.model;

import java.math.BigDecimal;

public record ResponseModel(BigDecimal value) {
    public ResponseModel(BigDecimal value) {
        this.value = value;
    }
}
