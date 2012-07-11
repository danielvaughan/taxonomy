package com.danielvaughan.taxonomy.server.daos;

import com.danielvaughan.taxonomy.shared.model.Taxon;

import java.util.List;

public interface TaxonDao {

  long addTaxon(Taxon taxon);

  Taxon getTaxonByTaxId(String taxId);

  void batchAddTaxons(List<Taxon> taxons);

  List<Taxon> searchTaxons(String searchString, int resultLimit);

}
