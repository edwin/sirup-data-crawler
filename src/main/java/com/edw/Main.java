package com.edw;

import com.edw.model.TblKlpd;
import com.edw.model.TblSatker;
import com.edw.mapper.TblKlpdMapper;
import com.edw.mapper.TblSatkerMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *  com.edw.Main
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 27 Apr 2026 11:52
 */
public class Main {

    private final Logger logger = LogManager.getLogger(Main.class);

    private static final String TBL_KLPD_ENDPOINT_TEMPLATE = "https://sirup.inaproc.id/sirup/datatablectr/datatablerupkldi2?tahun=%s&jenisID=%s&sEcho=1&iColumns=10&iDisplayStart=%d&iDisplayLength=%d&mDataProp_0=0&sSearch_0=&bRegex_0=false&bSearchable_0=true&bSortable_0=false&mDataProp_1=1&sSearch_1=&bRegex_1=false&bSearchable_1=true&bSortable_1=true&mDataProp_2=2&sSearch_2=&bRegex_2=false&bSearchable_2=true&bSortable_2=true&mDataProp_3=3&sSearch_3=&bRegex_3=false&bSearchable_3=true&bSortable_3=true&mDataProp_4=4&sSearch_4=&bRegex_4=false&bSearchable_4=true&bSortable_4=true&mDataProp_5=5&sSearch_5=&bRegex_5=false&bSearchable_5=true&bSortable_5=true&mDataProp_6=6&sSearch_6=&bRegex_6=false&bSearchable_6=true&bSortable_6=true&mDataProp_7=7&sSearch_7=&bRegex_7=false&bSearchable_7=true&bSortable_7=true&mDataProp_8=8&sSearch_8=&bRegex_8=false&bSearchable_8=true&bSortable_8=true&mDataProp_9=9&sSearch_9=&bRegex_9=false&bSearchable_9=true&bSortable_9=true&sSearch=&bRegex=false&iSortCol_0=0&sSortDir_0=asc&iSortingCols=1&_=1777267950991";

    private static final String TBL_SATKER_ENDPOINT_TEMPLATE = "https://sirup.inaproc.id/sirup/datatablectr/datatableruprekapkldi?idKldi=%s&tahun=%s&sEcho=2&iColumns=10&iDisplayStart=%d&iDisplayLength=%d&mDataProp_0=0&sSearch_0=&bRegex_0=false&bSearchable_0=true&bSortable_0=false&mDataProp_1=1&sSearch_1=&bRegex_1=false&bSearchable_1=true&bSortable_1=true&mDataProp_2=2&sSearch_2=&bRegex_2=false&bSearchable_2=true&bSortable_2=true&mDataProp_3=3&sSearch_3=&bRegex_3=false&bSearchable_3=true&bSortable_3=true&mDataProp_4=4&sSearch_4=&bRegex_4=false&bSearchable_4=true&bSortable_4=true&mDataProp_5=5&sSearch_5=&bRegex_5=false&bSearchable_5=true&bSortable_5=true&mDataProp_6=6&sSearch_6=&bRegex_6=false&bSearchable_6=true&bSortable_6=true&mDataProp_7=7&sSearch_7=&bRegex_7=false&bSearchable_7=true&bSortable_7=true&mDataProp_8=8&sSearch_8=&bRegex_8=false&bSearchable_8=true&bSortable_8=true&mDataProp_9=9&sSearch_9=&bRegex_9=false&bSearchable_9=true&bSortable_9=true&sSearch=&bRegex=false&iSortCol_0=0&sSortDir_0=asc&iSortingCols=1&sRangeSeparator=~";

    private String[] jenisInstansi = {"KEMENTERIAN", "LEMBAGA", "PROVINSI", "KOTA", "KABUPATEN"};

    public static void main(String[] args) {
        Main main = new Main();
//        main.runGenerateTblKlpd();
        main.runGenerateTblSatker();
    }

    private void runGenerateTblKlpd() {
        for (String jenisID : jenisInstansi) {
            generateTblKlpd("2026", jenisID, 0, 1000);
        }
    }

