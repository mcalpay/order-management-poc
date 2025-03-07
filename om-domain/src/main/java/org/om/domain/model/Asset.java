package org.om.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
//customerId, assetName, size, usableSize
public class Asset {

    @Id
    private Long id;

    private Serializable customerId;

    private String assetName;

    private int size;

    private int usableSize;

}
