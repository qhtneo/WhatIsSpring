package com.example.hellospring.controller;

import com.example.hellospring.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

}
