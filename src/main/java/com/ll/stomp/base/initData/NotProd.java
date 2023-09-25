package com.ll.stomp.base.initData;

import com.ll.stomp.domain.chat.entity.ChatRoom;
import com.ll.stomp.domain.chat.service.ChatService;
import com.ll.stomp.domain.member.entity.Member;
import com.ll.stomp.domain.member.service.MemberService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!prod")
public class NotProd {
    @Bean
    public ApplicationRunner init(MemberService memberService, ChatService chatService) {
        return args -> {
            final Member member1 = memberService.join("admin", "1234", "admin", "admin@test.com", null).getData();
            final Member member2 = memberService.join("user1", "1234", "nickname1", "user1@test.com", null).getData();
            final Member member3 = memberService.join("user2", "1234", "nickname2", "user2@test.com", null).getData();
            final Member member4 = memberService.join("user3", "1234", "nickname3", "user3@test.com", null).getData();

            final ChatRoom chatRoom1 = chatService.createRoom(member2, "사랑").getData();
            final ChatRoom chatRoom2 = chatService.createRoom(member3, "희망").getData();
            final ChatRoom chatRoom3 = chatService.createRoom(member4, "소망").getData();

            chatService.writeChatMsg(member2, chatRoom1, "안녕하세요.");
            chatService.writeChatMsg(member3, chatRoom1, "반가워요.");
            chatService.writeChatMsg(member4, chatRoom1, "만나서 반갑습니다.");
        };
    }
}
