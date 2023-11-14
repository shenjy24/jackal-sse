package com.jonas.sse.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SseService
 *
 * @author shenjy
 * @time 2023/11/14 16:52
 */
@Slf4j
@Service
public class SseService {

    private final Map<Long, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    public void addEmitter(Long clientId, SseEmitter emitter) {
        emitterMap.put(clientId, emitter);
        emitter.onCompletion(() -> emitterMap.remove(clientId));
        emitter.onTimeout(() -> emitterMap.remove(clientId));
    }

    public void push(Long clientId, String content) {
        SseEmitter sseEmitter = emitterMap.get(clientId);
        if (null != sseEmitter) {
            try {
                sseEmitter.send(new SseContent(content, new Timestamp(System.currentTimeMillis())));
            } catch (IOException e) {
                log.error("send error", e);
            }
        }
    }

    public void over(Long clientId) {
        SseEmitter sseEmitter = emitterMap.get(clientId);
        if (null != sseEmitter) {
            sseEmitter.complete();
            emitterMap.remove(clientId);
        }
    }
}
