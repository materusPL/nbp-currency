package pl.materus.nbp.model;

import java.util.List;

public record ExchangeTableModel(String table, String no, String effectiveDate, List<RateModel> rates) {

}
