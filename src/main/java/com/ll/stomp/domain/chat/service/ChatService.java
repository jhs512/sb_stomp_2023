package com.ll.stomp.domain.chat.service;

import com.ll.stomp.base.rsData.RsData;
import com.ll.stomp.domain.chat.entity.ChatMsg;
import com.ll.stomp.domain.chat.entity.ChatRoom;
import com.ll.stomp.domain.chat.repository.ChatMsgRepository;
import com.ll.stomp.domain.chat.repository.ChatRoomRepository;
import com.ll.stomp.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMsgRepository chatMsgRepository;

    @Transactional
    public RsData<ChatRoom> createRoom(Member maker, String name) {
        ChatRoom chatRoom = ChatRoom
                .builder()
                .name(name)
                .maker(maker)
                .build();

        chatRoomRepository.save(chatRoom);

        return RsData.of("S-1", "채팅방이 생성되었습니다.", chatRoom);
    }

    public RsData<ChatMsg> writeChatMsg(Member writer, ChatRoom chatRoom, String msg) {
        ChatMsg chatMsg = ChatMsg
                .builder()
                .chatRoom(chatRoom)
                .maker(writer)
                .body(msg)
                .build();

        chatMsgRepository.save(chatMsg);

        return RsData.of("S-1", "채팅메시지가 전송되었습니다.", chatMsg);
    }

    public RsData<ChatRoom> findByRoomId(Long roomId) {
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findById(roomId);

        return chatRoomOptional
                .map(chatRoom -> RsData.of("S-1", "채팅방을 불러왔습니다.", chatRoom))
                .orElseGet(() -> RsData.of("F-1", "존재하지 않는 채팅방입니다."));
    }
}
