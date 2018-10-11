package org.genedb.crawl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.genedb.crawl.client.CrawlClient;
import org.genedb.crawl.model.Feature;
import org.genedb.crawl.model.LocatedFeature;
import org.genedb.crawl.model.Organism;

import junit.framework.TestCase;

// uncomment if XStream is required
// import com.thoughtworks.xstream.XStream;

/**
 * Tests a simple client.
 * 
 * 
 */
public class ClientTest extends TestCase {

    private Logger              logger  = Logger.getLogger(ClientTest.class);

    private static final String baseURL = "http://beta.genedb.org/services";

    public void testOrganisms() throws CrawlException, IOException {

        CrawlClient client = new CrawlClient(baseURL);

        List<Organism> organisms = client.request(List.class, Organism.class, "organisms", "list", null);
        
        assertTrue(organisms.size() > 0);

    }

    public void testListRegions() throws CrawlException, IOException {

        CrawlClient client = new CrawlClient(baseURL);
// uncomment if XStream is required
//         XStream xstream = new XStream();

        List<Organism> organisms = client.request(List.class, Organism.class, "organisms", "list", null);
        for (Organism organism : organisms) {
            logger.info(" -" + organism.common_name);

            Map<String, String[]> inOrganismParameters = new HashMap<String, String[]>();
            inOrganismParameters.put("organism", new String[] { organism.common_name });

            List<Feature> regions = client.request(List.class, Feature.class, "regions", "inorganism", inOrganismParameters);
            
            int n = 0;
            for (Feature region : regions) {

                if (n == 0) {
                    
                     assertTrue(region.uniqueName != null);
                    
                     logger.info("  -" + region.uniqueName);

                     Map<String, String[]> locationsParameters = new HashMap<String, String[]>();
                     locationsParameters.put("region", new String[] { region.uniqueName });

                     locationsParameters.put("start", new String[] { "1" });
                     locationsParameters.put("end", new String[] { "1000000" });
                     locationsParameters.put("exclude", new String[] { "false" });
                     locationsParameters.put("types", new String[] { "gene" });
                     // logger.info("\nparams for features query:\n" + xstream.toXML(locationsParameters));

                     List<LocatedFeature> features = client.<List> request(List.class, LocatedFeature.class, "regions", "locations", locationsParameters);
                     // // extract first feature (provided there is at least one feature)
                     // if (features.size() > 0) {
                     //    LocatedFeature feature = features.get(0);
                     // extract first feature that isn't obsolete (provided there is at least one non-obsolete feature)
                     // (would be better to rewrite the query method to aexclude obsolete features from the results set, obvs.)
                     LocatedFeature feature = null;
                     for(int f = 0;  f < features.size() && (0 == f || true == feature.isObsolete ); f = f + 1) {
                        feature = features.get(f);
                        logger.info("   (checking if " + f + ": " + feature.uniqueName + " is flagged as obsolete)" );
                     }
                     if (feature != null) {
                        assertTrue(feature.uniqueName != null);

                        Map<String, String[]> infoParameters = new HashMap<String, String[]>();
                        infoParameters.put("uniqueName", new String[] { feature.uniqueName });

                        Feature gene = (Feature) client.<Feature> request(Feature.class, "feature", "hierarchy", infoParameters);

                        logger.info("   -" + feature.uniqueName);
                        for (Feature child : gene.children) {
                            logger.info("    -" + child.uniqueName);
                        }
                     }

                }

                n++;

            }

        }

    }

}
