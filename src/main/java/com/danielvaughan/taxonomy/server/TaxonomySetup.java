package com.danielvaughan.taxonomy.server;

import com.danielvaughan.taxonomy.server.daos.TaxonDao;
import com.danielvaughan.taxonomy.server.util.ApplicationContextLoader;
import com.danielvaughan.taxonomy.shared.model.Taxon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

@Component
public class TaxonomySetup {

  private static final int BATCH_SIZE = 10000;

  public static void main(String[] args) {
    TaxonomySetup taxonomySetup = new TaxonomySetup();
    ApplicationContextLoader loader = new ApplicationContextLoader();
    loader.load(taxonomySetup, "META-INF/applicationContext.xml");
    taxonomySetup.run();
  }

  private final String filePath;

  private Log log = LogFactory.getLog(TaxonomySetup.class);

  @Autowired
  private TaxonDao taxonDao;

  public TaxonomySetup() {
    this.filePath = "/Users/dvaughan/Downloads/taxonomy.xml";
  }

  public TaxonomySetup(final String filePath) {
    this.filePath = filePath;
  }

  @Transactional
  public void run() {
    staxParse();
  }

  protected Taxon loadXmlTaxon(Element eleTaxon) {
    try {
      String taxId = eleTaxon.getAttributeValue("taxId");
      String scientificName = eleTaxon.getAttributeValue("scientificName");
      String commonName = eleTaxon.getAttributeValue("commonName");
      Taxon taxon = new Taxon(taxId, scientificName, commonName);
      return taxon;
    } catch (Exception e) {
      log.error("Exception parseing taxon element", e);
      return null;
    }
  }

  @SuppressWarnings("unused")
  private void JDOMParse() {
    try {
      log.info("Loading xml data: " + filePath);
      SAXBuilder parser = new SAXBuilder();
      InputStream in = getClass().getClassLoader().getResourceAsStream(filePath);
      org.jdom.Document jdomDocument = parser.build(in);
      Element eleRoot = jdomDocument.getRootElement();
      @SuppressWarnings("unchecked")
      List<Element> taxonElements = eleRoot.getChildren("taxon");
      for (Element eleTaxon : taxonElements) {
        Taxon taxon = loadXmlTaxon(eleTaxon);
        taxonDao.addTaxon(taxon);
      }
    } catch (IOException e) {
      log.error("File error", e);

    } catch (JDOMException e) {
      log.error("JDOM error", e);
    }
  }

  private void staxParse() {
    XMLInputFactory2 xmlif = null;
    try {
      xmlif = (XMLInputFactory2) XMLInputFactory2.newInstance();
      xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
      xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
      xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
      xmlif.configureForSpeed();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    System.out.println("Starting to parse " + filePath);
    System.out.println("");
    long starttime = System.currentTimeMillis();
    int elementCount = 0;
    int depth = -1;
    try {
      XMLStreamReader2 xmlr = (XMLStreamReader2) xmlif.createXMLStreamReader(filePath, new FileInputStream(filePath));
      int eventType = xmlr.getEventType();
      String curElement = "";
      List<Taxon> taxonBatch = new ArrayList<Taxon>();
      while (xmlr.hasNext()) {
        eventType = xmlr.next();
        switch (eventType) {
          case XMLEvent.START_ELEMENT:
            depth++;
            curElement = xmlr.getName().toString();
            if (curElement.equals("taxon") && depth == 1) {
              String taxId = xmlr.getAttributeValue("", "taxId");
              String scientificName = xmlr.getAttributeValue("", "scientificName");
              String commonName = xmlr.getAttributeValue("", "commonName");
              Taxon taxon = new Taxon(taxId, scientificName, commonName);
              taxonBatch.add(taxon);
              if (taxonBatch.size() == BATCH_SIZE) {
                taxonDao.batchAddTaxons(taxonBatch);
                taxonBatch.clear();
              }
            }
            break;
          case XMLEvent.ATTRIBUTE:
          case XMLEvent.END_ELEMENT:
            depth--;
            if (curElement.equals("taxon")) {
              elementCount++;
            }
            break;
          case XMLEvent.END_DOCUMENT:
            System.out.println("Total of " + elementCount + " occurrences");
        }
        if (eventType == XMLEvent.START_ELEMENT) {
          curElement = xmlr.getName().toString();
        } else {
          if (eventType == XMLEvent.CHARACTERS) {
          }
        }
      }
    } catch (XMLStreamException ex) {
      System.out.println(ex.getMessage());
      if (ex.getNestedException() != null) {
        ex.getNestedException().printStackTrace();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    System.out.println(" completed in " + (System.currentTimeMillis() - starttime) + " ms");
  }
}