    private void generateTblKlpd(String tahun, String jenisID, int iDisplayStart, int iDisplayLength) {
        String resource = "mybatis-config.xml";

        String endpoint = String.format(TBL_KLPD_ENDPOINT_TEMPLATE, tahun, jenisID, iDisplayStart, iDisplayLength);

        // Log the generated URL for demonstration
        logger.info("Generated URL: {}", endpoint);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .build();

        try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

            try (SqlSession session = sqlSessionFactory.openSession()) {
                TblKlpdMapper mapper = session.getMapper(TblKlpdMapper.class);

                // set as autocommit
                session.getConnection().setAutoCommit(true);

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    String jsonResponse = response.body();
                    List<TblKlpd> klpdList = parseJsonToTblKlpd(jsonResponse, jenisID);

                    for (TblKlpd klpd : klpdList) {
                        try {
                            TblKlpd existing = mapper.getKlpdById(klpd.getIdKlpd());

                            if (existing != null) {
                                mapper.updateKlpd(klpd);
                                logger.info("Updated KLPD: {} - {} - {}", klpd.getIdKlpd(), klpd.getNamaKlpd(), klpd.getJenisKlpd());
                            } else {
                                mapper.insertKlpd(klpd);
                                logger.info("Inserted KLPD: {} - {} - {}", klpd.getIdKlpd(), klpd.getNamaKlpd(), klpd.getJenisKlpd());
                            }
                        } catch (Exception e) {
                            logger.error("Error processing KLPD: {} - {} - {}", klpd.getIdKlpd(), klpd.getNamaKlpd(), klpd.getJenisKlpd(), e);
                        }
                    }

                    logger.info("Successfully processed {} - {} KLPD records", klpdList.size(), jenisID);
                } else {
                    logger.error("HTTP request failed with status code: {}", response.statusCode());
                }

            } catch (Exception e) {
                logger.error("Error processing HTTP request or database operations", e);
            }

        } catch (IOException e) {
            logger.error("Error loading MyBatis configuration", e);
        }
    }

    private List<TblKlpd> parseJsonToTblKlpd(String jsonResponse, String jenisID) {
        List<TblKlpd> klpdList = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            // Get the aaData array
            JsonNode aaDataNode = rootNode.get("aaData");

            if (aaDataNode != null && aaDataNode.isArray()) {
                for (JsonNode arrayNode : aaDataNode) {
                    if (arrayNode.isArray() && arrayNode.size() >= 2) {
                        // Map first element to idKlpd and second element to namaKlpd
                        String idKlpd = arrayNode.get(0).asText();
                        String namaKlpd = arrayNode.get(1).asText();

                        TblKlpd klpd = new TblKlpd(idKlpd, namaKlpd, jenisID);
                        klpdList.add(klpd);

                        logger.debug("Parsed KLPD: {} - {}", idKlpd, namaKlpd);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Error parsing JSON response", e);
        }

        return klpdList;
    }

     private void runGenerateTblSatker() {
        String resource = "mybatis-config.xml";

        try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

            try (SqlSession session = sqlSessionFactory.openSession()) {
                TblKlpdMapper klpdMapper = session.getMapper(TblKlpdMapper.class);

                List<String> klpdIds = klpdMapper.getAllKlpdIds();
                logger.info("Found {} KLPD records to process for Satker generation", klpdIds.size());

                for (String idKlpd : klpdIds) {
                    generateTblSatker("2026", idKlpd, 0, 1000);
                }

            } catch (Exception e) {
                logger.error("Error processing Satker generation", e);
            }

        } catch (IOException e) {
            logger.error("Error loading MyBatis configuration for Satker generation", e);
        }
     }

    private void generateTblSatker(String tahun, String idKlpd, int iDisplayStart, int iDisplayLength) {
        String resource = "mybatis-config.xml";

        String endpoint = String.format(TBL_SATKER_ENDPOINT_TEMPLATE, idKlpd, tahun, iDisplayStart, iDisplayLength);

        // Log the generated URL for demonstration
        logger.info("Generated Satker URL for KLPD {}: {}", idKlpd, endpoint);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .build();

        try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

            try (SqlSession session = sqlSessionFactory.openSession()) {
                TblSatkerMapper mapper = session.getMapper(TblSatkerMapper.class);

                // set as autocommit
                session.getConnection().setAutoCommit(true);

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    String jsonResponse = response.body();
                    List<TblSatker> satkerList = parseJsonToTblSatker(jsonResponse, idKlpd);

                    for (TblSatker satker : satkerList) {
                        try {
                            TblSatker existing = mapper.getSatkerById(satker.getIdSatker());

                            if (existing != null) {
                                mapper.updateSatker(satker);
                                logger.info("Updated Satker: {} - {} - KLPD: {}", satker.getIdSatker(), satker.getNamaSatker(), satker.getIdKlpd());
                            } else {
                                mapper.insertSatker(satker);
                                logger.info("Inserted Satker: {} - {} - KLPD: {}", satker.getIdSatker(), satker.getNamaSatker(), satker.getIdKlpd());
                            }
                        } catch (Exception e) {
                            logger.error("Error processing Satker: {} - {} - KLPD: {}", satker.getIdSatker(), satker.getNamaSatker(), satker.getIdKlpd(), e);
                        }
                    }

                    logger.info("Successfully processed {} Satker records for KLPD: {}", satkerList.size(), idKlpd);
                } else {
                    logger.error("HTTP request failed with status code: {} for KLPD: {}", response.statusCode(), idKlpd);
                }

            } catch (Exception e) {
                logger.error("Error processing HTTP request or database operations for KLPD: {}", idKlpd, e);
            }

        } catch (IOException e) {
            logger.error("Error loading MyBatis configuration for KLPD: {}", idKlpd, e);
        }
    }

    private List<TblSatker> parseJsonToTblSatker(String jsonResponse, String idKlpd) {
        List<TblSatker> satkerList = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            // Get the aaData array
            JsonNode aaDataNode = rootNode.get("aaData");

            if (aaDataNode != null && aaDataNode.isArray()) {
                for (JsonNode arrayNode : aaDataNode) {
                    if (arrayNode.isArray() && arrayNode.size() >= 2) {
                        // Map first element to idSatker and second element to namaSatker
                        String idSatker = arrayNode.get(0).asText();
                        String namaSatker = arrayNode.get(1).asText();

                        TblSatker satker = new TblSatker(idSatker, namaSatker, idKlpd);
                        satkerList.add(satker);

                        logger.debug("Parsed Satker: {} - {} - KLPD: {}", idSatker, namaSatker, idKlpd);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Error parsing JSON response for KLPD: {}", idKlpd, e);
        }

        return satkerList;
    }
}
