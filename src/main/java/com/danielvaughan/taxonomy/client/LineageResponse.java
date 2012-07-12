package com.danielvaughan.taxonomy.client;

import com.danielvaughan.taxonomy.shared.model.Taxon;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class LineageResponse {

  @JsonProperty
  private List<Taxon> lineage;

  public List<Taxon> getLineage() {
    return lineage;
  }

  public void setIssue(List<Taxon> lineage) {
    this.lineage = lineage;
  }
}
