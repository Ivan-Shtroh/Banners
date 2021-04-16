package org.test.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "banner")
public class Banner {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotNull
    @Column(name = "name", unique = true)
    private String name;

    @NotNull
    @Column(name = "price")
    private BigDecimal price;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category categoryId;

    @Lob
    @NotNull
    @Column(name = "content", columnDefinition = "text")
    private String content;

    @NotNull
    @Column(name = "deleted")
    private boolean deleted;

}
