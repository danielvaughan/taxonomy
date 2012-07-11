package com.danielvaughan.taxonomy.shared.model;

public class Synonym {
  
  public enum SynonymField {
    TAX_ID, TYPE, NAME
  }

  private String taxId;
  private String type;
  private String name;

  public Synonym(String taxId, String type, String name) {
    this.taxId = taxId;
    this.type = type;
    this.name = name;
  }

  public String getTaxId() {
    return taxId;
  }

  public void setTaxId(String taxId) {
    this.taxId = taxId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
