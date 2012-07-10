package com.danielvaughan.taxonomy.server;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TaxonomySetup {

  @Autowired
  private GraphDatabaseService graphDbService;
  
  @Transactional
  public void run() {
    Node myNode = graphDbService.createNode();
    System.out.println("hello world");
  }

}
