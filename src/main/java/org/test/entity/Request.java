package org.test.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "request", uniqueConstraints = {@UniqueConstraint(columnNames = {"banner_id", "user_agent", "date"})})
public class Request {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;


    @NotEmpty
    @ManyToOne(optional = false)
    @JoinColumn(name = "banner_id")
    private Banner bannerId;

    @Lob
    @NotNull
    @Column(name = "user_agent", columnDefinition = "text")
    private String userAgent;

    @NotNull
    @Column(name = "ip_address")
    private String ipAddress;

    @NotEmpty
    @Column(name = "date")
    private LocalDate date;
}
