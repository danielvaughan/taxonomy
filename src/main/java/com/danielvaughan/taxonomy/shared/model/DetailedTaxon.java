package com.danielvaughan.taxonomy.shared.model;

import org.apache.commons.lang.builder.ToStringBuilder;

public class DetailedTaxon extends Taxon {

  private String parentTaxId;
  private String hidden;
  private String rank;
  private String pln;
  private String geneticCode;
  private String mitochondrialGeneticCode;

  public DetailedTaxon(final String taxId, final String scientificName, final String commonName) {
    super(taxId, scientificName, commonName);
  }

  public String getGeneticCode() {
    return geneticCode;
  }

  public String getHidden() {
    return hidden;
  }

  public String getMitochondrialGeneticCode() {
    return mitochondrialGeneticCode;
  }

  public String getParentTaxId() {
    return parentTaxId;
  }

  public String getPln() {
    return pln;
  }

  public String getRank() {
    return rank;
  }

  public void setGeneticCode(final String geneticCode) {
    this.geneticCode = geneticCode;
  }

  public void setHidden(final String hidden) {
    this.hidden = hidden;
  }

  public void setMitochondrialGeneticCode(final String mitochondrialGeneticCode) {
    this.mitochondrialGeneticCode = mitochondrialGeneticCode;
  }

  public void setParentTaxId(final String parentTaxId) {
    this.parentTaxId = parentTaxId;
  }

  public void setPln(final String pln) {
    this.pln = pln;
  }

  public void setRank(final String rank) {
    this.rank = rank;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("taxId", getTaxId()).append("commonName", getCommonName()).append("scientificName",
                                                                                                    getScientificName())
        .toString();
  }
}
