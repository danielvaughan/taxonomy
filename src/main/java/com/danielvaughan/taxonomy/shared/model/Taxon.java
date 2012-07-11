package com.danielvaughan.taxonomy.shared.model;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Taxon {

  public enum TaxonField {
    TAX_ID, SCIENTIFIC_NAME, COMMON_NAME, PARENT_TAX_ID, HIDDEN, GENETIC_CODE, MITOCHONDRIAL_GENETIC_CODE, RANK, PLN
  }

  private String scientificName;

  private String taxId;

  private String commonName;

  public Taxon(final String taxId, final String scientificName, final String commonName) {
    this.taxId = taxId;
    this.scientificName = scientificName;
    this.commonName = commonName;
  }

  public Taxon() {
    this("0","","");
  }

  public String getCommonName() {
    return commonName;
  }

  public String getScientificName() {
    return scientificName;
  }

  public String getTaxId() {
    return taxId;
  }

  public void setCommonName(final String commonName) {
    this.commonName = commonName;
  }

  public void setScientificName(final String scientificName) {
    this.scientificName = scientificName;
  }

  public void setTaxId(final String taxId) {
    this.taxId = taxId;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("taxId", taxId).append("commonName", commonName).append("scientificName",
                                                                                                    scientificName)
        .toString();
  }
}
