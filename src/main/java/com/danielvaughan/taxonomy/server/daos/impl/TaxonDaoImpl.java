package com.danielvaughan.taxonomy.server.daos.impl;

import com.danielvaughan.taxonomy.server.InterRelType;
import com.danielvaughan.taxonomy.server.ServerConstants.IndexId;
import com.danielvaughan.taxonomy.server.ServerErrorMessages;
import com.danielvaughan.taxonomy.server.ServerInfoMessages;
import com.danielvaughan.taxonomy.server.daos.TaxonDao;
import com.danielvaughan.taxonomy.shared.model.DetailedTaxon;
import com.danielvaughan.taxonomy.shared.model.Taxon;
import com.danielvaughan.taxonomy.shared.model.Taxon.TaxonField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.kernel.Traversal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Component
public class TaxonDaoImpl implements TaxonDao {

  @Autowired
  private GraphDatabaseService graphDbService;

  private final Log log = LogFactory.getLog(TaxonDaoImpl.class);
  private Index<Node> taxIdIndex;
  private final IndexId taxIdIndexId = IndexId.TAXID_INDEX;

  private Index<Node> textIndex;
  private final IndexId textIndexId = IndexId.TEXT_INDEX;

  @Transactional
  @Override
  public long addTaxon(final DetailedTaxon detailedTaxon) {
    final Node taxonNode = getNodeByTaxId(detailedTaxon.getTaxId());
    if (taxonNode == null) {
      log.info(ServerInfoMessages.ADDING_TAXON + detailedTaxon.getTaxId());
      return createTaxonNode(detailedTaxon).getId();
    } else {
      log.info(ServerInfoMessages.RETRIEVED_TAXON + detailedTaxon.getTaxId());
      return taxonNode.getId();
    }
  }

