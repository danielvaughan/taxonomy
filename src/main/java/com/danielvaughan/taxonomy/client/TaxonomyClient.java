package com.danielvaughan.taxonomy.client;

import com.danielvaughan.taxonomy.shared.model.Taxon;

import java.util.List;

public interface TaxonomyClient {

  Taxon getTaxonByTaxId(String string);

  List<Taxon> getLineage(String taxId);

  Taxon getTaxonByScientificName(String scientificName);

  Taxon getTaxonByCommonName(String commonName);

  boolean isValidTaxId(String taxId);

  List<Taxon> suggestTaxId(String partialTaxId);

  List<Taxon> suggest(String query, int limit);

}
