package org.example.business.dto;

import org.example.business.Validator;

public record ConfigurationDTO(String name, boolean enabled) {
  public ConfigurationDTO(String name, boolean enabled) {
    this.name = Validator.getValidConfigurationName(name);
    this.enabled = enabled;
  }

  @Override
  public boolean equals(Object instance) {
    if (this == instance) return true;
    if (instance == null || getClass() != instance.getClass()) return false;

    ConfigurationDTO that = (ConfigurationDTO) instance;

    return name.equals(that.name) && enabled == that.enabled;
  }
}
