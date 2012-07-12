package com.danielvaughan.taxonomy.client;

import com.danielvaughan.taxonomy.shared.model.Taxon;

import java.util.List;

public interface TaxonomyClient {

  Taxon getTaxonById(String string);

  List<Taxon> getLineage(String taxId);

  Taxon getTaxonByScientificName(String scientificName);

  Taxon getTaxonByCommonName(String commonName);

}
