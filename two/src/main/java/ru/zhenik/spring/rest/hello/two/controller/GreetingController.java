package ru.zhenik.spring.rest.hello.two.controller;


import io.opentracing.ActiveSpan;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GreetingController {


    @Qualifier("jaegerTracer")
    @Autowired
    private Tracer tracer;

    @RequestMapping("/me")
    public String chaining() {
        ActiveSpan serverSpan = tracer.activeSpan();

        ActiveSpan span = tracer.buildSpan("localSpan")
            .asChildOf(serverSpan.context())
                .startActive();

        try {
            // Traced work happens between start() and deactivate();
            return "me";
        } finally {


            span.deactivate();
        }

    }

}
