package com.danielvaughan.taxonomy.client;

import com.danielvaughan.taxonomy.shared.model.Taxon;

import org.codehaus.jackson.annotate.JsonProperty;


public class TaxonResponse {
  @JsonProperty
  private Taxon taxon;

  public Taxon getTaxon() {
    return taxon;
  }

  public void setTaxon(Taxon taxon) {
    this.taxon = taxon;
  }
}