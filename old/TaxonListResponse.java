package com.danielvaughan.taxonomy.client;

import com.danielvaughan.taxonomy.shared.model.Taxon;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class TaxonListResponse {

  @JsonProperty
  private List<Taxon> taxonList;

  public List<Taxon> getTaxonList() {
    return taxonList;
  }

  public void setTaxonList(List<Taxon> taxonList) {
    this.taxonList = taxonList;
  }
}
