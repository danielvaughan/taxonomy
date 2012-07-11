package com.danielvaughan.taxonomy.shared.model;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Taxon {

  public enum TaxonField {
    TAX_ID, SCIENTIFIC_NAME, COMMON_NAME
  }

  private String scientificName;

  private String taxId;

  private String commonName;

  public Taxon(String taxId, String scientificName, String commonName) {
    this.taxId = taxId;
    this.scientificName = scientificName;
    this.commonName = commonName;
  }

  public String getScientificName() {
    return scientificName;
  }

  public void setScientificName(String scientificName) {
    this.scientificName = scientificName;
  }

  public String getTaxId() {
    return taxId;
  }

  public void setTaxId(String taxId) {
    this.taxId = taxId;
  }

  public String getCommonName() {
    return commonName;
  }

  public void setCommonName(String commonName) {
    this.commonName = commonName;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("taxId", taxId).append("commonName", commonName).append("scientificName",
                                                                                                    scientificName)
        .toString();
  }
}
