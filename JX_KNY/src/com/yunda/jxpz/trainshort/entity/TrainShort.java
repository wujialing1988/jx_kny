package com.yunda.jxpz.trainshort.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

@Entity
@Table(name="jxpz_train_short")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class TrainShort implements Serializable {
        
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name="short")
    private String shortName;
    
    @Column(name="train_short")
    private String trainShort;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getTrainShort() {
        return trainShort;
    }
    
    public void setTrainShort(String trainShort) {
        this.trainShort = trainShort;
    }
}
