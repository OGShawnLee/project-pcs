package org.example.business.dto;

import org.example.business.Util;

import java.time.LocalDateTime;

public interface Record {
  LocalDateTime getCreatedAt();

  default String getFormattedCreatedAt() {
    return Util.formatDateTimeToSpanish(getCreatedAt());
  }
}
