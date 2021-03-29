package model.tap;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import model.CsvSerializable;

import java.time.LocalDateTime;

@JsonPropertyOrder({"ID", "DateTimeUTC", "TapType", "StopId", "CompanyId", "BusID", "PAN"})
public class Tap implements CsvSerializable {

    @JsonProperty ("ID")
    private int id;

    @JsonProperty("DateTimeUTC")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dateTimeUTC;


    @JsonProperty("TapType")
    private TapType tapType;

    @JsonProperty("StopId")
    private String stopId;

    @JsonProperty("CompanyId")
    private String companyId;

    @JsonProperty("BusID")
    private String busId;

    @JsonProperty("PAN")
    private String pan;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateTimeUTC() {
        return dateTimeUTC;
    }

    public void setDateTimeUTC(LocalDateTime dateTimeUTC) {
        this.dateTimeUTC = dateTimeUTC;
    }

    public TapType getTapType() {
        return tapType;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public void setTapType(TapType tapType) {
        this.tapType = tapType;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }
}
