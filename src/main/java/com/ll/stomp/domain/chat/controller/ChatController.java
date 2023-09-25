package com.ll.stomp.domain.chat.controller;

import com.ll.stomp.base.rq.Rq;
import com.ll.stomp.base.rsData.RsData;
import com.ll.stomp.domain.chat.dto.CreateMsgDto;
import com.ll.stomp.domain.chat.dto.SignalMsgDto;
import com.ll.stomp.domain.chat.dto.SignalType;
import com.ll.stomp.domain.chat.entity.ChatMsg;
import com.ll.stomp.domain.chat.entity.ChatRoom;
import com.ll.stomp.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr/chat")
public class ChatController {
    private final ChatService chatService;
    private final Rq rq;
    private final SimpMessagingTemplate messagingTemplate;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/createRoom")
    public String showCreate() {
        return "usr/chat/createRoom";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/createRoom")
    public String create(String name) {
        RsData createRoomRsData = chatService.createRoom(rq.getMember(), name);

        return rq.redirect("/usr/chat/roomList", createRoomRsData.getMsg());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/room/{roomId}")
    public String showRoom(Model model, @PathVariable Long roomId) {
        RsData<ChatRoom> rsData = chatService.findByRoomId(roomId);

        if (rsData.isFail()) {
            return rq.historyBack(rsData.getMsg());
        }

        model.addAttribute("chatRoom", rsData.getData());

        return "usr/chat/room";
    }

    @MessageMapping("/chat/room/{roomId}/createMsg")
    public void createMsg(Long roomId, @Payload CreateMsgDto createMsgDto) {

        ChatRoom chatRoom = chatService.findByRoomId(roomId).getData();

        RsData<ChatMsg> rsData = chatService.writeChatMsg(rq.getMember(), chatRoom, createMsgDto.getMsg());

        if (rsData.isSuccess()) {
            SignalMsgDto dto = SignalMsgDto.builder()
                    .writerId(rq.getMember().getId())
                    .type(SignalType.CREATE_MESSAGE)
                    .build();
            messagingTemplate.convertAndSend("/chat/room/" + roomId, dto);
        }
    }
}
