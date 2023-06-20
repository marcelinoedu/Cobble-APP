package com.manager.user.models.Token;

import com.manager.user.models.User.UserModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens_table")
public class TokenModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;


    @Column(name = "expired", columnDefinition = "BOOLEAN DEFAULT False")
    private boolean expired;

    @Column(name = "revoked", columnDefinition = "BOOLEAN DEFAULT False")
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserModel user;


}
