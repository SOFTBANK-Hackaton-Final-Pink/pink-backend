package com.pink.backend.feature.function.entity;

import com.pink.backend.feature.common.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "functions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Function extends BaseTimeEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 50)
    private String runtime;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String code;

    @Column(nullable = false)
    private Integer latestVersion;

    public void updateCode(String newCode, Integer newVersion) {
        this.code = newCode;
        this.latestVersion = newVersion;
    }
}
