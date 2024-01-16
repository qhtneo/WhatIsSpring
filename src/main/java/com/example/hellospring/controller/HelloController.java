package com.example.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
    // 기본 html
    @GetMapping("hello")
    public String hello(Model model){
        model.addAttribute("data","<this is data>.");
        return "hello";
    }

    // thymeleaf 템플릿
    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam(value = "name") String name, Model model){
        model.addAttribute("name", name);
        return "hello-template";
    }

    // API 방식
    @GetMapping("hello-string")
    @ResponseBody
    public String helloString(@RequestParam("name") String name){
        return "hello " + name;
    }

    // api 중 JSON 방식
    @GetMapping("hello-api")
    @ResponseBody
    private Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }

    private static class Hello {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