  @Override
  @Transactional
  public void assignParent(final Node childNode, final Node parentNode) {
    if (parentNode != null && childNode != null) {
      if (childNode.hasRelationship(Direction.INCOMING, InterRelType.PARENT_OF)) {
        System.out.println("Deleting existing relationship");
        for (final Relationship relationship : childNode.getRelationships(Direction.INCOMING, InterRelType.PARENT_OF)) {
          relationship.delete();
        }
      }
      parentNode.createRelationshipTo(childNode, InterRelType.PARENT_OF);
    } else {
      System.out.println("Problem with child:");
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  @Transactional
  public void assignParents() {
    Set<String> missing = new HashSet<String>();
    for (Node node : graphDbService.getAllNodes()) {
      final String parentTaxId = (String) node.getProperty(TaxonField.PARENT_TAX_ID.name(), "");
      Node parentNode = null;
      if (parentTaxId != null) {
        if (parentTaxId.equals("1")) {
          System.out.println("Root Node");
          parentNode = graphDbService.getNodeById(0);
        } else {
          parentNode = getNodeByTaxId(parentTaxId);
          if (parentNode == null) {
            missing.add(parentTaxId);
          }
        }
      }
      if (parentNode != null) {
        assignParent(node, parentNode);
      }
    }
    for (String entry : missing) {
      System.out.println(entry);
    }

  }

  @Transactional
  @Override
  public void batchAddTaxons(final List<DetailedTaxon> detailedTaxons) {
    for (final DetailedTaxon detailedTaxon : detailedTaxons) {
      addTaxon(detailedTaxon);
    }
    log.info("Added batch");
  }

  @Override
  @Transactional
  public DetailedTaxon getDetailedTaxonByTaxId(final String taxId) {
    final Node taxonNode = getNodeByTaxId(taxId);
    return buildDetailedTaxon(taxonNode);
  }

  @Transactional
  @Override
  public List<Taxon> getLineageByTaxId(final String taxId, final boolean full) {
    final Node rootNode = graphDbService.getNodeById(0);
    final Node taxonNode = getNodeByTaxId(taxId);
    final PathFinder<Path> finder =
        GraphAlgoFactory.shortestPath(Traversal.expanderForTypes(InterRelType.PARENT_OF, Direction.INCOMING), 100);
    final Path path = finder.findSinglePath(taxonNode, rootNode);
    final List<Taxon> lineage = new ArrayList<Taxon>();
    for (Node node : path.nodes()) {
      if (node.getId() != 0) {
        if (full) {
          lineage.add(buildTaxon(node));
        } else {
          String hidden = (String) node.getProperty(TaxonField.HIDDEN.name(), "");
          if (!hidden.equals("true")) {
            lineage.add(buildTaxon(node));
          }
        }
      }
    }
    return lineage;
  }

  @Transactional
  @Override
  public List<Taxon> getRelationshipByTaxIds(final String taxIdA, final String taxIdB) {
    final Node taxonANode = getNodeByTaxId(taxIdA);
    final Node taxonBNode = getNodeByTaxId(taxIdB);
    final PathFinder<Path> finder =
        GraphAlgoFactory.shortestPath(Traversal.expanderForTypes(InterRelType.PARENT_OF, Direction.BOTH), 100);
    final Path path = finder.findSinglePath(taxonANode, taxonBNode);
    final List<Taxon> relationship = new ArrayList<Taxon>();
    for (Node node : path.nodes()) {
      relationship.add(buildTaxon(node));
    }
    return relationship;
  }

  @Override
  @Transactional
  public Taxon getTaxonByTaxId(final String taxId) {
    final Node taxonNode = getNodeByTaxId(taxId);
    return buildTaxon(taxonNode);
  }

  @Override
  public List<Taxon> searchTaxons(final String searchString, final int resultLimit) {
    final List<Taxon> taxons = new ArrayList<Taxon>();
    final Set<Node> taxonNodes = getNodesByPartialKey(searchString);
    int count = 0;
    for (final Node taxonNode : taxonNodes) {
      if (count <= resultLimit) {
        taxons.add(buildTaxon(taxonNode));
        count++;
      }
    }
    return taxons;
  }

  private void addTextToIndex(final Node node, final Taxon taxon) {
    getTextIndex().add(node, textIndexId.name(), taxon.getTaxId());
    if (taxon.getCommonName() != null) {
      getTextIndex().add(node, textIndexId.name(), taxon.getCommonName().toLowerCase());
    }
    getTextIndex().add(node, textIndexId.name(), taxon.getScientificName().toLowerCase());
  }

  private DetailedTaxon buildDetailedTaxon(Node taxonNode) {
    final String taxId = (String) taxonNode.getProperty(TaxonField.TAX_ID.name(), "");
    final String scientificName = (String) taxonNode.getProperty(TaxonField.SCIENTIFIC_NAME.name(), "");
    final String commonName = (String) taxonNode.getProperty(TaxonField.COMMON_NAME.name(), "");
    final String parentTaxId = (String) taxonNode.getProperty(TaxonField.PARENT_TAX_ID.name(), "");
    final String geneticCode = (String) taxonNode.getProperty(TaxonField.GENETIC_CODE.name(), "");
    final String mitochondrialGeneticCode =
        (String) taxonNode.getProperty(TaxonField.MITOCHONDRIAL_GENETIC_CODE.name(), "");
    final String hidden = (String) taxonNode.getProperty(TaxonField.HIDDEN.name(), "");
    final String pln = (String) taxonNode.getProperty(TaxonField.PLN.name(), "");
    final String rank = (String) taxonNode.getProperty(TaxonField.RANK.name(), "");
    final DetailedTaxon detailedTaxon = new DetailedTaxon(taxId, scientificName, commonName);
    detailedTaxon.setParentTaxId(parentTaxId);
    detailedTaxon.setGeneticCode(geneticCode);
    detailedTaxon.setMitochondrialGeneticCode(mitochondrialGeneticCode);
    detailedTaxon.setHidden(hidden);
    detailedTaxon.setPln(pln);
    detailedTaxon.setRank(rank);
    return detailedTaxon;
  }

  private Taxon buildTaxon(final Node node) {
    Taxon taxon = new Taxon();
    if (node != null) {
      final String taxId = (String) node.getProperty(TaxonField.TAX_ID.name(), "");
      final String scientificName = (String) node.getProperty(TaxonField.SCIENTIFIC_NAME.name(), "");
      final String commonName = (String) node.getProperty(TaxonField.COMMON_NAME.name(), "");
      taxon = new Taxon(taxId, scientificName, commonName);
    }
    return taxon;
  }

  private Node createIndexedNode(String key) {
    final Node node = createNode();
    key = key.toLowerCase();
    getTaxIdIndex().add(node, taxIdIndexId.name(), key);
    log.debug("Indexed key: " + key + " in index " + taxIdIndexId.name());
    return node;
  }

  private Node createNode() {
    final Node node = graphDbService.createNode();
    try {
      log.debug("Created node " + node.getId());
    } catch (final Exception e) {
      log.error("Error creating node", e);
    }
    return node;
  }

  private Node createTaxonNode(final DetailedTaxon detailedTaxon) {
    final String externalKey = detailedTaxon.getTaxId();
    final Node node = createIndexedNode(externalKey);
    addTextToIndex(node, detailedTaxon);
    try {
      node.setProperty(TaxonField.TAX_ID.name(), detailedTaxon.getTaxId());
      node.setProperty(TaxonField.SCIENTIFIC_NAME.name(), detailedTaxon.getScientificName());
      if (detailedTaxon.getCommonName() != null) {
        node.setProperty(TaxonField.COMMON_NAME.name(), detailedTaxon.getCommonName());
      }
      if (detailedTaxon.getParentTaxId() != null) {
        node.setProperty(TaxonField.PARENT_TAX_ID.name(), detailedTaxon.getParentTaxId());
      }
      if (detailedTaxon.getHidden() != null) {
        node.setProperty(TaxonField.HIDDEN.name(), detailedTaxon.getHidden());
      }
      if (detailedTaxon.getGeneticCode() != null) {
        node.setProperty(TaxonField.GENETIC_CODE.name(), detailedTaxon.getGeneticCode());
      }
      if (detailedTaxon.getMitochondrialGeneticCode() != null) {
        node.setProperty(TaxonField.MITOCHONDRIAL_GENETIC_CODE.name(), detailedTaxon.getMitochondrialGeneticCode());
      }
      if (detailedTaxon.getPln() != null) {
        node.setProperty(TaxonField.PLN.name(), detailedTaxon.getPln());
      }
      if (detailedTaxon.getRank() != null) {
        node.setProperty(TaxonField.RANK.name(), detailedTaxon.getRank());
      }

      return node;
    } catch (final Exception e) {
      log.error(ServerErrorMessages.TAXON_NODE_CREATION_FAILED, e);
      return node;
    }
  }

  private Node getNodeByTaxId(final String taxId) {
    Node node = null;
    try {
      log.debug("Looking for " + taxId + " in " + taxIdIndexId.name());
      final IndexHits<Node> nodes = getTaxIdIndex().get(taxIdIndexId.name(), taxId);
      if (nodes.size() > 1) {
        log.error("More than one node found for " + taxId);
      }
      node = nodes.getSingle();
      if (node == null) {
        log.debug("node not found");
      } else {
        log.debug("found node");
      }
    } catch (final NoSuchElementException e) {
      log.error("Exception finding unique node", e);
    }
    return node;
  }

  private Set<Node> getNodesByPartialKey(final String partialKey) {
    try {
      final Set<Node> nodes = new HashSet<Node>();
      if (partialKey.trim().equals("")) {
        log.debug("Empty key sent");
      } else {
        final String query = partialKey.toLowerCase().concat("*");
        final Index<Node> textIndex = getTextIndex();
        final IndexHits<Node> hits = textIndex.query(textIndexId.name(), query);
        for (final Node hit : hits) {
          nodes.add(hit);
        }
      }
      return nodes;
    } catch (final RuntimeException re) {
      log.warn("Runtime exception when using key: " + partialKey, re);
      return null;
    }
  }

  private Index<Node> getTaxIdIndex() {
    if (taxIdIndex == null) {
      final IndexManager index = graphDbService.index();
      taxIdIndex = index.forNodes(taxIdIndexId.name(), MapUtil.stringMap("type", "exact", "to_lower_case", "true"));
    }
    return taxIdIndex;
  }

  private Index<Node> getTextIndex() {
    if (textIndex == null) {
      final IndexManager index = graphDbService.index();
      textIndex =
          index.forNodes(textIndexId.name(), MapUtil.stringMap(IndexManager.PROVIDER, "lucene", "type", "fulltext"));
    }
    return textIndex;
  }
}
