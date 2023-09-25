package com.ll.stomp.domain.emailVerification.controller;

import com.ll.stomp.base.rq.Rq;
import com.ll.stomp.base.rsData.RsData;
import com.ll.stomp.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/emailVerification")
public class EmailVerificationController {
    private final MemberService memberService;
    private final Rq rq;

    @GetMapping("/verify")
    public String verify(long memberId, String code) {
        RsData verifyEmailRsData = memberService.verifyEmail(memberId, code);

        if (verifyEmailRsData.isFail()) {
            return rq.redirect("/", verifyEmailRsData.getMsg());
        }

        String successMsg = verifyEmailRsData.getMsg();

        if (rq.isLogout()) {
            return rq.redirect("/usr/member/login", successMsg);
        }

        return rq.redirect("/", successMsg);
    }
}
