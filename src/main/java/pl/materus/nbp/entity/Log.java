package pl.materus.nbp.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String currency;
    private String name;
    private BigDecimal value;
    private LocalDateTime date;

    public Log() {
    }

    public Log(final String currency, final String name, final BigDecimal value, final LocalDateTime date) {
        this.currency = currency;
        this.name = name;
        this.value = value;
        this.date = date;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public void setName(final BigDecimal value) {
        this.value = value;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public void setDate(final LocalDateTime date) {
        this.date = date;
    }

}
