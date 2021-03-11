package model.transactionrecord;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * Contain Data related to a single transaction
 *
 */
@JsonPropertyOrder({ "transactionId", "fromAccountId", "toAccountId", "createdAt","amount","transactionType","relatedTransaction" })
public class TransactionRecord {


    private String transactionId;
    private String fromAccountId;
    private String toAccountId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    private BigDecimal amount;
    private TransactionType transactionType;
    private String relatedTransaction;


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getRelatedTransaction() {
        return relatedTransaction;
    }

    public void setRelatedTransaction(String relatedTransaction) {
        this.relatedTransaction = relatedTransaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionRecord that = (TransactionRecord) o;
        return Objects.equals(transactionId, that.transactionId) &&
                Objects.equals(fromAccountId, that.fromAccountId) &&
                Objects.equals(toAccountId, that.toAccountId) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(amount, that.amount) &&
                transactionType == that.transactionType &&
                Objects.equals(relatedTransaction, that.relatedTransaction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, fromAccountId, toAccountId, createdAt, amount, transactionType, relatedTransaction);
    }
}
