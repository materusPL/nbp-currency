package pl.materus.nbp.model;

public record ErrorModel(String error, Integer status) {
    public ErrorModel(String error, Integer status) {
        this.error = error;
        this.status = status;
    }
}
