package com.danielvaughan.taxonomy.client;

import org.codehaus.jackson.annotate.JsonProperty;

public class ValidResponse {
  @JsonProperty
  private boolean valid;

  public boolean isValid() {
    return valid;
  }

  public void setTaxon(boolean valid) {
    this.valid = valid;
  }
}
