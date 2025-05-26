package org.example.business.dto;

import org.example.business.Validator;

public record ConfigurationDTO(String name, String value) {
  public ConfigurationDTO(String name, String value) {
    this.name = Validator.getValidConfigurationName(name);
    this.value = Validator.getValidConfigurationValue(name, value);
  }

  @Override
  public boolean equals(Object instance) {
    if (this == instance) return true;
    if (instance == null || getClass() != instance.getClass()) return false;

    ConfigurationDTO that = (ConfigurationDTO) instance;

    return name.equals(that.name) && value.equals(that.value);
  }
}
