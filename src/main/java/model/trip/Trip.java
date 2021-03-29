package model.trip;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import model.CsvSerializable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonPropertyOrder({"Started", "Finished", "DurationSecs", "FromStopId", "ToStopId", "ChargeAmount", "CompanyId", "BusID", "PAN", "Status"})
public class Trip implements CsvSerializable {

    @JsonProperty("Started")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime startDateTime;

    @JsonProperty("Finished")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime finishedDateTime;

    @JsonProperty("DurationSecs")
    private long durationInSec;

    @JsonProperty("FromStopId")
    private String fromStopId;

    @JsonProperty("ToStopId")
    private String toStopId;

    @JsonProperty("ChargeAmount")
    private String chargeAmount;

    @JsonProperty("CompanyId")
    private String company;

    @JsonProperty("BusID")
    private String bus;

    @JsonProperty("PAN")
    private String pan;

    @JsonProperty("Status")
    private TripType status;


    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getFinishedDateTime() {
        return finishedDateTime;
    }

    public void setFinishedDateTime(LocalDateTime finishedDateTime) {
        this.finishedDateTime = finishedDateTime;
    }

    public long getDurationInSec() {
        return durationInSec;
    }

    public void setDurationInSec(long durationInSec) {
        this.durationInSec = durationInSec;
    }

    public String getFromStopId() {
        return fromStopId;
    }

    public void setFromStopId(String fromStopId) {
        this.fromStopId = fromStopId;
    }

    public String getToStopId() {
        return toStopId;
    }

    public void setToStopId(String toStopId) {
        this.toStopId = toStopId;
    }


    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public TripType getStatus() {
        return status;
    }

    public void setStatus(TripType status) {
        this.status = status;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }
}
