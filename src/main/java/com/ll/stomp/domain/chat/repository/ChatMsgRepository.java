package com.ll.stomp.domain.chat.repository;

import com.ll.stomp.domain.chat.entity.ChatMsg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMsgRepository extends JpaRepository<ChatMsg, Long> {
}
