package de.starwit.persistence.analytics.entity;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.starwit.persistence.common.serializer.ZonedDateTimeDeserializer;
import de.starwit.persistence.common.serializer.ZonedDateTimeSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * AreaOccupancy Entity class
 */
@Entity
@Table(name = "areaoccupancy")
public class AreaOccupancyEntity {

    @Id
    @Column(name = "occupancytime", nullable = false)
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime occupancyTime;

    // entity fields
    @NotNull
    @Column(name = "parkingareaid", nullable = false)
    private Long parkingAreaId;

    @NotNull
    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "objectclassid")
    private Integer objectClassId;

    // entity fields getters and setters
    public ZonedDateTime getOccupancyTime() {
        return occupancyTime;
    }

    public void setOccupancyTime(ZonedDateTime occupancyTime) {
        this.occupancyTime = occupancyTime;
    }

    public Long getParkingAreaId() {
        return parkingAreaId;
    }

    public void setParkingAreaId(Long parkingAreaId) {
        this.parkingAreaId = parkingAreaId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    // entity relations getters and setters
    public Integer getObjectClassId() {
        return objectClassId;
    }

    public void setObjectClassId(Integer objectClassId) {
        this.objectClassId = objectClassId;
    }

}
