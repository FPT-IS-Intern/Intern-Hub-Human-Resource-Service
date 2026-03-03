package com.fis.hrmservice.api.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Global Jackson configuration to serialize Long/long as JSON String.
 *
 * <p><b>Why:</b> Snowflake IDs stored as BIGINT (Long) exceed JavaScript's Number.MAX_SAFE_INTEGER
 * (2^53−1). Browser JSON.parse() silently rounds them.
 *
 * <p><b>How:</b> Exposes a Jackson {@link SimpleModule} bean. Spring Boot auto-configuration picks
 * up all Module beans and registers them with the auto-configured ObjectMapper. Result: every
 * {@code Long}/{@code long} field in every JSON response is written as a quoted string.
 *
 * <p><b>Placement:</b> API layer — this is a presentation concern. Core domain and infra remain
 * unaware.
 *
 * <p><b>Deserialization:</b> Unaffected. Jackson parses both {@code "userId": 123} and {@code
 * "userId": "123"} into Long.
 *
 * <p><b>Side effects:</b> ALL Long fields become strings in JSON output (IDs, timestamps, counts).
 * Frontend must parse string IDs for comparison.
 */
@Configuration
public class JacksonConfig {

  /**
   * Module bean auto-registered by Spring Boot into the ObjectMapper. Serializes both {@code Long}
   * (boxed) and {@code long} (primitive) as JSON string values.
   */
  @Bean
  public SimpleModule longToStringModule() {
    System.out.println("LongToStringModule REGISTERED");
    SimpleModule module = new SimpleModule("LongToStringModule");
    module.addSerializer(Long.class, ToStringSerializer.instance);
    module.addSerializer(Long.TYPE, ToStringSerializer.instance);
    return module;
  }
}
