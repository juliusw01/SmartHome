package com.katzenklappe.gateway;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

@Configuration
@EnableRedisWebSession
public class SessionConfig extends AbstractHttpSessionApplicationInitializer {
}
