package org.example.business.dto;

import org.example.business.dao.Configuration;

public record ConfigurationDTO(Configuration name, boolean isEnabled) {
  @Override
  public boolean equals(Object instance) {
    if (this == instance) return true;
    if (instance == null || getClass() != instance.getClass()) return false;

    ConfigurationDTO that = (ConfigurationDTO) instance;

    return name.equals(that.name) && isEnabled == that.isEnabled;
  }
}
