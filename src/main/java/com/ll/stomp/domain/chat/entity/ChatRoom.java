package com.ll.stomp.domain.chat.entity;

import com.ll.stomp.base.jpa.BaseEntity;
import com.ll.stomp.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Setter
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class ChatRoom extends BaseEntity {
    @Column(unique = true)
    private String name;
    @ManyToOne
    private Member maker;
}
