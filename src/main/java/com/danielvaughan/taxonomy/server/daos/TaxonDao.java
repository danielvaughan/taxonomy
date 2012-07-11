package com.danielvaughan.taxonomy.server.daos;

import com.danielvaughan.taxonomy.shared.model.DetailedTaxon;
import com.danielvaughan.taxonomy.shared.model.Synonym;
import com.danielvaughan.taxonomy.shared.model.Taxon;

import org.neo4j.graphdb.Node;

import java.util.List;

public interface TaxonDao {

  long addTaxon(DetailedTaxon detailedTaxon);

  void assignParent(Node childNode, Node parentNode);

  void assignParents();

  void batchAddTaxons(List<DetailedTaxon> detailedTaxon);
  
  DetailedTaxon getDetailedTaxonByTaxId(String taxId);

  List<Taxon> getLineageByTaxId(String taxId, boolean full);

  Taxon getTaxonByTaxId(String taxId);

  List<Taxon> searchTaxons(String searchString, int resultLimit);

  List<Taxon> getRelationshipByTaxIds(String taxIdA, String taxIdB);

  long addSynonym(Synonym synonym);

}
